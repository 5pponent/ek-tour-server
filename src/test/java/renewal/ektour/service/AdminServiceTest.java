package renewal.ektour.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import renewal.ektour.repository.AdminRepository;
import renewal.ektour.util.AdminConfig;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminServiceTest {

    @Autowired
    AdminService adminService;

    @Autowired
    AdminRepository adminRepository;

    HttpServletRequest request;

    @BeforeEach
    void testRequestInit() {
        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    @Test
    @DisplayName("[login] 관리자 로그인")
    void login() {
        // given
        String adminPassword = adminRepository.findAll().get(0).getAdminPassword();

        // when
        boolean result = adminService.login(request, adminPassword);
        boolean fail1 = adminService.login(request, "7894");
        boolean fail2 = adminService.login(request, "4865");

        // then
        assertThat(result).isTrue();
        assertThat(fail1).isFalse();
        assertThat(fail2).isFalse();
    }

    @Test
    void logout() {
        // given
        Object loginState = request.getSession().getAttribute(AdminConfig.ADMIN);
        adminService.login(request, adminRepository.findAll().get(0).getAdminPassword());

        // when
        adminService.logout(request);

        // then
        assertThat(loginState).isNull();
    }

    @Test
    void updatePassword() {
    }

    @Test
    void getCompanyInfo() {
    }

    @Test
    void updateCompanyInfo() {
    }

    @Test
    void uploadLogo() {
    }
}