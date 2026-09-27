package dev.back.dto.member;

import dev.back.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final LocalDateTime createdAt;

    public SignUpResponse(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.createdAt = member.getCreatedAt();
    }
}
