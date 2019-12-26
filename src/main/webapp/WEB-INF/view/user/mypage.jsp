<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Page</title>
<script>
	$(document).ready(function() {
		
		$("#minfo").show(); // 회원정보 : 초기값으로 보이게 설정
		
		$("#oinfo").hide(); // 주문내역
		
		$(".saleLine").each(function() {
			$(this).hide();
		})
		$("#tab1").addClass("select");
	})
	
	function disp_div(id, tab) {
		$(".info").each(function() {
			$(this).hide();
		})
		
		$(".tab").each(function() {
			$(this).removeClass("select");
		})
		
		// id ==> minfo / oinfo
		$("#"+id).show();
		
		$("#"+tab).addClass("select");
	}
	
	// list_display('saleLine${stat.index}') 주문내역 보였다 안보였다
	function list_display(id) {
		$("#"+id).toggle();
	}
</script>
<style>
	.select {
		padding : 3px;
		background-color : #0000ff;
	}
	.select>a {
		color : #ffffff;
		text-decoration : none;
		font-weight : bold;
	}
</style>
</head>
<body>
<table>
	<tr><td id="tab1" class="tab">
			<a href="javascript:disp_div('minfo', 'tab1')">회원정보보기</a>
		</td>
		<c:if test="${param.id !='headmaster'}">
		<td id="tab2" class="tab">
			<a href="javascript:disp_div('oinfo', 'tab2')">주문정보보기</a>
		</td>
		</c:if>
	</tr>
</table>
<%-- oinfo : 주문 정보 출력
	 - 컨트롤러에서 넘어와야 하는 정보들
	 - 주문목록 : items="${salelist}"
	 - 주문목록의 아이템 : items="${sale.itemList}"
--%>
<div id="oinfo" class="info" style="display : none; width:100%;">
	<table>
		<tr><th>주문번호</th>
			<th>주문일자</th>
			<th>총주문금액</th>
		</tr>
		<c:forEach items="${salelist}" var="sale" varStatus="stat">
		<tr><td align="center">
				<a href="javascript:list_display('saleLine${stat.index}')">${sale.saleid}</a>
			</td>
			<td align="center">
				<fmt:formatDate value="${sale.updatetime}" pattern="yyyy-MM-dd" />
			</td>
			<td align="center">
				${sale.total}원
				<%--
					산 물건들의 총 합계 == Sale 빈클래스에서 설정 getTotal()
				--%>
			</td>
		</tr>
		<tr id="saleLine${stat.index}" class="saleLine">
			<td colspan="3" align="center">
				<table>
					<tr><th width="25%">상품명</th>
						<th width="25%">상품가격</th>
						<th width="25%">구매수량</th>
						<th width="25%">상품총액</th>
					</tr>
					<c:forEach items="${sale.itemList}" var="saleItem">
					<tr><td class="title">${saleItem.item.name}</td>
						<td>${saleItem.item.price}원</td>
						<td>${saleItem.quantity}개</td>
						<td>${saleItem.item.price * saleItem.quantity}원</td>
					</tr>
					</c:forEach>
				</table>
			</td>
		</tr>
		</c:forEach>
	</table>
</div>
<%-- 회원정보출력 --%>
<div id="minfo" class="info">
	<table>
		<tr><th>아이디</th>
			<td>${user.userid}</td>
		</tr>
		<tr><th>이름</th>
			<td>${user.username}</td>
		</tr>
		<tr><th>우편번호</th>
			<td>${user.postcode}</td>
		</tr>
		<tr><th>전화번호</th>
			<td>${user.phoneno}</td>
		</tr>
		<tr><th>주소</th>
			<td>${user.address}</td>
		</tr>
		<tr><th>이메일</th>
			<td>${user.email}</td>
		</tr>
		<tr><th>생년월일</th>
			<td><fmt:formatDate value="${user.birthday}" pattern="yyyy년 MM월 dd일" /></td>
		</tr>
	</table>
<hr>
	<a href="update.shop?id=${user.userid}">[회원정보수정]</a>
	&nbsp;&nbsp;&nbsp;
	<c:if test="${loginUser.userid !='headmaster'}">
		<a href="delete.shop?id=${user.userid}">[회원탈퇴]</a>
		&nbsp;&nbsp;&nbsp;
	</c:if>
	<c:if test="${loginUser.userid =='headmaster'}">
		<a href="../admin/list.shop">[회원목록]</a>
		<%-- admin controller가 필요하고 view단은 list.shop이 됨 --%>
		&nbsp;&nbsp;&nbsp;
	</c:if>
</div>
<br>
</body>
</html>