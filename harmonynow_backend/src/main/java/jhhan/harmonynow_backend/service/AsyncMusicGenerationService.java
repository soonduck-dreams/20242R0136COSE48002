/**
 * AsyncMusicGenerationService
 *
 * [1. 역할]
 * - 비동기적으로 음악 생성 요청을 처리하는 서비스 클래스.
 * - 사용자의 진행 데이터를 기반으로 음악 생성 모델 서버에 요청을 전송하고 결과를 처리.
 *
 * [2. 주요 기능]
 * - 모델 서버에 MIDI 파일을 포함한 요청 전송.
 * - 응답 데이터를 파일로 저장하고 작업 상태를 업데이트.
 * - 예외 상황을 처리하며 작업 실패 시 적절한 상태로 갱신.
 *
 * [3. 사용 사례]
 * - 음악 생성 작업을 비동기적으로 실행하여 사용자 요청에 빠르게 응답.
 *
 */

package jhhan.harmonynow_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
@RequiredArgsConstructor
public class AsyncMusicGenerationService {

    private final RestTemplate restTemplate;
    private final ProgressionService progressionService;
    private final TaskService taskService;

    @Value("${model-server.url}")
    private String modelServerUrl;

    @Async
    public void startMusicGeneration(String taskId, Long progressionId) {
        String url = modelServerUrl + "/infill";

        try {
            File introFile = progressionService.getProgressionSampleMidi(progressionId);
            File outroFile = progressionService.getProgressionSampleMidi(progressionService.getRandomCadenceProgressionIdWithSampleMidi());

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("intro_file", new FileSystemResource(introFile));
            body.add("outro_file", new FileSystemResource(outroFile));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 모델 서버에 POST 요청 전송
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                taskService.updateTaskFile(taskId, response.getBody());
                taskService.updateTaskStatus(taskId, TaskStatus.COMPLETED_PENDING_RESULT);
            } else {
                taskService.updateTaskStatus(taskId, TaskStatus.FAILED);
            }
        } catch (Exception e) {
            System.out.println("Failed Music Generation!");
            try {
                taskService.updateTaskStatus(taskId, TaskStatus.FAILED);
            } catch (Exception e1) {
                e.printStackTrace();
                System.out.println("AsyncMusicGenerationService.startMusicGeneration: " + e.getMessage());
            }
        }
    }
}
