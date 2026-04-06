package com.health.tracker.repository;

import com.health.tracker.model.WaterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface WaterLogRepository extends JpaRepository<WaterLog, Long> {
    List<WaterLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);
    List<WaterLog> findByUserIdOrderByLoggedAtDesc(Long userId);

    @Query("SELECT COALESCE(SUM(w.amountMl), 0) FROM WaterLog w WHERE w.userId = :userId AND w.logDate = :date")
    Integer getTotalWaterForDay(Long userId, LocalDate date);
}
