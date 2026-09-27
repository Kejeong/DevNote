package dev.back.service.member;

import dev.back.domain.member.Member;
import dev.back.dto.auth.LoginRequest;
import dev.back.dto.auth.LoginResponse;
import dev.back.dto.member.SignUpResponse;
import dev.back.global.exception.DuplicateEmailException;
import dev.back.global.exception.InvalidCredentialsException;
import dev.back.global.jwt.JwtTokenProvider;
import dev.back.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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

    // 로그인
    public LoginResponse login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(),
                member.getEmail()
        );

        return new LoginResponse(accessToken);
    }
}
