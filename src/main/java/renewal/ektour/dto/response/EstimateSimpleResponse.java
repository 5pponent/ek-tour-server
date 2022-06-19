package renewal.ektour.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class EstimateSimpleResponse {
    private String name;        // 등록자
    private String travelType;  // 여행 구분
    private String departPlace; // 출발지
    private String arrivalPlace;// 도착지
    private String vehicleType; // 차량 구분
    private String createdDate; // 요청일
}
