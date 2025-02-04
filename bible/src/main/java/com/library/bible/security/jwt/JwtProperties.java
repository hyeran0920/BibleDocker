package com.library.bible.security.jwt;

public interface JwtProperties {
	// access token
	String ACCESS_SECRET = "cos1"; // 우리 서버만 알고 있는 비밀값
	//int ACCESS_EXPIRATION_TIME = 10 * 60 * 1000; // 10분 (1/1000초)
	int ACCESS_EXPIRATION_TIME = 1000 * 60 * 1000; 
	String ACCESS_HEADER_STRING = "Authorization";
	String ACCESS_COOKIE_STRING = "authorization";
	
	// refresh token
	String REFRESH_SECRET = "cos2"; // 우리 서버만 알고 있는 비밀값
	int REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7일
	String REFRESH_HEADER_STRING = "Refresh-Token";
	String REFRESH_COOKIE_STRING = "refresh_token";
	
	// common
	String TOKEN_PREFIX = "Bearer ";
}
