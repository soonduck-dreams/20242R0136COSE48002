package jhhan.harmonynow_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Audio {

    @Id
    @GeneratedValue
    @Column(name = "audio_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String url;
}
