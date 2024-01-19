package forus.naviforyou.global.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * Common : 1XXX
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 1000, "잘못된 요청입니다\n다시 한 번 확인해주세요"),

    NOT_FOUND(HttpStatus.NOT_FOUND, 1001, "리소스를 찾을 수 없음"),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 1002, "허용되지 않은 Request Method 호출"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1003, "알 수 없는 오류가 발생하였습니다."),

    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, 1004, "요청 인자가 유효하지 않음"),

    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, 1005, "유효하지 않은 값 타입"),

    FORBIDDEN(HttpStatus.FORBIDDEN, 1006,"접근 권한이 없습니다."),

    /**
     * JWT : 2XXX
     */
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, 2000, "유효하지 않은 JWT 서명입니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, 2001, "유효하지 않은 JWT 토큰입니다."),

    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, 2001, "만료된 JWT 토큰입니다."),

    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, 2002, "지원하지 않는 JWT 토큰입니다."),

    /**
     * Member : 3XXX
     */
    NO_SUCH_EMAIL(HttpStatus.NOT_FOUND, 3000, "해당 이메일이 존재하지 않습니다." ),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST,3001 , "아이디와 비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}
