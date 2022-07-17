package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.EstimateRequest;
import renewal.ektour.dto.request.FindEstimateRequest;
import renewal.ektour.dto.response.EstimateDetailResponse;
import renewal.ektour.dto.response.EstimateListPagingResponse;
import renewal.ektour.dto.response.EstimateSimpleResponse;
import renewal.ektour.dto.response.PageTotalCountResponse;
import renewal.ektour.exception.ValidationException;
import renewal.ektour.service.EmailService;
import renewal.ektour.service.EstimateService;
import renewal.ektour.util.PageConfig;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static renewal.ektour.dto.response.RestResponse.success;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/estimate")
public class ClientEstimateController {

    private final EstimateService estimateService;
    private final EmailService emailService;

    /**
     * 견적요청 생성(저장)
     */
    @PostMapping("")
    public EstimateDetailResponse saveAndAlarm(@Valid @RequestBody EstimateRequest form,
                                          BindingResult bindingResult,
                                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        Estimate savedEstimate = estimateService.createAndSave(request, form);
        emailService.sendMail(form);
        return savedEstimate.toDetailResponse();
    }


    /**
     * 견적 요청 상세 조회 (핸드폰 번호, 비밀번호와 함께 요청)
     */
    @PostMapping("/{estimateId}")
    public EstimateDetailResponse findById(@PathVariable("estimateId") Long estimateId,
                                      @Valid @RequestBody FindEstimateRequest form,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);

        return estimateService.findById(estimateId, form).toDetailResponse();
    }

    /**
     * 견적 요청 상세 조회 (폼 없이)
     */
    @GetMapping("/{estimateId}")
    public EstimateDetailResponse findById(@PathVariable("estimateId") Long estimateId) {
        return estimateService.findById(estimateId).toDetailResponse();
    }

    /**
     * 견적 요청 목록 조회
     */
    // 클라이언트 견적요청 목록 조회 (페이징)
    @GetMapping("/all")
    public EstimateListPagingResponse findAllByPageClient(
            @PageableDefault(
                    size = PageConfig.PAGE_PER_COUNT,
                    sort = PageConfig.SORT_STANDARD,
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {
        return estimateService.findAllByPage(pageable);
    }

    // 존재하는 전체 페이지 수 조회
    @GetMapping("/all/page")
    public PageTotalCountResponse getAllPageCount() {
        return new PageTotalCountResponse(estimateService.getAllPageCount());
    }

    // 클라이언트 내가 쓴 견적 요청 목록 조회 (페이징 X, 리스트 전체 반환)
    @PostMapping("/search/my")
    public List<EstimateSimpleResponse> findAllMyEstimates(@Valid @RequestBody FindEstimateRequest form,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new ValidationException(bindingResult);
        return estimateService.findAllMyEstimates(form);
    }

    // 클라이언트 내가 쓴 견적 요청 목록 조회 (페이징 O)
    @PostMapping("/search/my/all")
    public EstimateListPagingResponse findAllMyEstimatesPaging(
            @Valid @RequestBody FindEstimateRequest form,
            @PageableDefault(
                    size = PageConfig.PAGE_PER_COUNT,
                    sort = PageConfig.SORT_STANDARD,
                    direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return estimateService.findAllMyEstimates(pageable, form);
    }

    /**
     * 견적요청 수정
     */
    @PutMapping("/{estimateId}")
    public EstimateDetailResponse updateById(
            @PathVariable("estimateId") Long estimateId,
            @Valid @RequestBody EstimateRequest updateForm
    ) {
        return estimateService.update(estimateId, updateForm).toDetailResponse();
    }

    /**
     * 견적요청 삭제 - 안 보이도록 설정
     */
    @DeleteMapping("/{estimateId}")
    public ResponseEntity<?> deleteById(@PathVariable("estimateId") Long estimateId) {
        estimateService.delete(estimateId);
        return success();
    }

    /**
     * 견적요청 알림 보내기
     */
    @PostMapping("/alarm")
    public ResponseEntity<?> alarm(@RequestBody EstimateRequest form) {
        emailService.sendMail(form);
        return success();
    }
}
