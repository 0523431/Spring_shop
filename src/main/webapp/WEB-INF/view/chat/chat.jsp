<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>채팅하기 websocket client</title>
<c:set var="port" value="${pageContext.request.localPort}" />
<c:set var="server" value="${pageContext.request.serverName}" />
<c:set var="path" value="${pageContext.request.contextPath}" />
<script>
	$(function() {
		// 스프링에서 websocket을 쓸 수 있게 설정이 필요함 --> spring-mvc.xml
		var ws = new WebSocket("ws://${server}:${port}${path}/chatting.shop");
		
		ws.onopen = function() {
			$("#chatStatus").text("info:connection opened")
			$("input[name=chatInput]").on("keydown", function(evt) {
				if(evt.keyCode ==13) {
					var msg = $("input[name=chatInput]").val();
					ws.send(msg);
					$("input[name=chatInput]").val("");
				}
			})
		}
		
		ws.onmessage = function(event) {
			$("textarea").eq(0).prepend(event.data+"\n");
		}
		
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