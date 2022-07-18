package renewal.ektour.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import renewal.ektour.dto.response.CompanyInfoResponse;
import renewal.ektour.exception.AdminException;
import renewal.ektour.repository.AdminRepository;
import renewal.ektour.util.AdminConfig;

import javax.servlet.http.HttpServletRequest;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

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
    @DisplayName("[logout] 관리자 로그아웃")
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
    @DisplayName("[updatePassword] 관리자 비밀번호 변경")
    void updatePassword() {
        // given
        String oldPassword = adminRepository.findAll().get(0).getAdminPassword();
        String wrongOldPassword = "9999";
        String newPassword = "0000";

        // when
        adminService.updatePassword(oldPassword, newPassword);

        // then
        assertThat(adminService.login(request, newPassword)).isTrue();
        assertThatThrownBy(() -> {
            adminService.updatePassword(wrongOldPassword, newPassword);
        }).isInstanceOf(AdminException.class).hasMessage("현재 비밀번호가 틀립니다");
    }

    @Test
    @DisplayName("[getCompanyInfo] 회사 정보 조회")
    void getCompanyInfo() {
        // given

        // when
        CompanyInfoResponse expected = adminService.getCompanyInfo();
        CompanyInfoResponse actual = adminRepository.findAll().get(0).toCompanyInfoResponse();

        // then
        log.info("서비스 : {}", expected.toString());
        log.info("저장소 : {}", actual.toString());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("[updateCompanyInfo] 회사 정보 수정")
    void updateCompanyInfo() {
        // given
        CompanyInfoResponse expected = CompanyInfoResponse.builder()
                .adminName("테스트")
                .infoHandlerName("테스트2")
                .businessNum("000-00-00000")
                .registrationNum("000000-0000000")
                .address("서울")
                .tel("02.0000.0000")
                .fax("02.0000.0000")
                .phone("010-0000-0000")
                .email("test@test.te")
                .accountBank("은행")
                .accountNum("000000-00-000000")
                .accountHolder("테스트")
                .kakaoTalkId("test")
                .build();

        // when
        adminService.updateCompanyInfo(expected);
        CompanyInfoResponse actual = adminRepository.findAll().get(0).toCompanyInfoResponse();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("[uploadLogo] 회사 로고 업로드")
    void uploadLogo() {
        // given
        MultipartFile file = new MockMultipartFile(
                "file", "logo.png", MediaType.IMAGE_PNG_VALUE, "foo".getBytes(StandardCharsets.UTF_8));

        // when

        // then
        assertThatNoException().isThrownBy(() -> {
            adminService.uploadLogo(file);
        });
    }
}