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
