package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import renewal.ektour.domain.Admin;
import renewal.ektour.service.AdminService;
import renewal.ektour.service.ExcelService;
import renewal.ektour.util.Login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final ExcelService excelService;

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

    @GetMapping("/setting")
    public String settingPage() {
        return "setting";
    }

    @GetMapping("/excel/{estimateId}")
    public void excelDownload(@PathVariable("estimateId") Long estimateId,
                              HttpServletResponse response) throws IOException, InvalidFormatException {
        excelService.createExcel(estimateId, response);
    }

}
