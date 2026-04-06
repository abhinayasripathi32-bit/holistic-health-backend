package com.health.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "workout_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkoutLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "workout_type", nullable = false, length = 50)
    private String workoutType; // Running, Walking, Cycling, Gym, Yoga, Swimming, Dancing, Others

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "calories_burned")
    private Double caloriesBurned;

    @Column(length = 255)
    private String notes;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "logged_at")
    private LocalDateTime loggedAt;

    @PrePersist
    protected void onCreate() {
        loggedAt = LocalDateTime.now();
        if (logDate == null) logDate = LocalDate.now();
        if (caloriesBurned == null && durationMinutes != null) {
            caloriesBurned = durationMinutes * 7.0; // approximate
        }
    }
}
