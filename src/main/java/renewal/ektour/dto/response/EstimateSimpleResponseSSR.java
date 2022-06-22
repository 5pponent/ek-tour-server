package renewal.ektour.dto.response;

import lombok.Data;

@Data
public class EstimateSimpleResponseSSR {

    private Long id;            // 번호
    private String name;        // 요청자
    private String phone;       // 연락처
    private String travelType;  // 여행 구분
    private String departDate;  // 출발일
    private String arrivalDate; // 도착일
    private String departPlace; // 출발지
    private String arrivalPlace;// 도착지
    private String vehicleType; // 차량 구분
    private String createdDate; // 요청일
}
