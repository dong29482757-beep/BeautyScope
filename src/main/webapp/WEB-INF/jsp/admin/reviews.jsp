<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 - 리뷰 관리</title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container">
		<%@ include file="adminNav.jsp" %>

		<a href="${ pageContext.request.contextPath }/admin/products">&larr; 상품 목록</a>
		<h2><c:out value="${ product.productName }" /> 리뷰 관리</h2>

		<div class="bs-recommend-form">
			<h3 style="margin:0 0 14px;font-size:14px;">리뷰 추가</h3>
			<form action="${ pageContext.request.contextPath }/admin/products/${product.platform}/${product.productId}/reviews/new" method="post" class="bs-auth-form">
				<label>평점 (1~5)</label>
				<input type="number" name="rating" min="1" max="5" step="0.5" required>

				<label>감성</label>
				<select name="sentiment">
					<option value="positive">positive</option>
					<option value="neutral">neutral</option>
					<option value="negative">negative</option>
				</select>

				<label>닉네임</label>
				<input type="text" name="nickname" placeholder="익명">

				<label>리뷰 내용</label>
				<input type="text" name="reviewContent" required>

				<label>작성일 (YYYY-MM-DD)</label>
				<input type="text" name="reviewDate" placeholder="2026-06-25">

				<button type="submit" class="bs-btn" style="margin-top:16px;">리뷰 추가</button>
			</form>
		</div>

		<h3>전체 리뷰 (${ reviewResult.totalCount }건)</h3>
		<table class="bs-table">
			<tr><th>평점</th><th>감성</th><th>내용</th><th>닉네임</th><th>날짜</th><th></th></tr>
			<c:forEach items="${ reviewResult.reviews }" var="r">
				<tr>
					<td>★ ${ r.rating }</td>
					<td class="sentiment-${ r.sentiment }">${ r.sentiment }</td>
					<td><c:out value="${ r.reviewContent }" /></td>
					<td><c:out value="${ r.nickname }" /></td>
					<td><c:out value="${ r.reviewDate }" /></td>
					<td>
						<form action="${ pageContext.request.contextPath }/admin/products/${product.platform}/${product.productId}/reviews/${r.reviewNo}/delete" method="post" onsubmit="return confirm('이 리뷰를 삭제할까요?');">
							<button type="submit" style="border:none;background:none;color:#8B3A3A;cursor:pointer;font-weight:700;">삭제</button>
						</form>
					</td>
				</tr>
			</c:forEach>
		</table>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
