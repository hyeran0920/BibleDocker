package com.library.bible.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class ControllerLoggingAspect {
	private final PrintLog printLog;
	
	@Before("@within(org.springframework.web.bind.annotation.RestController) ||"
			+ "@within(org.springframework.stereotype.Controller)")
    public void beforeControllerMethods(JoinPoint joinPoint) {
		printLog.printInfoByJoinPoint(joinPoint);
    }
	
	@AfterThrowing(pointcut = "@within(org.springframework.web.bind.annotation.RestController) || "
			+ "@within(org.springframework.stereotype.Controller) || "
	        + "@within(org.springframework.web.bind.annotation.RestControllerAdvice)",
	        throwing = "ex")
	public void afterThrowingAdvice(JoinPoint joinPoint, Exception ex) {
		printLog.printErrorByJoinPoint(joinPoint, ex);
	}
}
