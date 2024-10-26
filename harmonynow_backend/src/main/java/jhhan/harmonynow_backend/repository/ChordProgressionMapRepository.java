package jhhan.harmonynow_backend.repository;

import jakarta.persistence.EntityManager;
import jhhan.harmonynow_backend.domain.ChordProgressionMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
