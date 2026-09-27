package dev.back.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class SignUpRequest {
    @NotBlank(message =  "이메일은 필수 입력 항목입니다.")
    @Email
    private String email;
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max=72)
    private String password;
    @NotBlank
    @Size(max = 30)
    private String nickname;
}
