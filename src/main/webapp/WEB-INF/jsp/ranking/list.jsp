<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>카테고리 랭킹 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container">
		<h2>카테고리 랭킹</h2>

		<form class="bs-filterbar" action="${ pageContext.request.contextPath }/ranking" method="get">
			<select name="category" onchange="this.form.submit()">
				<c:forEach items="${ categories }" var="cat">
					<option value="${ cat }" ${ cat == selectedCategory ? 'selected' : '' }><c:out value="${ cat }" /></option>
				</c:forEach>
			</select>
		</form>

		<h3 style="margin-top:30px;"><c:out value="${ selectedCategory }" /> 평점 랭킹</h3>
		<table class="bs-table">
			<tr><th>순위</th><th></th><th>플랫폼</th><th>제품명</th><th>브랜드</th><th>평점</th><th>리뷰수</th></tr>
			<c:forEach items="${ rankingList }" var="p" varStatus="st">
				<tr>
					<td>${ st.index + 1 }</td>
					<td>
						<c:if test="${ not empty p.imagePath }">
							<img src="${ pageContext.request.contextPath }/resources/product_images/${ p.imagePath }" style="width:40px;height:40px;object-fit:cover;border:1px solid var(--bs-line);" alt="">
						</c:if>
					</td>
					<td>${ p.platform }</td>
					<td>
						<a href="${ pageContext.request.contextPath }/product/${p.platform}/${p.productId}">
							<c:out value="${ p.productName }" />
						</a>
					</td>
					<td><c:out value="${ p.brandName }" /></td>
					<td>${ p.avgRating }</td>
					<td>${ p.reviewCount }</td>
				</tr>
			</c:forEach>
		</table>

		<h3 style="margin-top:40px;">브랜드별 평균 평점 비교</h3>
		<div class="bs-chart-box" style="max-width:600px;">
			<canvas id="brandChart"></canvas>
		</div>
	</section>

	<%@ include file="../common/footer.jsp" %>

	<script>
		const brandLabels = [<c:forEach items="${brandStats}" var="b" varStatus="st">"<c:out value="${b.brandName}"/>"<c:if test="${!st.last}">,</c:if></c:forEach>];
		const brandRatings = [<c:forEach items="${brandStats}" var="b" varStatus="st">${b.avgRating}<c:if test="${!st.last}">,</c:if></c:forEach>];
		new Chart(document.getElementById('brandChart'), {
			type: 'bar',
			data: {
				labels: brandLabels,
				datasets: [{ label: '평균 평점', data: brandRatings, backgroundColor: '#9C7A3C', borderRadius: 0 }]
			},
			options: { indexAxis: 'y', plugins: { legend: { display: false } } }
		});
	</script>
</body>
</html>
