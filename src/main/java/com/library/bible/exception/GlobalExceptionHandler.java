package com.library.bible.exception;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ExceptionResponseUtil exceptionResponseUtil;

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionResponse> handleCustomException(CustomException e, HttpServletRequest request) {
        exceptionResponseUtil.printErrorLog("CustomException", e);
		return exceptionResponseUtil.createExceptionResponseEntity(e.getErrorCode(), request, e);
	}

	// MethodArgumentNotValidException 처리 (유효성 검증 실패)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException e,
			HttpServletRequest request) {
        exceptionResponseUtil.printErrorLog("MethodArgumentNotValidException", e);
		return exceptionResponseUtil.createExceptionResponseEntity(ExceptionCode.INVALID_INPUT_VALUE, request, e);
	}

	// HttpRequestMethodNotSupportedException 처리
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ExceptionResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException e,
			HttpServletRequest request) {
        exceptionResponseUtil.printErrorLog("HttpRequestMethodNotSupportedException", e);
		return exceptionResponseUtil.createExceptionResponseEntity(ExceptionCode.METHOD_NOT_ALLOWED, request, e);
	}

	// AccessDeniedException 처리
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e,
			HttpServletRequest request) {
        exceptionResponseUtil.printErrorLog("AccessDeniedException", e);
		return exceptionResponseUtil.createExceptionResponseEntity(ExceptionCode.FORBIDDEN, request, e);
	}

	// SQLException 처리
	@ExceptionHandler(SQLException.class)
	public ResponseEntity<ExceptionResponse> handleSQLException(SQLException e, HttpServletRequest request) {
        exceptionResponseUtil.printErrorLog("SQLException", e);
		return exceptionResponseUtil.createExceptionResponseEntity(ExceptionCode.DATABASE_ERROR, request, e);
	}

	// 나머지 모든 예외 처리
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleAllException(Exception e, HttpServletRequest request) {
        exceptionResponseUtil.printErrorLog("Unhandled exception occurred: ", e);
		return exceptionResponseUtil.createExceptionResponseEntity(ExceptionCode.INTERNAL_SERVER_ERROR, request, e);
	}
}
