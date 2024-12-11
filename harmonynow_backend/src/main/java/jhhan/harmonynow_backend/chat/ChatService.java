/**
 * ChatService
 *
 * [1. 역할]
 * - ChatController에서 요청받은 데이터를 OpenAI API에 전송하고, 응답을 처리하는 서비스 클래스.
 * - 대화 프롬프트 요청의 직렬화 및 역직렬화를 처리.
 *
 * [2. 주요 기능]
 * - OpenAI API와 HTTP 통신을 통해 프롬프트를 전송하고 응답을 수신.
 * - 요청 및 응답 중 발생한 예외를 처리하고, 오류 메시지를 반환.
 *
 * [3. 사용 사례]
 * - 질문하기 기능에서 OpenAI API 호출을 통해 사용자 요청에 대한 응답을 생성.
 */


package jhhan.harmonynow_backend.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatConfig chatConfig;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    public Map<String, Object> prompt(ChatCompletionDTO chatCompletionDto) {
        Map<String, Object> originalResultMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();

        HttpHeaders headers = chatConfig.httpHeaders();

        HttpEntity<ChatCompletionDTO> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = chatConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            ObjectMapper om = new ObjectMapper();
            originalResultMap = om.readValue(response.getBody(), new TypeReference<>() {});
            resultMap.put("result", (List<Map<String, Object>>) originalResultMap.get("choices"));
        } catch (JsonProcessingException e) {
            log.warn("JsonMappingException :: " + e.getMessage());
            resultMap.put("content", "[에러] 답변을 불러오지 못했어요. (JSON parcing 실패)");
        } catch (RuntimeException e) {
            log.warn("RuntimeException :: " + e.getMessage());
            resultMap.put("content", "[에러] 답변을 불러오지 못했어요.");
        }
        return resultMap;
    }
}
