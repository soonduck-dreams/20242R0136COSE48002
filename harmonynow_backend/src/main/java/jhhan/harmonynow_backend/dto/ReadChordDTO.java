package jhhan.harmonynow_backend.dto;

import jhhan.harmonynow_backend.domain.Chord;
import lombok.Getter;

@Getter
public class ReadChordDTO {

    private Long id;
    private String name;
    private String description;
    private String displayLevel;
    private String displayIsPublic;
    private String imageUrl;
    private String audioUrl;
    // private String member;

    public ReadChordDTO(Chord chord) {
        this.id = chord.getId();
        this.name = chord.getName();
        this.description = chord.getDescription();
        this.imageUrl = chord.getImageUrl();
        this.audioUrl = chord.getAudioUrl();

        switch (chord.getLevel()) {
            case B:
                displayLevel = "초급";
                break;
            case C:
                displayLevel = "중급";
                break;
            case D:
                displayLevel = "고급";
                break;
        }

        if (Boolean.TRUE.equals(chord.getIsPublic())) {
            displayIsPublic = "공개";
        } else {
            displayIsPublic = "비공개";
        }
    }
}
