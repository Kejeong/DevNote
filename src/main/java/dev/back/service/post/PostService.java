package dev.back.service.post;

import dev.back.domain.member.Member;
import dev.back.domain.post.Post;
import dev.back.dto.common.PageResponse;
import dev.back.dto.post.PostCreateRequest;
import dev.back.dto.post.PostDetailResponse;
import dev.back.dto.post.PostListResponse;
import dev.back.dto.post.PostUpdateRequest;
import dev.back.global.exception.ForbiddenException;
import dev.back.global.exception.ResourceNotFoundException;
import dev.back.repository.member.MemberRepository;
import dev.back.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 글 작성
    @Transactional
    public PostDetailResponse create(Long memberId, PostCreateRequest request) {
        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("회원"));

        Post post = postRepository.save(new Post(request.title(), request.content(), author));
        return PostDetailResponse.from(post);
    }

    // 페이지 조회
    public PageResponse<PostListResponse> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        return PageResponse.from(postRepository.findPageWithAuthorAndCommentCount(pageable));
    }

    // 글 상세 조회
    public PostDetailResponse findById(Long postId) {
        return PostDetailResponse.from(findPost(postId));
    }

    // 글 수정
    @Transactional
    public PostDetailResponse update(Long postId, Long memberId, PostUpdateRequest request) {
        Post post = findPost(postId);
        verifyAuthor(post.getAuthor().getId(), memberId);
        post.update(request.title(), request.content());
        return PostDetailResponse.from(post);
    }

    // 글 삭제
    @Transactional
    public void delete(Long postId, Long memberId) {
        Post post = findPost(postId);
        verifyAuthor(post.getAuthor().getId(), memberId);
        postRepository.delete(post);
    }


    // 게시글 존재 검증
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글"));
    }

    // 글쓴이 검증
    private void verifyAuthor(Long authorId, Long memberId) {
        if (!authorId.equals(memberId)) {
            throw new ForbiddenException();
        }
    }
}
