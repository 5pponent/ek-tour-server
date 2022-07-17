package renewal.ektour.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.AdminSearchForm;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.dto.request.FindEstimateRequest;
import renewal.ektour.dto.response.EstimateDetailResponse;
import renewal.ektour.dto.response.EstimateListPagingResponse;
import renewal.ektour.repository.EstimateRepository;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EstimateServiceTest {

    @Autowired
    EstimateService estimateService;

    @Autowired
    EstimateRepository estimateRepository;

    HttpServletRequest request;

    // 테스트용 pageable 객체, 견적 요청 및 찾기 폼
    int page = 0; int size = 15;
    Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
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
    FindEstimateRequest form = new FindEstimateRequest(testEstimate.getPhone(), testEstimate.getPassword());

    @BeforeEach
    void testRequestInit() {
        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    @Test
    @DisplayName("[createAndSave] 요청 견적 저장")
    void createAndSave() {
        // given

        // when
        Estimate expected = estimateService.createAndSave(request, testEstimate);
        List<Estimate> actual = estimateRepository.findAllByPhoneAndPassword(expected.getPhone(), expected.getPassword());

        // then
        assertThat(actual).contains(expected);
    }

    @Test
    @DisplayName("[findById] id로 견적 상세 조회")
    void findById() {
        // given
        Long id = 1L;

        // when
        Estimate expected = estimateService.findById(id);
        Estimate actual = estimateRepository.findById(id).get();

        // then
        assertThat(actual).isEqualTo(expected);
        assertThatThrownBy(() -> {
            estimateService.findById(0L);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("[findById] form, id로 견적 상세 조회")
    void testFindById() {
        // given
        Estimate insertEstimate = estimateService.createAndSave(request, testEstimate);
        FindEstimateRequest errorForm = new FindEstimateRequest("01012341234", "1234");

        // when
        Estimate expected = estimateService.findById(insertEstimate.getId(), form);
        Estimate actual = estimateRepository.findById(insertEstimate.getId()).get();

        // then
        assertThat(actual).isEqualTo(expected);
        assertThatThrownBy(() -> {
            estimateService.findById(0L, form);
        }).isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(() -> {
            estimateService.findById(insertEstimate.getId(), errorForm);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("[findAllByPage] CSR 페이징 견적 모두 조회")
    void findAllByPage() {
        // given

        // when
        EstimateListPagingResponse expected = estimateService.findAllByPage(pageable);
        Page<Estimate> actual = estimateRepository.findAll(pageable);

        // then
        assertThat(actual.getTotalPages()).isEqualTo(expected.getTotalPage());
        assertThat(actual.getNumber()).isEqualTo(page);
        assertThat(actual.getContent().get(0).toSimpleResponse()).isEqualTo(expected.getEstimateList().get(0));
    }

    @Test
    @DisplayName("[findAllByPageAdmin] SSR 페이징 견적 모두 조회")
    void findAllByPageAdmin() {
        // given

        // when
        Page<Estimate> expected = estimateService.findAllByPageAdmin(pageable);
        Page<Estimate> actual = estimateRepository.findAll(pageable);

        // then
        assertThat(actual.getTotalPages()).isEqualTo(expected.getTotalPages());
        assertThat(actual.getNumber()).isEqualTo(page);
        assertThat(actual.getContent()).isEqualTo(expected.getContent());
    }

    @Test
    @DisplayName("[getAllPageCount] 존재하는 모든 페이지 수 조회")
    void getAllPageCount() {
        // given
        int size = 15;

        // when
        int expected = estimateService.getAllPageCount();
        int actual = estimateRepository.countAll() / size + 1;

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("[findAllMyEstimates] 내 견적 요청 목록 조회")
    void findAllMyEstimates() {
        // given
        estimateService.createAndSave(request, testEstimate);

        // when
        EstimateListPagingResponse expected = estimateService.findAllMyEstimates(pageable, form);
        Page<Estimate> actual = estimateRepository.findAllByPhoneAndPassword(pageable, form.getPhone(), form.getPassword());

        // then
        assertThat(actual.getTotalPages()).isEqualTo(expected.getTotalPage());
        assertThat(actual.getNumber()).isEqualTo(page);
        assertThat(actual.getContent().get(0).toSimpleResponse()).isEqualTo(expected.getEstimateList().get(0));
    }

    @Test
    @DisplayName("[searchByPageAdmin] 관리자 페이지 견적 검색")
    void searchByPageAdmin() {
        // given
        Estimate insertedEstimate = estimateService.createAndSave(request, testEstimate);
        AdminSearchForm form = new AdminSearchForm();
        form.setStart(LocalDate.now());
        form.setEnd(LocalDate.now().plusDays(1L));
        form.setSearchType("");
        form.setKeyword("");

        // when
        Page<Estimate> expected = estimateService.searchByPageAdmin(pageable, form);
        Page<Estimate> actual = estimateRepository.searchAllByDate(pageable, form.getStart().toString(), form.getEnd().toString());

        form.setKeyword("user1");
        Page<Estimate> expected2 = estimateService.searchByPageAdmin(pageable, form);
        Page<Estimate> actual2 = estimateRepository.searchAllByName(pageable, form.getStart().toString(), form.getEnd().toString(), form.getKeyword());

        form.setSearchType("phone");
        form.setKeyword("0100000");
        Page<Estimate> expected3 = estimateService.searchByPageAdmin(pageable, form);
        Page<Estimate> actual3 = estimateRepository.searchAllByPhone(pageable, form.getStart().toString(), form.getEnd().toString(), form.getKeyword());

        // then
        assertThat(actual).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected2);
        assertThat(actual3).isEqualTo(expected3);
        log.info("날짜 검색 : {}", expected.getContent().get(0).toSimpleResponse());
        log.info("이름 검색 : {}", expected2.getContent().get(0).toSimpleResponse());
        log.info("핸드폰 검색 : {}", expected3.getContent().get(0).toSimpleResponse());
    }

    @Test
    @DisplayName("[update] 클라이언트 견적 수정")
    void update() {
        // given
        Estimate insertedEstimate = estimateService.createAndSave(request, testEstimate);
        EstimateRequest updateEstimate = EstimateRequest.builder()
                .name("user2")
                .email("user2@email.com")
                .phone("01011111111")
                .password("1111")
                .vehicleType("25인승 소형")
                .vehicleNumber(2)
                .memberCount(10)
                .departDate("2022-03-02T12:30")
                .arrivalDate("2022-04-02T18:00")
                .departPlace("[충남] 출발지")
                .arrivalPlace("[인천] 도착지")
                .travelType("관혼상제")
                .stopPlace("-")
                .wayType("편도")
                .payment("카드")
                .taxBill("발급안함")
                .build();

        // when
        Estimate expected = estimateService.update(insertedEstimate.getId(), updateEstimate);
        Estimate actual = estimateService.findById(insertedEstimate.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("[update] 관리자 견적 수정")
    void updateByAdmin() {
        // given
        Estimate insertedEstimate = estimateService.createAndSave(request, testEstimate);
        EstimateDetailResponse updateEstimate = EstimateDetailResponse.builder()
                .name("user2")
                .email("user2@email.com")
                .phone("01011111111")
                .password("1111")
                .vehicleType("25인승 소형")
                .vehicleNumber(2)
                .memberCount(10)
                .departDate("2022-03-02T12:30")
                .arrivalDate("2022-04-02T18:00")
                .departPlace("[충남] 출발지")
                .arrivalPlace("[인천] 도착지")
                .travelType("관혼상제")
                .stopPlace("-")
                .wayType("편도")
                .payment("카드")
                .taxBill("발급안함")
                .build();

        // when
        Estimate expected = estimateService.update(insertedEstimate.getId(), updateEstimate);
        Estimate actual = estimateService.findById(insertedEstimate.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("[delete] 클라이언트 페이지에서 견적 삭제")
    void delete() {
        // given
        Estimate insertEstimate = estimateService.createAndSave(request, testEstimate);

        // when
        estimateService.delete(insertEstimate.getId());
        Estimate deletedEstimate = estimateService.findById(insertEstimate.getId());

        // then
        assertThat(deletedEstimate.isVisibility()).isFalse();
        assertThatThrownBy(() -> {
            estimateService.delete(0L);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("[realDelete] 관리자 페이지에서 견적 삭제")
    void realDelete() {
        // given
        Estimate insertEstimate = estimateService.createAndSave(request, testEstimate);

        // when
        estimateService.realDelete(insertEstimate.getId());

        // then
        assertThatThrownBy(() -> {
            estimateService.findById(insertEstimate.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }
}