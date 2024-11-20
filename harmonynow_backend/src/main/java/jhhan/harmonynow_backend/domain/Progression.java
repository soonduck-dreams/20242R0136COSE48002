package jhhan.harmonynow_backend.domain;

import jakarta.persistence.*;
import jhhan.harmonynow_backend.dto.CreateProgressionDTO;
import jhhan.harmonynow_backend.dto.EditProgressionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Progression {

    @Id
    @GeneratedValue
    @Column(name = "progression_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String audioUrl;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String sampleMidiUrl;

    @Column(nullable = false)
    private Boolean isCadence;

    @OneToMany(mappedBy = "progression", cascade = CascadeType.REMOVE)
    private List<ChordProgressionMap> maps = new ArrayList<>();

    public static Progression CreateProgression(CreateProgressionDTO dto) {
        Progression progression = new Progression();
        progression.description = dto.getDescription();
        progression.isCadence = dto.getIsCadence();
        return progression;
    }

    public void updateProgression(EditProgressionDTO dto) {
        this.description = dto.getDescription();
        this.isCadence = dto.getIsCadence();
    }
}
