package renewal.ektour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import renewal.ektour.domain.estimate.Estimate;

import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query("SELECT e FROM Estimate e WHERE e.name = :name")
    Optional<Estimate> findByWriter(@Param("name") String writerName);
}
