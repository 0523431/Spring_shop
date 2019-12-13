package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;
import util.UserValidator;

/*
	p:shopService-ref="shopService"
	p:userValidator-ref="userValidator"
 */
public class UserEntryController {
	private ShopService shopService;
	private UserValidator userValidator;
	
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	public void setUserValidator(UserValidator userValidator) {
		this.userValidator = userValidator;
	}
	
	/*
	 * <form:form modelAttribute="user" method="post" action="userEntry.shop">
	 */
	@ModelAttribute
	public User getUser() {
		User user = new User();
		user.setUsername("해리 제임스 포터");
		// return new User();
		return user;
	}
	
	/* spring-mvc.xml에서
	 * 	<value>
			userEntry.shop=userEntryController
		</value>
		userEntryController
	 */
	@RequestMapping(method=RequestMethod.GET) // url에 보임
	public String userEntryForm() {
		return "userEntry"; // view만 설정
		// return null;을 해도 userEntry.shop으로 감 (그럴게 설정해 놓았음)
	}
		
	
	@RequestMapping(method=RequestMethod.POST) // url에 안보임
	/*
		User user : (리턴타입 파라미터값)
					user ==파라미터값(입력된 값)을 저장하고 있는 객체
		BindingResult bindResult : 유효성 검증
	*/
	public ModelAndView userEntry(User user, BindingResult bindResult) {
		ModelAndView mav = new ModelAndView();
		
		userValidator.validate(user, bindResult);
		
		// 유효성 검증에서 걸리면,
		if(bindResult.hasErrors()) {
			mav.getModel().putAll(bindResult.getModel());
			// System.out.println(bindResult.getModel());
			return mav;
		}
		
		try {
			shopService.insertUser(user);
			mav.addObject("user", user); // 입력된 객체 user가 catch에 해당안되고 userEntrySuccess.jsp로 return됨
		} catch(DataIntegrityViolationException e) {
			// DataIntegrityViolationException : spring jdbc에서만 발생되는 예외 (mybatis에서는 xxx)
			// 키 중복 오류
			// 아이디 중복이 grobal
			e.printStackTrace();
			bindResult.reject("error.duplicate.user");
			mav.getModel().putAll(bindResult.getModel());
			return mav;
		}
		mav.setViewName("userEntrySuccess"); // userEntrySuccess.jsp
		return mav;
	}
	
	
	/* birthday yyyy-MM-dd 형식에 맞게 입력했는지 유효성확인 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format, false));
		/*
			Date.class : 형변환 대상이 되는 자료형 (bean클래스인 User 클래스에서 Date 타입을 찾음)
			format : 형식지정
			true/false : 비입력 허용-선택입력(null o) / 비입력 불허-필수입력(null x)
		*/
	}
}
