<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="bs-navbar">
	<div class="container">
		<a class="bs-logo" href="${ pageContext.request.contextPath }/">
			<img src="${ pageContext.request.contextPath }/resources/img/logo.png" alt="BeautyScope">
		</a>
		<div class="bs-nav-links">
			<a href="${ pageContext.request.contextPath }/">홈</a>
			<a href="${ pageContext.request.contextPath }/product">제품</a>
			<a href="${ pageContext.request.contextPath }/ranking">랭킹</a>
			<a href="${ pageContext.request.contextPath }/recommend">AI 추천</a>
			<c:choose>
				<c:when test="${ not empty sessionScope.loginMember }">
					<c:if test="${ sessionScope.loginMember.role == 'ADMIN' }">
						<a href="${ pageContext.request.contextPath }/admin/members">관리자</a>
					</c:if>
					<a href="${ pageContext.request.contextPath }/face/register">얼굴 등록</a>
					<a href="${ pageContext.request.contextPath }/logout"><c:out value="${ sessionScope.loginMember.nickname }" />님 로그아웃</a>
				</c:when>
				<c:otherwise>
					<a href="${ pageContext.request.contextPath }/login">로그인</a>
					<a href="${ pageContext.request.contextPath }/signup">회원가입</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</nav>
