/**
 * MusicGenerationService
 *
 * [1. 역할]
 * - 음악 생성 작업을 관리하는 서비스 클래스.
 * - 작업(task) 생성, 상태 조회 및 결과 파일 관리와 같은 음악 생성의 전체 흐름을 담당.
 *
 * [2. 주요 기능]
 * - 새 작업 생성 및 고유 ID(taskId) 생성.
 * - 비동기 음악 생성 서비스(AsyncMusicGenerationService)를 호출하여 작업 수행.
 * - 작업 상태(TaskStatus) 및 결과 파일 관리.
 *
 * [3. 사용 사례]
 * - 클라이언트 요청에 따라 새로운 음악 생성 작업을 시작하거나, 진행 상태를 조회하며, 결과 파일을 제공.
 *
 */


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
            e.printStackTrace();
            System.out.println("MusicGenerationService.createTask: " + e.getMessage());
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

    public void updateTaskStatus(String taskId, TaskStatus status) {
        try {
            taskService.updateTaskStatus(taskId, status);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("MusicGenerationService.updateTaskStatus: failed to update task status");
        }
    }
}
