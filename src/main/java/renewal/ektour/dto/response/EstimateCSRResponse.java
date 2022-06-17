package renewal.ektour.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor
public class EstimateCSRResponse {

    private Long id;            // 순번
    private String name;        // 등록자
    private String travelType;  // 여행 구분
    private String departPlace; // 출발지
    private String arrivalPlace;// 도착지
    private String vehicleType; // 차량 구분
    private String createdDate; // 요청일

    @Builder
    public EstimateCSRResponse(Long id, String name, String travelType, String departPlace, String arrivalPlace, String vehicleType, String createdDate) {
        this.id = id;
        this.name = name;
        this.travelType = travelType;
        this.departPlace = departPlace;
        this.arrivalPlace = arrivalPlace;
        this.vehicleType = vehicleType;
        this.createdDate = createdDate;
    }

}
