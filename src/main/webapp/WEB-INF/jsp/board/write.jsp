<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
	const checkForm = function() {
		
		let f = document.inputForm
		
		if(f.title.value == '') {
			alert('제목을 입력하세요')
			f.title.focus()
			return false
		}
		
		if(f.writer.value === '') {
			alert('작성자를 입력하세요')
			f.writer.focus()
			return false
		}
		
		if(f.content.value === '') {
			alert('내용은 필수항목입니다')
			f.content.focus()
			return false
		}
		
		return true
	}
</script>
</head>
<body>

	<hr>
	<h2>새글등록폼</h2>
	<hr>
	<br>
	<form method="post" onsubmit="return checkForm()" name="inputForm">
		<table border="1" style="width:80%">
			<tr>
				<th width="25%">제목</th>
				<td><input type="text" name="title" /></td>
			</tr>
			<tr>
				<th width="25%">글쓴이</th>
				<td><input type="text" name="writer" /></td>
			</tr>
			<tr>
				<th width="25%">내용</th>
				<td>
					<textarea rows="10" name="content"></textarea>
				</td>
			</tr>
		</table>
		<br>
		<button type="submit">등록</button>
	</form>

</body>
</html>