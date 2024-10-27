package jhhan.harmonynow_backend.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatCompletionDTO {
    private String model;
    private List<ChatRequestMessageDTO> messages;
    private Integer n;
}
