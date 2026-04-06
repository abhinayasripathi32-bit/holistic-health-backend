package com.health.tracker.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "sleep_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SleepLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bedtime", nullable = false)
    private LocalTime bedtime;

    @Column(name = "wake_time", nullable = false)
    private LocalTime wakeTime;

    @Column(name = "hours_slept")
    private Double hoursSlept;

    @Column(name = "sleep_quality", length = 20)
    private String sleepQuality;

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
