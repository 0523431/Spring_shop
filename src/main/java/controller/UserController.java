package controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private ShopService service;
	
	@GetMapping("*")
	public String form(Model model) {
		model.addAttribute(new User());
		return null;
	}
	
	@PostMapping("userEntry")
	public ModelAndView userEntry(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		
		// 유효성 검증
		// @Valid : 이 가능하려면 User 객체에 어노테이션이 되어있어야함
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user"); // (최상단)입력정보에 문제가 있습니다
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		
		try {
			// useraccount테이블에 내용 등록
			service.userInsert(user);
			// login.jsp로 이동
			// 이동방법1 (아이디값 넘겨주기)
			mav.setViewName("user/login");
			mav.addObject("user", user); // login.jsp로 넘어가는 값 modelAttribute="user"
			
			// 이동방법2 (넘겨주지않음)
			// mav.addObject("user", new User()); // 값이 나타나지않음
			
			// 이동방법3 (넘겨주지않음)
			// mav.setViewName("redirect:user/login.shop"); // 값이 나타나지 않음
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			bresult.reject("error.duplicate.user");
		}
		return mav;
	}
	
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) {
		
		ModelAndView model = new ModelAndView(); // view
		
		// validation
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			model.getModel().putAll(bresult.getModel());
			return model;
		}
		
		try {
			User dbUser = service.getUser(user.getUserid());
			if(!dbUser.getPassword().equals(user.getPassword())) {
				bresult.reject("error.login.password");
				return model;
			} else {
				session.setAttribute("loginUser", dbUser);
				model.setViewName("redirect:main.shop");
			}
		} catch(EmptyResultDataAccessException e) {
			e.printStackTrace();
			bresult.reject("error.login.id");
		}
		return model;
	}
	
//	@RequestMapping("logout")
//	public ModelAndView logout(HttpSession session) {
//		ModelAndView mav = new ModelAndView();
//		session.invalidate();
//		mav.setViewName("redirect:login.shop");
//		return mav;
//	}
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login.shop";
	}
	
	/* check로 시작하고, session로 끝나면 ==> 
	 * checkmain : 핵심로직이야 (받아서 user/main.shop으로 보내는거)
	 * 
	 * session은 aop를 실행하게 만드는 조건 때문에 넣어줌
	 * 
	 * UserLoginAspect 클래스에 해당하는 핵심로직
	 */
	@RequestMapping("main")
	public String checkmain(HttpSession session) {
		return "user/main";
	}
}
