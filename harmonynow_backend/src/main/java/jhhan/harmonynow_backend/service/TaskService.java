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
