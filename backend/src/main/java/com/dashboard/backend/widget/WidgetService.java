package com.dashboard.backend.widget;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WidgetService {

    private final WidgetRepository widgetRepository;

    public List<WidgetDto.Response> getWidgets() {
        return widgetRepository.findAll().stream()
                .map(WidgetDto.Response::from)
                .toList();
    }

    @Transactional
    public WidgetDto.Response saveOrupdateWidget(WidgetDto.Request request) {
        String widgetId = (request.getId() != null && !request.getId().isBlank()) ? request.getId()
                : UUID.randomUUID().toString();

        String resolvedType = (request.getType() != null && !request.getType().isBlank()) ? request.getType()
                : "default";

        int resolvedX = request.getX() != null ? request.getX() : 0;
        int resolvedY = request.getY() != null ? request.getY() : 0;
        int resolvedW = request.getW() != null ? request.getW() : 1;
        int resolvedH = request.getH() != null ? request.getH() : 1;

        String resolvedMode = "todo".equals(resolvedType)
                ? ((request.getMode() != null && !request.getMode().isBlank()) ? request.getMode() : "todo")
                : null;

        String resolvedViewMode = "memo".equals(resolvedType)
                ? ((request.getViewMode() != null && !request.getViewMode().isBlank() ? request.getViewMode()
                        : "normal"))
                : null;

        Widget widget = widgetRepository.findById(widgetId)
                .map(existing -> {
                    existing.setX(resolvedX);
                    existing.setY(resolvedY);
                    existing.setW(resolvedW);
                    existing.setH(resolvedH);
                    existing.setMode(resolvedMode);
                    existing.setViewMode(resolvedViewMode);
                    existing.setData(request.getData());
                    return existing;
                }).orElseGet(() -> Widget.builder()
                        .id(widgetId)
                        .type(resolvedType)
                        .x(resolvedY)
                        .y(resolvedY)
                        .h(resolvedH)
                        .w(resolvedW)
                        .mode(resolvedMode)
                        .viewMode(resolvedViewMode)
                        .data(request.getData())
                        .build());

        Widget saved = widgetRepository.save(widget);
        return WidgetDto.Response.from(saved);
    }

    @Transactional
    public void deleteWidget(String id) {
        if (!widgetRepository.existsById(id)) {
            throw new NoSuchElementException("해당 위젯을 찾을 수 없습니다.");
        }
        widgetRepository.deleteById(id);
    }
}
