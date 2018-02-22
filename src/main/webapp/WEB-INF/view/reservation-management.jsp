<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html ">
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/management-style.css" />
		<title>Simple ABC Library -  Zarządzanie Rezerwacjami</title>
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
		
			<form class="form-signin" action="reservation-management"method="post">
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
				<h3 class="h3-heading">Zarządzanie Rezerwacjami</h3>	
				<input class="form-control" type="text" placeholder="ID Użytkownika" name="customerId" value="<%=(session.getAttribute("customerId")==null) ? "" : session.getAttribute("customerId")%>">
				<input class="form-control" type="text" placeholder="Imię Użytkownika" name="customerFirstName" value="<%=(session.getAttribute("customerFirstName")==null) ? "" : session.getAttribute("customerFirstName")%>">
				<input class="form-control" type="text" placeholder="Nazwisko Użytkownika" name="customerLastName" value="<%=(session.getAttribute("customerLastName")==null) ? "" : session.getAttribute("customerLastName")%>">
				<input class="form-control" type="text" placeholder="PESEL Użytkwonika" name="customerPesel" value="<%=(session.getAttribute("customerPesel")==null) ? "" : session.getAttribute("customerPesel")%>">
				<input class="form-control" type="text" placeholder="Id Książki" name="bookId" value="<%=(session.getAttribute("bookId")==null) ? "" : session.getAttribute("bookId")%>">
				<input class="form-control" type="text" placeholder="Tytuł Książki" name="bookTitle" value="<%=(session.getAttribute("bookTitle")==null) ? "" : session.getAttribute("bookTitle")%>">
				<button class="big-button" type="submit">Szukaj</button>
			</form>
			
	
			<div class="return-container">			
				<form action ="clearReservationSearchParameters" >
					<button class="big-button" type="submit">Wyczyść dane wyszukiwania</button>	
				</form>
			</div>

			
		
		
		<div class="container">
			
			
		
			<c:url var="showMoreLink" value="/reservation/reservation-management">					
				<c:param name="startResult" value="${showMoreLinkValue}"/>					
			</c:url>
			
			<c:url var="showLessLink" value="/reservation/reservation-management">					
				<c:param name="startResult" value="${showLessLinkValue}"/>					
			</c:url>
		
			<p id="result-paragraph">Znaleziono: ${amountOfResults}</p><br>
			<button class="nav-small-button" onclick="window.location.href='${showMoreLink}'"> >>> </button>
			<button class="nav-special-button"> ${resultRange} </button>
			<button class="nav-small-button" onclick="window.location.href='${showLessLink}'"> <<< </button>
			<br><br>
		
			<table>
				<caption>Rezerwacje</caption>
				<tr>
					<th id="id-column">Id Kl.</th>
					<th>Imię i Nazwisko</th>
					<th>Pesel</th>
					<th id="id-column">Id Ks.</th>
					<th>Tytuł Książki</th>
					<th>Ważność Rezerwacji</th>
					</tr>
							
						<c:forEach var="tempReservation" items="${reservationList}">
							
							<c:url var="deleteReservationLink" value="/reservation/delete-reservation">					
								<c:param name="reservationId" value="${tempReservation.id}"/>					
							</c:url>
				
							<c:url var="increaseExpDateLink" value="/reservation/increase-exp-date">					
								<c:param name="reservationId" value="${tempReservation.id}"/>					
							</c:url>
			
								
							<tr>
								<td id="id-column">${tempReservation.user.id}</td>
								<td>${tempReservation.user.firstName} ${tempReservation.user.lastName}</td>
								<td>${tempReservation.user.pesel}</td>
								<td id="id-column">${tempReservation.book.id}</td>
								<td>${tempReservation.book.title}</td>
								<td><fmt:formatDate value="${tempReservation.endDate }" pattern="HH:mm dd-MM"/>	</td>
							</tr>
							<tr>

								<td colspan="5">${tempReservation.status}</td>
								<td>
									<button class="small-button" onclick="window.location.href='${deleteReservationLink}'">Usuń</button>
									<button class="small-button" onclick="window.location.href='${increaseExpDateLink}'">+24h</button>
								</td>
							</tr>
							
					</c:forEach>
				</table>
			</div>
		</div>
	</body>
</html>