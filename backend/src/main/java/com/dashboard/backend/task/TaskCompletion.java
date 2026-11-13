package com.dashboard.backend.task;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity 
@Table (
    name = "task_completions",
    uniqueConstraints = {
        @UniqueConstraint (name = "uq_task_date", 
            columnNames = {
                "task_id", "completed_date"
            }
        )
    }
)
@Getter 
@Setter  
@NoArgsConstructor 
public class TaskCompletion {


    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false)
    private Integer id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="task_id", nullable = false)
    private Task task;

    @Column (name="completed_date", nullable = false)
    private LocalDate compLocalDate;

    @Column (name="created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
