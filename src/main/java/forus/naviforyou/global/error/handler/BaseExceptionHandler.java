package forus.naviforyou.global.error.handler;

import forus.naviforyou.global.error.dto.BaseErrorResponse;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class BaseExceptionHandler {

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<BaseErrorResponse> handleBusinessException(
            BaseException e,
            HttpServletRequest request) {
        log.error("BusinessException: {} - {}", e.getErrorCode().getMessage(), request.getRequestURL());
        final ErrorCode errorCode = e.getErrorCode();
        final BaseErrorResponse response = BaseErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(200));
    }


    // HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못 할 경우 발생 - @Valid, @Validated
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<BaseErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {
        log.error("MethodArgumentNotValidException: {} - {}", e.getMessage(), request.getRequestURL());
        final BaseErrorResponse response = BaseErrorResponse.of(ErrorCode.METHOD_ARGUMENT_NOT_VALID, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // @ModelAttribute 으로 binding error 발생시 BindException
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<BaseErrorResponse> handleBindException(
            BindException e, HttpServletRequest request) {
        log.error("BindException: {} - {}", e.getMessage(), request.getRequestURL());
        final BaseErrorResponse response = BaseErrorResponse.of(
                ErrorCode.METHOD_ARGUMENT_NOT_VALID, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    // Enum type 일치하지 않아 binding 못할 경우 발생
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<BaseErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e,
            HttpServletRequest request) {
        log.error("MethodArgumentTypeMismatchException: {} - {}", e.getMessage(), request.getRequestURL());
        final BaseErrorResponse response = BaseErrorResponse.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 지원하지 않은 HTTP method 호출 할 경우 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<BaseErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {
        log.error("HttpRequestMethodNotSupportedException: {} - {}", e.getMessage(), request.getRequestURL());
        final BaseErrorResponse response = BaseErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }


}
