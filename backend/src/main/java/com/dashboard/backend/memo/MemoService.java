package com.dashboard.backend.memo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {
    // 비즈니스 로직 계층
    private final MemoRepository memoRepository;

    public MemoListResponseDto getMemos(int page, int limit, String search) {

        int validPage = Math.max(1, page);
        int validLimit = Math.max(1, limit);
        String keyword = (search == null) ? "" : search.trim();

        Pageable pageable = PageRequest.of(validPage - 1, validLimit, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Memo> memoPage = keyword.isEmpty() ? memoRepository.findAll(pageable)
                : memoRepository.findByTitleContainingOrMemoTextContaining(keyword, keyword, pageable);

        List<MemoListResponseDto.MemoSummary> items = memoPage.getContent().stream()
                .map(MemoListResponseDto.MemoSummary::from)
                .toList();

        MemoListResponseDto.Pagination pagination = MemoListResponseDto.Pagination.builder()
                .page(validPage)
                .limit(validLimit)
                .total(memoPage.getTotalElements())
                .totalPage(memoPage.getTotalPages())
                .build();

        return MemoListResponseDto.builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    public Memo getMemoById(String id) {
        return memoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메모를 찾을 수 없습니다."));

    }

    @Transactional
    public Memo creatMemo(MemoRequestDto request) {
        String newMemoId = "memo_" + UUID.randomUUID();

        Memo memo = Memo.builder()
                .id(newMemoId)
                .title(request.getTitle())
                .memoText(request.getMemoText())
                .imageUrl(request.getImageUrl())
                .build();

        return memoRepository.save(memo);
    }

    @Transactional
    public void updateMemo(String id, MemoRequestDto request) {

        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메모를 찾을 수 없습니다."));

        if (request.getTitle() != null) {
            memo.setTitle(request.getTitle());
        }

        if (request.getMemoText() != null) {
            memo.setMemoText(request.getMemoText());
        }
        if (request.isImageUrlProvided()) {
            memo.setImageUrl(request.getImageUrl());
        }
    }

    @Transactional
    public void deleteMemo(String id) {
        if (!memoRepository.existsById(id)) {
            throw new NoSuchElementException("해당 메모가 존재하지 않습니다.");
        }
        memoRepository.deleteById(id);
    }
}
