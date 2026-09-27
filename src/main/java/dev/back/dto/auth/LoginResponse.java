package dev.back.dto.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginResponse {
    private final String accessToken;
    private final String tokenType = "Bearer";
}
