<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>제품 목록 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container">
		<h2>제품 목록</h2>

		<form class="bs-filterbar" action="${ pageContext.request.contextPath }/product" method="get">
			<select name="category">
				<option value="">전체 카테고리</option>
				<c:forEach items="${ categories }" var="cat">
					<option value="${ cat }" ${ cat == cri.category ? 'selected' : '' }><c:out value="${ cat }" /></option>
				</c:forEach>
			</select>
			<input type="text" name="keyword" placeholder="제품명 검색" value="${ cri.keyword }">
			<select name="sort">
				<option value="rating_desc" ${ cri.sort == 'rating_desc' ? 'selected' : '' }>평점 높은순</option>
				<option value="rating_asc" ${ cri.sort == 'rating_asc' ? 'selected' : '' }>평점 낮은순</option>
				<option value="latest" ${ cri.sort == 'latest' ? 'selected' : '' }>최신순</option>
			</select>
			<button type="submit" class="bs-btn">검색</button>
		</form>

		<p style="color:var(--bs-ink-soft);">총 ${ totalCount }개 (${ cri.page } / ${ totalPages } 페이지)</p>

		<div class="bs-card-grid">
			<c:forEach items="${ productList }" var="p">
				<a class="bs-card" href="${ pageContext.request.contextPath }/product/${p.platform}/${p.productId}">
					<c:if test="${ not empty p.imagePath }">
						<img class="bs-thumb" src="${ pageContext.request.contextPath }/resources/product_images/${ p.imagePath }" alt="">
					</c:if>
					<c:if test="${ empty p.imagePath }">
						<div class="bs-thumb-empty">이미지 없음</div>
					</c:if>
					<span class="bs-platform-tag">${ p.platform }</span>
					<div class="bs-name"><c:out value="${ p.productName }" /></div>
					<div class="bs-rating">${ p.avgRating }</div>
					<div class="bs-meta"><c:out value="${ p.brandName }" /> · <c:out value="${ p.category }" /> · 리뷰 ${ p.reviewCount }건</div>
				</a>
			</c:forEach>
		</div>

		<div class="bs-paging">
			<c:forEach begin="1" end="${ totalPages }" var="pg">
				<c:choose>
					<c:when test="${ pg == cri.page }">
						<span class="current">${ pg }</span>
					</c:when>
					<c:otherwise>
						<a href="${ pageContext.request.contextPath }/product?category=${ cri.category }&keyword=${ cri.keyword }&sort=${ cri.sort }&page=${ pg }">${ pg }</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
