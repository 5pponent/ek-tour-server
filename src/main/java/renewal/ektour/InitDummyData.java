package renewal.ektour;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.service.AdminService;
import renewal.ektour.service.EstimateService;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitDummyData {

    private final EstimateService estimateService;
    private final AdminService adminService;

    @PostConstruct
    public void initData() {
        // 관리자 설정
        adminService.createAdmin("1234");

        // 견적 더미 데이터 생성
        for (int i = 0; i < 6; i++) {
            String idx = String.valueOf(i + 1);
            EstimateRequest data = EstimateRequest.builder()
                    .name("user" + idx)
                    .password("1234")
                    .phone("01088725482")
                    .build();
            estimateService.createAndSave(data);
        }
        for (int i = 6; i < 11; i++) {
            String idx = String.valueOf(i + 1);
            EstimateRequest data = EstimateRequest.builder()
                    .name("user" + idx)
                    .password("4567")
                    .phone("01012341234")
                    .build();
            estimateService.createAndSave(data);
        }
        estimateService.createAndSave(EstimateRequest.builder()
                .name("배윤재")
                .phone("01021287755")
                .password("7755")
                .email("test@naver.com")
                .travelType("일반여행")
                .memberCount(20)
                .vehicleType("25인승 소형")
                .vehicleNumber(1)
                .departDate("2022-06-24")
                .arrivalDate("2022-06-25")
                .departPlace("경기 하남대로 947")
                .arrivalPlace("전북 무주")
                .wayType("왕복")
                .payment("현금")
                .taxBill(true)
                .memo("15인 이상 견적싼걸로 부탁드리겠습니다.")
                .build());
        estimateService.createAndSave(EstimateRequest.builder()
                .name("이지수")
                .phone("01024006992")
                .password("5123")
                .email("hom1994@smu.ac.kr")
                .travelType("학교단체")
                .memberCount(90)
                .vehicleType("45인승 대형")
                .vehicleNumber(2)
                .departDate("2022-06-24")
                .arrivalDate("2022-06-25")
                .departPlace("서울 상명대학교")
                .arrivalPlace("경기 가평군 북면 노씨터길 12")
                .wayType("왕복")
                .payment("현금")
                .taxBill(true)
                .memo("견적서 메일로 보내주시면 감사하겠습니다 메일 주소 : hom1994@smu.ac.kr")
                .build());
        estimateService.createAndSave(EstimateRequest.builder()
                .name("이종호")
                .phone("01074471326")
                .password("0373")
                .email("mmm@naver.com")
                .travelType("일반여행")
                .memberCount(56)
                .vehicleType("28인승 리무진")
                .vehicleNumber(2)
                .departDate("2022-07-02")
                .arrivalDate("2022-07-02")
                .departPlace("대구")
                .arrivalPlace("서울")
                .wayType("왕복")
                .payment("현금")
                .taxBill(true)
                .memo("")
                .build());
    }
}
