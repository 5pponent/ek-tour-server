package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import renewal.ektour.domain.Admin;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.AdminSearchForm;
import renewal.ektour.dto.request.UpdateAdminPasswordForm;
import renewal.ektour.dto.response.CompanyInfoResponse;
import renewal.ektour.exception.AdminException;
import renewal.ektour.service.AdminService;
import renewal.ektour.service.EstimateService;
import renewal.ektour.service.ExcelService;
import renewal.ektour.util.Login;
import renewal.ektour.util.PageConfig;
import renewal.ektour.util.SearchManager;
import renewal.ektour.util.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static renewal.ektour.dto.response.RestResponse.success;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final EstimateService estimateService;
    private final ExcelService excelService;
    private final SearchManager searchManager;

    @PostMapping("/login")
    public String login(@RequestParam("adminPassword") String adminPassword,
                        Model model,
                        HttpServletRequest request) {
        boolean loginResult = adminService.login(request, adminPassword);
        if (!loginResult) {
            model.addAttribute("loginResult", false);
            return "loginPage";
        }
        return "redirect:/admin/main";
    }

    @GetMapping("")
    public String welcomePage(@Login Admin loginAdmin) {
        if (loginAdmin != null) {
            return "redirect:/admin/main";
        }
        else return "loginPage";
    }

    /**
     * 관리자 메인 페이지 (견적요청 목록 조회 - 페이징)
     */
    @GetMapping("/main")
    public String main(@Login Admin loginAdmin,
                       Model model,
                       @PageableDefault(size = PageConfig.PAGE_PER_COUNT, sort = PageConfig.SORT_STANDARD, direction = Sort.Direction.DESC) Pageable pageable) {
        if (loginAdmin == null) return "login";
        log.info("현재 페이지 : {}", pageable.getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber() + 1);
        Page<Estimate> eList = estimateService.findAllByPageAdmin(pageable);
        model.addAttribute("eList", eList);
        model.addAttribute("maxPage", 10);
        model.addAttribute("adminSearchForm", new AdminSearchForm());
        return "mainPage";
    }

    // 견적 요청 처음 검색
    @PostMapping("/search")
    public String search(@Valid @ModelAttribute("adminSearchForm") AdminSearchForm form,
                         BindingResult bindingResult,
                         Model model,
                         @PageableDefault(size = PageConfig.PAGE_PER_COUNT, sort = PageConfig.SORT_STANDARD, direction = Sort.Direction.DESC) Pageable pageable) {
        if (bindingResult.hasErrors()) {
            log.info("{}", bindingResult.getAllErrors());
            throw new AdminException("검색 일시적 오류");
        }
        log.info("현재 페이지 : {}", pageable.getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber() + 1);
        searchManager.setValue("search", form);
        Page<Estimate> eList = estimateService.searchByPageAdmin(pageable, form);
        model.addAttribute("eList", eList);
        model.addAttribute("maxPage", 10);
        model.addAttribute("adminSearchForm", form);
        return "searchPage";
    }
    
    // 검색한 상태에서 페이징
    @GetMapping("/search")
    public String searchPage(@PageableDefault(size = PageConfig.PAGE_PER_COUNT, sort = PageConfig.SORT_STANDARD, direction = Sort.Direction.DESC) Pageable pageable,
                             Model model) {
        AdminSearchForm form = searchManager.getValue("search");
        log.info("현재 페이지 : {}", pageable.getPageNumber());
        model.addAttribute("currentPage", pageable.getPageNumber() + 1);
        searchManager.setValue("search", form);
        Page<Estimate> eList = estimateService.searchByPageAdmin(pageable, form);
        model.addAttribute("eList", eList);
        model.addAttribute("maxPage", 10);
        model.addAttribute("adminSearchForm", form);
        return "searchPage";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        adminService.logout(request);
        return "redirect:/admin";
    }

    /**
     * 관리자 설정 페이지로 이동
     */
    @GetMapping("/setting")
    public String settingPage(Model model) {
        CompanyInfoResponse companyInfo = adminService.getCompanyInfo();
        model.addAttribute("infoForm", companyInfo);
        model.addAttribute("pwForm", new UpdateAdminPasswordForm());
        return "settingPage";
    }

    /**
     * 관리자 설정 - 회사 정보 변경
     */
    @PostMapping("/setting/info")
    public String updateCompanyInfo(@ModelAttribute("infoForm") CompanyInfoResponse companyInfo,
                                    Model model) {
        adminService.updateCompanyInfo(companyInfo);
        model.addAttribute("infoForm", companyInfo);
        return "redirect:/admin/setting";
    }

    @PostMapping("/setting/password")
    public String updateAdminPassword(@Valid @ModelAttribute("pwForm") UpdateAdminPasswordForm passwordForm,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new AdminException("비밀번호가 일치하지 않습니다.");
        }
        if (!passwordForm.passwordCheck()) {
            return "redirect:/admin/setting";
        }
        adminService.updatePassword(passwordForm.getNowPassword(), passwordForm.getNewPassword());
        return "redirect:/admin/setting";
    }

    @GetMapping("/excel/{estimateId}")
    public void createExcel(@PathVariable("estimateId") Long estimateId, HttpServletResponse response) throws IOException, InvalidFormatException {
        excelService.createExcel(estimateId, response);
    }

    // 클라이언트로 어드민 정보(회사 정보) 내려주기
    @ResponseBody
    @GetMapping("/info")
    public ResponseEntity<?> getAdminInfo() {
        CompanyInfoResponse companyInfo = adminService.getCompanyInfo();
        return success(companyInfo);
    }

    // 회사 로고 업로드
    @PostMapping("/logo")
    public String updateLogo(@ModelAttribute("file") MultipartFile file) {
        adminService.uploadLogo(file);
        return "redirect:/admin/setting";
    }


}
