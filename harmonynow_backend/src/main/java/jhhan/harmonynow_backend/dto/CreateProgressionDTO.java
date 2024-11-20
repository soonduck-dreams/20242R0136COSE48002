package jhhan.harmonynow_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jhhan.harmonynow_backend.validation.ValidChordIds;
import jhhan.harmonynow_backend.validation.ValidFileExtension;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CreateProgressionDTO {

    @ValidChordIds
    private List<Long> chordIds;

    @NotNull(message = "종지 여부는 필수 항목입니다.")
    private Boolean isCadence;

    private String description;

    @ValidFileExtension(extensions = {"mp3", "wav"}, message = "오디오 파일 형식은 mp3, wav만 가능합니다.")
    private MultipartFile audioFile;

    @ValidFileExtension(extensions = {"mid"}, message = "미디 파일 형식은 mid만 가능합니다.")
    private MultipartFile sampleMidiFile;
}
