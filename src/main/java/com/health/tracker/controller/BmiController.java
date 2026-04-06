package com.health.tracker.controller;

import com.health.tracker.model.BmiRecord;
import com.health.tracker.repository.BmiRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bmi")
@CrossOrigin(origins = "http://localhost:3000")
public class BmiController {

    @Autowired
    private BmiRecordRepository bmiRepo;

    @PostMapping("/save")
    public ResponseEntity<?> saveBmi(@RequestBody BmiRecord record) {
        double bmi = record.getWeight() / (record.getHeight() * record.getHeight());
        record.setBmiValue(Math.round(bmi * 100.0) / 100.0);
        if (bmi < 18.5) record.setCategory("Underweight");
        else if (bmi < 25.0) record.setCategory("Normal");
        else if (bmi < 30.0) record.setCategory("Overweight");
        else record.setCategory("Obese");
        return ResponseEntity.ok(bmiRepo.save(record));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<BmiRecord>> getBmiHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(bmiRepo.findByUserIdOrderByRecordedAtDesc(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBmi(@PathVariable Long id) {
        if (bmiRepo.existsById(id)) {
            bmiRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Deleted"));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBmi(@PathVariable Long id, @RequestBody BmiRecord record) {
        return bmiRepo.findById(id).map(existing -> {
            existing.setWeight(record.getWeight());
            existing.setHeight(record.getHeight());
            double bmi = record.getWeight() / (record.getHeight() * record.getHeight());
            existing.setBmiValue(Math.round(bmi * 100.0) / 100.0);
            if (bmi < 18.5) existing.setCategory("Underweight");
            else if (bmi < 25.0) existing.setCategory("Normal");
            else if (bmi < 30.0) existing.setCategory("Overweight");
            else existing.setCategory("Obese");
            return ResponseEntity.ok(bmiRepo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }
}
