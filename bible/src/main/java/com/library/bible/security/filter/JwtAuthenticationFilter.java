package com.library.bible.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.bible.aop.PrintLog;
import com.library.bible.exception.ExceptionCode;
import com.library.bible.exception.ExceptionResponseUtil;
import com.library.bible.security.jwt.JwtProvider;
import com.library.bible.security.utils.LoginDto;
import com.library.bible.security.utils.MemberUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.*;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 존재함
// /login 요청 시 username, password 전송(POST) 하면
// UsernamePasswordAuthenticationFilter 동작함
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ExceptionResponseUtil exceptionResponseUtil;
    private final JwtProvider jwtProvider;
	private final PrintLog printLog;

    // /login 요청 을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1. username과 password 받아서
        try {
            // request에서 로그인 객체
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

            // 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            // 2. 정상인지 로그인 시도를 해봄
            // authenticationmanager로 로그인 시도하면 PrincipalDetailsService 호출 -> loadUserByUsernam()이 자동으로 실행됨
            // MemberDetailsService의 loadUserByUsernam() 함수가 실행된 후 정상이면 authentication이 리던됨
            // DB에 있는 username과 passwrod가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 3. PrincipalDetails를 세션에 담고
            // 세션에 담는 이유 : 권한 관리를 위해서
//            MemberUserDetails principalDetails = (MemberUserDetails) authentication.getPrincipal();

            // authentication 객체가 session 영역에 저장을 해야하고 그 방법이 returne됨
            // 리턴 이유 : 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리 때문에 session 넣음

            // JWT 토큰 생성은 successfulAuthentication() 함수에서

            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 4. JWT 토큰을 만들어서 반환
    // attemptAuthentication 실행 이후 인증이 정상적으로 되었으면 실행됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
    	// JWT 토큰 생성
        MemberUserDetails memberUserDetails = (MemberUserDetails) authResult.getPrincipal();
        String accescToken = jwtProvider.generateAccessToken(memberUserDetails);
        String refreshToken = jwtProvider.generateRefreshToken(memberUserDetails);
        
        System.out.println("로그인했습니다");
        // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response에 반환
        // header 방식
//        jwtProvider.saveTokenInHeader(response, accescToken, refreshToken);
        
        // cookie 방식
        jwtProvider.saveTokenInCookie(response, accescToken, refreshToken);
        
		// logging
		printLog.printInfoByRequest(request);
    }
    
    // 로그인 실패 시 실행됨
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, 
            HttpServletResponse response, AuthenticationException failed) 
            throws IOException {
		exceptionResponseUtil.printErrorLog("# Authentication failed: ", failed);
		
	    ExceptionCode exceptionCode;
	    if (failed instanceof DisabledException) {
	        exceptionCode = ExceptionCode.ACCOUNT_DISABLED;
	    } else if (failed instanceof LockedException) {
	        exceptionCode = ExceptionCode.ACCOUNT_LOCKED;
	    } else if (failed instanceof AccountExpiredException) {
	        exceptionCode = ExceptionCode.ACCOUNT_EXPIRED;
	    } else if (failed instanceof CredentialsExpiredException) {
	        exceptionCode = ExceptionCode.CREDENTIALS_EXPIRED;
	    } else {
	        exceptionCode = ExceptionCode.INVALID_CREDENTIALS;
	    }
		exceptionResponseUtil.sendErrorResponse(exceptionCode, request, response, failed);
		
		// logging
		printLog.printInfoByRequest(request);
		printLog.printErrorByRequest(request, failed);
    }
}
