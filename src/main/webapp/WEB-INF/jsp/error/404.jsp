<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>페이지를 찾을 수 없음 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container" style="text-align:center; padding:80px 0;">
		<div class="eyebrow">404</div>
		<h2 style="margin:14px 0 10px;">페이지를 찾을 수 없어요</h2>
		<p style="color:var(--bs-ink-soft); max-width:480px; margin:0 auto 24px;">
			주소가 잘못됐거나, 이미 삭제된 페이지일 수 있어요.
		</p>
		<a class="bs-btn" href="${ pageContext.request.contextPath }/">홈으로 가기</a>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
