package renewal.ektour.domain.estimate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estimate {

    @Id @GeneratedValue
    @Column(name = "estimate_id")
    private Long id;

    // 신청자 정보
    private String name;
    private String email;
    private String phone;
    private String password;

    // 필수 견적 요청
    private String travelType; // 일반여행, 관혼상제, 학교단체, 기타단체
    private String vehicleType; // 25인승 소형, 28인승 리무진, 45인승 대형
    private int vehicleNumber; // 차량대수
    private int memberCount; // 인원수
    private LocalDateTime departDate; // 출발일시
    private LocalDateTime arrivalDate; // 도착일시
    private String departPlace; // 출발지
    private String arrivalPlace; // 도착지
    private String memo; // 기타메모

    // 선택 견적 요청
    private String stopPlace; // 경유지
    private String wayType; // 왕복구분 : 왕복(round), 편도(one-way)
    private String payment; // 결제방법 : 현금(cash), 카드(credit)
    private boolean taxBill; // 세금계산서 : 발급(true), 발급안함(false)

    @Builder

    public Estimate(String name, String email, String phone, String password, String travelType, String vehicleType, int vehicleNumber, int memberCount, LocalDateTime departDate, LocalDateTime arrivalDate, String departPlace, String arrivalPlace, String memo, String stopPlace, String wayType, String payment, boolean taxBill) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.travelType = travelType;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.memberCount = memberCount;
        this.departDate = departDate;
        this.arrivalDate = arrivalDate;
        this.departPlace = departPlace;
        this.arrivalPlace = arrivalPlace;
        this.memo = memo;
        this.stopPlace = stopPlace;
        this.wayType = wayType;
        this.payment = payment;
        this.taxBill = taxBill;
    }
}
