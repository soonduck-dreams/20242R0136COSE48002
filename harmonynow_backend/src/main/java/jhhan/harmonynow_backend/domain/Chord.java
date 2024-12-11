/**
 * Chord
 *
 * [1. 역할]
 * - 화성학에서 코드(Chord)는 동시에 울리는 음들의 집합으로, 음악의 기본적인 화음을 형성하는 단위입니다.
 * - 코드의 데이터를 데이터베이스에 저장하고 관리하는 JPA 엔티티 클래스.
 *
 * [2. 주요 기능]
 * - 코드의 기본 속성(이름, 설명, 난이도, 공개 여부, 이미지 및 오디오 URL)을 저장.
 * - 코드와 관련된 코드 진행(Progression)을 관리하기 위한 매핑(`ChordProgressionMap`) 관계 설정.
 * - `CreateChordDTO`와 `EditChordDTO`를 기반으로 코드 객체를 생성하거나 업데이트.
 *
 * [3. 사용 사례]
 * - 코드 생성, 수정, 조회와 관련된 비즈니스 로직에서 사용.
 * - 데이터베이스에서 코드 데이터를 읽거나 저장할 때 JPA를 통해 매핑.
 */


package jhhan.harmonynow_backend.domain;

import jakarta.persistence.*;
import jhhan.harmonynow_backend.dto.CreateChordDTO;
import jhhan.harmonynow_backend.dto.EditChordDTO;
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

    @OneToMany(mappedBy = "chord")
    private List<ChordProgressionMap> maps = new ArrayList<>();


    /* Methods */

    public static Chord createChord(CreateChordDTO dto) {
        Chord chord = new Chord();
        chord.name = dto.getName();
        chord.description = dto.getDescription();
        chord.level = dto.getLevel();
        chord.isPublic = dto.getIsPublic();
        return chord;
    }

    public void updateChord(EditChordDTO dto) {
        name = dto.getName();
        description = dto.getDescription();
        level = dto.getLevel();
        isPublic = dto.getIsPublic();
    }

}
