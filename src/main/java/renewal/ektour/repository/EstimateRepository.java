package renewal.ektour.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import renewal.ektour.domain.Estimate;

import java.util.List;
import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query("SELECT e FROM Estimate e WHERE e.visibility = true")
    Page<Estimate> findAll(Pageable pageable);

    @Query("SELECT e FROM Estimate e")
    Page<Estimate> findAllByAdmin(Pageable pageable);

    @Query("SELECT COUNT(e) FROM Estimate e WHERE e.visibility = true")
    int countAll();

    Page<Estimate> findAllByPhoneAndPassword(Pageable pageable, String phone, String password);

    List<Estimate> findAllByPhoneAndPassword(String phone, String password);

    Optional<Estimate> findByIdAndPhoneAndPassword(Long id, String phone, String password);

    @Query("SELECT e FROM Estimate e WHERE e.createdDate BETWEEN :start AND :end AND e.name = :name")
    Page<Estimate> searchAllByName(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("name") String name);

    @Query("SELECT e FROM Estimate e WHERE e.createdDate BETWEEN :start AND :end AND e.phone = :phone")
    Page<Estimate> searchAllByPhone(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("phone") String phone);

    @Query("SELECT e FROM Estimate e WHERE e.createdDate BETWEEN :start AND :end")
    Page<Estimate> searchAllByDate(Pageable pageable, @Param("start") String start, @Param("end") String end);
}
