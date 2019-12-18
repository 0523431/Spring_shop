<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>

<!--
	jsp의 내장객체는 8+1인데,
	내장객체란? jsp페이지에서 선언과 객체 생성없이 바로 사용 가능한 이미 존재하는 객체
			 java영역(=Script)에서 사용되는 객체
	
	에러페이지에서만 exception객체가 필요해, 그래서 써준게 isErrorPage="true"
-->


<script>
	alert("${exception.message}");
	location.href="${exception.url}";
</script>
