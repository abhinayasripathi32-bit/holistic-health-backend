package com.health.tracker.repository;

import com.health.tracker.model.CalorieLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface CalorieLogRepository extends JpaRepository<CalorieLog, Long> {
    List<CalorieLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);
    List<CalorieLog> findByUserIdOrderByLoggedAtDesc(Long userId);

    @Query("SELECT COALESCE(SUM(c.calories), 0) FROM CalorieLog c WHERE c.userId = :userId AND c.logDate = :date")
    Integer getTotalCaloriesForDay(Long userId, LocalDate date);
}
