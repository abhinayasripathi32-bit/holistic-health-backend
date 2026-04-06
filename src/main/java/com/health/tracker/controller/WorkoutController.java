package com.health.tracker.controller;

import com.health.tracker.model.WorkoutLog;
import com.health.tracker.repository.WorkoutLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workout")
@CrossOrigin(origins = "http://localhost:3000")
public class WorkoutController {

    @Autowired
    private WorkoutLogRepository workoutRepo;

    @PostMapping("/log")
    public ResponseEntity<?> logWorkout(@RequestBody WorkoutLog log) {
        if (log.getCaloriesBurned() == null && log.getDurationMinutes() != null) {
            double multiplier = switch (log.getWorkoutType()) {
                case "Running" -> 11.0;
                case "Cycling" -> 8.0;
                case "Swimming" -> 9.0;
                case "Gym" -> 7.0;
                case "Yoga" -> 4.0;
                case "Dancing" -> 6.0;
                case "Walking" -> 5.0;
                default -> 6.0;
            };
            log.setCaloriesBurned(log.getDurationMinutes() * multiplier);
        }
        return ResponseEntity.ok(workoutRepo.save(log));
    }

    @GetMapping("/{userId}/today")
    public ResponseEntity<List<WorkoutLog>> getTodayWorkouts(@PathVariable Long userId) {
        return ResponseEntity.ok(workoutRepo.findByUserIdAndLogDate(userId, LocalDate.now()));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<WorkoutLog>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(workoutRepo.findByUserIdOrderByLogDateDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable Long id) {
        if (workoutRepo.existsById(id)) {
            workoutRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkout(@PathVariable Long id, @RequestBody WorkoutLog log) {
        return workoutRepo.findById(id).map(existing -> {
            existing.setWorkoutType(log.getWorkoutType());
            existing.setDurationMinutes(log.getDurationMinutes());
            existing.setCaloriesBurned(log.getCaloriesBurned());
            existing.setNotes(log.getNotes());
            return ResponseEntity.ok(workoutRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
