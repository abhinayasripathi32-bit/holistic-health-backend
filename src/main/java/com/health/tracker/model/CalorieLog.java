package com.health.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "calorie_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalorieLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "food_name", nullable = false, length = 100)
    private String foodName;

    @Column(nullable = false)
    private Integer calories;

    @Column(name = "meal_type", length = 20)
    private String mealType; // Breakfast, Lunch, Dinner, Snacks

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "logged_at")
    private LocalDateTime loggedAt;

    @PrePersist
    protected void onCreate() {
        loggedAt = LocalDateTime.now();
        if (logDate == null) logDate = LocalDate.now();
    }
}
