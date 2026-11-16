package com.dashboard.backend.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class TaskDto {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request {

        private String id;
        private String title;
        private String content;
        private Boolean isDday;
        private LocalDate startDate;
        private LocalDate dueDate;
        private String status;
        private String imageUrl;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ToggleCompletionRequest {
        private LocalDate date;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {

        private String id;
        private String title;
        private String content;
        private boolean isDday;
        private LocalDate startDate;
        private LocalDate dueDate;
        private String status;
        private String imageUrl;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("updated_at")
        private LocalDateTime updatedAt;

        private List<String> completedDates;

        public static Response from(Task task, List<String> completedDates) {
            return Response.builder()
                    .id(task.getId())
                    .title(task.getTitle())
                    .content(task.getContent())
                    .isDday(task.getIsDday())
                    .startDate(task.getStartDate())
                    .dueDate(task.getDueDate())
                    .status(task.getStatus())
                    .imageUrl(task.getImageUrl())
                    .createdAt(task.getCreatedAt())
                    .updatedAt(task.getUpdatedAt())
                    .completedDates(completedDates != null ? completedDates : List.of())
                    .build();
        }
    }

}
