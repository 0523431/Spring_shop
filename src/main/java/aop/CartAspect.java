package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;

@Component
@Aspect
@Order(2)
public class CartAspect {
	
	/*  AOP가 걸리는 시점
		모든 접근제어자,리턴타입
		controller패키지 + Cart*클래스 + check*메서드 + 모든 매개변수
		
		pointcut 조건에 의해서 public String checkout(HttpSession session)이 핵심로직이 되고,
		이 핵심로직이 실행하기 전,후에 cartCheck가 실행되는 거야
	*/
	@Around("execution(* controller.Cart*.check*(..))")
	public Object cartCheck(ProceedingJoinPoint joinPoint) throws Throwable {
		
		// 핵심로직에서 session객체를 첫번째 매개변수로 설정했기때문에 사용가능
		HttpSession session = (HttpSession)joinPoint.getArgs()[0];
		
		// session으로부터 값을 받아서 if의 조건을 확인
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart ==null || cart.getItemSetList().size() ==0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다", "../item/list.shop"); 
		}
		// session으로부터 값을 받아서 확인
		if(session.getAttribute("loginUser") ==null) {
			throw new LoginException("로그인 한 고객만 상품 주문이 가능합니다", "../user/login.shop");
		}
		
		// 그냥 진행해 == 핵심로직으로 가 == CartController의 checkout()메서드 실행
		Object ret = joinPoint.proceed();
		return ret;
	}
}
