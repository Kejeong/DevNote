package dev.back.domain.comment;

import dev.back.domain.member.Member;
import dev.back.domain.post.Post;
import dev.back.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(nullable = false, length = 1000)
    private String content;

    public Comment(Member author, String content) {
        this.author = author;
        this.content = content;
    }
}
