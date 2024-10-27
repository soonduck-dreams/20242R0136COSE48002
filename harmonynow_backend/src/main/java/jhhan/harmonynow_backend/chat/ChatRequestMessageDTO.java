package jhhan.harmonynow_backend.chat;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRequestMessageDTO {
    private String role;
    private String content;

    public ChatRequestMessageDTO(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
