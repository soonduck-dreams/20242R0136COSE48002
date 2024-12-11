/**
 * MusicGenerationController
 *
 * [1. 역할]
 * - 학습하기 페이지에서 코드 진행을 기반으로 음악 생성 요청을 처리하는 API 컨트롤러.
 * - 음악 생성 작업 요청 및 생성된 음악 파일의 결과를 제공.
 *
 * [2. 주요 기능]
 * - 코드 진행 ID를 기반으로 음악 생성 작업을 시작.
 * - 생성 작업의 상태를 조회하여 결과를 반환하거나 상태를 전달.
 * - 음악 생성이 완료된 경우 결과 파일(ZIP 형식)을 반환.
 *
 * [3. 사용 사례]
 * - 학습하기 페이지에서 사용자가 코드 진행을 기반으로 음악 생성을 요청하고 결과를 다운로드할 때 사용.
 */


package jhhan.harmonynow_backend.controller;

import jhhan.harmonynow_backend.service.MusicGenerationService;
import jhhan.harmonynow_backend.service.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/learn/progressions")
@RequiredArgsConstructor
public class MusicGenerationController {

    private final MusicGenerationService musicGenerationService;

    @PostMapping("/generate-music/{progressionId}")
    public ResponseEntity<?> generateMusic(@PathVariable Long progressionId) {
        String taskId = musicGenerationService.createTask(progressionId);

        if (taskId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create task.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("task_id", taskId));
    }

    @GetMapping("/generate-music-result/{taskId}")
    public ResponseEntity<?> generateMusicResult(@PathVariable String taskId) {
        TaskStatus taskStatus;
        try {
            taskStatus = musicGenerationService.getTaskStatus(taskId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("failed to read taskStatus");
        }

        if (taskStatus == TaskStatus.IN_PROGRESS) {
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("Task is not completed yet. Try again later.");
        }

        if (taskStatus == TaskStatus.FAILED) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Task failed.");
        }

        if (taskStatus == TaskStatus.COMPLETED_RESULT_PROVIDED) {
            return ResponseEntity.status(HttpStatus.GONE)
                    .body("Task has been completed, but the result has already been provided to you.");
        }

        // 파일 가져오기
        byte[] file = musicGenerationService.getTaskFile(taskId);
        if (file == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Task has been completed, but File not found for task ID: " + taskId);
        }

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // ZIP 파일 MIME 타입
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("generated_music.zip") // 파일 이름을 ZIP 형식으로 설정
                .build());

        musicGenerationService.updateTaskStatus(taskId, TaskStatus.COMPLETED_RESULT_PROVIDED);

        // ResponseEntity 생성 및 반환
        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
    }
}
