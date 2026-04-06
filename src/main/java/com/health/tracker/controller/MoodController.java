package com.health.tracker.controller;

import com.health.tracker.model.MoodLog;
import com.health.tracker.repository.MoodLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mood")
@CrossOrigin(origins = "http://localhost:3000")
public class MoodController {

    @Autowired
    private MoodLogRepository moodRepo;

    @PostMapping("/log")
    public ResponseEntity<?> logMood(@RequestBody MoodLog log) {
        return ResponseEntity.ok(moodRepo.save(log));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<MoodLog>> getMoodHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(moodRepo.findByUserIdOrderByLogDateDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMood(@PathVariable Long id) {
        if (moodRepo.existsById(id)) {
            moodRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMood(@PathVariable Long id, @RequestBody MoodLog log) {
        return moodRepo.findById(id).map(existing -> {
            existing.setMood(log.getMood());
            existing.setEnergyLevel(log.getEnergyLevel());
            existing.setNotes(log.getNotes());
            return ResponseEntity.ok(moodRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
