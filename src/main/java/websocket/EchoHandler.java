package websocket;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// 객체화
@Component
public class EchoHandler extends TextWebSocketHandler {
	
	/*  Set : 중복된 내용은 거름
		clients : 접속한 클라이언트 목록
	*/
	private Set<WebSocketSession> clients = new HashSet<WebSocketSession>();
	
	/* 	처음에 오픈 될 경우
		session은 브라우저로부터 받음
	*/
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		System.out.println("클라언트 접속" + session.getId());
		clients.add(session); // clients라는 컬렉션에 session 저장
	}
	
	// client에서 메시지 수신
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// loadMessage : 클라이언트가 전송한 메세지
		String loadMessage = (String)message.getPayload();
		System.out.println("클라이언트 메세지 : " + loadMessage);
		// 혹시 없어질지-연결이 끊긴- 모를 클라이언트를 추가 저장 (Set이니까 중복은 거름)
		clients.add(session);
		
		// 접속된 클라이언트들에게 수신된 메세지를 전송
		for(WebSocketSession s : clients) {
			s.sendMessage(new TextMessage(loadMessage));
		}
	}
	
	// 혹시 에러가 발생하면,
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		super.handleTransportError(session, exception);
		System.out.println("오류발생 : " + exception.getMessage());
	}
	
	// 클라이언트 종료 (연결 종료)
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		System.out.println("클라이언트 접속 해제 : " + status.getReason()); // null로 나오게 됨
		clients.remove(session);
	}
}
