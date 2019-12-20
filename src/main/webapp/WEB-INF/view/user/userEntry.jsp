<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사용자 등록</title>
</head>
<body>
<h2>사용자 등록</h2>
<!--
	post타입으로 userEntry.shop이 실행됨 (모든 파라미터를 가지고)
	modelAttribute="user" : 유효성 검증을 위해 필요한 객체 user
							UserController.java에서 
 -->
<form:form modelAttribute="user" method="post" action="userEntry.shop">
	<spring:hasBindErrors name="user">
		<font color="green">
			<c:forEach var="error" items="${errors.globalErrors}">
				<!--< globalErrors >
					if(errors.hasErrors()) {
						errors.reject("error.input.user");
					}
				-->
				<spring:message code="${error.code}" />
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	
	<table border="1" style="border-collapse: collapse;">
		<tr height="40px">
			<td>아이디</td>
			<td><form:input path="userid" />
				<font color="red">
					<form:errors path="userid" />
					<!-- error.required.userid=유저ID를 입력해 주세요  -->
				</font></td>
		</tr>
		<tr height="40px">
			<td>비밀번호</td>
			<td><form:input path="password" />
				<font color="purple">
					<form:errors path="password" />
				</font></td>
		</tr>
		<tr height="40px">
			<td>이름</td>
			<td><form:input path="username" />
				<font color="blue">
					<form:errors path="username" />
				</font></td>
		</tr>
		<tr height="40px">
			<td>전화번호</td>
			<td><form:input path="phoneno" />
				<font color="pink">
					<form:errors path="phoneno" />
				</font></td>
		</tr>
		<tr height="40px">
			<td>우편번호</td>
			<td><form:input path="postcode" />
				<font color="gray">
					<form:errors path="postcode" />
				</font></td>
		</tr>
		<tr height="40px">
			<td>주소</td>
			<td><form:input path="address" />
				<font color="orange">
					<form:errors path="address" />
				</font></td>
		</tr>
		<tr height="40px">
			<td>이메일</td>
			<td><form:input path="email" />
				<font color="skyblue">
					<form:errors path="email" />
				</font>
		</tr>
		<tr height="40px">
			<td>생년월일</td>
			<td><form:input path="birthday" />
				<font color="olive">
					<form:errors path="birthday" />
				</font></td>
		</tr>
		<tr height="40px">
			<td colspan="2" align="center">
				<input type="submit" value="등록">
				<input type="reset" value="초기화">
			</td>
		</tr>
	</table>
</form:form>
</body>
</html>