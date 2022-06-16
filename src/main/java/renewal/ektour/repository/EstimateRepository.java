package renewal.ektour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import renewal.ektour.domain.estimate.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
}
