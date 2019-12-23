<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 탈퇴하기</title>
</head>
<body>
<h2>정말 탈퇴를 원하십니까?</h2>
<table>
	<tr><td>아이디</td>
		<td>${user.userid}</td>
	</tr>
	<tr><td>이름</td>
		<td>${user.username}</td>
	</tr>
	<tr><td>생년월일</td>
		<td><fmt:formatDate value="${user.birthday}" pattern="yyyy년 MM월 dd일" /></td>
	</tr>
</table>

<form action="delete.shop" method="post" name="deleteform">
	<input type="hidden" name="userid" value="${param.id}">
	<input type="hidden" name="username" value="${user.username}">
	비밀번호 <input type="password" name="password">
	<a href="javascript:deleteform.submit()">[회원탈퇴]</a>
</form>

</body>
</html>