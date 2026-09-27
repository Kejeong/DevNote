package dev.back.controller.member;

import dev.back.dto.member.SignUpRequest;
import dev.back.dto.member.SignUpResponse;
import dev.back.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
       return memberService.join(
               signUpRequest.getEmail(),
               signUpRequest.getPassword(),
               signUpRequest.getNickname()
       );
    }
}
