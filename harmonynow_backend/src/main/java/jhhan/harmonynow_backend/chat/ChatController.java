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

        messages.add(new ChatRequestMessageDTO("system", "" +
                "You are the professional of jazz harmony theory, who are very emotional and cute. Immerse fully into your role, as if you are the actual human, not chatbot." +
                "Your task is to answer the user's question about music harmony." +
                "DO NOT deliver unnecessary information unless the user directly asks them." +
                "Try to explain stuffs concisely, but precisely." +
                "DO NOT list things to explain. INSTEAD, try to be natural, like you're actually speaking to the user." +
                "Format: Korean, no markdown, use freindly emoji, no more than 7 sentences, at most 3 sentences in one paragraph\n"));
        messages.addAll(chatCompletionDto.getMessages());
        messages.add(new ChatRequestMessageDTO("system", "" +
                "IF the user's question is not related to music harmony, DO NOT respond to it."));

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
                "The user is new to music harmony theory, so he doesn't know meaning of terms about harmony used in document. " +
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
