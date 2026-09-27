package dev.back.controller.post;

import dev.back.dto.common.PageResponse;
import dev.back.dto.post.PostCreateRequest;
import dev.back.dto.post.PostDetailResponse;
import dev.back.dto.post.PostListResponse;
import dev.back.dto.post.PostUpdateRequest;
import dev.back.service.post.PostService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    // 글 작성
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDetailResponse create(Authentication authentication, @Valid @RequestBody PostCreateRequest request) {
        return postService.create(memberId(authentication), request);
    }

    //
    @GetMapping
    public PageResponse<PostListResponse> findPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return postService.findPage(page, size);
    }

    @GetMapping("/{postId}")
    public PostDetailResponse findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @PutMapping("/{postId}")
    public PostDetailResponse update(
            @PathVariable Long postId, Authentication authentication, @Valid @RequestBody PostUpdateRequest request
    ) {
        return postService.update(postId, memberId(authentication), request);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(postId, memberId(authentication));
    }

    private Long memberId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
