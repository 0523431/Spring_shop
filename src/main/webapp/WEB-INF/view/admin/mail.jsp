<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메일보내기</title>
<script>
	function idinputck(f) {
		if(f.naverid.value == "") {
			alert("naver 아이디를 입력하세요");
			f.naverid.focus();
			return false;
		}
		if(f.naverfw.value == "") {
			alert("naver 비밀번호를 입력하세요");
			f.naverpw.focus();
			return false;
		}
		return true;
	}
	
	function goback() {
		location.href='list.shop';
		return false;
	}
</script>
</head>
<body>
<h2>메일보내기</h2>
<form name="mailform" method="post"
	  action="mail.shop" enctype="multipart/form-data"
	  onsubmit="return idinputchk(this)">
	<table>
		<tr><th width="20%">본인 네이버 아이디</th>
			<td><input type="text" name="naverid"></td>
		</tr>
		<tr><th width="20%">본인 네이버 비밀번호</th>
			<td><input type="password" name="naverpw"></td>
		</tr>
	</table>
	<hr>
	<table>
		<tr><th width="20%">보내는 사람</th>
			<td>${loginUser.email}</td>
		</tr>
		<tr><th>받는 사람</th>
			<td><input type="text" name="recipient" size="100"
				 value='<c:forEach var="user" items="${list}">${user.username}&lt;${user.email}&gt;,</c:forEach>'>
			</td>
		</tr>
		<tr><th>제목</th>
			<td><input type="text" name="title" size="100">
		</tr>
		<tr><th>메시지 형식</th>
			<td style="text-align:left"><select name="mtype">
					<option value="text/html; charset=UTF-8">HTML</option>
					<option value="text/plain; charset=UTF-8">TEXT</option>
				</select>
			</td>
		</tr>
		<tr><th>첨부파일1</th>
			<td><input type="file" name="file1"></td>
		</tr>
		<tr><th>첨부파일2</th>
			<td><input type="file" name="file1"></td>
		</tr>
		<tr><td colspan="2">
			<textarea name="contents" cols="120" rows="10"></textarea>
			<script>
				CKEDITOR.replace("contents")
				// contents : 사진업로드는 안됨
			</script>
			</td>
		</tr>
		<tr><td colspan="2">
			<input type="submit" value="메일보내기">
			<button onclick="location.href='list.shop'">회원목록</button>
			<button onclick="return goback()">회원목록</button>
			<input type="button" value="회원목록button" onclick="location.href='list.shop'">
			</td>
		</tr>
	</table>
</form>
</body>
</html>