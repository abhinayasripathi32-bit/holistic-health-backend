package com.health.tracker.repository;

import com.health.tracker.model.MoodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {
    List<MoodLog> findByUserIdOrderByLogDateDesc(Long userId);
}
