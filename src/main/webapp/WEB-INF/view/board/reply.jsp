<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 답글 쓰기</title>
</head>
<body>
<form:form modelAttribute="board" action="reply.shop" method="post" name="f">
	<%-- 답변글 포인트!
		  원글에 대한 정보 4가지
		 - num / grp 는 그대로
		 - grplevel / grpstep 은 +1 증가시켜주기
	--%>
	<form:hidden path="num" />
	<form:hidden path="grp" />
	<form:hidden path="grplevel" />
	<form:hidden path="grpstep" />
	<table>
		<caption>Spring 게시판 답글 등록</caption>
		<tr><td>글쓴이</td>
			<td><input type="text" name="name">
				<font color="purple">
					<form:errors path="name" />
				</font>
			</td>
		</tr>
		<tr><td>비밀번호</td>
			<td><input type="password" name="pass">
				<font color="purple">
					<form:errors path="pass" />
				</font>
			</td>
		</tr>
		<tr><td>제목</td>
			<td><form:input path="title" />
				<font color="purple">
					<form:errors path="title" />
				</font>
			</td>
		</tr>
		<tr><td>내용</td>
			<td><textarea name="content" id="content" rows="15" cols="80">
				</textarea>
				<font color="purple">
					<form:errors path="content" />
				</font>
			</td>
		</tr>
		<script>
			// ckEditor가 정해준대로
			CKEDITOR.replace("content", {
				filebrowserImageUploadUrl : "imgupload.shop"
			});
		</script>
		<tr><td colspan="2">
				<a href="javascript:document.f.submit()">[답변글등록]</a>
				<a href="list.shop">[게시글 목록]</a>
			</td>
		</tr>
	</table>
</form:form>
</body>
</html>