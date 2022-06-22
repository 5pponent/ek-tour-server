package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import renewal.ektour.domain.Admin;
import renewal.ektour.dto.request.AdminPasswordForm;
import renewal.ektour.dto.response.CompanyInfoResponse;
import renewal.ektour.service.AdminService;
import renewal.ektour.util.Login;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/login")
    public String login(@RequestParam("adminPassword") String adminPassword,
                        Model model,
                        HttpServletRequest request) {
        boolean loginResult = adminService.login(request, adminPassword);
        if (!loginResult) {
            model.addAttribute("loginResult", false);
            return "login";
        }
        return "redirect:/admin";
    }

    @GetMapping("")
    public String welcomePage(@Login Admin loginAdmin) {
        if (loginAdmin != null) return "main";
        else return "login";
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
        model.addAttribute("pwForm", new AdminPasswordForm());
        return "setting";
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
    public String updateAdminPassword(@ModelAttribute("pwForm") AdminPasswordForm passwordForm,
                                      Model model) {
        if (!passwordForm.passwordCheck()) {
            return "redirect:/admin/setting";
        }
        adminService.updatePassword(passwordForm.getPassword());
        return "redirect:/admin/setting";
    }

}
