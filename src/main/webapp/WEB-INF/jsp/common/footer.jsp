<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${ not empty param.msg }">
	<div id="bsFlashMsg" style="display:none;"><c:out value="${ param.msg }"/></div>
	<script>
	(function () {
		var el = document.getElementById('bsFlashMsg');
		if (!el) return;
		alert(el.textContent);
		var url = new URL(window.location.href);
		url.searchParams.delete('msg');
		window.history.replaceState({}, document.title, url.toString());
	})();
	</script>
</c:if>
<footer class="bs-footer">
	<div class="container">
		<div class="bs-footer-grid">
			<div class="bs-footer-col">
				<h4>BeautyScope</h4>
				<p>쿠팡 · 무신사 · 올리브영 화장품 리뷰<br>538,774건 기반 분석 서비스</p>
			</div>
			<div class="bs-footer-col">
				<h4>바로가기</h4>
				<a href="${ pageContext.request.contextPath }/product">제품 목록</a>
				<a href="${ pageContext.request.contextPath }/ranking">카테고리 랭킹</a>
			</div>
			<div class="bs-footer-col">
				<h4>데이터 출처</h4>
				<p>쿠팡 · 무신사 · 올리브영<br>리뷰 크롤링 데이터 기반 분석</p>
			</div>
		</div>
		<div class="bs-footer-bottom">© 2026 BeautyScope. 본 사이트는 학습용 프로젝트입니다.</div>
	</div>
</footer>
