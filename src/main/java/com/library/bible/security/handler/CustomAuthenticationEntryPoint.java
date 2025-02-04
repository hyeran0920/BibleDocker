package com.library.bible.security.handler;

import java.io.IOException;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.library.bible.exception.ExceptionResponseUtil;
import com.library.bible.aop.PrintLog;
import com.library.bible.exception.ExceptionCode;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

// 인증되지 않은 사용자가 보호된 리소스에 접근할 때 호출됨
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ExceptionResponseUtil exceptionResponseUtil;
	private final PrintLog printLog;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		// logging
		printLog.printInfoByRequest(request);
		printLog.printErrorByRequest(request, authException);
		
		// response
		exceptionResponseUtil.printErrorLog("Unauthorized error happened : ", authException);
		exceptionResponseUtil.sendErrorResponse(ExceptionCode.UNAUTHORIZED, request, response, authException);
	}
}