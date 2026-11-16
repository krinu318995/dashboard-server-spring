package com.dashboard.backend.task;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getTasks(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        try {
            List<TaskDto.Response> items = taskService.getTasks(
                    start, end);
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "data", Map.of("items", items)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "일정 목록을 가져오는 중 오류가 발생하였습니다."));
        }

    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDto.Request request) {
        try {
            TaskDto.Response data = taskService.createTask(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "일정이 성공적으로 등록되었습니다.",
                    "data", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "일정 등록 중 오류가 발생하였습니다."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id, @RequestBody TaskDto.Request request) {
        try {
            TaskDto.Response data = taskService.updateTask(id, request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "일정이 성공적으로 수정되었습니다.",
                    "data", data));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false,
                            "error", e.getMessage()));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "일정이 성공적으로 삭제되었습니다.",
                    "data", Map.of("id", id)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "success", false,
                            "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "일정 삭제 중 오류가 발생했습니다."));
        }
    }

    // GET /api/tasks/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String id) {
        try {
            TaskDto.Response data = taskService.getTaskById(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", data));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "일정 조회 중 오류가 발생했습니다."));
        }
    }

    // POST /api/tasks/{id}/completions
    @PostMapping("/{id}/completions")
    public ResponseEntity<?> toggleTaskCompletion(
            @PathVariable String id,
            @RequestBody(required = false) TaskDto.ToggleCompletionRequest request) {
        if (request == null || request.getDate() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "success", false,
                            "error", "선택한 날짜 정보가 전달되지 않았습니다."));
        }

        try {
            TaskService.ToggleResult result = taskService.toggleTaskCompletion(id, request.getDate());
            if (result.created()) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                                "success", true,
                                "message", "일정 완료 상태가 성공적으로 등록되었습니다.",
                                "data", result.completedDates()));
            } else {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "일정 완료 상태가 성공적으로 취소되었습니다.",
                        "data", result.completedDates()));
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "일정 완료 상태 변경 중 오류가 발생했습니다."));
        }
    }

}
