package jhhan.harmonynow_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

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

    @Column(columnDefinition = "TEXT")
    private String progressionMidiUrl;

    @Column(columnDefinition = "TEXT")
    private String melodyMidiUrl;

    @OneToMany(mappedBy = "progression", cascade = CascadeType.ALL)
    private List<ChordProgressionMap> maps = new ArrayList<>();

}
