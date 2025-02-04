package com.library.bible.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PrintLog {
	public void printInfoByJoinPoint(JoinPoint joinPoint) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                    .currentRequestAttributes())
                    .getRequest();
            
            String host = request.getHeader("Host");
            String requestURI = request.getRequestURI();
            String httpMethod = request.getMethod();
            String className = joinPoint.getTarget().getClass().getSimpleName();
            Method method = getMethod(joinPoint);
            String clientIp = getClientIp();
            
            log.info("=============== Request Info ================");
            log.info("Host: {}", host);
            log.info("URI: {}", requestURI);
            log.info("HTTP Method: {}", httpMethod);
            log.info("Controller: {}", className);
            log.info("Method: {}", method.getName());
            log.info("Client IP: {}", clientIp);
            
            // 쿠키 정보 로깅
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                log.info("================ Cookie Info ================");
                for (Cookie cookie : cookies) {
                    log.info("Cookie Name: {}", cookie.getName());
                    log.info("Cookie Value: {}", cookie.getValue());
                }
            }

    	    log.info("==============================================");            	
        } catch (Exception e) {
            log.error("Logging failed", e);
        }
	}
	
	public void printErrorByJoinPoint(JoinPoint joinPoint, Exception ex) {
	    log.error("================ Exception Info ================");
	    log.error("Class: {}", joinPoint.getSignature().getDeclaringTypeName());
	    log.error("Method: {}", joinPoint.getSignature().getName());
	    log.error("error: {}", ex.getClass().getName());
	    log.error("Message: {}", ex.getMessage());
	    log.info("==============================================");            	
	}
	
	public void printInfoByRequest(HttpServletRequest request) {
	    try {
	        String host = request.getHeader("Host");
	        String requestURI = request.getRequestURI();
	        String httpMethod = request.getMethod();
	        String className = requestURI.split("/").length > 2 ? requestURI.split("/")[2] : ""; // URI에서 컨트롤러 이름 추출
	        String methodName = requestURI.split("/").length > 3 ? requestURI.split("/")[3] : ""; // URI에서 컨트롤러 이름 추출
	        String clientIp = getClientIp();
	        
	        log.info("=============== Request Info ================");
	        log.info("Host: {}", host);
	        log.info("URI: {}", requestURI);
	        log.info("HTTP Method: {}", httpMethod);
	        log.info("Controller: {}", className);
	        log.info("Method: {}", methodName);
	        log.info("Client IP: {}", clientIp);
	        
	        // 쿠키 정보 로깅
	        Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            log.info("================ Cookie Info ================");
	            for (Cookie cookie : cookies) {
	                log.info("Cookie Name: {}", cookie.getName());
	                log.info("Cookie Value: {}", cookie.getValue());
	            }
	        }

	        log.info("==============================================");            	

	    } catch (Exception e) {
	        log.error("Logging failed", e);
	    }
	}

	public void printErrorByRequest(HttpServletRequest request, Exception ex) {
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        log.error("================ Exception Info ================");
        log.error("URI: {}", requestURI);
        log.error("Method: {}", httpMethod);
        log.error("error: {}", ex.getClass().getName());
        log.error("Message: {}", ex.getMessage());
        log.error("==============================================");           	
	}

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
                
        // 프록시나 로드밸런서를 통해 접근하는 경우를 위한 헤더 체크
        String[] headerNames = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        String ip = null;
        for (String headerName : headerNames) {
            ip = request.getHeader(headerName);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                // 여러 IP가 쉼표로 구분되어 있는 경우 첫 번째 IP만 사용
                if (ip.contains(",")) {
                    ip = ip.split(",")[0];
                }
                break;
            }
        }
        
        // 모든 헤더에서 IP를 찾지 못한 경우
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // localhost IPv6 주소인 경우 IPv4로 변환
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        
        return ip;
    }
}
