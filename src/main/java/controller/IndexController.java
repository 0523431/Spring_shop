package controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

// index.shop 요청시 호출되는 클래스
public class IndexController {
	private ShopService shopService;
	
	// p:shopService-ref="shopService"
	// shopService 객체 주입
	// =========== : itemDao 객체를 저장하고 있음
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	
	/*
	 	index.shop 요청시 실행되는 메서드가 itemList()
	 	클래스 ModelAndView
	 		- Model : view에 전달될 데이터를 저장하는 객체
	 		- View : view 설정 객체
	 		- (mvc2에서 ActionForward 역할)
	 */	
	@RequestMapping
	public ModelAndView itemList() {
		/*
			itemList : item테이블의 모든 컬럼, 모든 레코드 정보를 Item 객체의 List 객체로 저장
			return mav : mav는 DispatcherServlet이 받아
		*/
		List<Item> itemList = shopService.getItemList();
		ModelAndView mav = new ModelAndView("index"); // view 설정
		mav.addObject("itemList", itemList); // 데이터 설정
		return mav;
	}
}
