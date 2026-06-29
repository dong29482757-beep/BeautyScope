<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BeautyScope — 화장품 리뷰 분석</title>
<%@ include file="common/header.jsp" %>
</head>
<body>
	<%@ include file="common/navbar.jsp" %>

	<section class="bs-hero">
		<div class="container bs-hero-grid">
			<div class="bs-hero-text">
				<div class="eyebrow">Cosmetics Review Analytics</div>
				<h1>나에게 맞는 화장품을<br>찾는 가장 빠른 방법</h1>
				<p>쿠팡 · 무신사 · 올리브영에서 모은 화장품 리뷰 538,774건을 분석해서<br>
				카테고리별 평점, 랭킹, 리뷰 검색까지 한 번에 보여드립니다.</p>
				<form class="bs-hero-search" action="${ pageContext.request.contextPath }/product" method="get">
					<input type="text" name="keyword" placeholder="제품명으로 검색 (예: 토너, 선크림...)">
					<button type="submit">검색</button>
				</form>
				<div class="bs-hero-cta">
					<a class="bs-btn" href="${ pageContext.request.contextPath }/product">제품 둘러보기</a>
					<a class="bs-btn bs-btn-outline" href="${ pageContext.request.contextPath }/ranking">카테고리 랭킹 보기</a>
				</div>
			</div>
			<div class="bs-hero-photo">
				<img src="${ pageContext.request.contextPath }/resources/img/home-hero.png" alt="BeautyScope">
			</div>
		</div>
	</section>

	<section class="bs-stats-band">
		<div class="container">
			<div>
				<div class="stat-num"><fmt:formatNumber value="${ stats.reviewCount }" pattern="#,###"/></div>
				<div class="stat-label">분석한 리뷰</div>
			</div>
			<div>
				<div class="stat-num"><fmt:formatNumber value="${ stats.productCount }" pattern="#,###"/></div>
				<div class="stat-label">등록된 제품</div>
			</div>
			<div>
				<div class="stat-num">${ stats.platformCount }</div>
				<div class="stat-label">연계 플랫폼</div>
			</div>
			<div>
				<div class="stat-num">★ ${ stats.avgRating }</div>
				<div class="stat-label">전체 평균 평점</div>
			</div>
		</div>
	</section>

	<section class="bs-section container">
		<h2>평점 TOP5</h2>
		<div class="bs-card-grid">
			<c:forEach items="${ top5 }" var="p">
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
					<div class="bs-meta">리뷰 ${ p.reviewCount }건</div>
				</a>
			</c:forEach>
		</div>
	</section>

	<section class="bs-section container">
		<h2>카테고리 바로가기</h2>
		<div class="bs-chips">
			<c:forEach items="${ categories }" var="cat">
				<a href="${ pageContext.request.contextPath }/product?category=${ cat }"><c:out value="${ cat }" /></a>
			</c:forEach>
		</div>
	</section>

	<%@ include file="common/footer.jsp" %>
</body>
</html>
