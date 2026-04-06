package com.health.tracker.controller;

import com.health.tracker.model.CalorieLog;
import com.health.tracker.repository.CalorieLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calories")
@CrossOrigin(origins = "http://localhost:3000")
public class CalorieController {

    @Autowired
    private CalorieLogRepository calorieRepo;

    @PostMapping("/log")
    public ResponseEntity<?> logCalorie(@RequestBody CalorieLog log) {
        return ResponseEntity.ok(calorieRepo.save(log));
    }

    @GetMapping("/{userId}/today")
    public ResponseEntity<?> getTodayCalories(@PathVariable Long userId) {
        LocalDate today = LocalDate.now();
        List<CalorieLog> logs = calorieRepo.findByUserIdAndLogDate(userId, today);
        Integer total = calorieRepo.getTotalCaloriesForDay(userId, today);
        return ResponseEntity.ok(Map.of("logs", logs, "totalCalories", total, "goalCalories", 2000));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<CalorieLog>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(calorieRepo.findByUserIdOrderByLoggedAtDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCalorie(@PathVariable Long id) {
        if (calorieRepo.existsById(id)) {
            calorieRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCalorie(@PathVariable Long id, @RequestBody CalorieLog log) {
        return calorieRepo.findById(id).map(existing -> {
            existing.setFoodName(log.getFoodName());
            existing.setCalories(log.getCalories());
            existing.setMealType(log.getMealType());
            return ResponseEntity.ok(calorieRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
