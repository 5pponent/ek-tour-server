package renewal.ektour.dto.request;

import lombok.*;
import renewal.ektour.domain.Estimate;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EstimateRequest {

    // 신청자 정보
    @NotBlank
    private String name;

    @Email(message = "이메일 형식이 아닙니다")
    private String email;

    @NotBlank
    @Size(min = 11, max = 11, message = "01012341234 형식이 아닙니다")
    private String phone;

    @NotBlank
    @Size(min = 4, max = 4, message = "비밀번호는 4자리로 설정해주세요")
    @PositiveOrZero(message = "비밀번호는 0~9자리 숫자로만 가능합니다")
    private String password;

    // 필수 견적 요청
    @NotEmpty
    private String vehicleType; // 25인승 소형, 28인승 리무진, 45인승 대형
    @NotNull
    private int vehicleNumber; // 차량대수
    @NotNull
    private int memberCount; // 인원수
    @NotEmpty
    private String departDate; // 출발일시
    @NotEmpty
    private String arrivalDate; // 도착일시
    @NotEmpty
    private String departPlace; // 출발지
    @NotEmpty
    private String arrivalPlace; // 도착지
    private String memo; // 기타메모

    // 선택 견적 요청
    private String travelType; // 일반여행, 관혼상제, 학교단체, 기타단체
    private String stopPlace; // 경유지
    private String wayType; // 왕복구분 : 왕복(round), 편도(one-way)
    private String payment; // 결제방법 : 현금(cash), 카드(credit)
    private String taxBill; // 세금계산서 : 발급(true), 발급안함(false)

    private String ip;

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
                .stopPlace(stopPlace)
                .wayType(wayType)
                .payment(payment)
                .taxBill(taxBill)
                .visibility(true)
                .ip(ip)
                .build();
    }
}
