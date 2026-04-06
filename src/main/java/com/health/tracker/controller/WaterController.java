package com.health.tracker.controller;

import com.health.tracker.model.WaterLog;
import com.health.tracker.repository.WaterLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/water")
@CrossOrigin(origins = "http://localhost:3000")
public class WaterController {

    @Autowired
    private WaterLogRepository waterRepo;

    @PostMapping("/log")
    public ResponseEntity<?> logWater(@RequestBody WaterLog log) {
        return ResponseEntity.ok(waterRepo.save(log));
    }

    @GetMapping("/{userId}/today")
    public ResponseEntity<?> getTodayWater(@PathVariable Long userId) {
        LocalDate today = LocalDate.now();
        List<WaterLog> logs = waterRepo.findByUserIdAndLogDate(userId, today);
        Integer total = waterRepo.getTotalWaterForDay(userId, today);
        return ResponseEntity.ok(Map.of("logs", logs, "totalMl", total, "goalMl", 2500));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<WaterLog>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(waterRepo.findByUserIdOrderByLoggedAtDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWater(@PathVariable Long id) {
        if (waterRepo.existsById(id)) {
            waterRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWater(@PathVariable Long id, @RequestBody WaterLog log) {
        return waterRepo.findById(id).map(existing -> {
            existing.setAmountMl(log.getAmountMl());
            return ResponseEntity.ok(waterRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
