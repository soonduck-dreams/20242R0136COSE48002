package jhhan.harmonynow_backend.dto;

import lombok.Getter;

@Getter
public class ReadProgressionDTO {
    private Long id;
    private String name;
    private String description;
    private String audioUrl;
    private String sampleMidiUrl;

    public ReadProgressionDTO(Long id, String name, String description, String audioUrl, String sampleMidiUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.audioUrl = audioUrl;
        this.sampleMidiUrl = sampleMidiUrl;
    }
}
