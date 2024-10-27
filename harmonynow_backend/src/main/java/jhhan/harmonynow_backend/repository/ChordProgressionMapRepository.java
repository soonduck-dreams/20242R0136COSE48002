package jhhan.harmonynow_backend.repository;

import jakarta.persistence.EntityManager;
import jhhan.harmonynow_backend.domain.Chord;
import jhhan.harmonynow_backend.domain.ChordProgressionMap;
import jhhan.harmonynow_backend.domain.Progression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChordProgressionMapRepository {

    private final EntityManager em;

    public Long save(ChordProgressionMap chordProgressionMap) {
        em.persist(chordProgressionMap);
        return chordProgressionMap.getId();
    }

    public void delete(Long chordProgressionMapId) {
        ChordProgressionMap map = em.find(ChordProgressionMap.class, chordProgressionMapId);
        em.remove(map);
    }

    public Long countByChordId(Long chordId) {
        return em.createQuery("SELECT COUNT(map) FROM ChordProgressionMap map WHERE map.chord.id = :chordId", Long.class)
                .setParameter("chordId", chordId)
                .getSingleResult();
    }

    public List<Progression> findProgressionsByChord(Chord chord) {
        return em.createQuery(
                        "SELECT m.progression FROM ChordProgressionMap m WHERE m.chord = :chord", Progression.class)
                .setParameter("chord", chord)
                .getResultList();
    }
}
