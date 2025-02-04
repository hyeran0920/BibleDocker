package com.library.bible.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    // 회원 관련 에러
    MEMBER_INSERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원 등록에 실패했습니다."),
    MEMBER_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원 수정에 실패했습니다."),
    MEMBER_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "회원 삭제에 실패했습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    
    // ROLE 관련 에러
    ROLE_INSERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "권한 등록에 실패했습니다."),
    ROLE_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "역할 수정에 실패했습니다."),
    ROLE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "역할 삭제에 실패했습니다."),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다."),
    
    // Address 관련 에러
    ADDRESS_INSERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "주소 등록에 실패했습니다."),
    ADDRESS_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "주소 수정에 실패했습니다."),
    ADDRESS_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "주소 삭제에 실패했습니다."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "주소를 찾을 수 없습니다."),
    
    // MemberRent 관련 에러
    MEMBER_RENT_INSERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "대여 정보 등록에 실패했습니다."),
    MEMBER_RENT_UPDATE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "대여 정보 수정에 실패했습니다."),
    MEMBER_RENT_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "대여 정보 삭제에 실패했습니다."),
    MEMBER_RENT_NOT_FOUND(HttpStatus.NOT_FOUND, "대여 정보를 찾을 수 없습니다."),
    
	// 인증/인가 관련 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "해당 리소스에 대한 접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰 서명이 유효하지 않습니다."),
    TOKEN_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰의 사용자를 찾을 수 없습니다."),
    
    // 인증 실패 관련
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED, "계정이 비활성화되었습니다."),
    ACCOUNT_LOCKED(HttpStatus.UNAUTHORIZED, "계정이 잠겼습니다."),
    ACCOUNT_EXPIRED(HttpStatus.UNAUTHORIZED, "계정이 만료되었습니다."),
    CREDENTIALS_EXPIRED(HttpStatus.UNAUTHORIZED, "비밀번호가 만료되었습니다."),
	
    // 일반적인 에러
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메소드입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // QR 이미지 생성 에러
	QR_IMAGE_CREATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "QR 이미지 생성 실패");
	
    private final HttpStatus httpStatus;
    private final String message;
}
