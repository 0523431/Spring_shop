<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 상세 보기</title>
</head>
<body>
<h2>상품 상세 보기</h2>
<hr>
<table>
	<tr><td><img src="img/${item.pictureUrl}"></td>
		<td>
			<table>
				<tr><td>상품명</td>
					<td>${item.name}</td>
				</tr>
				<tr><td>가격</td>
					<td>${item.price}</td>
				</tr>
				<tr><td>상품설명</td>
					<td>${item.description}</td>
				</tr>
				<tr><td colspan="2">
					<form action="../cart/cartAdd.shop">
						<input type="hidden" name="id" value="${item.id}">
						<table>
							<tr><td><select name="quantity">
										<c:forEach var="i" begin="1" end="10">
											<option>${i}</option>
										</c:forEach>
									</select>
								</td>
								<td><input type="submit" value="장바구니">
									<input type="button" value="상품목록" onclick="location.href='list.shop'">
								</td>
							</tr>
						</table>
					</form>
			</table>
		</td>
	</tr>
</table>
</body>
</html>