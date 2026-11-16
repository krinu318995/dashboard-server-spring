package com.dashboard.backend.memo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
@RequestMapping("/api/memos")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @GetMapping
    public ResponseEntity<?> getMemos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String search) {
        try {
            MemoListResponseDto data = memoService.getMemos(page, limit, search);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", data));
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("err", "메모 목록 조회 실패"));
        }
    }// end get

    @GetMapping("/{id}")
    public ResponseEntity<?> getMemoById(@PathVariable String id) {
        try {
            Memo memo = memoService.getMemoById(id);

            Map<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("id", memo.getId());
            dataMap.put("title", memo.getTitle());
            dataMap.put("memoText", memo.getMemoText());
            dataMap.put("imageUrl", memo.getImageUrl());
            dataMap.put("createdAt", memo.getCreatedAt());
            dataMap.put("updatedAt", memo.getUpdatedAt());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "data", dataMap));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("err", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에서 정확한 원인 확인용
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("err", "메모 조회 실패"));
        }
    }

    @PostMapping()
    public ResponseEntity<?> createMemo(@RequestBody MemoRequestDto request) {

        try {

            Memo created = memoService.creatMemo(request);

            Map<String, Object> dataMap = new LinkedHashMap<>();
            dataMap.put("id", created.getId());
            dataMap.put("title", created.getTitle());
            dataMap.put("memoText", created.getMemoText());
            dataMap.put("imageUrl", created.getImageUrl());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "data", dataMap));
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("err", "메모 등록 실패"));
        }

    }// end PostMapping

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMemo(@PathVariable String id, @RequestBody MemoRequestDto request) {

        try {

            memoService.updateMemo(id, request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "id", id));
        } catch (NoSuchElementException error) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("err", error.getMessage()));

        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("err", "메모 수정 실패"));
        }

    }// PutMapping

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMemo(@PathVariable String id) {
        try {
            memoService.deleteMemo(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "메모 삭제가 완료되었습니다."

            ));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("err", "메모 삭제 실패"));
        }
    }

}
