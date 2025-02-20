package com.chone.server.domains.user.exception;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ExceptionCode {
    // 인증
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "유효하지 않은 JwtToken"),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "만료된 JwtToken"),

    // 회원
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    USER_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "중복된 아이디 입니다."),
    USER_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "중복된 Email입니다."),
    USER_NOT_AUTHORITY(HttpStatus.BAD_REQUEST,"권한이 없습니다."),
    USER_CANT_LOGIN(HttpStatus.BAD_REQUEST, "사용할 수 없는 계정입니다."),
    USER_DELETED(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다.");



    // 상태, 메시지, 에러코드
    private final HttpStatus status;
    private final String message;
    private final String code = this.name();
}
