package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.AdminSearchForm;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.dto.request.FindEstimateRequest;
import renewal.ektour.dto.response.EstimateDetailResponse;
import renewal.ektour.dto.response.EstimateListPagingResponse;
import renewal.ektour.dto.response.EstimateSimpleResponse;
import renewal.ektour.repository.EstimateRepository;
import renewal.ektour.util.IpTracker;
import renewal.ektour.util.PageConfig;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public Estimate createAndSave(HttpServletRequest request, EstimateRequest form) {
        form.setIp(IpTracker.getClientIp(request));
        return repository.save(form.toEntity());
    }

    /**
     * 견적 요청 상세 조회
     */
    public Estimate findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    // 하나 조회 (상세 페이지용)
    public Estimate findById(Long id, FindEstimateRequest form) {
        return repository.findByIdAndPhoneAndPassword(id, form.getPhone(), form.getPassword())
                .orElseThrow(() -> new NoSuchElementException("해당 데이터가 없습니다."));
    }

    /**
     * 견적 요청 목록 조회
     */
    private EstimateListPagingResponse makeEstimateListResponse(Pageable pageable, Page<Estimate> estimates) {
        int totalEstimateCount = repository.countAll();
        int totalPage = estimates.getTotalPages();
        List<EstimateSimpleResponse> result = new ArrayList<>();
        estimates.forEach(estimate -> { if (estimate.isVisibility()) result.add(estimate.toSimpleResponse());});
        int currentPageEstimateCount = result.size();
//        return EstimateListPagingResponse.builder()
//                .currentPageCount(currentPageEstimateCount)
//                .totalCount(totalEstimateCount)
//                .estimateList(result)
//                .currentPage(pageable.getPageNumber())
//                .totalPage(totalPage)
//                .build();
        return new EstimateListPagingResponse(pageable.getPageNumber(), totalPage, currentPageEstimateCount, totalEstimateCount, result);
    }

    // 클라이언트로 내려지는 견적요청 목록 (페이징)
    public EstimateListPagingResponse findAllByPage(Pageable pageable) {
        Page<Estimate> estimates = repository.findAll(pageable);
        return makeEstimateListResponse(pageable, estimates);
    }

    // 관리자페이지 내려지는 견적요청 목록 (페이징)
    public Page<Estimate> findAllByPageAdmin(Pageable pageable) {
        return repository.findAllByAdmin(pageable);
    }

    // 존재하는 모든 페이지 수 조회
    public int getAllPageCount() {
        return (repository.countAll() / PageConfig.PAGE_PER_COUNT) + 1;
    }

    // 클라이언트 내가 쓴 견적 조회
    public EstimateListPagingResponse findAllMyEstimates(Pageable pageable, FindEstimateRequest form) {
        Page<Estimate> estimates = repository.findAllByPhoneAndPassword(pageable, form.getPhone(), form.getPassword());
        return makeEstimateListResponse(pageable, estimates);
    }

    // 관리자페이지 검색
    public Page<Estimate> searchByPageAdmin(Pageable pageable, AdminSearchForm form) {
        if (form.getKeyword().equals("")) {
            return repository.searchAllByDate(pageable, form.getStart().toString(), form.getEnd().toString());
        }
        if (form.getSearchType().equals("phone")) {
            return repository.searchAllByPhone(pageable, form.getStart().toString(), form.getEnd().toString(), form.getKeyword());
        } else {
            return repository.searchAllByName(pageable, form.getStart().toString(), form.getEnd().toString(), form.getKeyword());
        }
    }

    // 클라이언트 내가 쓴 견적 조회 페이징 없이 리스트 전체 반환
    public List<EstimateSimpleResponse> findAllMyEstimates(FindEstimateRequest form) {
        List<Estimate> estimates = repository.findAllByPhoneAndPassword(form.getPhone(), form.getPassword());
        Collections.reverse(estimates);
        List<EstimateSimpleResponse> result = new ArrayList<>();
        for (Estimate e : estimates) {
            result.add(e.toSimpleResponse());
        }
        return result;
    }

    /**
     * 견적요청 수정
     */
    // 사용자 (클라이언트) 에서 수정
    @Transactional
    public Estimate update(Long estimateId, EstimateRequest updateForm) {
        Estimate estimate = findById(estimateId);
        estimate.update(updateForm);
        return estimate;
    }

    // 관리자 페이지에서 수정
    @Transactional
    public Estimate update(Long estimateId, EstimateDetailResponse updateForm) {
        Estimate estimate = findById(estimateId);
        log.info("기존 데이터 : {}", estimate.toDetailResponse().toString());
        estimate.update(updateForm);
        log.info("수정된 데이터 : {}", estimate.toDetailResponse().toString());
        return estimate;
    }

    /**
     * 견적요청 삭제
     */
    @Transactional
    public void delete(Long estimateId) {
        Estimate estimate = repository.findById(estimateId).orElseThrow();
        estimate.setInvisible();
    }

    /**
     * 견적요청 관리자페이지에서 진짜 삭제 (디비삭제)
     */
    @Transactional
    public void realDelete(Long estimateId) {
        repository.deleteById(estimateId);
    }
}
