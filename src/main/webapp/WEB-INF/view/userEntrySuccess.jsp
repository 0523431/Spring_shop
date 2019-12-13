<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자 등록 결과 화면</title>
</head>
<body>
<h2>사용자 등록 결과 화면</h2>
<table border="1" style="border-collapse: collapse;">
	<tr><td>아이디</td>
		<td>${user.userid}</td>
	</tr>
	<tr><td>이름</td>
		<td>${user.username}</td>
	</tr>
	<tr><td>우편변호</td>
		<td>${user.postcode}</td>
	</tr>
	<tr><td>주소</td>
		<td>${user.address}</td>
	</tr>
	<tr><td>전화번호</td>
		<td>${user.phoneno}</td>
	</tr>
	<tr><td>이메일</td>
		<td>${user.email}</td>
	</tr>
	<tr><td>생년월일</td>
		<td><fmt:formatDate value="${user.birthday}" pattern="yyyy년 MM월 dd일" /></td>
	</tr>
</table>
</body>
</html>