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
                taskService.updateTaskStatus(taskId, TaskStatus.COMPLETED);
            } else {
                taskService.updateTaskStatus(taskId, TaskStatus.FAILED);
            }
        } catch (Exception e) {
            System.out.println("Failed Music Generation!");
            try {
                taskService.updateTaskStatus(taskId, TaskStatus.FAILED);
            } catch (Exception e1) {
                System.err.println("update task failed: " + e1.getMessage());
            }
        }
    }
}
