/**
 * ChatController
 *
 * [1. 역할]
 * - 학습하기 페이지에서 사용되는 대화형 프롬프트 요청을 처리하는 API 컨트롤러.
 * - 사용자의 요청 데이터를 기반으로 OpenAI API와 통신하여 결과를 반환.
 *
 * [2. 주요 기능]
 * - 프롬프트 설계 및 대화 요청 처리:
 *   1. `selectPrompt`: 음악 화성학 관련 대화 프롬프트를 생성하여 결과 반환.
 *   2. `selectStarter`: 대화 시작 질문을 생성하여 결과 반환.
 * - ChatService를 호출하여 외부 API와의 통신을 처리.
 *
 * [3. 사용 사례]
 * - 사용자가 학습하기 페이지에서 질문을 작성하거나 대화형 도움을 받을 때 API 호출.
 */


package jhhan.harmonynow_backend.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat/prompt")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDTO chatCompletionDto) {
        /* 프롬프트 설계 */
        ChatCompletionDTO processedDto = new ChatCompletionDTO();
        processedDto.setModel(chatCompletionDto.getModel());
        List<ChatRequestMessageDTO> messages = new ArrayList<>();

        messages.addAll(chatCompletionDto.getMessages());
        messages.add(new ChatRequestMessageDTO("system", "" +
                "You are a friend of user who is studying jazz harmony theory. Immerse fully into your role, as if you are the actual human, not chatbot." +
                "Your task is to answer the user's question about music harmony, as far as you know." +
                "DO NOT list things to explain. INSTEAD, try to be natural, like you're actually speaking to the user." +
                "IF the user's question is not related to music harmony, DO NOT respond to it." +
                "Answer Style Requirements:\n" +
                "1. Be casual, like we’ve been best friends forever.\n" +
                "2. Keep it short and to the point." +
                "3. Speak in Korean." +
                "4. Format: one sentence"));

        processedDto.setMessages(messages);
        processedDto.setN(1);
        processedDto.setModel("gpt-4o-mini");

        Map<String, Object> result = chatService.prompt(processedDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/chat/starter")
    public ResponseEntity<Map<String, Object>> selectStarter(@RequestBody ChatCompletionDTO chatCompletionDto) {

        /* 프롬프트 설계 */
        ChatCompletionDTO processedDto = new ChatCompletionDTO();
        processedDto.setModel(chatCompletionDto.getModel());
        List<ChatRequestMessageDTO> messages = new ArrayList<>();

        messages.add(new ChatRequestMessageDTO("system", "" +
//                "The user is new to music harmony theory, so he doesn't know meaning of terms about harmony used in document. " +
                "Your task is to create one starter question, given the following conversation history and document page content. " +
                "Act as if you are the user who asks a question to other person." +
                "Mimic the Following Speaking Style: 코드가 뭐야? 화성학이 뭐야? 난이도는 어떻게 정한 거야? 그럼, 그게 뭔데?" +
                "Format: 1 short question sentence in Korean"));
        messages.addAll(chatCompletionDto.getMessages());

        processedDto.setMessages(messages);
        processedDto.setN(2);
        processedDto.setModel("gpt-4o-mini");

        /* 전송 */
        Map<String, Object> result = chatService.prompt(processedDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
