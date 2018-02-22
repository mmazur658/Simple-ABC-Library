<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html >
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/details-style.css" />
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
			
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
			
				<table class="user-details-table">
					<tr>
						<td id="header-td" colspan="2">Dane Książki: </td>

					</tr>
				 		
					<tr>
						<th>Id:</th><td>${tempBook.id }</td>
					</tr><tr>
						<th>Tytuł:</th><td>${tempBook.title }</td>
					</tr><tr>
						<th>Autor:</th><td>${tempBook.author }</td>
					</tr><tr>
						<th>Język:</th><td>${tempBook.language }</td>
					</tr><tr>
						<th>Wydawnictwo:</th><td>${tempBook.publisher }</td>
					</tr><tr>
						<th>Strony:</th><td>${tempBook.pages }</td>
					</tr><tr>
						<th>ISBN:</th><td>${tempBook.isbn}</td>
					</tr><tr>
						<th>Dostępność:</th>
						<c:choose>
								<c:when test="${tempBook.isAvailable}"> <td>Dostępna</td> </c:when>
								<c:otherwise> <td>Niedostępna</td> </c:otherwise>
						</c:choose>						
					</tr>
				
				</table>
			
				<c:url var="reservationLink" value="/book/reservation">					
					<c:param name="bookId" value="${tempBook.id}"/>					
				</c:url>
				
				<c:url var="updateLink" value="/book/updateBook">					
					<c:param name="bookId" value="${tempBook.id}"/>					
				</c:url>
							
				<c:url var="deleteLink" value="/book/deleteBook">					
					<c:param name="bookId" value="${tempBook.id}"/>					
				</c:url>
				
				<c:url var="generateLabelLink" value="/book/generate-book-label">					
					<c:param name="bookId" value="${tempBook.id}"/>					
				</c:url>
			
				<button class="big-button" onclick="window.location.href='${reservationLink}'">Rezerwuj</button>
			
				<c:if test="${userAccessLevel eq 'Employee' or  userAccessLevel eq 'Administrator' }">	
					<button class="big-button" onclick="window.location.href='${updateLink}'">Aktualizuj</button>	
					<button class="big-button" onclick="window.location.href='${deleteLink}'">Usuń </button>
					<button class="big-button" onclick="window.location.href='${generateLabelLink}'">Etykieta</button>
				</c:if>
			
				<button class="return-button" onclick="history.back()">Wróć</button>
			
			</div>
		</div>

	</body>
</html>