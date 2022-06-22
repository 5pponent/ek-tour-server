package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renewal.ektour.domain.Admin;
import renewal.ektour.dto.response.CompanyInfoResponse;
import renewal.ektour.repository.AdminRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    @Transactional
    public Admin createAdmin(String password, String adminName, String infoHandlerName, String businessNum, String registrationNum, String address, String tel, String fax, String phone, String email, String accountBank, String accountNum, String accountHolder) {
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
        return adminRepository.save(admin);
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

    @Transactional
    public void updatePassword(String oldPassword, String newPassword) {
        Admin admin = adminRepository.findByAdminPassword(oldPassword).orElseThrow();
        admin.updatePassword(newPassword);
    }

    @Transactional
    public CompanyInfoResponse getCompanyInfo() {
        return adminRepository.findById(1L).orElseThrow().toCompanyInfoResponse();
    }

}
