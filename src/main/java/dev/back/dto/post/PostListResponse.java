package dev.back.dto.post;

import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        String title,
        String authorNickname,
        LocalDateTime createdAt,
        long commentCount
) { }
