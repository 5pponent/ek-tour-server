package renewal.ektour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import renewal.ektour.domain.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    boolean existsByAdminPassword(String adminPassword);
    Optional<Admin> findByAdminPassword(String adminPassword);
}
