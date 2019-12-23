package controller;

import java.util.List;

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

import exception.LoginException;
import logic.Item;
import logic.Sale;
import logic.SaleItem;
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
	
	/*  AOP에 걸리는 핵심로직
		1. 로그인 확인
		2. session등록 아이디와 로그인 아이디 확인
		3. 관리자인 경우 확인
	*/
//	@RequestMapping("mypage")
	@GetMapping("mypage")
	public ModelAndView checkmypage(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// heamaster인 경우 파라미터에 해당하는 id조회
		// (관리자때문에 userLogin은 사용할 수 없음)
		User user = service.getUser(id);
		
		// 사용자가 주문한 모든 주문내역을 조회
		List<Sale> salelist = service.salelist(id);
		for(Sale sale : salelist) {
			// 주문번호에 해당하는 주문 상품 내역 조회(아이템 리스트)
			List<SaleItem> saleitemlist = service.saleItemList(sale.getSaleid());
			for(SaleItem si :saleitemlist) { // 주문내역 1개 saleitemlist
				// 주문내역 1개에 해당하는 Item 조회
				// 주문내역이 있을 때, 아이템 삭제가 불가능함 (db 외래키 설정)
				Item item = service.itemInfo(si.getItemid());
				si.setItem(item); // SaleItem에 저장
			}
			sale.setItemList(saleitemlist);
		}
		
		mav.addObject("user", user);
		mav.addObject("salelist", salelist); // 결국 모든 정보는 다 salelist가 가지고 있게 됨
		return mav;
	}
	
	/* AOP 대상
	 * update, delete 두개의 요청을 하나의 method로 실행할 수 있음
	 */
	@GetMapping(value= {"update", "delete"})
	public ModelAndView checkview(String id, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.getUser(id);
		mav.addObject("user", user);
		return mav;
	}
	
//	@PostMapping("update")
	@RequestMapping("update")
	public ModelAndView checkupdate(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			bresult.reject("error.input.user");
			return mav;
		}
		// 비밀번호 검증 : 입력된 비밀번호와 db에 저장된 비밀번호를 비교
		// 일치 : update
		// 불일치 : 메시지를 유효성출력으로 화면에 출력
		// ↓ 이건 관리자일때 곤란해짐
//		User dbUser = service.getUser(user.getUserid());
//		if(!dbUser.getPassword().equals(user.getPassword())) {
//			bresult.reject("error.login.password");
//			return mav;
//		}
		// ↓ 이렇게 하면, 관리자일때 다른사람의 정보를 수정하고자 할 때 관리자의 비밀번호를 입력하면 수정이 가능해짐
		User loginUser = (User)session.getAttribute("loginUser");
		if(!user.getPassword().equals(loginUser.getPassword())) {
			bresult.reject("error.login.password");
			return mav;
		}
		
		// 이제 진짜 업데이트 기능
		try {
			service.userUpdate(user);
			mav.setViewName("redirect:mypage.shop?id="+user.getUserid());
			// 관리자가 아닌경우, session에 있는 로그인정보 역시 수정해줘야함
			// 업데이트가 성공하면 수정된 로그인 정보를 바꿔줌
			if(!loginUser.getUserid().equals("headmaster")) {
				session.setAttribute("loginUser", user);
			}
		} catch(Exception e) {
			e.printStackTrace();
			bresult.reject("error.user.update");
		}
		return mav;
	}
	
	// parameter가 2개 필요해 ==> 아이디, 비밀번호
	// User user 객체로 받아도 되고 (아이디와 비밀번호만 있음 == form에서 그것만 넘겨줌)
	// String userid(히든), String password 이렇게 따로 받아도 됨
	@PostMapping("delete")
	public ModelAndView checkdelete(User user, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		// 웹단에서 유효성 검증을 하지 않음 ==> 예외처리 할거야
		// session에서 loginUser를 가져옴
		User loginUser = (User)session.getAttribute("loginUser");
		if(!loginUser.getPassword().equals(user.getPassword())) {
			throw new LoginException("회원탈퇴시 비밀번호가 틀립니다", "delete.shop?id="+user.getUserid());
		}
		try {
			service.userDelete(user.getUserid());
			if(loginUser.getUserid().equals("headmaster")) {
				mav.setViewName("redirect:/admin/list.shop");
			} else {
				System.out.println(user.getUserid());
				System.out.println(user.getPassword());
				System.out.println(user.getUsername());
				// 로그아웃
				mav.addObject("msg", loginUser.getUsername() + " 회원님, 탈퇴! GOODBYE" + user.getUsername() + "getUsername()으로 불러움");
				session.invalidate();
				mav.addObject("url", "login.shop");
				mav.setViewName("alert");
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new LoginException("회원 탈퇴 중 오류가 발생했습니다 ==> 전산부 연락 요망", "delete.shop?id="+user.getUserid());
		}
		return mav;
	}
}
