<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
	<title>Simple ABC Library - Error Page</title>
</head>
<body>
	<div class="wrapper">
		<br><br><br><br><br><br>
		<div class="container  pt-5 mt-4">
			
			<br>
			${errorMessage}
			
			<br><br>
			
			<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/user/login-page'">Powrót do logowania</button>
				
		</div>	
	</div>
</body>
</html>