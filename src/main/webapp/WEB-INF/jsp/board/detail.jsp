<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세</title>
</head>
<body>
	<hr>
	<h2>게시글 상세</h2>
	<hr>

	<table border="1" style="width: 80%;">
		<tr>
			<th width="15%">번호</th>
			<td>${ board.no }</td>
		</tr>
		<tr>
			<th>제목</th>
			<td><c:out value="${ board.title }" /></td>
		</tr>
		<tr>
			<th>작성자</th>
			<td><c:out value="${ board.writer }" /></td>
		</tr>
		<tr>
			<th>등록일</th>
			<td>${ board.regDate }</td>
		</tr>
		<tr>
			<th>내용</th>
			<td style="padding: 10px; min-height: 100px;">
				<c:out value="${ board.content }" />
			</td>
		</tr>
	</table>

	<br>

	<form method="post" action="${ pageContext.request.contextPath }/board/delete"
	      onsubmit="return confirm('정말 삭제하시겠습니까?')">
		<input type="hidden" name="no" value="${ board.no }" />
		<button type="submit">삭제</button>
	</form>

	<br>
	<a href="${ pageContext.request.contextPath }/board">목록으로</a>

	<hr>
	<h3>댓글 목록</h3>
	<ul id="replyList"></ul>

	<hr>
	<h3>댓글 작성</h3>
	<form name="rform">
		<input type="text" name="writer" placeholder="작성자" /><br>
		<textarea name="content" placeholder="댓글 내용"></textarea><br>
		<button type="button" id="replyAddBtn">댓글 등록</button>
	</form>

	<script type="text/javascript">
	    document.addEventListener("DOMContentLoaded", function(){

	    	function loadReplyList() {
	    		fetch('${pageContext.request.contextPath}/reply/${board.no}')
	    		.then(response => response.json())
	    		.then(result => {
	    			console.log(result);
	    			const replyList = document.getElementById('replyList');
	    			replyList.innerHTML = '';
	    			result.forEach(function(reply) {
	    				const li = document.createElement('li');
	    				li.textContent = '[' + reply.writer + '] ' + reply.content + ' (' + reply.regDate + ')';
	    				replyList.appendChild(li);
	    			});
	    		})
	    	}

	    	loadReplyList();

	        const replyAddBtn = document.getElementById('replyAddBtn')
	        const content = document.rform.content
	        const writer = document.rform.writer
	        replyAddBtn.addEventListener('click', function() {
	            fetch('${pageContext.request.contextPath}/reply/${board.no}', {
	                method: 'post',
	                headers : {
	                	'Content-type' : 'application/x-www-form-urlencoded'
	                },
	                body : new URLSearchParams({
	                	boardNo: ${board.no},
	                	content: content.value,
	                	writer : writer.value
	                })
	            }).then(response => response.text())
	            .then(result => {
	            	console.log(result);
	            	loadReplyList();
	            })
	        })

	    })
	</script>

</body>
</html>
