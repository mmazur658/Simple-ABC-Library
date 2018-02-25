<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
	<title>Simple ABC Library - Strona Główna</title>
</head>
<body>

	<c:url var="userDetailsLink" value="/user/user-details">
		<c:param name="userDetailsUserId" value="${user.id}" />
		<c:param name="userDetailsWayBack" value="main" />
	</c:url>

	<header>
		<button class="header-button" onclick="window.location.href='${userDetailsLink}'"> <%=session.getAttribute("userFirstName")%> <%=session.getAttribute("userLastName")%></button>
		<button class="header-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-inbox'">MessageBox</button>
		<button class="header-button" onclick="window.location.href='${pageContext.request.contextPath}/user/logout'">Wyloguj</button>
	</header>

	<div class="wrapper">
		<div class="container">
			<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
			
		 	<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/book/main-bookstore'">Biblioteka</button>
			<c:if test="${userAccessLevel eq 'Employee' or  userAccessLevel eq 'Administrator' }">
				<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/user/employee-panel'">Portal Pracownika</button>
			</c:if>
			<c:if test="${userAccessLevel eq 'Administrator' }">	
				<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/user/admin-panel'">Portal Administratora</button>
			</c:if>
				
		</div>	
	</div>
		
</body>
</html>