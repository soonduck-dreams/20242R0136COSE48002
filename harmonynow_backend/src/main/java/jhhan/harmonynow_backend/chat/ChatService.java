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

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatConfig.httpHeaders();

        // [STEP5] 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<ChatCompletionDTO> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = chatConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // [STEP6] String -> HashMap 역직렬화를 구성합니다.
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
