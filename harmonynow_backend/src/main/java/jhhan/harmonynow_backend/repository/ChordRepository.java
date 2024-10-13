package jhhan.harmonynow_backend.repository;

import jakarta.persistence.EntityManager;
import jhhan.harmonynow_backend.domain.Chord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
        return em.createQuery("select c from Chord c", Chord.class).getResultList();
    }

    public void delete(Long chordId) {
        Chord chord = findOne(chordId);
        em.remove(chord);
    }
}
