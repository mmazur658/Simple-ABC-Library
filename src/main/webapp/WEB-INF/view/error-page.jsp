
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
    
<!DOCTYPE html>
<html lang="pl">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
	<title><spring:message code="view.error-page.title"/></title>
</head>

<body>

<spring:message var="login-page" code="view.error-page.login-page"/>

	<div class="wrapper">
		<br><br><br><br><br><br>
		<div class="container  pt-5 mt-4">
			
			<br>
			${errorMessage}
			
			<br><br>
			
			<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/user/login-page'">${login-page }</button>
				
		</div>	
	</div>
</body>
</html>