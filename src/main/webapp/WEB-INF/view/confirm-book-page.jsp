<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
		<title>Simple ABC Library - Szczegóły Książki</title>
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
	
		<c:if test="${not empty systemMessage}">
			<div class="system-message-container">
				<p id="system-message">Komunikat: ${systemMessage}</p>
			</div>
		</c:if>

		<div class="wrapper">
			<div class="container">
			
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo-update-form" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
				<h3 class="h3-heading">Książka została dodana  </h3>
			
				<c:url var="generateBookLabel" value="/book/generate-book-label">					
					<c:param name="bookId" value="${tempBook.id}"/>					
				</c:url>			
			
				<button class="big-button" onclick="window.location.href='${generateBookLabel}'">Etykieta</button>
				<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/book/add-book-form'">Dodaj Następną Książkę</button>
				<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/user/main'">Strona Główna</button>
			
			</div>
		</div>

	</body>

</html>