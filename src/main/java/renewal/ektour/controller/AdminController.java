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
import org.springframework.web.bind.annotation.*;
import renewal.ektour.domain.Admin;
import renewal.ektour.domain.Estimate;
import renewal.ektour.dto.request.UpdateAdminPasswordRequest;
import renewal.ektour.dto.response.CompanyInfoResponse;
import renewal.ektour.service.AdminService;
import renewal.ektour.service.EstimateService;
import renewal.ektour.service.ExcelService;
import renewal.ektour.util.Login;
import renewal.ektour.util.PageConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/main")
    public String main(@Login Admin loginAdmin,
                       Model model,
                       @PageableDefault(size = PageConfig.PAGE_PER_COUNT, sort = PageConfig.SORT_STANDARD, direction = Sort.Direction.DESC) Pageable pageable) {
        if (loginAdmin == null) return "login";
        Page<Estimate> eList = estimateService.findAllByPageAdmin(pageable);
        model.addAttribute("eList", eList);
        model.addAttribute("maxPage", 10);
        return "mainPage";
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
        model.addAttribute("pwForm", new UpdateAdminPasswordRequest());
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
    public String updateAdminPassword(@ModelAttribute("pwForm") UpdateAdminPasswordRequest passwordForm) {
        if (!passwordForm.passwordCheck()) {
            return "redirect:/admin/setting";
        }
        adminService.updatePassword(passwordForm.getPassword());
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


}
