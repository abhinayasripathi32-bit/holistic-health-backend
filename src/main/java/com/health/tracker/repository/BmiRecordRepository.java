package com.health.tracker.repository;

import com.health.tracker.model.BmiRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BmiRecordRepository extends JpaRepository<BmiRecord, Long> {
    List<BmiRecord> findByUserIdOrderByRecordedAtDesc(Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
}
