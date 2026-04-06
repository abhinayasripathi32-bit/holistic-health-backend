package com.health.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "step_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "step_count", nullable = false)
    private Integer stepCount;

    @Column(name = "calories_burned")
    private Double caloriesBurned;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "logged_at")
    private LocalDateTime loggedAt;

    @PrePersist
    protected void onCreate() {
        loggedAt = LocalDateTime.now();
        if (logDate == null) logDate = LocalDate.now();
        if (stepCount != null) {
            caloriesBurned = stepCount * 0.04;
            distanceKm = stepCount * 0.000762;
        }
    }
}
