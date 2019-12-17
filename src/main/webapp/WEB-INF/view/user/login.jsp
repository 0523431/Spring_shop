<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 화면</title>
</head>
<body>
<h2>사용자 로그인</h2>
<%--post 방식으로 login.shop을 간다 ! --%>
<form:form modelAttribute="user" method="post" action="login.shop">
	<%--유효성 검증에서 이름을 반드시입력하게 만들어서 그 유효성 검증에 걸려서 로그인이 안됨 --%>
	<input type="hidden" name="username" value="의미없는 이름"/>
	<spring:hasBindErrors name="user">
		<font color="green">
			<c:forEach var="error" items="${errors.globalErrors}">
				<spring:message code="${error.code}" />
			</c:forEach>
		</font>
	</spring:hasBindErrors>
	
	<table border="1" style="border-collapse: collapse; padding:10;">
		<tr height="40px">
			<td>아이디</td>
			<td><form:input path="userid" />
				<font color="red">
					<!-- LoginValidator에서
						 errors.rejectValue("userid", group);
						 path의 값이 같아야함 "userid"
					-->
					<form:errors path="userid" />
				</font></td>
		</tr>
		<tr height="40px">
			<td>비밀번호</td>
			<td><form:password path="password" />
				<font color="red">
					<form:errors path="password" />
				</font></td>
		</tr>
		<tr height="40px">
			<td colspan="2" align="center">
				<input type="submit" value="로그인">
				<input type="button" value="회원가입" onclick="location.href='userEntry.shop'">
			</td>
		</tr>
	</table>
</form:form>
</body>
</html>