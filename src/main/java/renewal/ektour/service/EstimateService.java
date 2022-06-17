package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.dto.response.EstimateCSRResponse;
import renewal.ektour.repository.EstimateRepository;

import java.util.*;

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
     * 견적 요청 상세 조회
     */
    // 하나 조회 (상세 페이지용)
    public Estimate findById(Long id) {
        return estimateRepository.findById(id).orElseThrow();
    }

    /**
     * 견적 요청 목록 조회
     */
    // CSR 목록 조회 (페이징)
    public List<EstimateCSRResponse> getEstimates(Integer page) {
        Page<Estimate> estimates = estimateRepository.findAll(PageRequest.of(page, 15, Sort.by("id").descending()));
        List<EstimateCSRResponse> result = new ArrayList<>();
        for (Estimate estimate : estimates.getContent()) {
            EstimateCSRResponse data = EstimateCSRResponse.builder()
                    .id(estimate.getId())
                    .name(estimate.getName())
                    .travelType(estimate.getTravelType())
                    .arrivalPlace(estimate.getArrivalPlace())
                    .departPlace(estimate.getDepartPlace())
                    .vehicleType(estimate.getVehicleType())
                    .createdDate(estimate.getCreateDate())
                    .build();
            result.add(data);
        }
        // 없는 페이지 요청 시 예외 발생
        if (result.size() == 0) {
            throw new NoSuchElementException();
        }
        return result;
    }

    // TODO SSR 목록 조회 (페이징)

    /**
     * 견적요청 삭제 (수정은 필요 없음)
     */
    public void delete(Long estimateId) {
        Estimate estimate = estimateRepository.findById(estimateId).orElseThrow();
        log.info("견적 = {} 삭제(안보이도록 설정)", estimate);
        estimate.setInvisible();
    }
}
