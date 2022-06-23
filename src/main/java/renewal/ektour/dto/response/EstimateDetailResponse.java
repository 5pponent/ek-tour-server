package renewal.ektour.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EstimateDetailResponse {
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

    private String createdDate;
}
