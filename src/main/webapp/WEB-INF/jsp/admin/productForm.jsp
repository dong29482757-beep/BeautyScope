<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 - 상품 <c:out value="${ empty product ? '추가' : '수정' }" /></title>
<%@ include file="../common/header.jsp" %>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container" style="max-width:480px;">
		<%@ include file="adminNav.jsp" %>

		<h2><c:out value="${ empty product ? '상품 추가' : '상품 수정' }" /></h2>

		<c:set var="formAction" value="${ pageContext.request.contextPath }/admin/products/new" />
		<c:if test="${ not empty product }">
			<c:set var="formAction" value="${ pageContext.request.contextPath }/admin/products/${product.platform}/${product.productId}/edit" />
		</c:if>

		<form class="bs-auth-form" action="${ formAction }" method="post">

			<label>플랫폼</label>
			<c:choose>
				<c:when test="${ empty product }">
					<select name="platform">
						<option value="coupang">coupang</option>
						<option value="musinsa">musinsa</option>
						<option value="oliveyoung">oliveyoung</option>
					</select>
				</c:when>
				<c:otherwise>
					<input type="text" value="${ product.platform }" disabled>
				</c:otherwise>
			</c:choose>

			<label>상품 ID</label>
			<c:choose>
				<c:when test="${ empty product }">
					<input type="text" name="productId" required>
				</c:when>
				<c:otherwise>
					<input type="text" value="${ product.productId }" disabled>
				</c:otherwise>
			</c:choose>

			<label>상품명</label>
			<input type="text" name="productName" value="${ product.productName }" required>

			<label>브랜드</label>
			<input type="text" name="brandName" value="${ product.brandName }">

			<label>카테고리</label>
			<input type="text" name="category" list="categoryOptions" value="${ product.category }" required>
			<datalist id="categoryOptions">
				<c:forEach items="${ categories }" var="cat">
					<option value="${ cat }">
				</c:forEach>
			</datalist>

			<button type="submit" class="bs-btn" style="width:100%;margin-top:20px;">
				<c:out value="${ empty product ? '추가하기' : '수정하기' }" />
			</button>
		</form>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>
