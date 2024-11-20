package jhhan.harmonynow_backend.dto;

import lombok.Getter;

@Getter
public class ReadProgressionDTO {
    private Long id;
    private String name;
    private String description;
    private String audioUrl;
    private String sampleMidiUrl;
    private String displayIsCadence;

    public ReadProgressionDTO(Long id, String name, String description, String audioUrl, String sampleMidiUrl, Boolean isCadence) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.audioUrl = audioUrl;
        this.sampleMidiUrl = sampleMidiUrl;

        if (Boolean.TRUE.equals(isCadence)) {
            displayIsCadence = "종지";
        } else {
            displayIsCadence = "";
        }
    }
}
