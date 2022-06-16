package renewal.ektour.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import renewal.ektour.domain.estimate.Estimate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class EstimateSimpleRequest {
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

    /**
    // 선택 견적 요청
    private String stopPlace; // 경유지
    private WayType wayType; // 왕복구분 : 왕복(round), 편도(one-way)
    private Payment payment; // 결제방법 : 현금(cash), 카드(credit)
    private boolean taxBill; // 세금계산서 : 발급(true), 발급안함(false)
     */

    public Estimate toEntity() {
        return Estimate.builder()
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
                .stopPlace(null)
                .wayType(null)
                .payment(null)
                .taxBill(false)
                .build();
    }
}
