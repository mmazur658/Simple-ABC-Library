<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Wybór Książki</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container">	
	
	<c:if test="${not empty extraMessage }">
			<div class="alert alert-primary mt-1" role="alert">
				<strong>${extraMessage}</strong>
			</div>
	</c:if>
	
	<c:if test="${not empty systemMessage}">
			<div class="alert alert-primary mt-1" role="alert">
				<strong>${systemMessage} </strong>
			</div>
	</c:if>
	
	<c:if test="${not empty errorMessage}">
			<div class="alert alert-danger mt-1" role="alert">
				<strong>${errorMessage}</strong>
			</div>
	</c:if>
	
<!-- Tablica z wybranym użytkwonikiem - po kliknięciu powórt do wyboru użytkwonika -->
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Wybrany Użytkownik</h1>
<!-- Przycisk, gdzie wchodzisz w rezerwację, gdzie możesz wybrać książkę do dodania -->
		<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25" data-toggle="modal" data-target="#userReservationList" data-whatever="">Rezerwacje (${fn:length(userReservationList)})</button>
			<div class="modal fade" id="userReservationList" tabindex="-1" role="dialog" aria-labelledby="userReservationListLabel" aria-hidden="true">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="userReservationListLabel">Lista Rezerwacji</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			      	<table class="table table-hover">
						<thead>
							<tr>
							    <th scope="col">Id</th>
							     <th scope="col">Tytuł</th>
							     <th scope="col">Author</th>
							      <th scope="col">ISBN</th>
							     <th scope="col">Data Końca</th>
							  </tr>
						</thead>
						<tbody>
						<c:forEach var="tempReservation" items="${userReservationList}">
						
							<c:url var="addReservedBookLink" value="/borrow-book/addReservedBookToList">					
								<c:param name="reservationId" value="${tempReservation.id}"/>	
								<c:param name="isAbleToBorrow" value="${isAbleToBorrow }"/>		
							</c:url>			
											
							<tr onclick="window.location.href='${addReservedBookLink}'">
								<td>${tempReservation.book.id }</td>
								<td>${tempReservation.book.title }</td>
								<td>${tempReservation.book.author }</td>
								<td>${tempReservation.book.isbn }</td>
								<td><fmt:formatDate value="${tempReservation.endDate }" pattern="dd-MM-yyyy"/></td>
							</tr>					
						</c:forEach>	
					</table>
			        <button type="button" class="btn btn-secondary float-right" data-dismiss="modal">Zamknij</button>
			      </div>
			    </div>
			  </div>
		</div>
<!-- Przycisk, gdzie wchodzisz w wypożyczone książki -->
		<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25 mr-1" data-toggle="modal" data-target="#userBorrowedBookList" data-whatever="">Wypożyczone Książki  (${fn:length(borrowedBookList)})</button>
			<div class="modal fade" id="userBorrowedBookList" tabindex="-1" role="dialog" aria-labelledby="userBorrowedBookListLabel" aria-hidden="true">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="userBorrowedBookListLabel">Wypożyczone Książki </h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
						<table class="table table-hover">
							<thead>
							  	<tr>
							    	<th scope="col">Id</th>
							      	<th scope="col">Tytuł</th>
							      	<th scope="col">Author</th>
							      	<th scope="col">ISBN</th>
							      	<th scope="col">Data Zwrotu</th>
							    </tr>
							 </thead>
							 <tbody>
							  <c:forEach var="tempBorrowed" items="${borrowedBookList}">							
								<tr>
									<td>${tempBorrowed.book.id }</td>
									<td>${tempBorrowed.book.title }</td>
									<td>${tempBorrowed.book.author }</td>
									<td>${tempBorrowed.book.isbn }</td>
									<td><fmt:formatDate value="${tempBorrowed.expectedEndDate }" pattern="dd-MM-yyyy"/></td>
								</tr>					
							  </c:forEach>
							</tbody>	
						</table>
					<button type="button" class="btn btn-secondary float-right" data-dismiss="modal">Zamknij</button>			   	
			      </div>
			    </div>
			  </div>
		</div>

	<table class="table table-hover">
		<thead>
		  	<tr>
		    	<th scope="col">Id</th>
		      	<th scope="col">Imię</th>
		      	<th scope="col">Nazwisko</th>
		      	<th scope="col">Email</th>
		      	<th scope="col">Pesel</th>
		    </tr>
		 </thead>
		 <tbody>
			<tr onclick="window.location.href='${pageContext.request.contextPath}/borrow-book/borrow-book-choose-user'">
		 		<td>${theUser.id}</td>
				<td>${theUser.firstName}</td>
				<td>${theUser.lastName}</td>
				<td>${theUser.email}</td>	
				<td>${theUser.pesel}</td>	
			</tr>
		 </tbody>
	</table>

<!--  tablica z wybranymi książkami, po kliknięciu usuwa -->	
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Wybrane Książki</h1>
		
	<form action="cancel-borrowed-book">
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4"  type="submit">Anuluj Wydanie</button>	
	</form>
	
	<form action="borrow-books">
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1"  type="submit">Wydaj Książki</button>	
	</form>


		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">ID</th>
		      <th scope="col">Tytuł</th>
		      <th scope="col">Author</th>
		      <th scope="col">ISBN</th>
		      <th scope="col">Dostępność</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="tempBook" items="${tempBookList}">
		    
						<c:url var="deleteBookLink" value="/borrow-book/deleteBookFromList">					
							<c:param name="bookId" value="${tempBook.id}"/>					
						</c:url>
						
						<c:set var="isAvailable" value="${tempBook.isAvailable}"/>
						
						<tr onclick="window.location.href='${deleteBookLink}'">
							<td>${tempBook.id }</td>
							<td>${tempBook.title }</td>
							<td>${tempBook.author }</td>
							<td>${tempBook.isbn }</td>
							<c:choose>
								<c:when test="${isAvailable}"> <td>Dostępna</td> </c:when>
								<c:otherwise> <td>Niedostępna</td > </c:otherwise>
							</c:choose>
						</tr>			
				</c:forEach>
		  </tbody>
		</table>
	<!--  tablica z książkami do wyboru, po kliknięciu dodaje -->
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Księgozbiór</h1>
	<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25" data-toggle="modal" data-target="#searchBookModal" data-whatever="">Znajdź Książkę</button>
		 <form action ="clearBookSearchParameters" >
		 	<button class="btn btn-sm btn-secondary float-right mt-4 mr-1 btn-block w-25" type="submit">Wyczyść Dane Szukania</button>
		 </form>
		 <div class="modal fade" id="searchBookModal" tabindex="-1" role="dialog" aria-labelledby="searchBookModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="searchBookModalLabel">Znajdź Książkę</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <form action="borrow-book-choose-books" method="post">
			          <div class="form-group">
			        	<input class="form-control" type="text" placeholder="ID" name ="bookId" value = "<%=(session.getAttribute("borrowBookSeachParamId")==null) ? "" : session.getAttribute("borrowBookSeachParamId")%>"/>
						<input class="form-control" type="text" placeholder="Tytuł" name="title" value = "<%=(session.getAttribute("borrowBookSeachParamTitle")==null) ? "" : session.getAttribute("borrowBookSeachParamTitle")%>"/>
						<input class="form-control" type="text" placeholder="Author" name="author" value = "<%=(session.getAttribute("borrowBookSeachParamAuthor")==null) ? "" : session.getAttribute("borrowBookSeachParamAuthor") %>"/>
						<input class="form-control" type="text" placeholder="Wydawca"  name="publisher" value = "<%=(session.getAttribute("borrowBookSeachParamPublisher")== null) ? "" : session.getAttribute("borrowBookSeachParamPublisher")%>"/>
						<input class="form-control" type="text" placeholder="ISBN"  name="isbn" value = "<%=(session.getAttribute("borrowBookSeachParamIsbn")==null) ? "" : session.getAttribute("borrowBookSeachParamIsbn")%>"/>
					 </div>
			         <button type="button" class="btn btn-secondary float-right" data-dismiss="modal">Zamknij</button>
			         <input class="btn btn-secondary float-right mr-2" type="submit" value="Szukaj">
			   		</form>  
			      </div>
			    </div>
			  </div>
		</div>
		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">ID</th>
		      <th scope="col">Tytuł</th>
		      <th scope="col">Author</th>
		      <th scope="col">ISBN</th>
		      <th scope="col">Dostępność</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="tempBook" items="${bookList}">
		    
						<c:url var="addBookLink" value="/borrow-book/addBookToList">					
							<c:param name="bookId" value="${tempBook.id}"/>			
							<c:param name="isAbleToBorrow" value="${isAbleToBorrow}"/>		
						</c:url>
						
						<c:set var="isAvailable" value="${tempBook.isAvailable}"/>
						
						
						<c:choose>
							<c:when test="${isAvailable}"> 
								<tr onclick="window.location.href='${addBookLink}'">
									<td>${tempBook.id }</td>
									<td>${tempBook.title }</td>
									<td>${tempBook.author }</td>
									<td>${tempBook.isbn }</td>
									<td>Dostępna</td>
								</tr>
							</c:when>
							<c:otherwise> 
								<tr>
									<td>${tempBook.id }</td>
									<td>${tempBook.title }</td>
									<td>${tempBook.author }</td>
									<td>${tempBook.isbn }</td>
									<td>Niedostępna</td>
								</tr>						
							</c:otherwise>
						</c:choose>		
				</c:forEach>
		  </tbody>
		</table>	
		<nav aria-label="Page navigation example">
		
			<c:url var="showMoreLink" value="/borrow-book/borrow-book-choose-books">					
				<c:param name="borrowBookChooseBookStartResult" value="${showMoreLinkValue}"/>					
			</c:url>
			
			<c:url var="showLessLink" value="/borrow-book/borrow-book-choose-books">					
				<c:param name="borrowBookChooseBookStartResult" value="${showLessLinkValue}"/>					
			</c:url>
			
			  <ul class="pagination justify-content-end">
			    <li class="page-item"><a class="page-link text-dark" href="${showLessLink}"> <<< </a></li>
			    <li class="page-item"><p class="page-link text-dark" >${resultRange} z ${amountOfResults}</p></li>
			    <li class="page-item"><a class="page-link text-dark" href="${showMoreLink}"> >>> </a></li>
			  </ul>
		</nav>
	</div>	
	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>

<!--  

					

						
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

			
 -->