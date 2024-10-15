package jhhan.harmonynow_backend.dto;

import lombok.Getter;

@Getter
public class ChordNameIdDTO {
    private String name;
    private Long id;

    public ChordNameIdDTO(String name, Long id) {
        this.name = name;
        this.id = id;
    }
}
