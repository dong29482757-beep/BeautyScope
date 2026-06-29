<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<hr>
	<h2>전체게시글 조회</h2>
	<hr>
	
	<table border="1" style="width: 80%;">
		<tr>
			<th width="10%">번호</th>
			<th>제목</th>
			<th width="19%">작성자</th>
			<th width="22%">등록일</th>
		</tr>
		<c:forEach items="${ boardList }" var="board">
			<tr>
				<td>${ board.no }</td>
				<td>
					<%-- <a href="${ pageContext.request.contextPath }/board/detail?no=${board.no}"> --%>
					<a href="${ pageContext.request.contextPath }/board/${board.no}">
						<c:out value="${ board.title }" />
					</a>
				</td>
				<td><c:out value="${ board.writer }" /></td>
				<td>${ board.regDate }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>