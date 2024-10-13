package jhhan.harmonynow_backend.domain;

import jakarta.persistence.*;
import jhhan.harmonynow_backend.dto.CreateProgressionDTO;
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

    @OneToMany(mappedBy = "progression", cascade = CascadeType.ALL)
    private List<ChordProgressionMap> maps = new ArrayList<>();

    public static Progression CreateProgression(CreateProgressionDTO dto) {
        Progression progression = new Progression();
        progression.description = dto.getDescription();
        return progression;
    }
}
