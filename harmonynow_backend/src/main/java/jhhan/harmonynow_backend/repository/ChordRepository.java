package jhhan.harmonynow_backend.repository;

import jakarta.persistence.EntityManager;
import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.domain.Level;
import jhhan.harmonynow_backend.dto.ChordNameIdDTO;
import jhhan.harmonynow_backend.dto.ReadChordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChordRepository {

    private final EntityManager em;

    public Long save(Chord chord) {
        em.persist(chord);
        return chord.getId();
    }

    public Chord findOne(Long chordId) {
        return em.find(Chord.class, chordId);
    }

    public List<Chord> findAll() {
        return em.createQuery("select c from Chord c ORDER BY c.id", Chord.class).getResultList();
    }

    public List<Chord> findPublicChordsByLevel(Level level) {
        return em.createQuery("select c from Chord c where c.level = :level and c.isPublic = true", Chord.class)
                .setParameter("level", level)
                .getResultList();
    }

    // 특정 Progression에 포함된 각 Chord를 순서대로 조회
    public List<ChordNameIdDTO> findChordNameIdByProgressionId(Long progressionId) {
        List<Object[]> results = em.createQuery(
                        "SELECT map.chord.name, map.chord.id FROM ChordProgressionMap map WHERE map.progression.id = :progressionId ORDER BY map.position ASC", Object[].class)
                .setParameter("progressionId", progressionId)
                .getResultList();

        List<ChordNameIdDTO> dtoList = new ArrayList<>();
        for (Object[] result : results) {
            dtoList.add(new ChordNameIdDTO((String) result[0], (Long) result[1]));
        }

        return dtoList;
    }

    // 특정 Progression에 포함된 Chord를 중복 없이, 순서 무관하게 조회
    public List<ReadChordDTO> findChordsByProgressionId(Long progressionId) {
        List<Chord> chords = em.createQuery("SELECT map.chord FROM ChordProgressionMap map WHERE map.progression.id = :progressionId", Chord.class)
                .setParameter("progressionId", progressionId).getResultList();

        List<ReadChordDTO> dtoList = new ArrayList<>();
        for (Chord chord : chords) {
            dtoList.add(new ReadChordDTO(chord));
        }

        return dtoList;
    }

    public void delete(Long chordId) {
        Chord chord = findOne(chordId);
        em.remove(chord);
    }
}
