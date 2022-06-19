package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.EstimateRequest;
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
    private final EstimateRepository estimateRepository;

    /**
     * 견적요청 생성(저장)
     */
    @Transactional
    public Estimate createAndSave(EstimateRequest form) {
        Estimate savedEstimate = estimateRepository.save(form.toEntity());
        log.info("견적 저장 = {}", savedEstimate.toDetailResponse().toString());
        return savedEstimate;
    }

    /**
     * 견적 요청 상세 조회
     */
    // 하나 조회 (상세 페이지용)
    public Estimate findById(Long id) {
        return estimateRepository.findById(id).orElseThrow();
    }

    /**
     * 견적 요청 목록 조회
     */
    // 리액트 클라이언트로 내려지는 견적요청 목록 (페이징)
    public EstimateListResponse findAllByPage(Pageable pageable) {
        Page<Estimate> estimates = estimateRepository.findAll(pageable);
        int totalEstimateCount = estimateRepository.countAll();
        int currentPageEstimateCount = estimates.getSize();
        int totalPage = estimates.getTotalPages();
        List<EstimateSimpleResponse> result = new ArrayList<>();
        estimates.forEach(estimate -> result.add(estimate.toSimpleResponse()));
        return EstimateListResponse.builder()
                .currentPageCount(currentPageEstimateCount)
                .totalCount(totalEstimateCount)
                .estimateList(result)
                .currentPage(pageable.getPageNumber())
                .totalPage(totalPage)
                .build();
    }

    // TODO SSR 목록 조회 (페이징)

    /**
     * 견적요청 삭제 (수정은 필요 없음)
     */
    @Transactional
    public void delete(Long estimateId) {
        Estimate estimate = estimateRepository.findById(estimateId).orElseThrow();
        log.info("견적 = {} 삭제(안보이도록 설정)", estimate);
        estimate.setInvisible();
    }
}
