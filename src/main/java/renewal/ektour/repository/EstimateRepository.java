package renewal.ektour.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import renewal.ektour.domain.Estimate;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {

    @Query("SELECT e FROM Estimate e WHERE e.visibility = true")
    Page<Estimate> findAll(Pageable pageable);

    @Query("SELECT COUNT(e) FROM Estimate e")
    int countAll();

    @Query("SELECT e FROM Estimate e WHERE e.name LIKE %:name%")
    Page<Estimate> searchAllByName(Pageable pageable, String name);

    @Query("SELECT e FROM Estimate e WHERE e.travelType LIKE %:travelType%")
    Page<Estimate> searchAllByTravelType(Pageable pageable, String travelType);

    @Query("SELECT e FROM Estimate e WHERE e.vehicleType LIKE %:vehicleType%")
    Page<Estimate> searchAllByVehicleType(Pageable pageable, String vehicleType);

    List<Estimate> findAllByPhoneAndPassword(String phone, String password);

    @Query("SELECT e FROM Estimate e WHERE e.createdDate BETWEEN :start AND :end AND e.name = :name")
    Page<Estimate> searchAllByName(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("name") String name);

    @Query("SELECT e FROM Estimate e WHERE e.createdDate BETWEEN :start AND :end AND e.phone = :phone")
    Page<Estimate> searchAllByPhone(Pageable pageable, @Param("start") String start, @Param("end") String end, @Param("phone") String phone);
}
