<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html ">
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/borrow-return-book-choose-books-style.css" />
		<title>Simple ABC Library - Wydanie Książki - Wybierz Książki</title>
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
		
		<c:if test="${not empty systemMessage or not empty extraMessage }">
			<div class="system-message-container">
				<p id="system-message">Komunikat: ${extraMessage} ${systemMessage} </p>
			</div>
		</c:if>
	
		<div class="wrapper">		
			<div class="big-container">
				<div class="left-container">
				
					<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
					<h3 class="h3-heading">Wybrany Użytkownik  - ${theUser.firstName} ${theUser.lastName} <button class="small-button" onclick="window.location.href='${pageContext.request.contextPath}/borrow-book/borrow-book-choose-user'">Zmień</button></h3>
				
					<table>
						<caption>Wybrane Książki</caption>
						<tr>
							<th id="id-column">Id</th>
							<th id="title-column">Tytuł</th>
							<th id="author-column">Autor</th>
							<th id="action-delete-column">Akcja</th>
						</tr>
						
						<c:choose>
								<c:when test="${empty tempBookList}">
									<td colspan="4">Lista jest pusta</td>
								</c:when>
								<c:otherwise>
									<c:forEach var="tempBook" items="${tempBookList}">
						
									<c:url var="deleteBookLink" value="/borrow-book/deleteBookFromList">					
										<c:param name="bookId" value="${tempBook.id}"/>					
									</c:url>
							
									<tr>
										<td id="id-column">${tempBook.id }</td>
										<td id="title-column">${tempBook.title }</td>
										<td id="author-column">${tempBook.author }</td>
										<td id="action-delete-column"><button class="small-button" onclick="window.location.href='${deleteBookLink}'">Usuń</button></td> 
									</tr>	
				
									</c:forEach>		
						
								</c:otherwise>
							</c:choose>	
					</table>
					
					<form action="borrow-books">
						<button class="big-button" type="submit">Wydaj Książki</button>	
					</form>
					
					<form action="cancel-book-borrowing">
						<button class="big-button" type="submit">Anuluj Wydanie</button>	
					</form>
				
					
						<table>	
							<caption>Wypożyczone Książki</caption>	
							<tr>
								<th id="id-column">Id</th>
								<th id="title-column">Tytuł</th>
								<th id="author-column">Autor</th>
								<th>Zwrot do dnia</th>
							</tr>			
							<c:choose>
								<c:when test="${empty borrowedBookList}">
									<td colspan="4">Brak Wypożyczonych Książek</td>
								</c:when>
								<c:otherwise>
									<c:forEach var="tempBorrowed" items="${borrowedBookList}">							
										<tr>
											<td id="id-column">${tempBorrowed.book.id }</td>
											<td id="title-column">${tempBorrowed.book.title }</td>
											<td id="author-column">${tempBorrowed.book.author }</td>
											
											<td><fmt:formatDate value="${tempBorrowed.expectedEndDate }" pattern="dd-MM-yyyy"/></td>
										</tr>					
									</c:forEach>	
								</c:otherwise>
							</c:choose>	
		
							
					</table>
						
					<br>
						
					<table>	
						<caption>Zarezerwowane Książki</caption>		
						<tr>
							<th id="title-column">Tytuł</th>
							<th id="author-column">Autor</th>
							<th id="action-column">Akcja</th>	
						</tr>			

						<c:choose>
							<c:when test="${empty userReservationList}">
								<td colspan="3">Brak Rezerwacji</td>
							</c:when>
							<c:otherwise>
								<c:forEach var="tempReservation" items="${userReservationList}">						
									<c:url var="addReservedBookLink" value="/borrow-book/addReservedBookToList">					
											<c:param name="reservationId" value="${tempReservation.id}"/>					
									</c:url>
								
									<tr>
										<td id="title-column">${tempReservation.book.title }</td>
										<td id="author-column">${tempReservation.book.author }</td>
										<c:choose>
											<c:when test="${isAbleToBorrow == 'true'}">
												<td id="action-column"><button class="small-button" onclick="window.location.href='${addReservedBookLink}'">Do Wydania</button></td>	
											</c:when>
											<c:otherwise>
												<td id="action-column"> ---</td>
											</c:otherwise>
										</c:choose>
									</tr>					
								</c:forEach>	
							</c:otherwise>
						</c:choose>			
						
					</table>
				
				
				</div>			
			
				<div class="right-container">
				
					<h3 class="h3-heading">Wyszukiwarka Książek</h3>
								
					<form class="form-signin" action="borrow-book-choose-books" method="post">
						<input class="form-control" type="text" placeholder="ID" name ="bookId" value = "<%=(session.getAttribute("borrowBookSeachParamBookId")==null) ? "" : session.getAttribute("borrowBookSeachParamBookId")%>"/>
						<input class="form-control" type="text" placeholder="Tytuł" name="title" value = "<%=(session.getAttribute("borrowBookSeachParamBookTitle")==null) ? "" : session.getAttribute("borrowBookSeachParamBookTitle")%>"/>
						<input class="form-control" type="text" placeholder="Author" name="author" value = "<%=(session.getAttribute("borrowBookSeachParamBookAuthor")==null) ? "" : session.getAttribute("borrowBookSeachParamBookAuthor") %>"/>
						<button class="big-button" type="submit">Szukaj</button>
					</form>
	
					<div class="return-container">
						<form action ="clearBookSearchParameters" >
							<button class="big-button" type="submit">Wyczyść dane wyszukiwania</button>	
						</form>
					</div>	
			
					<div class="container">				

						<c:choose>
							<c:when test="${isAbleToBorrow == 'true'}">
								<c:url var="showMoreLink" value="/borrow-book/borrow-book-choose-books">					
									<c:param name="borrowBookChooseBookStartResult" value="${showMoreLinkValue}"/>					
								</c:url>
								<c:url var="showLessLink" value="/borrow-book/borrow-book-choose-books">					
									<c:param name="borrowBookChooseBookStartResult" value="${showLessLinkValue}"/>					
								</c:url>
							
								<h3 class="h3-heading">Wybierz Książkę</h3>
								<p id="result-paragraph">Znaleziono: ${amountOfResults}</p><br>
								<button class="nav-small-button" onclick="window.location.href='${showMoreLink}'"> >>> </button>
								<button class="nav-special-button"> ${resultRange} </button>
								<button class="nav-small-button" onclick="window.location.href='${showLessLink}'"> <<< </button>
								
								<table>
									<tr>
										<th id="id-column">Id</th>
										<th id="title-column">Tytuł</th>
										<th id="author-column">Autor</th>	
										<th id="action-column">Akcja</th>
									</tr>		
								
									<c:forEach var="tempBook" items="${bookList}">
										
										<c:set var="isAvailable" value="${tempBook.isAvailable}"/>
										
										<c:url var="addBookLink" value="/borrow-book/addBookToList">					
												<c:param name="bookId" value="${tempBook.id}"/>					
										</c:url>
										
										<tr>
											<td id="id-column">${tempBook.id }</td>
											<td id="title-column">${tempBook.title }</td>
											<td id="author-column">${tempBook.author }</td>			
											<c:choose>
												<c:when test="${isAvailable}"> 
													<td id="action-column"><button class="small-button" onclick="window.location.href='${addBookLink}'">Dodaj</button></td>
												</c:when>
												<c:otherwise> 
													<td id="action-column">---</td> 
												</c:otherwise>
											</c:choose>		
										</tr>								
									</c:forEach>
								</table>					
							</c:when>
							<c:otherwise>
								<p>Nie można wybrać więcej książek</p>
							</c:otherwise>
						</c:choose>	
						
						
					</div>	
				</div>	
			</div>	
				
		</div>
	
</body>
</html>