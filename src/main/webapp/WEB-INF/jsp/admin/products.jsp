<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 - 상품 관리</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container">
		<%@ include file="adminNav.jsp" %>

		<h2>상품 관리</h2>

		<div style="margin-bottom:20px;">
			<a class="bs-btn" href="${ pageContext.request.contextPath }/admin/products/new">+ 상품 추가</a>
		</div>

		<p style="color:var(--bs-ink-soft);">총 ${ totalCount }개 (${ cri.page } / ${ totalPages } 페이지)</p>

		<table class="bs-table">
			<tr><th>플랫폼</th><th>상품명</th><th>브랜드</th><th>카테고리</th><th>평점</th><th>리뷰수</th><th></th></tr>
			<c:forEach items="${ productList }" var="p">
				<tr>
					<td>${ p.platform }</td>
					<td><c:out value="${ p.productName }" /></td>
					<td><c:out value="${ p.brandName }" /></td>
					<td><c:out value="${ p.category }" /></td>
					<td>${ p.avgRating }</td>
					<td>${ p.reviewCount }</td>
					<td style="white-space:nowrap;">
						<a href="${ pageContext.request.contextPath }/admin/products/${p.platform}/${p.productId}/edit">수정</a>
						&nbsp;
						<a href="${ pageContext.request.contextPath }/admin/products/${p.platform}/${p.productId}/reviews">리뷰</a>
						&nbsp;
						<form action="${ pageContext.request.contextPath }/admin/products/${p.platform}/${p.productId}/delete" method="post" style="display:inline;" onsubmit="return confirm('삭제하면 이 상품의 리뷰도 모두 삭제됩니다. 계속할까요?');">
							<button type="submit" style="border:none;background:none;color:#8B3A3A;cursor:pointer;font-weight:700;">삭제</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>

		<div class="bs-paging">
			<c:forEach begin="1" end="${ totalPages }" var="pg">
				<c:choose>
					<c:when test="${ pg == cri.page }"><span class="current">${ pg }</span></c:when>
					<c:otherwise><a href="${ pageContext.request.contextPath }/admin/products?page=${ pg }">${ pg }</a></c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
