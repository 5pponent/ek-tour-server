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
import renewal.ektour.dto.response.BoolResponse;
import renewal.ektour.dto.response.EstimateListPagingResponse;
import renewal.ektour.dto.response.EstimateListResponse;
import renewal.ektour.dto.response.PageTotalCountResponse;
import renewal.ektour.service.EmailService;
import renewal.ektour.service.EstimateService;
import renewal.ektour.util.PageConfig;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

import static renewal.ektour.dto.response.ErrorResponse.convertJson;
import static renewal.ektour.dto.response.RestResponse.badRequest;
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
    public ResponseEntity<?> saveAndAlarm(@Valid @RequestBody EstimateRequest form, BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        if (bindingResult.hasErrors()) {
            log.error("estimate validation errors = {}", bindingResult.getFieldErrors());
            return badRequest(convertJson(bindingResult.getFieldErrors()));
        }
        Estimate savedEstimate = estimateService.createAndSave(form);
        emailService.sendMail(form);
        return success(savedEstimate.toDetailResponse());
    }

    /**
     * 견적 요청 상세 조회
     */
    @GetMapping("/{estimateId}")
    public ResponseEntity<?> findById(@PathVariable("estimateId") Long estimateId) {
        Estimate findEstimate = estimateService.findById(estimateId);
        return success(findEstimate.toDetailResponse());
    }

    /**
     * 견적 요청 목록 조회
     */
    // 클라이언트 견적요청 목록 조회 (페이징)
    @GetMapping("/all")
    public ResponseEntity<?> findAllByPageClient(
            @PageableDefault(size = PageConfig.PAGE_PER_COUNT, sort = PageConfig.SORT_STANDARD, direction = Sort.Direction.DESC) Pageable pageable) {
        EstimateListPagingResponse estimateList = estimateService.findAllByPage(pageable);
        return success(estimateList);
    }

    // 존재하는 전체 페이지 수 조회
    @GetMapping("/all/page")
    public ResponseEntity<?> getAllPageCount() {
        return success(new PageTotalCountResponse(estimateService.getAllPageCount()));
    }

    /**
     * 검색
     */
    // 클라이언트 검색
    @GetMapping("/search/{searchType}/{keyword}")
    public ResponseEntity<?> searchClient(@PathVariable("searchType") String searchType,
                                          @PathVariable("keyword") String keyword,
                                          @PageableDefault(size = PageConfig.PAGE_PER_COUNT, sort = PageConfig.SORT_STANDARD, direction = Sort.Direction.DESC) Pageable pageable) {
        EstimateListPagingResponse estimateListResponse = estimateService.searchClient(searchType, keyword, pageable);
        return success(estimateListResponse);
    }

    // 클라이언트 내가 쓴 견적요청 조회
    @PostMapping("/search/my")
    public ResponseEntity<?> findAllMyEstimates(@Valid @RequestBody FindEstimateRequest form,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("find estimate form validation errors = {}", bindingResult.getFieldErrors());
            return badRequest(convertJson(bindingResult.getFieldErrors()));
        }
        EstimateListResponse estimates = estimateService.findAllMyEstimates(form);
        return success(estimates);
    }

    /**
     * 견적요청 수정
     */
    @PutMapping("/{estimateId}")
    public ResponseEntity<?> updateById(@PathVariable("estimateId") Long estimateId,
                                        @Valid @RequestBody EstimateRequest updateForm) {
        Estimate updatedEstimate = estimateService.update(estimateId, updateForm);
        return success(updatedEstimate.toDetailResponse());
    }


    /**
     * 견적요청 삭제 - 안 보이도록 설정
     */
    @DeleteMapping("/{estimateId}")
    public ResponseEntity<?> deleteById(@PathVariable("estimateId") Long estimateId) {
        estimateService.delete(estimateId);
        return success(new BoolResponse(true));
    }

    /**
     * 견적요청 알림 보내기
     */
    @PostMapping("/alarm")
    public ResponseEntity<?> alarm(@RequestBody EstimateRequest form) {
        emailService.sendMail(form);
        return success(new BoolResponse(true));
    }
}
