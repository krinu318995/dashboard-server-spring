package com.dashboard.backend.memo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, String> {

    Page<Memo> findByTitleContainingOrMemoTextContaining(String title, String memoText, Pageable pageable);
}
