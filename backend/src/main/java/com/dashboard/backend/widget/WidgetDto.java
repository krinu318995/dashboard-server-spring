package com.dashboard.backend.widget;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class WidgetDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {
        private String id;
        private String type;
        private Integer x;
        private Integer y;
        private Integer w;
        private Integer h;
        private String viewMode;
        private String mode;
        private Map<String, Object> data;

    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {

        private String id;
        private String type;

        private Integer x;
        private Integer y;
        private Integer w;
        private Integer h;

        private String viewMode;
        private String mode;
        private Map<String, Object> data;

        public static Response from(Widget widget) {
            return Response.builder()
                    .id(widget.getId())
                    .type(widget.getType())
                    .x(widget.getX())
                    .y(widget.getY())
                    .w(widget.getW())
                    .h(widget.getH())
                    .viewMode(widget.getViewMode())
                    .mode(widget.getMode())
                    .data(widget.getData())
                    .build();

        }
    }

}
