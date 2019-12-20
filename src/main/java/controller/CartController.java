package controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart") // cart폴더의 컨트롤러가 되는거야
public class CartController {
	@Autowired
	private ShopService service;
	
	/* 매개변수는
			id, 수량, 장바구니를 세션에 등록하니까 session객체  */
	@RequestMapping("cartAdd")
	public ModelAndView add(String id, Integer quantity, HttpSession session) {
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
	
	/*  index 파라미터값에 해당하는 값을 Cart의 itemSetList 객체에서 제거
	 	message에 000상품을 장바구니에서 제거했습니다 메시지를 cart.jsp에 전달하기
		
		1. session에서 CART 객체 조회하기
		2. cart 객체에서 itemSetList객체에서 index에 해당하는 값을 제거
			ItemSet itemSet = cart.getItemSetList().remove(index);
		3. message를 cart.jsp view에 전달하기
	*/
	@RequestMapping("cartDelete")
	public ModelAndView cartDelete(int index, HttpSession session) { // Integer는 객체라서 대상을 찾아가니까 안됨, int는 정수값을 찾아주지
		ModelAndView mav = new ModelAndView("cart/cart"); // view : cart.jsp
		
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet itemSet = null;
		try {
			itemSet = cart.getItemSetList().remove(index);
			mav.addObject("message", itemSet.getItem().getName()+"상품을 장바구니에서 삭제했습니다");
		} catch(Exception e) {
			// 이미 상품을 삭제한 상태
			mav.addObject("message", "장바구니 상품이 삭제되지 않았습니다");
		}
		
		mav.addObject("cart", cart); // 남은 session이 cart.jsp로 보내짐
		return mav;
	}
	
	/*  장바구니에 상품이 없는 경우,
	 	CartEmptyException을 발생 ==> "장바구니에 상품이 없습니다" 메시지 alert창으로 출력
	 	/item/list.shop페이지로 이동
	*/
	@RequestMapping("cartView")
	public ModelAndView cartView(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		
		// session에서 CART객체 조회
		Cart cart = (Cart)session.getAttribute("CART");
		// cart가 없거나, 아이템리스트가 0일때
		if(cart ==null || cart.getItemSetList().size() ==0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다", "../item/list.shop");
		}
		
		mav.addObject("cart", cart);
		return mav;
	}
	
	/*  주문하기 => 주문전 확인
		<조건 (예외)>
		1. 로그인이 반드시 필요함
		2. 장바구니에 상품이 있어야함
		------------------------ 그래서 우리는 AOP aspect클래스를 만들어야해
	*/
//	@RequestMapping("checkout")
//	public String checkout(HttpSession session) {
//		return "cart/checkout";
//	}
	@RequestMapping("checkout")
	public ModelAndView checkout(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/checkout");
		
		return mav;
	}
	
	/*  주문확정
		1. 세션에서 CART, loginUser 정보 조회
		2. sale, saleitem 테이블 데이터 추가
		3. 장바구니에서 상품 제거
		4. cart/end.jsp 페이지로 이동
		
		이름에 check가 붙는 이유 : AOP의 대상이 되는 클래스로 만들기위함 => 카드에 상품이 있고, 로그인이 되어있어야함
	*/
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		// CART 정보 가져오기
		Cart cart = (Cart)session.getAttribute("CART");
		// 로그인 정보 가져오기
		User loginUser = (User)session.getAttribute("loginUser");
		
		// 2번, sale객체만 가져와서 view단에 전달하면 웬만한 정보는 다 불러올 수 있음
		// 즉, sale에는 주문 정보 내역이 다 들어있게 됨
		Sale sale = service.checkend(loginUser, cart);
		// 그리고 cart로 부터 getTotal()을 호출하면 주문상품의 총 금액이 리턴 됨
		long total = cart.getTotal();
		
		// 다 사고 나면, 장바구니CART의 내용을 제거
		session.removeAttribute("CART");
		mav.addObject("sale", sale);
		mav.addObject("total", total);
		return mav; // end.jsp로 보내줌
	}
}
