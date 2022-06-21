package renewal.ektour.domain;

import javax.persistence.*;

@Entity
public class FinalVehicle {

    @Id @GeneratedValue
    @Column(name = "final_vehicle_id")
    private Long id;

    // 일자는 빠져도 된다고함
    private String content; // 내용 (출발지~도착지)
    private String type; // 규격 (몇 인승인지)
    private int vehicleNumber; // 차량 댓수
    // 일수도 빼자
    private Long unitPrice; // 단가
    private Long totalPrice; // 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "final_estimate_id")
    private FinalEstimate finalEstimate;

}
