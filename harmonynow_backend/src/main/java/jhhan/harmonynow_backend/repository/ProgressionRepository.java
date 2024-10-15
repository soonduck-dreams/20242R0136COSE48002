package jhhan.harmonynow_backend.repository;

import jakarta.persistence.EntityManager;
import jhhan.harmonynow_backend.domain.Progression;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProgressionRepository {

    private final EntityManager em;

    public Long save(Progression progression) {
        em.persist(progression);
        return progression.getId();
    }

    public Progression findOne(Long progressionId) {
        return em.find(Progression.class, progressionId);
    }

    public List<Progression> findAll() {
        return em.createQuery("select p from Progression p", Progression.class).getResultList();
    }
}
