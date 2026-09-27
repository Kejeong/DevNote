package dev.back.service.comment;

import dev.back.domain.comment.Comment;
import dev.back.domain.member.Member;
import dev.back.domain.post.Post;
import dev.back.dto.comment.CommentCreateRequest;
import dev.back.dto.comment.CommentResponse;
import dev.back.dto.comment.CommentUpdateRequest;
import dev.back.global.exception.ForbiddenException;
import dev.back.global.exception.ResourceNotFoundException;
import dev.back.repository.comment.CommentRepository;
import dev.back.repository.member.MemberRepository;
import dev.back.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // 댓글 등록
    @Transactional
    public CommentResponse create(Long postId, Long memberId, CommentCreateRequest request) {
        Post post = findPost(postId);
        Member author = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("회원"));
        Comment comment = commentRepository.save(new Comment(post, author, request.content()));
        return CommentResponse.from(comment);
    }

    // 게시물 조회
    public List<CommentResponse> findByPostId(Long postId) {
        findPost(postId);
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 댓글 수정
    @Transactional
    public CommentResponse update(Long commentId, Long memberId, CommentUpdateRequest request) {
        Comment comment = findComment(commentId);
        verifyAuthor(comment.getAuthor().getId(), memberId);
        comment.update(request.content());
        return CommentResponse.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId, Long memberId) {
        Comment comment = findComment(commentId);
        verifyAuthor(comment.getAuthor().getId(), memberId);  // 글쓴이 검증
        commentRepository.delete(comment);
    }

    // 게시글 존재 검증
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("게시글"));
    }

    // 댓글 존재 검증
    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("댓글"));
    }

    // 글쓴이 검증
    private void verifyAuthor(Long authorId, Long memberId) {
        if (!authorId.equals(memberId)) {
            throw new ForbiddenException();
        }
    }
}
