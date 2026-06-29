<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AI 추천 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container">
		<div class="bs-rec-intro">
			<div class="eyebrow">AI Recommendation Engine</div>
			<h2 style="margin:14px 0 10px;">합성 점수 기반 추천</h2>
			<p style="color:var(--bs-ink-soft);max-width:640px;">
				리뷰 53만여 건을 8개 속성(보습/수분, 트러블/자극, 발림성/흡수력 등)으로 분석한 데이터를
				바탕으로, <b>선택한 속성들의 만족도(리뷰 수가 적으면 카테고리 평균쪽으로 보정) + 전체
				리뷰 만족도 + 평점</b>을 가중합한 점수로 제품을 정렬합니다. 속성은 여러 개 골라도 됩니다.
			</p>
		</div>

		<form class="bs-recommend-form" action="${ pageContext.request.contextPath }/recommend" method="get">
			<div class="row">
				<div>
					<label>카테고리</label>
					<select name="category">
						<c:forEach items="${ categories }" var="cat">
							<option value="${ cat }" ${ cat == selectedCategory ? 'selected' : '' }><c:out value="${ cat }" /></option>
						</c:forEach>
					</select>
				</div>
				<div>
					<label>신경 쓰는 속성 (복수 선택 가능)</label>
					<div class="bs-aspect-checks">
						<c:forEach items="${ aspectNames }" var="asp">
							<label class="bs-aspect-check">
								<input type="checkbox" name="aspects" value="${ asp }"
									${ selectedAspects != null && selectedAspects.contains(asp) ? 'checked' : '' }>
								<c:out value="${ asp }" />
							</label>
						</c:forEach>
					</div>
				</div>
				<button type="submit" class="bs-btn">AI 추천받기</button>
			</div>
		</form>

		<c:if test="${ not empty result }">
			<div class="bs-rec-result-head">
				<span class="bs-platform-tag" style="background:var(--bs-bronze);">분석 결과</span>
				<h2 style="margin:10px 0 4px;"><c:out value="${ selectedCategory }" /> 추천 TOP <c:out value="${ result.size() }"/></h2>
				<p style="color:var(--bs-ink-soft);font-size:13px;">
					점수 = 선택 속성 만족도 50% + 전체 리뷰 만족도 30% + 평점 20%로 계산했습니다. 리뷰 수가
					적은 제품은 베이지안 보정으로 점수를 카테고리 평균쪽으로 당겨서, 리뷰 몇 건짜리 제품이
					운 좋게 상위에 뜨는 걸 방지합니다.
				</p>
			</div>
			<div class="bs-card-grid">
				<c:forEach items="${ result }" var="p" varStatus="st">
					<a class="bs-card bs-rec-card" href="${ pageContext.request.contextPath }/product/${p.platform}/${p.productId}">
						<c:if test="${ st.index == 0 }"><span class="bs-rec-badge">BEST MATCH</span></c:if>
						<span class="bs-platform-tag">${ st.index + 1 }위 · ${ p.platform }</span>
						<div class="bs-name"><c:out value="${ p.productName }" /></div>
						<div class="bs-aspect-row" style="margin:0 0 8px;">
							<div class="bar-track"><div class="bar-fill" style="width:${ p.score }%;"></div></div>
							<div class="aspect-pct">종합 ${ p.score }점</div>
						</div>
						<div class="bs-meta" style="margin-bottom:4px;">
							<c:out value="${ p.matchedAspects }"/> 만족도 <b>${ p.positiveRatio }%</b> ·
							전체 만족도 <b>${ p.overallRatio }%</b>
						</div>
						<div class="bs-meta">★ ${ p.avgRating } · <c:out value="${ p.brandName }" /> · 리뷰 ${ p.reviewCount }건</div>
					</a>
				</c:forEach>
			</div>
		</c:if>

		<c:if test="${ selectedCategory != null and empty result }">
			<p>조건에 맞는 제품을 찾지 못했어요. 다른 속성이나 카테고리를 선택해보세요.</p>
		</c:if>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
