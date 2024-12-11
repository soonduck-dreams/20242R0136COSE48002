/**
 * TaskService
 *
 * [1. 역할]
 * - Redis를 활용하여 음악 생성 작업(task)의 상태와 결과 파일을 관리하는 서비스 클래스.
 *
 * [2. 주요 기능]
 * - 작업 상태(TaskStatus)를 Redis에 저장 및 업데이트.
 * - 작업 상태를 Redis에서 조회하고 JSON 데이터를 역직렬화.
 * - 작업 결과 파일을 Redis에 저장 및 만료 시간 설정.
 * - 작업 결과 파일을 Redis에서 조회.
 *
 * [3. 사용 사례]
 * - 작업 상태를 클라이언트에 제공하거나, 비동기 작업의 결과 파일을 저장 및 전달.
 * - 작업 관리의 주요 데이터 저장소로 Redis를 활용.
 *
 */

package jhhan.harmonynow_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void updateTaskStatus(String taskId, TaskStatus status) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(status);

        redisTemplate.opsForHash().put("task:" + taskId, "status", json);

        System.out.println("Updated task status: " + taskId + " is " + status);
    }

    public TaskStatus getTaskStatus(String taskId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFromRedis = (String) redisTemplate.opsForHash().get("task:" + taskId, "status");

        return objectMapper.readValue(jsonFromRedis, TaskStatus.class);
    }

    public void updateTaskFile(String taskId, byte[] file) {
        redisTemplate.opsForHash().put("task:" + taskId, "file", file);
        redisTemplate.expire("task:" + taskId, 1, TimeUnit.MINUTES);

        System.out.println("Updated task file: " + taskId);
    }

    public byte[] getTaskFile(String taskId) {
        byte[] file = (byte[]) redisTemplate.opsForHash().get("task:" + taskId, "file");

        if (file == null) {
            System.out.println("No file found for task: " + taskId);
            return null;
        }

        System.out.println("Retrieved task file for task: " + taskId);
        return file;
    }
}
