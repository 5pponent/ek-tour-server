package renewal.ektour.domain;

import javax.persistence.*;

@Entity
public class FinalEtc {

    @Id @GeneratedValue
    @Column(name = "final_etc_id")
    private Long id;

    private String content; // 내용
    private int amount; // 댓수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_estimate_id")
    private FinalEstimate finalEstimate;
}
