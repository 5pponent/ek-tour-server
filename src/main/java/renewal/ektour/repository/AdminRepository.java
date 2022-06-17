package renewal.ektour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import renewal.ektour.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByAdminPassword(String adminPassword);
}
