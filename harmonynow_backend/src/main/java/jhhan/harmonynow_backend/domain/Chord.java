package jhhan.harmonynow_backend.domain;

import jakarta.persistence.*;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class Chord {

    @Id
    @GeneratedValue
    @Column(name = "chord_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @Column(nullable = false)
    private Boolean isPublic;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String audioUrl;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "chord", cascade = CascadeType.ALL)
    private List<ChordProgressionMap> maps = new ArrayList<>();


    /* Methods */

    public static Chord createChord(CreateChordDTO dto) {
        Chord chord = new Chord();
        chord.name = dto.getName();
        chord.description = dto.getDescription();
        chord.level = dto.getLevel();
        chord.isPublic = dto.getIsPublic();
        chord.member = dto.getMember();
        return chord;
    }

}
