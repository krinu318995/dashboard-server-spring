package com.dashboard.backend.task;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, String> {

    // 날짜 조회 조건 복잡, 쿼리 사용
    List<Task> findAllByOrderByStartDateAsc();

    @Query("""
            SELECT t FROM Task t
            WHERE t.dueDate >= : start
            AND COALESCE (t.startDate, t.dueDate) <= :end
            ORDER BY t.startDate ASC""")
    List<Task> findByDateRange(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
