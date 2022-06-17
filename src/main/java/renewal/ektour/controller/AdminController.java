package renewal.ektour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import renewal.ektour.service.AdminService;

import javax.security.auth.login.LoginException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/login")
    public String login(@RequestParam("adminPassword") String password) throws LoginException {
        adminService.login(password);
        return "test";
    }

    @GetMapping("")
    public String welcomePage() {
        return "login";
    }


}
