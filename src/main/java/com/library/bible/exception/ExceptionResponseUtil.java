package com.library.bible.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionResponseUtil {
    
    private final ObjectMapper objectMapper;

    // ExceptionResonse 생성
    public ExceptionResponse createExceptionResponse(ExceptionCode errorCode, HttpServletRequest request, Exception e) {
        return ExceptionResponse.builder()
				.status(errorCode.getHttpStatus().value())
				.error(errorCode.getHttpStatus().name())
				.message(errorCode.getMessage())
				.detailMessage(e.getMessage())
				.timestamp(LocalDateTime.now())
				.path(request.getRequestURI())
				.build();
    }

    // security에서 ErrorResponse 반환
    public void sendErrorResponse(ExceptionCode errorCode, HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
    	ExceptionResponse errorResponse = createExceptionResponse(errorCode, request, e);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorResponse.getStatus());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
    
    // GlobalExceptionHandler에서 사용함
	public ResponseEntity<ExceptionResponse> createExceptionResponseEntity(ExceptionCode errorCode, HttpServletRequest request, Exception e) {
		ExceptionResponse response = createExceptionResponse(errorCode, request, e);
		return ResponseEntity
				.status(errorCode.getHttpStatus())
				.body(response);
	}
	
	// 로그에 에러 출력
	public void printErrorLog(String fommat, Exception e) {
        log.error(fommat, e);
	}
}
