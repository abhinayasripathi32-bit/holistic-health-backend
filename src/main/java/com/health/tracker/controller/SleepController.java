package com.health.tracker.controller;

import com.health.tracker.model.SleepLog;
import com.health.tracker.repository.SleepLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sleep")
@CrossOrigin(origins = "http://localhost:3000")
public class SleepController {

    @Autowired
    private SleepLogRepository sleepRepo;

    @PostMapping("/log")
    public ResponseEntity<?> logSleep(@RequestBody SleepLog log) {
        if (log.getBedtime() != null && log.getWakeTime() != null) {
            LocalTime bed = log.getBedtime();
            LocalTime wake = log.getWakeTime();
            double hours;
            if (wake.isAfter(bed)) {
                hours = (wake.toSecondOfDay() - bed.toSecondOfDay()) / 3600.0;
            } else {
                hours = (86400 - bed.toSecondOfDay() + wake.toSecondOfDay()) / 3600.0;
            }
            log.setHoursSlept(Math.round(hours * 10.0) / 10.0);
        }
        return ResponseEntity.ok(sleepRepo.save(log));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<SleepLog>> getSleepHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(sleepRepo.findByUserIdOrderByLogDateDesc(userId));
    }

    @GetMapping("/{userId}/week")
    public ResponseEntity<List<SleepLog>> getWeekSleep(@PathVariable Long userId) {
        return ResponseEntity.ok(sleepRepo.findTop7ByUserIdOrderByLogDateDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSleep(@PathVariable Long id) {
        if (sleepRepo.existsById(id)) {
            sleepRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSleep(@PathVariable Long id, @RequestBody SleepLog log) {
        return sleepRepo.findById(id).map(existing -> {
            existing.setBedtime(log.getBedtime());
            existing.setWakeTime(log.getWakeTime());
            existing.setSleepQuality(log.getSleepQuality());
            if (log.getBedtime() != null && log.getWakeTime() != null) {
                LocalTime bed = log.getBedtime();
                LocalTime wake = log.getWakeTime();
                double hours = wake.isAfter(bed)
                        ? (wake.toSecondOfDay() - bed.toSecondOfDay()) / 3600.0
                        : (86400 - bed.toSecondOfDay() + wake.toSecondOfDay()) / 3600.0;
                existing.setHoursSlept(Math.round(hours * 10.0) / 10.0);
            }
            return ResponseEntity.ok(sleepRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
