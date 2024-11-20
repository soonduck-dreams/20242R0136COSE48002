package jhhan.harmonynow_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MusicGenerationService {

    private final AsyncMusicGenerationService asyncMusicGenerationService;
    private final TaskService taskService;

    public String createTask(Long progressionId) {
        String taskId = UUID.randomUUID().toString();

        try {
            taskService.updateTaskStatus(taskId, TaskStatus.IN_PROGRESS);
        } catch (Exception e) {
            System.err.println("update task failed: " + e.getMessage());
            return null;
        }

        // 비동기 메서드 호출
        asyncMusicGenerationService.startMusicGeneration(taskId, progressionId);
        return taskId;
    }

    public TaskStatus getTaskStatus(String taskId) throws Exception {
        return taskService.getTaskStatus(taskId);
    }

    public byte[] getTaskFile(String taskId) {
        return taskService.getTaskFile(taskId);
    }
}
