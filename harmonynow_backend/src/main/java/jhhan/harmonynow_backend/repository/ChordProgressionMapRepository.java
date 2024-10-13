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
}
