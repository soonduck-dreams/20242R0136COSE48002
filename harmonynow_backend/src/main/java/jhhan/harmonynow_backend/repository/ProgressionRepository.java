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
        return em.createQuery("select p from Progression p order by p.id", Progression.class).getResultList();
    }

    public void delete(Long progressionId) {
        Progression progression = findOne(progressionId);
        em.remove(progression);
    }

    public List<Progression> findAllCadenceProgressionsWithSampleMidi() {
        return em.createQuery(
                "select p from Progression p where p.sampleMidiUrl is not null and p.sampleMidiUrl <> '' and p.isCadence order by p.id",
                Progression.class
        ).getResultList();
    }
}
