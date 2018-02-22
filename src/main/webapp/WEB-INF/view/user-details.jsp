<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html >
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/details-style.css" />
		<title>Simple ABC Library - Dane Użytkownika</title>
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
						<td id="header-td" colspan="2">Dane Użytkownika: </td>

					</tr>
				 		
					<tr>
						<th>Id:</th><td>${theUser.id}</td>
					</tr><tr>
						<th>Imię:</th><td>${theUser.firstName}</td>
					</tr><tr>
						<th>Nazwisko:</th><td>${theUser.lastName}</td>
					</tr><tr>
						<th>Email:</th><td>${theUser.email}</td>
					</tr><tr>
						<th>Pesel:</th><td>${theUser.pesel}</td>
					</tr><tr>
						<th>Płeć:</th><td>${theUser.sex}</td>
					</tr><tr>
						<th>Adres:</th><td>${theUser.street} ${theUser.houseNumber }</td>
					</tr><tr>
						<th>Miasto:</th><td>${theUser.city} ${theUser.postalCode}</td>
					</tr><tr>
						<th>Status:</th><td>${userAccessLevel}</td>
					</tr><tr>
						<th>Data urodzenia:</th><td><fmt:formatDate value="${theUser.birthday}" pattern="yyyy-MM-dd"/></td>
					</tr><tr>
						<th>Dołączył(a):</th><td><fmt:formatDate value="${theUser.startDate}" pattern="yyyy-MM-dd"/></td>
					</tr>
				
				</table>	
				
				<c:url var="userUpdateLink" value="/user/user-update-form">					
					<c:param name="userUpdateUserId" value="${theUser.id}"/>		
				</c:url>
				<c:url var="changePasswordLink" value="/user/change-password-form">					
					<c:param name="changePasswordUserId" value="${theUser.id}"/>		
				</c:url>
				<c:url var="increaseAccessLevelLink" value="/user/increase-access-level">					
					<c:param name="increaseAccessLevelUserId" value="${theUser.id}"/>		
				</c:url>
				<c:url var="decreaseAccessLevelLink" value="/user/decrease-access-level">					
					<c:param name="decreaseAccessLevelUserId" value="${theUser.id}"/>		
				</c:url>						
					
				<button class="big-button" onclick="window.location.href='${userUpdateLink}'">Aktualizacja Danych</button>	
				<button class="big-button" onclick="window.location.href='${changePasswordLink}'">Zmiana Hasła</button>	
					
				<c:if test="${loginUserAccessLevel eq 'Administrator' }">	
					<button class="big-button" onclick="window.location.href='${increaseAccessLevelLink}'">Zwiększ Uprawnienia</button>	
					<button class="big-button" onclick="window.location.href='${decreaseAccessLevelLink}'">Zmniejsz Uprawnienia</button>
				</c:if>
				
				<c:choose>		
					<c:when test="${loginUserAccessLevel eq 'user-management'}">
						<button class="return-button" onclick="window.location.href='${pageContext.request.contextPath}/user/user-management'">Powrót</button>
					</c:when>
					<c:otherwise>
						<button class="return-button" onclick="window.location.href='${pageContext.request.contextPath}/user/main'">Powrót</button>
					</c:otherwise>
				</c:choose>
				
				
				
				<table class="reservation-table">
				
					<tr>
						<td id="header-td" colspan="4">Rezerwacje </td>
					</tr>
				
					<tr>
						<th id="title-column">Tytuł</th>
						<th id="author-column">Autor</th>
						<th id="date-column">Data końca</th>
						<th id="action-column">Akcja</th>
					</tr>
					
					<c:forEach var="tempReservation" items="${reservationList}">
			
					<c:url var="deleteReservationLink" value="/book/deleteReservation">					
						<c:param name="reservationId" value="${tempReservation.id}"/>	
						<c:param name="deleteReservationWayBack" value="user-details"/>			
					</c:url>

					<tr>
						<td id="title-column">${tempReservation.book.title}</td>
						<td id="author-column">${tempReservation.book.author }</td>
						<td id="date-column"><fmt:formatDate value="${tempReservation.endDate }" pattern="hh:mm dd-MM"/></td>
						<td id="action-column"><button class="small-button" onclick="window.location.href='${deleteReservationLink}'">Usuń</button></td>
					</tr>

					</c:forEach>	

				</table>
				
				<table class="reservation-table">
				
					<tr>
						<td id="header-td" colspan="4">Pożyczone Książki </td>
					</tr>
				
					<tr>
						<th id="borrowred-book-table-title-column">Tytuł</th>
						<th id="borrowred-book-table-author-column">Author</th>
						<th id="borrowred-book-table-date-column">Data końca</th>
					</tr>
					
					<c:forEach var="tempBookBorrowing" items="${bookBorrowingList}">

						<tr>
							<td id="borrowred-book-table-title-column">${tempBookBorrowing.book.title}</td>
							<td id="borrowred-book-table-author-column">${tempBookBorrowing.book.author }</td>
							<td id="borrowred-book-table-date-column"><fmt:formatDate value="${tempBookBorrowing.expectedEndDate}" pattern="hh:mm dd-MM"/></td>		
						</tr>

					</c:forEach>	
					
				</table>
				
			</div>
		</div>



</body>
</html>