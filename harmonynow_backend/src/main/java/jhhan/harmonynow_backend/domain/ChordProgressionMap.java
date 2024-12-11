/**
 * ChordProgressionMap
 *
 * [1. 역할]
 * - 코드(Chord)와 코드 진행(Progression) 간의 매핑 관계를 정의하는 JPA 엔티티 클래스.
 * - 코드 진행에서 코드의 순서(position)를 관리하며, 두 엔티티 간 다대다 관계를 풀어내기 위한 중간 테이블 역할.
 *
 * [2. 주요 기능]
 * - 특정 코드가 코드 진행에서 어떤 위치에 있는지(position)를 저장.
 * - 코드와 코드 진행 간의 매핑 정보를 생성하거나 업데이트.
 *
 * [3. 사용 사례]
 * - 코드 진행에 포함된 코드들의 순서와 구성을 조회하거나 수정할 때 사용.
 * - 데이터베이스에서 코드와 코드 진행 간의 관계를 관리할 때 JPA를 통해 매핑.
 */


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
