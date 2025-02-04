package com.library.bible.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.library.bible.aop.PrintLog;
import com.library.bible.security.jwt.JwtProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	private final JwtProvider jwtProvider;
	private final PrintLog printLog;
    
    @Override
    public void onLogoutSuccess(HttpServletRequest request, 
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
		// logging
		printLog.printInfoByRequest(request);
		
		// response
    	jwtProvider.removeTokenInCookie(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}
