package jhhan.harmonynow_backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class Article extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "article_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String audio_url;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "progression_id", nullable = false)
    private Progression progression;
}
