package renewal.ektour.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.dto.request.FindEstimateRequest;
import renewal.ektour.service.EstimateService;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClientEstimateControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    EstimateService estimateService;

    @BeforeEach
    void setUp() {
        HttpServletRequest mockRequest = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        Estimate andSave = estimateService.createAndSave(mockRequest, EstimateRequest.builder()
                .name("테스트")
                .email("test@naver.com")
                .phone("01012341234")
                .password("1234")
                .vehicleType("25인승 소형")
                .vehicleNumber(1)
                .memberCount(1)
                .departDate("2022-03-01")
                .arrivalDate("2022-04-01")
                .departPlace("용인")
                .arrivalPlace("천안")
                .memo("메모")
                .travelType("일반여행")
                .stopPlace("없음")
                .wayType("왕복")
                .payment("현금")
                .taxBill("발급")
                .ip("123.123.123")
                .build());
    }

    @Test
    @DisplayName("견적요청 생성(저장) 성공")
    void saveAndAlarmTest1() throws Exception {
        // given

        // when
        String body = mapper.writeValueAsString(EstimateRequest.builder()
                .name("테스트")
                .email("test@naver.com")
                .phone("01012341234")
                .password("1234")
                .vehicleType("25인승 소형")
                .vehicleNumber(1)
                .memberCount(1)
                .departDate("2022-03-01")
                .arrivalDate("2022-04-01")
                .departPlace("용인")
                .arrivalPlace("천안")
                .memo("메모")
                .travelType("일반여행")
                .stopPlace("없음")
                .wayType("왕복")
                .payment("현금")
                .taxBill("발급")
                .ip("123.123.123")
                .build());

        // then
        mvc.perform(post("/estimate")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departDate").value("2022-03-01"))
                .andDo(print());

    }

    @Test
    @DisplayName("견적요청 생성(저장) 실패 : 유효성 검증")
    void saveAndAlarmTest2() throws Exception {
        // given

        // when
        String body = mapper.writeValueAsString(EstimateRequest.builder()
                .name(null)
                .email(null)
                .phone(null)
                .password(null)
                .vehicleType(null)
                .vehicleNumber(1)
                .memberCount(1)
                .departDate(null)
                .arrivalDate(null)
                .departPlace(null)
                .arrivalPlace(null)
                .memo("메모")
                .travelType("일반여행")
                .stopPlace("없음")
                .wayType("왕복")
                .payment("현금")
                .taxBill("발급")
                .ip("123.123.123")
                .build());

        // then
        mvc.perform(post("/estimate")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("견적 요청 상세 조회")
    void findById() throws Exception {
        // given

        // when
        FindEstimateRequest form = new FindEstimateRequest("01012341234", "1234");
        String body = mapper.writeValueAsString(form);

        // then
        mvc.perform(post("/estimate/1")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("클라이언트 견적요청 목록 조회 (페이징)")
    void findAllByPageClient() throws Exception {
        mvc.perform(get("/estimate/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하는 전체 페이지 수 조회")
    void getAllPageCount() throws Exception {
        mvc.perform(get("/estimate/all/page"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").isNotEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("견적요청 수정 - 조회 실패")
    void updateById() throws Exception {
        mvc.perform(put("/estimate/{estimateId}",298473984)
                        .content("null")
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("견적요청 삭제 - 안 보이도록 설정")
    void deleteById() throws Exception {
        mvc.perform(delete("/estimate/{estimateId}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andDo(print());
    }

}
