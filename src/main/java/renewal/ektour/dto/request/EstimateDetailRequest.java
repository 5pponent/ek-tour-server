package renewal.ektour.dto.request;

import lombok.*;
import renewal.ektour.domain.estimate.Estimate;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EstimateDetailRequest extends EstimateSimpleRequest {
    /**
    // 신청자 정보
    private String name;
    private String email;
    private String phone;
    private String password;

    // 필수 견적 요청
    private TravelType travelType; // 일반여행, 관혼상제, 학교단체, 기타단체
    private VehicleType vehicleType; // 25인승 소형, 28인승 리무진, 45인승 대형
    private int vehicleNumber; // 차량대수
    private int memberCount; // 인원수
    private LocalDateTime departDate; // 출발일시
    private LocalDateTime arrivalDate; // 도착일시
    private String departPlace; // 출발지
    private String arrivalPlace; // 도착지
    private String memo; // 기타메모
     */

    // 선택 견적 요청
    private String stopPlace; // 경유지
    private String wayType; // 왕복구분 : 왕복(round), 편도(one-way)
    private String payment; // 결제방법 : 현금(cash), 카드(credit)
    private boolean taxBill; // 세금계산서 : 발급(true), 발급안함(false)

    public EstimateDetailRequest(String name, String email, String phone, String password, String travelType, String vehicleType, int vehicleNumber, int memberCount, LocalDateTime departDate, LocalDateTime arrivalDate, String departPlace, String arrivalPlace, String memo) {
        super(name, email, phone, password, travelType, vehicleType, vehicleNumber, memberCount, departDate, arrivalDate, departPlace, arrivalPlace, memo);
    }

    public Estimate toEntity() {
        return Estimate.builder()
                .name(super.getName())
                .email(super.getEmail())
                .phone(super.getPhone())
                .password(super.getPassword())
                .travelType(super.getTravelType())
                .vehicleType(super.getVehicleType())
                .vehicleNumber(super.getVehicleNumber())
                .memberCount(super.getMemberCount())
                .departDate(super.getDepartDate())
                .arrivalDate(super.getArrivalDate())
                .departPlace(super.getDepartPlace())
                .arrivalPlace(super.getArrivalPlace())
                .memo(super.getMemo())
                .stopPlace(stopPlace)
                .wayType(wayType)
                .payment(payment)
                .taxBill(taxBill)
                .build();
    }
}
