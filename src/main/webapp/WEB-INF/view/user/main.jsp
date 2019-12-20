<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보</title>
</head>
<body>
<h2>
	환영합니다 ${sessionScope.loginUser.username}님
</h2>
<%--
	mypage  == AOP대상 (UserLoginAspect)
	1. 로그인 확인
	2. session등록 아이디와 로그인 아이디 확인
	3. 관리자인 경우 확인
--%>
<a href="mypage.shop?id=${loginUser.userid}">mypage</a>
<hr>
<a href="logout.shop">로그아웃</a>
</body>
</html>