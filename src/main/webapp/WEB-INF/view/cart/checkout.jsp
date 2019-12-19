<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문 전 상품 목록 보기</title>
</head>
<body>
<h2>배송지 정보</h2>
<table>
	<tr><td width="30%">주문아이디</td>
		<td width="70%">${sessionScope.loginUser.userid}</td>
	</tr>
	<tr><td width="30%">이름</td>
		<td width="70%">${sessionScope.loginUser.username}</td>
	</tr>
	<tr><td width="30%">우편번호</td>
		<td width="70%">${sessionScope.loginUser.postcode}</td>
	</tr>
	<tr><td width="30%">주소</td>
		<td width="70%">${sessionScope.loginUser.address}</td>
	</tr>
	<tr><td width="30%">전화번호</td>
		<td width="70%">${sessionScope.loginUser.phoneno}</td>
	</tr>
</table>
<br>
<h2>구매 상품</h2>
<table>
	<tr><th>상품명</th>
		<th>가격</th>
		<th>수량</th>
		<th>합계</th>
	</tr>
	<c:forEach items="${sessionScope.CART.itemSetList}" var="itemSet" varStatus="stat">
		<tr><td>${itemSet.item.name}</td>
			<td>${itemSet.item.price}</td>
			<td>${itemSet.quantity}</td>
			<td>${itemSet.item.price * itemSet.quantity}</td>
		</tr>
	</c:forEach>
	<tr><td colspan="4" align="right">
			총 구입 금액 : ${sessionScope.CART.total}원
			<%--
				sessionScope.CART == Cart객체
				즉, Cart객체에 getTotal()가 필요한거야  <gotTotal() 호출>
			--%>
		</td>
	</tr>
	<tr><td colspan="4">
			<a href="end.shop">[주문확정]</a><%-- session의 내용을 지우고 주문상품을 db에 저장 --%>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="../item/list.shop">[상품목록]</a>
		</td>
	</tr>
</table>
</body>
</html>