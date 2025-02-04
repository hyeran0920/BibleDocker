package com.library.bible.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.library.bible.security.filter.JwtAuthenticationFilter;
import com.library.bible.security.filter.JwtAuthorizationFilter;
import com.library.bible.security.handler.CustomAccessDeniedHandler;
import com.library.bible.security.handler.CustomAuthenticationEntryPoint;
import com.library.bible.security.handler.CustomLogoutSuccessHandler;
import com.library.bible.security.jwt.JwtProvider;
import com.library.bible.aop.PrintLog;
import com.library.bible.exception.ExceptionResponseUtil;
import com.library.bible.member.repository.IMemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final IMemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final CorsConfiguration corsConfiguration;
    private final ExceptionResponseUtil exceptionResponseUtil;
    private final PrintLog printLog;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	System.out.println("security - flter");

        http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.sessionManagement(session -> session
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), exceptionResponseUtil, jwtProvider, printLog)) // AuthenticationManager가 로그인 실행함
			.addFilter(new JwtAuthorizationFilter(authenticationManager(), memberRepository, jwtProvider, exceptionResponseUtil, printLog)) // AuthenticationManager 필요함
			.exceptionHandling(e -> e // 에러 처리
	                .authenticationEntryPoint(authenticationEntryPoint)
	                .accessDeniedHandler(accessDeniedHandler)
            )
			.logout(logout -> logout
	                .logoutUrl("/logout")
	                .logoutSuccessHandler(logoutSuccessHandler)
	                .permitAll()
            )
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
	                .requestMatchers(HttpMethod.POST, "/api/members/user").permitAll()  // 일반 사용자 생성은 모두 허용
	                .requestMatchers(HttpMethod.POST, "/api/members/admin").hasRole("ADMIN")  // 관리자만 관리자 생성 가능
	                .requestMatchers(HttpMethod.GET, "/api/members/me").hasAnyRole("ADMIN", "USER")  // 조회는 관리자만 가능
	                .requestMatchers(HttpMethod.GET, "/api/members/me/addresses/**").hasRole("USER")     // 본인 주소 조회
	                .requestMatchers(HttpMethod.GET, "/api/members/*/addresses/**").hasRole("USER")      // ID로 주소 조회	                .requestMatchers(HttpMethod.GET, "/api/members/**").hasRole("ADMIN")  // 조회는 관리자만 가능
//                    .requestMatchers("/**").permitAll(); // 모든 요청에 대해 인증 없이 접근 허용
                    .anyRequest().permitAll() // 그 외 요청은 인증 필요
//                    .anyRequest().authenticated()); // 그 외 요청은 인증 필요
            );
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 엔드포인트에 CORS 설정 적용
        return source;
    }
}
