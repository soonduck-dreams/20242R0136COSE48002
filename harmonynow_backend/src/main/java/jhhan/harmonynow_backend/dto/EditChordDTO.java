package jhhan.harmonynow_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jhhan.harmonynow_backend.domain.Level;
import jhhan.harmonynow_backend.validation.ValidFileExtension;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class EditChordDTO {

    private Long id;

    @NotBlank(message = "코드 이름은 필수 항목입니다.")
    private String name;

    private String description;
    private Level level;
    private Boolean isPublic;

    private String imageUrl; // Response

    @ValidFileExtension(extensions = {"jpg", "jpeg", "png"}, message = "이미지 파일 형식은 jpg, jpeg, png만 가능합니다.")
    private MultipartFile imageFile; // Request

    private Boolean isImageDeleteRequested;

    private String audioUrl; // Response

    private Boolean isAudioDeleteRequested;

    @ValidFileExtension(extensions = {"mp3", "wav"}, message = "오디오 파일 형식은 mp3, wav만 가능합니다.")
    private MultipartFile audioFile; // Request

    public EditChordDTO(Long id, String name, String description, Level level, Boolean isPublic, String imageUrl, String audioUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.level = level;
        this.isPublic = isPublic;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }
}
