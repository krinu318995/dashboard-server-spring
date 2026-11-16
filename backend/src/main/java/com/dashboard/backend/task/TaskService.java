package com.dashboard.backend.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskCompletionRepository taskCompletionRepository;

    public List<TaskDto.Response> getTasks(LocalDate start, LocalDate end) {
        List<Task> tasks = (start != null && end != null) ? taskRepository.findByDateRange(start, end)
                : taskRepository.findAllByOrderByStartDateAsc();

        Map<String, List<String>> completionsMap = new HashMap<>();

        if (!tasks.isEmpty()) {
            List<String> taskIds = tasks.stream().map(Task::getId).toList();
            List<TaskCompletion> completions = taskCompletionRepository.findByTaskIdIn(taskIds);

            for (TaskCompletion tc : completions) {
                String tId = tc.getTask().getId();
                completionsMap.computeIfAbsent(tId, k -> new ArrayList<>()).add(tc.getCompLocalDate().toString());
            }
        }

        return tasks.stream()
                .map(task -> TaskDto.Response.from(task,
                        completionsMap.getOrDefault(task.getId(), List.of())))
                .toList();

    }

    public TaskDto.Response getTaskById(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));
        List<String> completedDates = taskCompletionRepository.findByTaskId(id).stream()
                .map(tc -> tc.getCompLocalDate().toString())
                .toList();

        return TaskDto.Response.from(task, completedDates);

    }

    @Transactional
    public TaskDto.Response createTask(TaskDto.Request request) {
        Task task = Task.builder()
                .id(request.getId())
                .title(request.getTitle())
                .content(request.getContent())
                .isDday(request.getIsDday() != null ? request.getIsDday() : false)
                .startDate(request.getStartDate())
                .dueDate(request.getDueDate())
                .status(request.getStatus() != null && !request.getStatus().isBlank() ? request.getStatus() : "todo")
                .imageUrl(request.getImageUrl()).build();

        Task saved = taskRepository.save(task);
        return TaskDto.Response.from(saved, List.of());
    }

    @Transactional
    public TaskDto.Response updateTask(String id, TaskDto.Request request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        task.setContent(request.getContent());
        if (request.getIsDday() != null) {
            task.setIsDday(request.getIsDday());
        }
        task.setStartDate(request.getStartDate());
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        List<String> completedDates = taskCompletionRepository.findByTaskId(id).stream()
                .map(tc -> tc.getCompLocalDate().toString())
                .toList();

        return TaskDto.Response.from(task, completedDates);
    }

    @Transactional
    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new NoSuchElementException("일정이 존재하지 않습니다.");
        }
        taskRepository.deleteById(id);
    }

    @Transactional
    public ToggleResult toggleTaskCompletion(String taskId, LocalDate targetDate) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("일정이 존재하지 않습니다."));

        Optional<TaskCompletion> existing = taskCompletionRepository
                .findByTaskIdAndCompLocalDate(taskId, targetDate);

        boolean created;
        if (existing.isPresent()) {
            taskCompletionRepository.delete(existing.get());
            created = false;
        } else {
            TaskCompletion tc = new TaskCompletion();

            tc.setTask(task);
            tc.setCompLocalDate(targetDate);
            taskCompletionRepository.save(tc);
            created = true;
        }

        List<String> updatedDates = taskCompletionRepository.findByTaskId(taskId).stream()
                .map(tc -> tc.getCompLocalDate().toString())
                .toList();

        return new ToggleResult(created, updatedDates);
    }

    public record ToggleResult(boolean created, List<String> completedDates) {
    }

}
