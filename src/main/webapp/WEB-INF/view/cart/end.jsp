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
<h2>${loginUser.username}님이 주문하신  정보</h2>
<h2>${sale.user.username}님이 주문하신  정보</h2>
	sale.user.username을 통해서 이름을 가져오게 된건데,
	그게 가능한 이유는 Sale에 [[private User user;]]가 있어서 가능
<h2>배송지 확인</h2>
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
<br><h2>주문 완료 상품</h2>
<table>
	<tr><th>상품명</th>
		<th>가격</th>
		<th>수량</th>
		<th>합계</th>
	</tr>
	<%-- (sale테이블에서) Sale객체에서 정보를 가져오는거야
		 private List<SaleItem> itemList = new ArrayList<SaleItem>();
	--%>
	<c:forEach items="${sale.itemList}" var="saleitem" >
		<tr><td>${saleitem.item.name}</td>
			<td>${saleitem.item.price}</td>
			<td>${saleitem.quantity}</td>
			<td>${saleitem.item.price * saleitem.quantity}</td>
		</tr>
	</c:forEach>
	<tr><td colspan="4" align="right">
			총 구입 금액 : <fmt:formatNumber value="${total}" pattern="###,###" />원
			<%--
				Cart가 가지고 있는 값 : <gotTotal() 호출>
			--%>
		</td>
	</tr>
	<tr><td colspan="4">
			<a href="../item/list.shop">[상품목록]</a>
		</td>
	</tr>
</table>
</body>
</html>