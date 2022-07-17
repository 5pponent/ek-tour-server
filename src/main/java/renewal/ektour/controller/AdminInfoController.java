package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import renewal.ektour.domain.Admin;
import renewal.ektour.dto.request.UpdateAdminPasswordForm;
import renewal.ektour.dto.response.CompanyInfoResponse;
import renewal.ektour.exception.AdminException;
import renewal.ektour.service.AdminService;
import renewal.ektour.util.Login;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static renewal.ektour.dto.response.RestResponse.success;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminInfoController {

    private final AdminService adminService;

    /**
     * 관리자 로그인
     */
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

    /**
     * 관리자 페이지 처음 입장 리다이렉션
     */
    @GetMapping("")
    public String welcomePage(@Login Admin loginAdmin) {
        if (loginAdmin != null) {
            return "redirect:/admin/main";
        }
        else return "loginPage";
    }

    /**
     * 관리자 로그아웃
     */
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        adminService.logout(request);
        return "redirect:/admin";
    }

    /**
     * 관리자 설정
     */
    @GetMapping("/setting")
    public String settingPage(Model model) {
        CompanyInfoResponse companyInfo = adminService.getCompanyInfo();
        model.addAttribute("infoForm", companyInfo);
        model.addAttribute("pwForm", new UpdateAdminPasswordForm());
        return "settingPage";
    }

    // 관리자 정보 변경
    @PostMapping("/setting/info")
    public String updateCompanyInfo(@ModelAttribute("infoForm") CompanyInfoResponse companyInfo) {
        adminService.updateCompanyInfo(companyInfo);
        return "redirect:/admin/setting";
    }

    // 관리자 비밀번호 변경
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
