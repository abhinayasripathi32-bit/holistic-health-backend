package com.health.tracker.repository;

import com.health.tracker.model.SleepLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SleepLogRepository extends JpaRepository<SleepLog, Long> {
    List<SleepLog> findByUserIdOrderByLogDateDesc(Long userId);
    List<SleepLog> findTop7ByUserIdOrderByLogDateDesc(Long userId);
}
