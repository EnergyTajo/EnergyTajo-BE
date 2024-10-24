package com.energy.tajo.ride.repository;

import com.energy.tajo.ride.domain.Ride;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query("SELECT r FROM Ride r WHERE r.userId = :userId ORDER BY r.startRideDate DESC, r.startTime DESC")
    List<Ride> findAllByUserIdOrderByStartRideDateDesc(String userId);
}
