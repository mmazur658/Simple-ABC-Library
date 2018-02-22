<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html >
<html lang="pl-PL">
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
		<title>Simple ABC Library - Aktualizacja Danych Książki</title>
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
		
			<form:form class="form-signin" action="update-book" modelAttribute="book" method="Post">
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo-update-form" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
				<h3 class="h3-heading">Aktualizacja Książki</h3>
				<form:hidden class="form-control" path="id"/>
				<form:input class="form-control" type="text" placeholder="Tytuł" path="title"/>
				<form:input class="form-control" type="text" placeholder="Author" path="author"/>
				<form:input class="form-control" type="text" placeholder="ISBN" path="isbn"/>
				<form:input class="form-control" type="text" placeholder="Wydawnictwo" path="publisher"/>
				<form:input class="form-control" type="text" placeholder="Język" path="language"/>
				<form:input class="form-control" type="number" placeholder="Stron" path="pages"/>
				<button class="big-button" type="submit">Zapisz</button>
			</form:form>
			
			<div class="return-container">
				<button class="small-button" onclick="history.back()">Wróć</button>
			</div>
		
		</div>
	
</body>

</html>