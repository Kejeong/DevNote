package dev.back.dto.comment;

import dev.back.domain.comment.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        String authorNickname,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getAuthor().getNickname(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }
}
