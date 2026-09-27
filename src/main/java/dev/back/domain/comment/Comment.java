package dev.back.domain.comment;

import dev.back.domain.member.Member;
import dev.back.domain.post.Post;
import dev.back.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(nullable = false, length = 1000)
    private String content;

    public Comment(Post post, Member author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
