package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import renewal.ektour.domain.Admin;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.AdminSearchForm;
import renewal.ektour.dto.response.EstimateDetailResponse;
import renewal.ektour.exception.AdminException;
import renewal.ektour.service.EstimateService;
import renewal.ektour.service.ExcelService;
import renewal.ektour.util.Login;
import renewal.ektour.util.PageConfig;
import renewal.ektour.util.SearchManager;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminEstimateController {

    private final EstimateService estimateService;
    private final SearchManager searchManager;
    private final ExcelService excelService;

    /**
     * 관리자페이지 - 견적요청 조회
     */
    // 페이징 처리 변수 세팅 편의 메소드
    private void setPagingModel(Model model, Pageable pageable, Page<Estimate> eList, AdminSearchForm attributeValue) {
        model.addAttribute("currentPage", pageable.getPageNumber() + 1);
        model.addAttribute("eList", eList);
        model.addAttribute("maxPage", 10);
        model.addAttribute("adminSearchForm", attributeValue);
    }

    @GetMapping("/main")
    public String main(@Login Admin loginAdmin,
                       Model model,
                       @PageableDefault(size = PageConfig.PAGE_PER_COUNT,
                               sort = PageConfig.SORT_STANDARD,
                               direction = Sort.Direction.DESC) Pageable pageable) {
        if (loginAdmin == null) return "login";
        Page<Estimate> eList = estimateService.findAllByPageAdmin(pageable);
        setPagingModel(model, pageable, eList, new AdminSearchForm());
        return "mainPage";
    }

    /**
     * 견적요청 검색
     */
    // 검색 변수 설정 편의 메소드
    private void setSearchVariables(AdminSearchForm form, Pageable pageable, Model model) {
        searchManager.setValue("search", form);
        Page<Estimate> eList = estimateService.searchByPageAdmin(pageable, form);
        setPagingModel(model, pageable, eList, form);
    }

    // 견적 요청 처음 검색
    @PostMapping("/search")
    public String search(@Valid @ModelAttribute("adminSearchForm") AdminSearchForm form,
                         BindingResult bindingResult,
                         Model model,
                         @PageableDefault(size = PageConfig.PAGE_PER_COUNT,
                                 sort = PageConfig.SORT_STANDARD,
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        if (bindingResult.hasErrors()) {
            log.error("{}", bindingResult.getFieldErrors());
            throw new AdminException("검색 일시적 오류");
        }
        setSearchVariables(form, pageable, model);
        return "searchPage";
    }

    // 검색한 상태에서 페이징
    @GetMapping("/search")
    public String searchPage(
            @PageableDefault(size = PageConfig.PAGE_PER_COUNT,
            sort = PageConfig.SORT_STANDARD,
            direction = Sort.Direction.DESC) Pageable pageable,
                             Model model) {
        AdminSearchForm form = searchManager.getValue("search");
        setSearchVariables(form, pageable, model);
        return "searchPage";
    }

    // 견적요청 상세 조회
    @GetMapping("/estimate/{estimateId}")
    public String getEstimateDetail(@PathVariable("estimateId") Long estimateId, Model model) {
        EstimateDetailResponse estimate = estimateService.findById(estimateId).toDetailResponse();
        model.addAttribute("estimate", estimate);
        return "estimateDetailPage";
    }

    /**
     * 견적요청 엑셀 다운로드
     */
    @GetMapping("/estimate/{estimateId}/excel")
    public void createExcel(@PathVariable("estimateId") Long estimateId,
                            HttpServletResponse response) {
        excelService.createExcel(estimateId, response);
    }

    /**
     * 관리자페이지 - 견적요청 수정
     */
    @PostMapping("/update/estimate/{estimateId}")
    public String updateEstimate(@PathVariable("estimateId") Long estimateId,
                                 @ModelAttribute("estimate") EstimateDetailResponse estimate) {
        estimateService.update(estimateId, estimate);
        return "redirect:/admin/main";
    }
    
    /**
     * 관리자페이지 - 견적요청 진짜 삭제 (디비에서 삭제)
     */
    @DeleteMapping("/delete/estimate/{estimateId}")
    public String realDeleteEstimate(@PathVariable("estimateId") Long estimateId) {
        estimateService.realDelete(estimateId);
        return "redirect:/admin/main";
    }
}
