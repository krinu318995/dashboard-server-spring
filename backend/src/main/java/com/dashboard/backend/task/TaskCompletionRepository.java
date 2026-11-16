package com.dashboard.backend.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//JpaRepository<엔티티, ID타입> 각 테이블의 pk 타입
public interface TaskCompletionRepository extends JpaRepository<TaskCompletion, Integer> {

    List<TaskCompletion> findByTaskIdIn(List<String> taskIds);

    List<TaskCompletion> findByTaskId(String taskId);

    Optional<TaskCompletion> findByTaskIdAndCompLocalDate(String taskId, LocalDate compLocalDate);
}
