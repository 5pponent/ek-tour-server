package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.repository.EstimateRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class EstimateService {
    private final EstimateRepository estimateRepository;

    /**
     * 견적요청 생성(저장)
     */
    public Estimate save(EstimateRequest form) {
        Estimate savedEstimate = estimateRepository.save(form.toEntity());
        log.info("견적 저장 = {}", savedEstimate.toResponse().toString());
        return savedEstimate;
    }

    /**
     * 견적요청 조회
     */

    /**
     * 견적요청 삭제 (수정은 필요 없음)
     */
    public void delete(Long estimateId) {
        Estimate estimate = estimateRepository.findById(estimateId).orElseThrow();
        log.info("견적 = {} 삭제(안보이도록 설정)", estimate);
        estimate.setInvisible();
    }
}
