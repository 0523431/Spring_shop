<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%-- 업로드된 위치를 ck editor에게 알려주는 기능을 하는 jsp --%>
<script>
	window.parent.CKEDITOR.tools.callFunction(${CKEditorFuncNum},'${fileName}','이미지 업로드 완료')
</script>
