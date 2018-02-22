<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html ">
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bookstore-style.css" />
		<title>Simple ABC Library - Biblioteka</title>
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
		
			<form class="form-signin" action="main-bookstore">
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
				<h3 class="h3-heading">Księgozbiór</h3>	
				<input class="form-control" placeholder="ID" type="text" name ="id" value = "<%=(session.getAttribute("id")==null) ? "" : session.getAttribute("id")%>"/>
				<input class="form-control" placeholder="Tytuł" type="text" name="title" value = "<%=(session.getAttribute("title")==null) ? "" : session.getAttribute("title")%>"/>
				<input class="form-control" placeholder="Autor" type="text" name="author" value = "<%=(session.getAttribute("author")==null) ? "" : session.getAttribute("author") %>"/>
				<input class="form-control" placeholder="Wydawca" type="text" name="publisher" value = "<%=(session.getAttribute("publisher")== null) ? "" : session.getAttribute("publisher")%>"/>
				<input class="form-control" placeholder="ISBN" type="text" name="isbn" value = "<%=(session.getAttribute("isbn")==null) ? "" : session.getAttribute("isbn")%>"/>			
				<button class="big-button" type="submit">Szukaj</button>
			</form>
		
			<div class="return-container">
				<form action ="clearSearchParameters" >
					<button class="big-button" type="submit">Wyczyść dane wyszukiwania</button>	
					
				</form>		
			</div>	
		
			<div class="container">
			
				<button class="big-button" onclick="window.location.href='${userDetailsLink}'">Twoje Rezerwacje</button>			
			
				<c:url var="showMoreLink" value="/book/main-bookstore">					
					<c:param name="startResult" value="${showMoreLinkValue}"/>					
				</c:url>
				
				<c:url var="showLessLink" value="/book/main-bookstore">					
						<c:param name="startResult" value="${showLessLinkValue}"/>					
				</c:url>
			
				<p id="result-paragraph">Znaleziono: ${amountOfResults}</p><br>
				<button class="nav-small-button" onclick="window.location.href='${showMoreLink}'"> >>> </button>
				<button class="nav-special-button"> ${resultRange} </button>
				<button class="nav-small-button" onclick="window.location.href='${showLessLink}'"> <<< </button>
			
				<table>
					<tr>
						<th id="id-column">Id</th>
						<th>Tytuł</th>
						<th>Autor</th>
						<th id="isAvailable-column">Dostępna</th>				
						<th id="action-column">Akcja</th>
					</tr>		
				
					<c:forEach var="tempBook" items="${booksList}">
						
						<c:set var="isAvailable" value="${tempBook.isAvailable}"/>
						
						<c:url var="reservationLink" value="/book/reservation">					
								<c:param name="bookId" value="${tempBook.id}"/>					
						</c:url>
						
						<c:url var="bookDetailsLink" value="/book/book-details">					
								<c:param name="bookId" value="${tempBook.id}"/>					
						</c:url>
						
						<tr>
							<td id="id-column">${tempBook.id }</td>
							<td>${tempBook.title }</td>
							<td>${tempBook.author }</td>
		
							<c:choose>
								<c:when test="${isAvailable}"> <td id="isAvailable-column">Dostępna</td> </c:when>
								<c:otherwise> <td>Niedostępna</td id="isAvailable-column"> </c:otherwise>
							</c:choose>
		
							<td id="action-column">
								<button class="small-button" onclick="window.location.href='${reservationLink}'">Rezerwuj</button>
								<button class="small-button" onclick="window.location.href='${bookDetailsLink}'">Szczegóły</button>
							</td>
			
						</tr>			
					
					</c:forEach>
				
				</table>
			
			
			</div>	
		</div>

	
	
	
	
	
	
	<a href="${pageContext.request.contextPath}/user/main" >Powrót</a>
	

	
	
</body>
</html>