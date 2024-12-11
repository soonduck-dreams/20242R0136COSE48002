/**
 * Progression
 *
 * [1. 역할]
 * - 코드 진행(Progression)은 여러 코드(Chord)가 순차적으로 배열된 음악적 흐름을 나타냅니다.
 * - 코드 진행 데이터를 데이터베이스에 저장하고 관리하는 JPA 엔티티 클래스.
 *
 * [2. 주요 기능]
 * - 코드 진행의 기본 속성(설명, 오디오 URL, 샘플 MIDI URL, 종지 여부)을 저장.
 * - 코드 진행과 관련된 코드 목록을 관리하기 위한 매핑(`ChordProgressionMap`) 관계 설정.
 * - `CreateProgressionDTO`를 기반으로 코드 진행 객체를 생성하거나, `EditProgressionDTO`를 기반으로 업데이트.
 *
 * [3. 사용 사례]
 * - 코드 진행 생성, 수정, 조회와 관련된 비즈니스 로직에서 사용.
 * - 데이터베이스에서 코드 진행 데이터를 읽거나 저장할 때 JPA를 통해 매핑.
 */


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
