package jhhan.harmonynow_backend.dto;

import lombok.Getter;

@Getter
public class ChordPositionDTO {
    private String name;
    private Integer position;

    public ChordPositionDTO(String name, Integer position) {
        this.name = name;
        this.position = position;
    }
}
