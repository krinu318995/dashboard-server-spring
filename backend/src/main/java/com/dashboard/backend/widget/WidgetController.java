package com.dashboard.backend.widget;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/widgets")
@RequiredArgsConstructor
public class WidgetController {

    private final WidgetService widgetService;

    @GetMapping
    public ResponseEntity<?> getWidgets() {

        try {
            List<WidgetDto.Response> data = widgetService.getWidgets();
            return ResponseEntity.ok(
                    Map.of(
                            "success", true,
                            "message", "위젯 데이터를 불러오는데 성공했습니다.",
                            "data", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", "위젯 데이터를 불러오지 못하였습니다."));
        }

    }

    @PostMapping
    public ResponseEntity<?> createWidget(@RequestBody WidgetDto.Request request) {

        try {
            WidgetDto.Response data = widgetService.saveOrupdateWidget(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            Map.of("success", true,
                                    "message", "위젯 데이터가 정상적으로 등록되었습니다.", "data", data));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", "위젯 생성 중 오류가 발생하였습니다.."));
        }

    }

    @DeleteMapping
    public ResponseEntity<?> deleteWidget(@PathVariable String id) {
        try {

            widgetService.deleteWidget(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "위젯이 정삭적으로 삭제되었습니다.",
                    "data", Map.of("id", id)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", "위젯 삭제 중 오류가 발생하였습니다.."));
        }
    }

}
