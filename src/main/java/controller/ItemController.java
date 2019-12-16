package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

// @Component + Controller 기능
// 객체도 만들고 기능도 부여할 수 있어
// 스프링에서 만들어진 컨트롤러가 됨 ========> 액션클래스처럼 생각해줘라?
@Component
@RequestMapping("item") // item/xxxxxxxx.shop 이런식으로 이름으로 컨트롤러를 지정할 수 있어
public class ItemController {
	
	// 주입
	@Autowired
	private ShopService service;
	
	// item/list.shop이 실행되면(요청이 들어오면) 이 list() 메서드를 호출해
	@RequestMapping("list")
	public ModelAndView list() {
		
		// itemList : item테이블의 모든 레코드와 모든 컬럼 정보를 저장
		List<Item> itemList = service.getItemList();
		ModelAndView mav = new ModelAndView(); // 뷰 : 지정하지 않았기때문에 요청정보와 같은 item/list가 됨
		mav.addObject("itemList", itemList);
		return mav; // WEB-INF/view/item/list.shop
	}
	
	// item/create.shop(<이 요청이 들어오면) create()메서드 호출
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView("item/add"); // 뷰 WEB-INF/view/item/add.shop
		mav.addObject(new Item()); // new Item()이 값이 add.shop으로 넘어가지 (빈값 그래서 price default가 0)
		return mav;
	}
	
	/*
		item : Item에서
		BindingResult : 유효성 검증 ===============> @Valid 필수
		HttpServletRequest request : 파일 업로드
	 */
	@RequestMapping("register")
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("item/add"); // 뷰
		
		// 유효성 검증 @Valid에 에러가 있으면
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav; // 에러내용을 가지고 add.jsp로 가는거야
		}
		
		// error가 없으면 이제 여기로 오는거야
		// service를 통해서 저장해서 ShopService는 컨트롤러와 db를 중간에서 연결하는 클래스로 가는거야
		service.itemCreate(item, request);
		
		// redirect: ==> 등록을 하게 되면, list.shop 요청을 재 호출하게 명령을 내림
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	
}
