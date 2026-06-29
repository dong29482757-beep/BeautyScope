<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오류 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container" style="text-align:center; padding:80px 0;">
		<div class="eyebrow">ERROR</div>
		<h2 style="margin:14px 0 10px;">요청을 처리하는 중 문제가 발생했어요</h2>
		<p style="color:var(--bs-ink-soft); max-width:480px; margin:0 auto 24px;">
			<c:out value="${ errorMessage }" default="잠시 후 다시 시도해주세요." />
		</p>
		<a class="bs-btn" href="${ pageContext.request.contextPath }/">홈으로 가기</a>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
