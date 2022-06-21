package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.dto.request.FindEstimateRequest;
import renewal.ektour.dto.response.EstimateListResponse;
import renewal.ektour.dto.response.EstimateSimpleResponse;
import renewal.ektour.repository.EstimateRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstimateService {
    private final EstimateRepository repository;

    /**
     * 견적요청 생성(저장)
     */
    @Transactional
    public Estimate createAndSave(EstimateRequest form) {
        Estimate savedEstimate = repository.save(form.toEntity());
        log.info("견적 저장 = {}", savedEstimate.toDetailResponse().toString());
        return savedEstimate;
    }

    /**
     * 견적 요청 상세 조회
     */
    // 하나 조회 (상세 페이지용)
    public Estimate findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    /**
     * 견적 요청 목록 조회
     */
    private EstimateListResponse makeEstimateListResponse(Pageable pageable, Page<Estimate> estimates) {
        int totalEstimateCount = repository.countAll();
        int totalPage = estimates.getTotalPages();
        List<EstimateSimpleResponse> result = new ArrayList<>();
        estimates.forEach(estimate -> result.add(estimate.toSimpleResponse()));
        int currentPageEstimateCount = result.size();
        return EstimateListResponse.builder()
                .currentPageCount(currentPageEstimateCount)
                .totalCount(totalEstimateCount)
                .estimateList(result)
                .currentPage(pageable.getPageNumber())
                .totalPage(totalPage)
                .build();
    }

    // 리액트 클라이언트로 내려지는 견적요청 목록 (페이징)
    public EstimateListResponse findAllByPage(Pageable pageable) {
        Page<Estimate> estimates = repository.findAll(pageable);
        return makeEstimateListResponse(pageable, estimates);
    }

    // TODO SSR 목록 조회 (페이징)

    // 클라이언트 검색
    public EstimateListResponse searchClient(String searchType, String keyword, Pageable pageable) {
        switch (searchType) {
            case "name" :
                return makeEstimateListResponse(pageable, repository.searchAllByName(pageable, keyword));
            case "travelType" :
                return makeEstimateListResponse(pageable, repository.searchAllByTravelType(pageable, keyword));
            case "vehicleType" :
                return makeEstimateListResponse(pageable, repository.searchAllByVehicleType(pageable, keyword));
        }
        
        // 검색 요건이 맞지 않으면 그냥 다 내림
        return findAllByPage(pageable);
    }

    // 클라이언트 내가 쓴 견적 조회
    public EstimateListResponse findAllMyEstimates(FindEstimateRequest form, Pageable pageable) {
        Page<Estimate> estimates = repository.findAllByPhoneAndPassword(pageable, form.getPhone(), form.getPassword());
        return makeEstimateListResponse(pageable, estimates);
    }

    /**
     * 견적요청 삭제 (수정은 필요 없음)
     */
    @Transactional
    public void delete(Long estimateId) {
        Estimate estimate = repository.findById(estimateId).orElseThrow();
        log.info("견적 = {} 삭제(안보이도록 설정)", estimate);
        estimate.setInvisible();
    }
}
