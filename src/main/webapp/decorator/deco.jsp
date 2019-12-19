<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><decorator:title /></title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.ckeditor.com/4.5.7/full/ckeditor.js"></script>
<decorator:head />
<link rel="stylesheet" href="${path}/css/main.css">
</head>
<body>
<table>
	<tr><td colspan="3" style="text-align:right;">
		<c:if test="${empty sessionScope.loginUser}">
			<a href="${path}/user/login.shop">로그인</a>
			<a href="${path}/user/userEntry.shop">회원가입</a>
		</c:if>
		<c:if test="${!empty sessionScope.loginUser}">
			${sessionScope.loginUser.username}님 반갑습니당
			<a href="${path}/user/logout.shop">로그아웃</a>
		</c:if>
		</td>
	</tr>
	<tr><td width="15%" valign="top" style="text-align:right;">
			<a href="${path}/user/main.shop?id=${sessionScope.loginUser.userid}">회원관리</a>
			<hr>
			<a href="${path}/item/list.shop">상품관리</a>
			<hr>
			<a href="${path}/board/list.shop">게시판</a>
			<hr>
			<a href="${path}/chat/chat.shop">채팅</a>
		</td>
		<td colspan="2" style="text-align:left; vertical-align:top">
			<decorator:body />
		</td>
	</tr>
	<tr><td colspan="3">구디아카데미 Since 2016</td></tr>
</table>
</body>
</html>