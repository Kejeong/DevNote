package dev.back.controller.comment;

import dev.back.dto.comment.CommentCreateRequest;
import dev.back.dto.comment.CommentResponse;
import dev.back.dto.comment.CommentUpdateRequest;
import dev.back.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(
            @PathVariable Long postId, Authentication authentication, @Valid @RequestBody CommentCreateRequest request
    ) {
        return commentService.create(postId, memberId(authentication), request);
    }

    // 댓글 조회
    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> findByPostId(@PathVariable Long postId) {
        return commentService.findByPostId(postId);
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    public CommentResponse update(
            @PathVariable Long commentId, Authentication authentication, @Valid @RequestBody CommentUpdateRequest request
    ) {
        return commentService.update(commentId, memberId(authentication), request);
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId, Authentication authentication) {
        commentService.delete(commentId, memberId(authentication));
    }

    // 회원검증
    private Long memberId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
