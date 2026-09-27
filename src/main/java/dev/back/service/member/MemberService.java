package dev.back.service.member;

import dev.back.domain.member.Member;
import dev.back.dto.member.SignUpResponse;
import dev.back.global.exception.DuplicateEmailException;
import dev.back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignUpResponse join(String email, String password, String nickname) {
        if(memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();

        }

        String encodedPassword = passwordEncoder.encode(password);

        Member member = new Member(email, encodedPassword, nickname);
        Member savedMember = memberRepository.save(member);

        return new SignUpResponse(savedMember);
    }
}
