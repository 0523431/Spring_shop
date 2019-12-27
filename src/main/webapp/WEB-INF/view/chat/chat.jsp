<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅하기 websocket client</title>
<!-- url로부터 채팅 접속자를 받기 위해 설정?  -->
<c:set var="port" value="${pageContext.request.localPort}" />
<c:set var="server" value="${pageContext.request.serverName}" />
<c:set var="path" value="${pageContext.request.contextPath}" />

<script>
	// 문서 준비가 완료되면,
	$(function() {
		// 스프링에서 websocket을 쓸 수 있게 설정이 필요함 --> spring-mvc.xml
		var ws = new WebSocket("ws://${server}:${port}${path}/chatting.shop");
		
		// 서버와 연결 될 때 호출되는 이벤트
		ws.onopen = function() {
			$("#chatStatus").text("info:connection opened")
			
			// chatInput태그에 keydown 이벤트 등록
			$("input[name=chatInput]").on("keydown", function(evt) { // 키가 눌렀을 때,
				// 키 이벤트 : evt
				if(evt.keyCode ==13) { // enter키의 코드값 : 13
					var msg = $("input[name=chatInput]").val();
			
					// ws.send(msg) : 서버로 입력된 메시지 전송
					ws.send(msg);
					
					// 전송이 되면 입력된 내용을 "" == 지워줌
					$("input[name=chatInput]").val("");
				}
			})
		}
		
		// ws : 서버
		// 서버에서 메시지를 수신한 경우 --> echoHandler : s.sendMessage(new TextMessage(loadMessage));
		ws.onmessage = function(event) {
			// 서버로부터 들어온 event == message
			// prepend() : 앞쪽에 메시지를 출력해
			// $("textarea").eq(0).prepend(event.data+"\n");
			$("textarea").eq(0).append("\n"+event.data);
		}
		
		// 서버연결이 해제 된 경우
		ws.onclose = function(event) {
			$("#chatStatus").text("info:connection close");
		}
	})
</script>
</head>
<body>
<p>
	<div id="chatStatus"></div>
	<textarea name="chatMsg" rows="15" cols="40"></textarea>
	<br>
	메시지 입력 : <input type="text" name="chatInput">
</body>
</html>