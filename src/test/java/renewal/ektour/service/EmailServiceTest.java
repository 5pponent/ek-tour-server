package renewal.ektour.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import renewal.ektour.dto.request.EstimateRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    @DisplayName("이메일 송신")
    void sendMail() {
        // given
        EstimateRequest testEstimate = EstimateRequest.builder()
                .name("user1")
                .email("user1@email.com")
                .phone("01000000000")
                .password("1234")
                .vehicleType("25인승 소형")
                .vehicleNumber(1)
                .memberCount(5)
                .departDate("2022-03-01T12:30")
                .arrivalDate("2022-04-01T18:00")
                .departPlace("[서울] 출발지")
                .arrivalPlace("[서울] 도착지")
                .travelType("일반여행")
                .stopPlace("없음")
                .wayType("왕복")
                .payment("현금")
                .taxBill("발급")
                .build();

        // then
        assertThatNoException().isThrownBy(() -> {
            emailService.sendMail(testEstimate);
        });
    }
}