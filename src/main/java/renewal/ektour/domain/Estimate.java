package renewal.ektour.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import renewal.ektour.dto.response.EstimateResponse;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
    private String departDate; // 출발일시
    private String arrivalDate; // 도착일시
    private String departPlace; // 출발지
    private String arrivalPlace; // 도착지
    private String memo; // 기타메모

    // 선택 견적 요청
    private String stopPlace; // 경유지
    private String wayType; // 왕복구분 : 왕복(round), 편도(one-way)
    private String payment; // 결제방법 : 현금(cash), 카드(credit)
    private boolean taxBill; // 세금계산서 : 발급(true), 발급안함(false)

    // 견적 요청 삭제여부 (사용자에게 보여지는 여부)
    private boolean visibility;

    // 견적 요청일
    @CreatedDate
    private String createDate;

    @PrePersist
    public void onPrePersist(){
        this.createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Builder
    public Estimate(String name, String email, String phone, String password, String travelType, String vehicleType, int vehicleNumber, int memberCount, String departDate, String arrivalDate, String departPlace, String arrivalPlace, String memo, String stopPlace, String wayType, String payment, boolean taxBill, String createdDate) {
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
        this.visibility = true;
    }

    public EstimateResponse toResponse() {
        return EstimateResponse.builder()
                .name(name)
                .email(email)
                .phone(phone)
                .password(password)
                .travelType(travelType)
                .vehicleType(vehicleType)
                .vehicleNumber(vehicleNumber)
                .memberCount(memberCount)
                .departDate(departDate)
                .arrivalDate(arrivalDate)
                .departPlace(departPlace)
                .arrivalPlace(arrivalPlace)
                .memo(memo)
                .stopPlace(stopPlace)
                .wayType(wayType)
                .payment(payment)
                .taxBill(taxBill)
                .visibility(visibility)
                .build();
    }

    /**
     * 비즈니스 로직 (by dirty checking)
     */
    // 견적 요청 삭제 (보여지는걸 숨기기)
    public void setInvisible() {
        this.visibility = false;
    }
}
