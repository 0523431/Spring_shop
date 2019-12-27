package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chat")
public class ChatController {
	@RequestMapping("*")
	public String chat() {
		return null;
	}
}

/* 채팅 화면을 띄워주기(==view단) 위해서 필요한 컨트롤러  */
