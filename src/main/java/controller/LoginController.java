package controller;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

public class LoginController {
	private ShopService shopService;
	private Validator validator;
	
	public void setShopService (ShopService shopService) {
		this.shopService = shopService;
	}
	
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	
	/*
		spring 4.0 이후부터 사용가능
		UserEntryController에서 보면,
		@RequestMapping(method=RequestMethod.GET) // url에 보임
		
		매개변수 : Model model
	*/
	@GetMapping
	public String loginForm(Model model) {
		model.addAttribute(new User());
		return "login"; // login.jsp로 보냄
	}
	
	/*
		User user : (리턴타입 파라미터값)
					user 파라미터값(입력된 값)을 저장하고 있는 객체
						 (아이디와 비밀번호)
		BindingResult bresult : 유효성 검증
		
		매개변수에 session을 등록해주면, 스프링 엔진이 나의 세션을 등록해줌
		HttpSession session : 로그인 세션 등록
	*/
	@PostMapping
	public ModelAndView login(User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		validator.validate(user, bresult);
		// 유효성 검증에서 걸리면
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		// db와 일치여부 확인
		try {
			User dbuser = shopService.getUser(user.getUserid());
			if(user.getPassword().equals(dbuser.getPassword())) {
				session.setAttribute("loginUser", dbuser);
			} else {
				bresult.reject("error.login.password"); // error.login.password=비밀번호를 확인하세요.
				mav.getModel().putAll(bresult.getModel());
				return mav;
			}
		} catch(EmptyResultDataAccessException e) {
			// EmptyResultDataAccessException : dbuser ==null 즉, db에서 조회된 레코드가 없는 경우
			bresult.reject("error.login.id"); // error.login.id=아이디를 확인하세요.
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("loginSuccess"); // loginSuccess.jsp
		return mav;
	}
}
