package com.dashboard.backend.memo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemoListResponseDto {

    private List<MemoSummary> items;
    private Pagination pagination;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MemoSummary {

        private String id;
        private String title;
        private String memoText;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static MemoSummary from(Memo memo) {
            return MemoSummary.builder()
                    .id(memo.getId())
                    .title(memo.getTitle())
                    .memoText(memo.getMemoText())
                    .createdAt(memo.getCreatedAt())
                    .updatedAt(memo.getUpdatedAt())
                    .build();
        }

    }// end MemoSummary

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Pagination {
        private int page;
        private int limit;
        private long total;
        private int totalPage;

    }
}
