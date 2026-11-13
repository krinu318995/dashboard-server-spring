package com.dashboard.backend.memo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemoRequestDto {

    private String title;
    private String memoText;
    private String imageUrl;

    private boolean isImageUrlProvided = false;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.isImageUrlProvided = true;
    }
}
