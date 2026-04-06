package com.health.tracker.controller;

import com.health.tracker.model.WeightLog;
import com.health.tracker.repository.WeightLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/weight")
@CrossOrigin(origins = "http://localhost:3000")
public class WeightController {

    @Autowired
    private WeightLogRepository weightRepo;

    @PostMapping("/log")
    public ResponseEntity<?> logWeight(@RequestBody WeightLog log) {
        return ResponseEntity.ok(weightRepo.save(log));
    }

    @GetMapping("/{userId}/history")
    public ResponseEntity<List<WeightLog>> getHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(weightRepo.findTop10ByUserIdOrderByLogDateDesc(userId));
    }

    @GetMapping("/{userId}/all")
    public ResponseEntity<List<WeightLog>> getAll(@PathVariable Long userId) {
        return ResponseEntity.ok(weightRepo.findByUserIdOrderByLogDateDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWeight(@PathVariable Long id) {
        if (weightRepo.existsById(id)) {
            weightRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWeight(@PathVariable Long id, @RequestBody WeightLog log) {
        return weightRepo.findById(id).map(existing -> {
            existing.setWeight(log.getWeight());
            existing.setTargetWeight(log.getTargetWeight());
            return ResponseEntity.ok(weightRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
