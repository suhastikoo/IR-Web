<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1 align="center">Search Page</h1>
	<br>
	<table align="center">
		<tr>
			<td>
					<input type="text" name="fname" id="textbox" placeholder="Enter your query.">
				
			</td>
		</tr><tr></tr>
		<tr>
			<td align="center"><input type="submit" value="Search" onClick=myfunct()>
			</td>
		</tr>
	</table>
	
	<script type="text/javascript">
	function myfunct(){
		var value = document.getElementById('textbox').value;
		window.location = "Results.jsp?value="+value;
	}
	</script>
</body>
</html>