package com.health.tracker.repository;

import com.health.tracker.model.StepLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StepLogRepository extends JpaRepository<StepLog, Long> {
    Optional<StepLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);
    List<StepLog> findByUserIdOrderByLogDateDesc(Long userId);
}
