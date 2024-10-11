package jhhan.harmonynow_backend.repository;

import jakarta.persistence.EntityManager;
import jhhan.harmonynow_backend.domain.Chord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChordRepository {

    private final EntityManager em;

    public Long save(Chord chord) {
        em.persist(chord);
        return chord.getId();
    }
}
