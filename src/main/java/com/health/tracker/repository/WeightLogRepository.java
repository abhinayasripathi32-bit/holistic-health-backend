package com.health.tracker.repository;

import com.health.tracker.model.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {
    List<WeightLog> findTop10ByUserIdOrderByLogDateDesc(Long userId);
    List<WeightLog> findByUserIdOrderByLogDateDesc(Long userId);
}
