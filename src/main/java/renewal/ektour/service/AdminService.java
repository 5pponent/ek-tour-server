package renewal.ektour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import renewal.ektour.repository.AdminRepository;

import javax.security.auth.login.LoginException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public void login(String adminPassword) throws LoginException {
        if (!adminRepository.existsByAdminPassword(adminPassword)) throw new LoginException("비밀번호가 다릅니다.");
    }

}
