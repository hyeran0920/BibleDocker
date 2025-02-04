package com.library.bible.security.jwt;

import java.util.Arrays;
import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.library.bible.security.utils.MemberUserDetails;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtProvider {
	// JWT 생성(access token)
	public String generateAccessToken(MemberUserDetails memberUserDetails) {
        // HS256(RSA 방식 X)
        String jwt = JWT.create()
                .withSubject(memberUserDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .withClaim("id", memberUserDetails.getMember().getMemId()) // 비공개 클레임
                .sign(Algorithm.HMAC256(JwtProperties.ACCESS_SECRET));
        return jwt;
	}
	
	// refresh token 생성
	public String generateRefreshToken(MemberUserDetails memberUserDetails) {
        // HS256(RSA 방식 X)
        String jwt = JWT.create()
                .withSubject(memberUserDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(JwtProperties.REFRESH_SECRET));
        return jwt;
	}
	
	// header에서 JWT 존재 확인
	public String resolveTokenInHeader(HttpServletRequest request) {
		String accessToken = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);
		return accessToken;
	}
	
	// cookie에서 JWT 존재 확인
	public String resolveTokenInCookie(HttpServletRequest request) {
      Cookie[] cookies = request.getCookies();
      
      if (cookies == null) return null;
      
      String accessToken = Arrays.stream(cookies)
    		    .filter(cookie -> JwtProperties.ACCESS_COOKIE_STRING.equals(cookie.getName()))
    		    .findFirst()
    		    .map(Cookie::getValue)
    		    .orElse(null);
      return accessToken;
	}
	
	// 토큰 검증 후 memId 반환
	public Integer getMemIdAndVerify(String accessToken) {
        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        accessToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");

        // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
        // 내가 SecurityContext에 집적 접근해서 세션을 만들때 자동으로 UserDetailsService에 있는
        // loadByUsername이 호출됨.
        Integer memId = JWT.require(Algorithm.HMAC256(JwtProperties.ACCESS_SECRET))
                             .build()
                             // 토큰 검증(유효기간 포험)
                             .verify(accessToken)
                             .getClaim("id")
                             .asInt();
        
        return memId;
	}
	
	// 토큰 검증
	public void verify(String jwt) {
		JWT.require(Algorithm.HMAC256(JwtProperties.ACCESS_SECRET))
	        .build()
	        // 토큰 검증(유효기간 포험)
	        .verify(jwt);
	}
	
	// 헤더에 jwt 저장
	public void saveTokenInHeader(HttpServletResponse response, String accescToken, String refreshToken) {
      response.addHeader(JwtProperties.ACCESS_HEADER_STRING, JwtProperties.TOKEN_PREFIX+accescToken);
      response.addHeader(JwtProperties.REFRESH_HEADER_STRING, JwtProperties.TOKEN_PREFIX+refreshToken);
	}

	// 쿠키에 jwt 저장
	public void saveTokenInCookie(HttpServletResponse response, String accescToken, String refreshToken) {
        ResponseCookie accessCookie = ResponseCookie
        		.from(JwtProperties.ACCESS_COOKIE_STRING, accescToken)
	    	    .path("/")
//	    	    .secure(true)	 // HTTPS에서만 전송
	    	    .httpOnly(true)	 // JavaScript 접근 불가
	    	    .maxAge(JwtProperties.ACCESS_EXPIRATION_TIME / 1000) // 초단위
        	    .build();
        ResponseCookie refreshCookie = ResponseCookie
        		.from(JwtProperties.REFRESH_COOKIE_STRING, refreshToken)
        		.path("/")
//        		.secure(true)	 // HTTPS에서만 전송
        		.httpOnly(true)	 // JavaScript 접근 불가
        		.maxAge(JwtProperties.REFRESH_EXPIRATION_TIME / 1000) // 초단위
        		.build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
	}
	
	// 쿠키에 저장된 jwt 제거
	public void removeTokenInCookie(HttpServletResponse response) {
        // Access Token 쿠키 삭제
        ResponseCookie accessCookie = ResponseCookie
            .from(JwtProperties.ACCESS_COOKIE_STRING, "")
            .path("/")
            .maxAge(0)
            .build();

        // Refresh Token 쿠키 삭제
        ResponseCookie refreshCookie = ResponseCookie
            .from(JwtProperties.REFRESH_COOKIE_STRING, "")
            .path("/")
            .maxAge(0)
            .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
	}
}
