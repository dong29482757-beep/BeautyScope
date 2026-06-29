<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 - 회원 관리</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container">
		<%@ include file="adminNav.jsp" %>

		<h2>회원 관리</h2>

		<form class="bs-filterbar" method="get">
			<input type="text" name="keyword" placeholder="아이디/닉네임 검색" value="${ keyword }">
			<button type="submit" class="bs-btn">검색</button>
		</form>

		<table class="bs-table">
			<tr><th>아이디</th><th>닉네임</th><th>이메일</th><th>가입수단</th><th>가입일</th><th>권한</th></tr>
			<c:forEach items="${ memberList }" var="m">
				<tr>
					<td><c:out value="${ m.memberId }" /></td>
					<td><c:out value="${ m.nickname }" /></td>
					<td><c:out value="${ m.email }" /></td>
					<td>${ m.joinType }</td>
					<td><fmt:formatDate value="${ m.regDate }" pattern="yyyy-MM-dd"/></td>
					<td>
						<form action="${ pageContext.request.contextPath }/admin/members/role" method="post" style="display:flex;gap:6px;">
							<input type="hidden" name="memberId" value="${ m.memberId }">
							<select name="role" onchange="this.form.submit()">
								<option value="USER" ${ m.role == 'USER' ? 'selected' : '' }>USER</option>
								<option value="ADMIN" ${ m.role == 'ADMIN' ? 'selected' : '' }>ADMIN</option>
							</select>
						</form>
					</td>
				</tr>
			</c:forEach>
			<c:if test="${ empty memberList }">
				<tr><td colspan="6" style="text-align:center;color:var(--bs-ink-soft);">회원이 없습니다.</td></tr>
			</c:if>
		</table>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
