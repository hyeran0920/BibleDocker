package com.library.bible.security.handler;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.library.bible.aop.PrintLog;
import com.library.bible.exception.ExceptionCode;
import com.library.bible.exception.ExceptionResponseUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// 인증된 사용자가 권한이 없는 리소스에 접근할 때 호출됨
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ExceptionResponseUtil exceptionResponseUtil;
	private final PrintLog printLog;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		// logging
		printLog.printInfoByRequest(request);
		printLog.printErrorByRequest(request, accessDeniedException);
		
		// response
		exceptionResponseUtil.printErrorLog("Forbidden error happened :", accessDeniedException);
		exceptionResponseUtil.sendErrorResponse(ExceptionCode.FORBIDDEN, request, response, accessDeniedException);
	}
}
