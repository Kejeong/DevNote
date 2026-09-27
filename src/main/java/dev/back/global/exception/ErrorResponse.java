package dev.back.global.exception;

public record ErrorResponse(
        int status,
        String code,
        String message,
        String path
) {
}
