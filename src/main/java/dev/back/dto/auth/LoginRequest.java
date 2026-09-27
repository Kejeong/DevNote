package dev.back.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class LoginRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 8, max = 72)
    private String password;
}
