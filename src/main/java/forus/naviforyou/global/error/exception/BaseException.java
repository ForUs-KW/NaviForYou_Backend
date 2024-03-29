package forus.naviforyou.global.error.exception;

import forus.naviforyou.global.error.dto.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

//    public BaseException(ErrorCode errorCode) {
//        super(errorCode.getMessage());
//        this.errorCode = errorCode;
//    }
//
//    public BaseException(ErrorCode errorCode, String message) {
//        super(message);
//        this.errorCode = errorCode;
//    }
}