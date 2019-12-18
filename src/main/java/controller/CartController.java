package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.ShopService;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private ShopService service;
	
	/* 매개변수는
			id, 수량, 장바구니를 세션에 등록하니까 session객체  */
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart"); // view지정 (왜? cartAdd.jsp로 가는게 아니니까)
		
		// 선택된 상품 객체
		Item item = service.itemInfo(id);
		
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart ==null) { // 장바구니가 비었을 때,
			cart = new Cart(); // session에 등록된 cart
			session.setAttribute("CART", cart);
		}
		
		// itemSetList에 추가 됨
		cart.push(new ItemSet(item, quantity));

		mav.addObject("message", item.getName() + ":" + quantity + "개 장바구니 추가");
		mav.addObject("cart", cart);
		
		return mav;
	}
}
