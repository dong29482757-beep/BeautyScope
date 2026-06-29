<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container" style="max-width:440px;">
		<h2>로그인</h2>

		<c:if test="${ not empty error }">
			<p style="color:#8B3A3A;font-size:14px;"><c:out value="${ error }" /></p>
		</c:if>

		<form action="${ pageContext.request.contextPath }/login" method="post" class="bs-auth-form">
			<label>아이디</label>
			<input type="text" name="memberId" required>

			<label>비밀번호</label>
			<input type="password" name="password" required>

			<button type="submit" class="bs-btn" style="width:100%;margin-top:20px;">로그인</button>
		</form>

		<div style="margin-top:18px;">
			<a class="bs-social-btn kakao" href="${ pageContext.request.contextPath }/login/kakao">카카오로 로그인</a>
			<a class="bs-social-btn naver" href="${ pageContext.request.contextPath }/login/naver">네이버로 로그인</a>
			<a class="bs-social-btn" style="background:var(--bs-ink);color:#fff;border-color:var(--bs-ink);" href="${ pageContext.request.contextPath }/login/face">얼굴로 로그인</a>
		</div>

		<p style="margin-top:18px;font-size:13px;">
			아직 계정이 없으신가요? <a href="${ pageContext.request.contextPath }/signup">회원가입</a>
		</p>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
