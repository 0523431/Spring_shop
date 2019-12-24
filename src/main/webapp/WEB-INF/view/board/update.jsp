<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
<script>
	function file_delete() {
		document.f.fileurl.value = ""
		file_desc.style.display = "none";
	}
</script>
</head>
<body>
<%--enctype이 multipart이면 알서 post방식으로 인식함 --%>
<form:form modelAttribute="board" action="update.shop" enctype="multipart/form-data" name="f">
<form:hidden path="num" />
<table>
	<tr><td>글쓴이</td>
		<td><form:input path="name" />
			<font color="darkblue">
				<form:errors path="name" />
			</font>
		</td>
	</tr>
	<tr><td>비밀번호</td>
		<td><input type="password" name="pass">
			<font color="darkblue">
				<form:errors path="pass" />
			</font>
		</td>
	</tr>
	<tr><td>제목</td>
		<td><form:input path="title" />
			<font color="darkblue">
				<form:errors path="title" />
			</font>
		</td>
	</tr>
	<tr><td>내용</td>
		<td><form:textarea path="content" rows="15" cols="80" />
			<script>
				// ckEditor가 정해준대로
				CKEDITOR.replace("content", {
					filebrowserImageUploadUrl : "imgupload.shop"
				});
			</script>
			<font color="darkblue">
				<form:errors path="content" />
			</font>
		</td>
	</tr>
	<tr><td>첨부파일</td>
		<td>
			<c:if test="${!empty board.fileurl}">
				<div id="file_desc">
					<a href="file/${board.fileurl}">${board.fileurl}</a>
					<a href="javascript:file_delete()">[첨부파일 삭제]</a>
				</div>
			</c:if>
			<form:hidden path="fileurl" />
			<input type="file" name="file1">
		</td>
	</tr>
	<tr><td colspan="2">
			<a href="javascript:document.f.submit()">[게시글 수정]</a>
			<a href="list.shop">[게시글 목록]</a>
		</td>
	</tr>
</table>
</form:form>
</body>
</html>