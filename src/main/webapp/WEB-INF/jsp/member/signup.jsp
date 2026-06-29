<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container" style="max-width:440px;">
		<h2>회원가입</h2>

		<c:if test="${ not empty error }">
			<p style="color:#8B3A3A;font-size:14px;"><c:out value="${ error }" /></p>
		</c:if>

		<form action="${ pageContext.request.contextPath }/signup" method="post" class="bs-auth-form">
			<label>아이디</label>
			<input type="text" name="memberId" value="${ member.memberId }" required>

			<label>비밀번호</label>
			<input type="password" name="password" required>

			<label>닉네임</label>
			<input type="text" name="nickname" value="${ member.nickname }" required>

			<label>이메일</label>
			<input type="email" name="email" value="${ member.email }">

			<button type="submit" class="bs-btn" style="width:100%;margin-top:20px;">가입하기</button>
		</form>

		<p style="margin-top:18px;font-size:13px;">
			이미 계정이 있으신가요? <a href="${ pageContext.request.contextPath }/login">로그인</a>
		</p>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
