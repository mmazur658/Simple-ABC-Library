<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html ">
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
		<title>Simple ABC Library - Dodawanie Książki</title>
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
		
			<form:form class="form-signin" action="saveBook" modelAttribute="book" method="POST">
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo-update-form" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
				<h3 class="h3-heading">Dodawanie Nowej Książki</h3>
				<form:input class="form-control" type="text" path="title" placeholder="Tytuł" required="required"/>
				<form:input class="form-control" type="text" path="author" placeholder="Autor" required="required"/>
				<form:input class="form-control" type="text" path="isbn" placeholder="ISBN" required="required"/>
				<form:input class="form-control" type="text" path="publisher" placeholder="Wydawnictwo" required="required"/>
				<form:input class="form-control" type="text" path="language" placeholder="Język" required="required"/>
				<form:input class="form-control" type="number" path="pages" placeholder="Stron" required="required"/>			
				<button class="big-button" type="submit">Zapisz</button>
			</form:form>
			
			<div class="return-container">
				<button class="small-button" onclick="history.back()">Wróć</button>
			</div>
		
		</div>	
		
	</body>
</html>