package jhhan.harmonynow_backend.repository;

import jakarta.persistence.EntityManager;
import jhhan.harmonynow_backend.domain.Progression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProgressionRepository {

    private final EntityManager em;

    public Long save(Progression progression) {
        em.persist(progression);
        return progression.getId();
    }
}
