package aop;

import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Component
@Aspect
public class UserLoginAspect {
	
	/*
		advice 실행 시점을 정해주지 그 종류 중 하나가 
		- around : 핵심로직 전, 후
		- pointcut : 핵심로직 설정
			접근제어자 리턴타입 : *
			controller : 패키지
			User* : 클래스
			check* : 메서드
			(..) : 매개변수
			args(.., session) : 매개변수 내용
			
		pointcut 조건에 의해서 checkmain(HttpSession session)이 핵심로직이 되고,
		이 핵심로직이 실행하기 전,후에 userLoginCheck가 실행되는 거야
	 */
	@Around("execution(* controller.User*.check*(..)) && args(.., session)")
	public Object userLoginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser ==null) {
			// class LoginException .......
			throw new LoginException("로그인 후 거래하세요", "login.shop");
		}
		
		Object ret = joinPoint.proceed();
		return ret;
	}
}
