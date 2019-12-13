<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="EUC-KR"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>��ǰ ��� ����</title>
</head>
<body>
<h2>��ǰ ���</h2>
<table>
	<tr><th width="10%">��ǰID</th>
		<th width="50%">��ǰ��</th>
		<th width="20%">����</th>
		<th width="20%">�̹���</th>
	</tr>
	<c:forEach var="item" items="${itemList}">
		<tr><td>${item.id}</td>
			<td><a href="detail.shop?id=${item.id}">${item.name}</a></td>
			<td>${item.price}</td>
			<td><img src="img/${item.pictureUrl}" width="50"></td>
		</tr>
	</c:forEach>
</table>
</body>
</html>