<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
<script>
	// 검색 후, 그 결과를 페이지에 유지시켜주는 메서드함수
	function listdo(page) { // listdo 메서드에 page값을 넣고
		document.serchform.pageNum.value = page; // pageNum의 value값을 바꿔주는 거야
		document.serchform.submit(); // form을 submit해주는 거야
	}
</script>
</head>
<body>
<table>
	<tr><td colspan="5">
		<div style="display : inline;">
			<form action="list.shop" method="post" name="serchform">
				<input type="hidden" name="pageNum" value="1">
				<select name="searchtype" style="width:100px;">
					<option value="">선택하세요</option>
					<option value="title">제목</option>
					<option value="name">작성자</option>
					<option value="content">내용</option>
				</select>
				<%-- 검색어 유지 --%>
				<script type="text/javascript">
					document.serchform.searchtype.value = "${param.searchtype}";
				</script>
				<input type="text" name="searchcontent" value="${param.searchcontent}" style="width:250px;">
				<input type="submit" value="검색">
			</form>
		</div>
		</td>
	</tr>
	<%-- 등록된 게시물이 있는 경우 --%>
	<c:if test="${listcount >0}">
		<tr><td colspan="4">Spring 게시판 ㅠㅠㅠ</td>
			<td>글개수 : ${listcount}</td>
		</tr>
		<tr><th>NO</th>
			<th>TITLE</th>
			<th>WRITER</th>
			<th>DATE</th>
			<th>CNT</th>
		</tr>
		<c:forEach var="board" items="${boardlist}">
			<tr><td>${boardno}</td>
				
				<c:set var="boardno" value="${boardno -1}" />
				<%-- 제목처리 : 첨부 / 원글 / 답글 구분 --%>
				<td style="text-align : left;">
					<c:if test="${!empty board.fileurl}">
						<a href="file/${board.fileurl}">[첨부]</a>
					</c:if>
					<c:if test="${empty board.fileurl}">
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:if>
					<%-- 답글 : 들여쓰기 (grplevel ==0이면 원글) --%>
					<c:forEach begin="1" end="${board.grplevel}">
						&nbsp;&nbsp;
					</c:forEach>
					<c:if test="${board.grplevel >0}">┖답글 :</c:if>
					<a href="detail.shop?num=${board.num}">${board.title}</a>
				</td>
				<td>${board.name}</td>
				<td>
					<fmt:formatDate value="${board.regdate}" pattern="yy년 MM월 dd일 HH:mm" /> 
				</td>
				<td>${board.readcnt}</td>
			</tr>
		</c:forEach>
		<%-- 페이징 작업 --%>
		<tr><td colspan="5">
			<c:if test="${pageNum >1}">
				<%-- <a href="list.shop?pageNum=${pageNum -1}">[이 전]</a> --%>
				<a href="javascript:listdo(${pageNum -1})">[이 전]</a>
			</c:if>
			<c:if test="${pageNum <=1}">
				<%-- 이전으로 넘어갈 페이지가 없으니까 하이퍼링크 xxx --%>
				[이 전]
			</c:if>
			<%-- 페이지 넘버링 --%>
			<c:forEach var="a" begin="${startpage}" end="${endpage}">
				<c:if test="${a ==pageNum}">
					[${a}]
				</c:if>
				<c:if test="${a !=pageNum}">
					<%-- <a href="list.shop?pageNum=${a}">[${a}]</a> --%>
					<a href="javascript:listdo(${a})">[${a}]</a>
				</c:if>
			</c:forEach>
			<c:if test="${pageNum <maxpage}">
				<%-- <a href="list.shop?pageNum=${pageNum +1}">[다 음]</a> --%>
				<a href="javascript:listdo(${pageNum +1})">[다 음]</a>
			</c:if>
			<c:if test="${pageNum >=maxpage}">
				<%-- 다음으로 넘어갈 페이지가 없으니까 하이퍼링크 xxx --%>
				[다 음]
			</c:if>
			</td>
		</tr>
	</c:if>
	<%-- 등록된 게시물이 없는 경우 --%>
	<c:if test="${listcount ==0}">
		<tr><td colspan="5">
			등록된 게시물이 없습니다
			</td>
		</tr>
	</c:if>
	
	<tr><td colspan="5" style="text-align:right">
			<a href="write.shop">[글쓰기]</a>
		</td>
	</tr>
</table>

</body>
</html>