package dev.back.repository.post;

import dev.back.domain.post.Post;
import dev.back.dto.post.PostListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            select new dev.back.dto.post.PostListResponse(
                p.id, p.title, m.nickname, p.createdAt, count(c)
            )
            from Post p join p.author m left join p.comments c
            group by p.id, p.title, m.nickname, p.createdAt
            order by p.createdAt desc, p.id desc
            """,
            countQuery = "select count(p) from Post p")
    Page<PostListResponse> findPageWithAuthorAndCommentCount(Pageable pageable);
}
