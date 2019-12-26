package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.LoginException;
import logic.User;

@Component
@Aspect
@Order(1)
public class AdminAspect {
	
	// 선생님방법
	@Around("execution(* controller.Admin*.*(..))")
	public Object adminCheck(ProceedingJoinPoint joinPoint) throws Throwable {
		// 핵심로직에서 session객체의 순서를 몰라도 사용할 수 있게
		User loginUser = null;
		for(Object o : joinPoint.getArgs()) {
			if(o instanceof HttpSession) {
				HttpSession session = (HttpSession)o;
				loginUser = (User)session.getAttribute("loginUser");
			}
		}

		// session으로 부터 값을 받아서 로그인 여부를 확인
		if (loginUser == null) {
			// class LoginException : 우리가 강제로 만든 예외 (로그인이 안된 경우)
			throw new LoginException("로그인 후 가능합니다(admin)", "../user/login.shop");
		}

		if (!loginUser.getUserid().equals("headmaster")) {
			throw new LoginException("관리자만 가능한 거래 페이지 입니다", "../user/main.shop?id="+loginUser.getUserid());
		}

		Object ret = joinPoint.proceed();
		return ret;
	}
	
	// 선생님방법이랑 일부 동일
//	@Around("execution(* controller.Admin*.*(..))")
//	public Object adminIdCheck(ProceedingJoinPoint joinPoint) throws Throwable {
//		// 핵심로직에서 session객체를 첫번째 매개변수로 설정했기때문에 사용가능
//		HttpSession session = (HttpSession)joinPoint.getArgs()[0];
//						
//		// session으로 부터 값을 받아서 로그인 여부를 확인
//		User loginUser = (User)session.getAttribute("loginUser");
//		if(loginUser ==null) {
//			// class LoginException : 우리가 강제로 만든 예외 (로그인이 안된 경우)
//			throw new LoginException("로그인 후 가능합니다(admin)", "../user/login.shop");
//		}
//		
//		if(!loginUser.getUserid().equals("headmaster")) {
//			throw new LoginException("관리자만 가능한 거래 페이지 입니다", "../user/main.shop");
//		}
//		
//		Object ret = joinPoint.proceed();
//		return ret;
//	}
	
//	@Around("execution(* controller.Admin*.*(..)) && args(.., session)")
//	public Object adminIdCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable {
//	
//		// session으로 부터 값을 받아서 로그인 여부를 확인
//		User loginUser = (User)session.getAttribute("loginUser");
//		if(loginUser ==null) {
//			// class LoginException : 우리가 강제로 만든 예외 (로그인이 안된 경우)
//			throw new LoginException("로그인 후 가능합니다(admin)", "../user/login.shop");
//		}
//		
//		if(!loginUser.getUserid().equals("headmaster")) {
//			throw new LoginException("관리자만 가능한 거래 페이지 입니다", "../user/main.shop");
//		}
//		
//		Object ret = joinPoint.proceed();
//		return ret;
//	}
}
