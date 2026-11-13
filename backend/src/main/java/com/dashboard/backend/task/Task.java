package com.dashboard.backend.task;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id 
    @Column (name = "id" ,length = 36, nullable = false ) 
    private String id;

    @Column (name = "title", length = 255, nullable = false)
    private String title;

    @Column (name = "content", columnDefinition = "longtext")
    private String content;

    @Column (name = "is_dday")
    @Builder.Default
    private Boolean isDday = false;

    @Column (name = "start_date")
    private LocalDate startDate;

    @Column (name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column (name = "status", length = 20)
    @Builder.Default
    private String status = "todo";

    @Column (name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany (mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder .Default
    private List<TaskCompletion> competions = new ArrayList<>();

    @PrePersist 
    public void prePersist(){
        if(this.id==null || this.id.isBlank()){
            this.id = UUID.randomUUID().toString();
        }
        if(this.isDday==null){
            this.isDday = false;
        }
        if(this.status==null || this.status.isBlank()){
            this.status ="todo";
        }
    }
}
