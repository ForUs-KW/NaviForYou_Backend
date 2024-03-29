package forus.naviforyou.global.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

@Getter
@Order(Ordered.HIGHEST_PRECEDENCE)
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
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST,3001 , "아이디와 비밀번호가 일치하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, 3002, "이미 사용중인 이메일 입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, 3003, "이미 사용중인 닉네임 입니다."),
    NO_SUCH_NAVER_USER(HttpStatus.BAD_REQUEST, 3004, "네이버 사용자를 불러올 수 없습니다"),
    INVALID_NAVER_USER(HttpStatus.BAD_REQUEST, 3003, "유효하지 않은 네이버 사용자 입니다"),

    /**
     * OAUTH : 4XXX
     */
    GET_OAUTH_TOKEN_FAILED(HttpStatus.UNAUTHORIZED, 4000, "oAuth 토큰 요청에 실패했습니다."),
    GET_OAUTH_USER_INFO_FAILED(HttpStatus.UNAUTHORIZED, 4000, "oAuth 사용자 정보를 가져오는데 실패했습니다."),

    /**
     * MAIL : 5XXX
     */
    UNABLE_TO_SEND_EMAIL(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "메일을 보낼 수 없습니다."),
    /**
     * VERIFICATION : 6XXX
     */
    EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, 6000, "코드 유효기간이 만료 되었습니다."),
    INCORRECT_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, 6001, "코드가 일치하지 않습니다"),

    /**
     * search : 7XXX
     */
    NO_SUCH_SEARCH(HttpStatus.NOT_FOUND, 7000, "해당 검색 결과가 존재하지 않습니다."),

    /**
     * search : 8XXX
     */
    NO_MAPPING_ROUTE(HttpStatus.NOT_FOUND, 8000, "경로 입력이 올바르지 않습니다."),
    NO_CLOSER_DISTANCE(HttpStatus.NOT_FOUND, 8000, "출발지/도착지 간 거리가 너무 가깝습니다"),

    /**
    * real- time data : 9XXX
     */
    NO_MAPPING_STATION_NUM(HttpStatus.NOT_FOUND, 9000, "정류장 번호 정보를 불러오지 못했습니다"),
    NO_MAPPING_STATION_INFO(HttpStatus.NOT_FOUND, 9000, "현재 정류장 정보를 불러오지 못했습니다"),

    /**   
     * find-route : 10XXX
     */
    NO_MAPPING_ROUTE_INFO(HttpStatus.NOT_FOUND, 9000, "일부 구간의 길찾기 정보가 존재 하지 않습니다"),
  
    /**
     * Place : 10XXX
     */
    NO_SUCH_BUILDING(HttpStatus.NOT_FOUND, 10000, "해당 건물 정보가 존재하지 않습니다."),
    FAILED_CONVERT_LOCATION(HttpStatus.BAD_REQUEST,10001,"해당 좌표에 대한 주소를 불러오는데 실패했습니다."),
    FAILED_REAL_TIME_SUBWAY(HttpStatus.BAD_REQUEST,10002,"지하철 도착 정보를 가져오는데 실패했습니다.")

    ;
    private final HttpStatus status;
    private final int code;
    private final String message;
}
