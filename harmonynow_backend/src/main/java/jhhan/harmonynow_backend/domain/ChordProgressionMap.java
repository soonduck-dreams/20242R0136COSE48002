package jhhan.harmonynow_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class ChordProgressionMap {

    @Id
    @GeneratedValue
    @Column(name = "map_id")
    private Long id;

    @Column(nullable = false)
    private Integer position;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chord_id", nullable = false)
    private Chord chord;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "progression_id", nullable = false)
    private Progression progression;

    public static ChordProgressionMap createChordProgressionMap(Chord chord, Progression progression, Integer position) {
        ChordProgressionMap chordProgressionMap = new ChordProgressionMap();
        chordProgressionMap.chord = chord;
        chordProgressionMap.progression = progression;
        chordProgressionMap.position = position;
        return chordProgressionMap;
    }

    public void updateChordProgressionMap(Integer position, Chord chord) {
        this.chord = chord;
        this.position = position;
    }
}
