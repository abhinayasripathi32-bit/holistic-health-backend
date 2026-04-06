package com.health.tracker.controller;

import com.health.tracker.model.StepLog;
import com.health.tracker.repository.StepLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/steps")
@CrossOrigin(origins = "http://localhost:3000")
public class StepController {

    @Autowired
    private StepLogRepository stepRepo;

    @PostMapping("/log")
    public ResponseEntity<?> logSteps(@RequestBody StepLog log) {
        if (log.getStepCount() != null) {
            log.setCaloriesBurned(log.getStepCount() * 0.04);
            log.setDistanceKm(Math.round(log.getStepCount() * 0.000762 * 100.0) / 100.0);
        }
        return ResponseEntity.ok(stepRepo.save(log));
    }

    @GetMapping("/{userId}/today")
    public ResponseEntity<?> getTodaySteps(@PathVariable Long userId) {
        return stepRepo.findByUserIdAndLogDate(userId, LocalDate.now())
                .map(s -> ResponseEntity.ok((Object) Map.of("log", s, "goal", 10000)))
                .orElse(ResponseEntity.ok(Map.of("log", Map.of(), "goal", 10000)));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<StepLog>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(stepRepo.findByUserIdOrderByLogDateDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStep(@PathVariable Long id) {
        if (stepRepo.existsById(id)) {
            stepRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStep(@PathVariable Long id, @RequestBody StepLog log) {
        return stepRepo.findById(id).map(existing -> {
            existing.setStepCount(log.getStepCount());
            existing.setCaloriesBurned(log.getStepCount() * 0.04);
            existing.setDistanceKm(Math.round(log.getStepCount() * 0.000762 * 100.0) / 100.0);
            return ResponseEntity.ok(stepRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
