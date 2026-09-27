package dev.back.global.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("이 작업을 수행할 권한이 없습니다.");
    }
}
