package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import renewal.ektour.domain.Admin;
import renewal.ektour.dto.response.CompanyInfoResponse;
import renewal.ektour.exception.AdminException;
import renewal.ektour.repository.AdminRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;
    public static final Long ADMIN_ID = 1L;

    public void createAdmin(String password, String adminName, String infoHandlerName, String businessNum, String registrationNum, String address, String tel, String fax, String phone, String email, String accountBank, String accountNum, String accountHolder) {
        Admin admin = Admin.builder()
                .adminPassword(password)
                .adminName(adminName)
                .infoHandlerName(infoHandlerName)
                .businessNum(businessNum)
                .registrationNum(registrationNum)
                .address(address)
                .tel(tel)
                .fax(fax)
                .phone(phone)
                .email(email)
                .accountBank(accountBank)
                .accountNum(accountNum)
                .accountHolder(accountHolder)
                .build();
        adminRepository.save(admin);
    }

    public boolean login(HttpServletRequest request, String adminPassword) {
        try {
            Admin admin = adminRepository.findByAdminPassword(adminPassword).orElseThrow();
            HttpSession session = request.getSession();
            session.setAttribute("admin", admin);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public void updatePassword(String oldPassword, String newPassword) {
        Admin admin = adminRepository.findByAdminPassword(oldPassword)
                .orElseThrow(() -> new AdminException("현재 비밀번호가 틀립니다"));
        admin.updatePassword(newPassword);
    }

    public CompanyInfoResponse getCompanyInfo() {
        return adminRepository.findById(ADMIN_ID).orElseThrow().toCompanyInfoResponse();
    }

    public void updateCompanyInfo(CompanyInfoResponse companyInfo) {
        Admin admin = adminRepository.findById(ADMIN_ID).orElseThrow();
        admin.updateCompanyInfo(companyInfo);
    }

    public void updatePassword(String password) {
        Admin admin = adminRepository.findById(ADMIN_ID).orElseThrow();
        String oldPassword = admin.getAdminPassword();
        admin.updatePassword(password);
        log.info("관리자 비밀번호 변경 {} -> {}", oldPassword, admin.getAdminPassword());
    }

    // 로고 업로드
    public void uploadLogo(MultipartFile file) {
        try {
            file.transferTo(new File("/home/ubuntu/spring/logo.png"));
        } catch (IOException e) {
            throw new AdminException("로고 업로드 오류");
        }
    }
}
