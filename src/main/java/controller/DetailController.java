package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

public class DetailController {
	private ShopService shopService;
	
	public void setShopService(ShopService shopService) {
		this.shopService = shopService;
	}
	
	/*
 	detail.shop?id=1 요청시 실행되는 메서드가 detail(Integer id)
 	클래스 ModelAndView
 		- Model : view에 전달될 데이터를 저장하는 객체
 		- View : view 설정 객체
 		- (mvc2에서 ActionForward 역할)
 		
 	파라미터값은 매개변수로 받으면 됨
 	즉, 파라미터의 이름과 매개변수의 이름이 같아야 함
 		detail.shop?id=1 =====> Integer id
 		detail.shop?no=1 =====> Integer no
	*/
	@RequestMapping
	public ModelAndView detail(Integer id) {
		Item item = shopService.getItemById(id);
		ModelAndView mav = new ModelAndView();
		mav.addObject("item",item);
		return mav;
	}

}