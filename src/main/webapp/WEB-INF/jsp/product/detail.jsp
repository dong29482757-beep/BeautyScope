<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><c:out value="${ product.productName }" /> - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container">
		<a href="${ pageContext.request.contextPath }/product">&larr; 목록으로</a>

		<div class="bs-detail-head">
			<c:if test="${ not empty product.imagePath }">
				<img class="bs-detail-thumb" src="${ pageContext.request.contextPath }/resources/product_images/${ product.imagePath }" alt="">
			</c:if>
			<c:if test="${ empty product.imagePath }">
				<div class="bs-detail-thumb-empty">이미지 없음</div>
			</c:if>
			<div class="bs-detail-info">
				<span class="bs-platform-tag">${ product.platform }</span>
				<h1><c:out value="${ product.productName }" /></h1>
				<div style="color:var(--bs-ink-soft);">
					<c:out value="${ product.brandName }" /> · <c:out value="${ product.category }" />
				</div>
				<div class="bs-stat-row">
					<div><div class="num">${ product.avgRating }</div><div class="label">평균 평점</div></div>
					<div><div class="num">${ product.reviewCount }</div><div class="label">리뷰 수</div></div>
				</div>
			</div>
			<div class="bs-chart-box">
				<canvas id="ratingChart"></canvas>
			</div>
		</div>

		<h2>장점 · 단점</h2>
		<c:if test="${ empty aspects }">
			<div class="bs-aspect-list" style="color:var(--bs-ink-soft);font-size:13px;">이 제품은 속성별 분석 데이터가 없습니다 (리뷰 수가 적은 제품일 수 있어요).</div>
		</c:if>
		<c:if test="${ not empty aspects }">
			<div class="bs-proscons">
				<div class="bs-pros-col">
					<h3>장점</h3>
					<c:forEach items="${ prosAspects }" var="a">
						<div class="bs-proscons-item pro">
							<span class="name"><c:out value="${ a.aspect }" /></span>
							<span class="pct">긍정 리뷰 ${ a.positiveCnt }건</span>
						</div>
					</c:forEach>
					<c:if test="${ empty prosAspects }">
						<div class="bs-proscons-item" style="color:var(--bs-ink-soft);">긍정 리뷰에서 언급된 속성이 없어요.</div>
					</c:if>
				</div>
				<div class="bs-cons-col">
					<h3>단점</h3>
					<c:forEach items="${ consAspects }" var="a">
						<div class="bs-proscons-item con">
							<span class="name"><c:out value="${ a.aspect }" /></span>
							<span class="pct">부정 리뷰 ${ a.negativeCnt }건</span>
						</div>
					</c:forEach>
					<c:if test="${ empty consAspects }">
						<div class="bs-proscons-item" style="color:var(--bs-ink-soft);">부정 리뷰에서 언급된 속성이 없어요.</div>
					</c:if>
				</div>
			</div>
		</c:if>

		<h2>리뷰</h2>
		<div class="bs-search-box">
			<input type="text" id="keyword" placeholder="리뷰 내용 검색">
			<button class="bs-btn" onclick="searchReviews()">검색</button>
		</div>

		<div id="reviewList"></div>
		<div class="bs-paging" id="paging"></div>
	</section>

	<%@ include file="../common/footer.jsp" %>

	<script>
		const platform = "${ product.platform }";
		const productId = "${ product.productId }";
		let currentPage = 1;

		function escapeHtml(s) {
			const div = document.createElement('div');
			div.textContent = s == null ? '' : s;
			return div.innerHTML;
		}

		// V1-302: 별점 분포 차트 (서버에서 미리 집계해 내려준 ratingDist 모델 사용)
		const ratingLabels = [<c:forEach items="${ratingDist}" var="r" varStatus="st">${r.rating}<c:if test="${!st.last}">,</c:if></c:forEach>];
		const ratingCounts = [<c:forEach items="${ratingDist}" var="r" varStatus="st">${r.cnt}<c:if test="${!st.last}">,</c:if></c:forEach>];
		new Chart(document.getElementById('ratingChart'), {
			type: 'bar',
			data: {
				labels: ratingLabels.map(r => r + '점'),
				datasets: [{ label: '리뷰 수', data: ratingCounts, backgroundColor: '#9C7A3C', borderRadius: 0 }]
			},
			options: { plugins: { legend: { display: false } } }
		});

		// V1-303: 리뷰 목록 비동기(fetch) 로딩 / V1-402: 리뷰 내용 검색
		const apiBase = "${ pageContext.request.contextPath }/api/reviews";

		function loadReviews(page) {
			currentPage = page;
			const keyword = document.getElementById('keyword').value;
			const params = new URLSearchParams({ platform, productId, page, keyword });
			fetch(apiBase + '?' + params.toString()).then(res => res.json()).then(data => {
				const listEl = document.getElementById('reviewList');
				listEl.innerHTML = data.reviews.map(function(r) {
					return '<div class="bs-review">'
						+ '<div class="meta">' + escapeHtml(r.nickname || '익명') + ' · ' + escapeHtml(r.reviewDate || '') + ' · ★' + r.rating
						+ ' <span class="sentiment-' + escapeHtml(r.sentiment) + '">[' + escapeHtml(r.sentiment) + ']</span></div>'
						+ '<div>' + escapeHtml(r.reviewContent) + '</div></div>';
				}).join('');

				const pagingEl = document.getElementById('paging');
				let html = '';
				for (let p = 1; p <= data.totalPages; p++) {
					html += '<button class="bs-btn' + (p === data.page ? '' : ' bs-btn-outline') + '" style="margin:2px;" onclick="loadReviews(' + p + ')"' + (p === data.page ? ' disabled' : '') + '>' + p + '</button>';
				}
				pagingEl.innerHTML = html;
			});
		}

		function searchReviews() {
			loadReviews(1);
		}

		loadReviews(1);
	</script>
</body>
</html>
