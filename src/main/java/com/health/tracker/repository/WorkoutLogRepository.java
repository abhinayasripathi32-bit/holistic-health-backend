package com.health.tracker.repository;

import com.health.tracker.model.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    List<WorkoutLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);
    List<WorkoutLog> findByUserIdOrderByLogDateDesc(Long userId);
}
