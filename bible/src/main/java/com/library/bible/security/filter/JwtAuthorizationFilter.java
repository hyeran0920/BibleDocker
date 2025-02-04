package com.library.bible.security.filter;

import com.library.bible.security.jwt.JwtProvider;
import com.library.bible.security.utils.MemberUserDetails;
import com.library.bible.aop.PrintLog;
import com.library.bible.exception.CustomException;
import com.library.bible.exception.ExceptionCode;
import com.library.bible.exception.ExceptionResponseUtil;
import com.library.bible.member.model.Member;
import com.library.bible.member.repository.IMemberRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

//시큐리티가 filter 가리고 있는데, 그 필터 중에 BasicAuthenticationFilter라는 것이 있음
//권한이나 인증이 필요한 특정 주소를 요청했을 때 아래 필터를 무조건 동작함
//만약 권한이나 인증이 필요한 주소가 아니라면 동작하지 않음
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private IMemberRepository memberRepository;
    private JwtProvider jwtProvider;
    private final ExceptionResponseUtil exceptionResponseUtil;
	private final PrintLog printLog;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, IMemberRepository memberRepository, JwtProvider jwtProvider,
    		ExceptionResponseUtil exceptionResponseUtil, PrintLog printLog) {
        super(authenticationManager);
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
        this.exceptionResponseUtil = exceptionResponseUtil;
        this.printLog = printLog;
    }

    // 인증이나 권한이 필요한 주소요청이 있을 경우 해당 필터 동작
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // header에 JWT 토큰이 존재하는지 확인
//    	String accessToken = jwtProvider.resolveTokenInHeader(request);
//        if (accessToken == null || !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
//            chain.doFilter(request, response);
//            return;
//        }
        
        // cookie에 JWT 토큰이 존재하는지 확인
    	String accessToken = jwtProvider.resolveTokenInCookie(request);
    	if(accessToken == null) {
	          chain.doFilter(request, response);
	          return;
    	}

        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
    	Integer memId = null;
    	try {
            memId = jwtProvider.getMemIdAndVerify(accessToken);
    	} catch(Exception e) {
			exceptionResponseUtil.sendErrorResponse(ExceptionCode.INVALID_TOKEN_SIGNATURE, request, response, e);
			
			// logging
			printLog.printInfoByRequest(request);
			printLog.printErrorByRequest(request, e);
			return;
    	}

        if (memId != null) {
        	// 사용자 id로 사용자 조회
            Member member = memberRepository.selectMember(Integer.valueOf(memId));
            if (member == null) {
            	CustomException e = new CustomException(ExceptionCode.TOKEN_USER_NOT_FOUND);
    			exceptionResponseUtil.sendErrorResponse(e.getErrorCode(), request, response, e);
    			
    			// logging
    			printLog.printInfoByRequest(request);
    			printLog.printErrorByRequest(request, e);
    			return;
            }
            
            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장
            MemberUserDetails principalDetails = new MemberUserDetails(member);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함
                    null,
                    principalDetails.getAuthorities());

            // 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
