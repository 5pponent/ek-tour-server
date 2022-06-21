package renewal.ektour.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class FinalEstimate {

    @Id @GeneratedValue
    @Column(name = "final_estimate_id")
    private Long id;

    private String receiver; // 수신
    private String email; // 이메일
    private String phone; // 연락처
    private String companyName; // 회사명
    private String estimateDate; // 견적일
    private String validDate; // 유효일 (견적일 + 7일)

    // 차량
    @OneToMany(mappedBy = "finalEstimate", cascade = CascadeType.ALL)
    private List<FinalVehicle> vehicles = new ArrayList<>();

    private Long firstPriceSum; // 소계1

    // 기타
    @OneToMany(mappedBy = "finalEstimate", cascade = CascadeType.ALL)
    private List<FinalEtc> etcs = new ArrayList<>();

    private Long secondPriceSum; // 소계2
    private Long vat; // 부가세

    private Long finalPrice; // 최종금액 (부가세포함)

}
