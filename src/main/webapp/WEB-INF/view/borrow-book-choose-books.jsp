<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title><spring:message code="view.borrow-book-choose-books.title" /> </title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.borrow-book-choose-books.heading"/>
	<spring:message var="reservations" code="label.reservations"/>
	<spring:message var="reservationsList" code="label.reservations.list"/>
	<spring:message var="bookId" code="book.id"/>
	<spring:message var="bookTitle" code="book.title"/>
	<spring:message var="bookAuthor" code="book.author"/>
	<spring:message var="bookPublisher" code="book.publisher"/>
	<spring:message var="bookIsbn" code="book.isbn"/>
	<spring:message var="bookEndDate" code="book.end.date"/>
	<spring:message var="bookAvailability" code="label.availability"/>
	<spring:message var="closeButton" code="button.close"/>
	<spring:message var="borrowedBooks" code="button.borrowed.books"/>
	<spring:message var="userId" code="user.id"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="userLastName" code="user.last.name"/>
	<spring:message var="userEmail" code="user.email"/>
	<spring:message var="userPesel" code="user.pesel"/>
	<spring:message var="selectedBooks" code="label.selected.books"/>
	<spring:message var="cancelButton" code="button.cancel"/>
	<spring:message var="borrowButton" code="button.borrow"/>
	<spring:message var="available" code="label.available"/>
	<spring:message var="unavailable" code="label.unavailable"/>
	<spring:message var="bookstore" code="label.bookstore"/>
	<spring:message var="findBook" code="button.find.book"/>
	<spring:message var="clearSerPar" code="button.clear.ser.par"/>

	<div class="container pt-5 mt-4">	
	
	<c:if test="${not empty extraMessage}">
		<script>showToastrAlert("info","${extraMessage}");</script></c:if>
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	<c:if test="${not empty errorMessage}">
		<script>showToastrAlert("error","${errorMessage}");</script></c:if>

	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${heading }</h1>

		<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25 shadow" data-toggle="modal" data-target="#userReservationList" data-whatever="">${reservations } (${fn:length(userReservationList)})</button>
			<div class="modal fade" id="userReservationList" tabindex="-1" role="dialog" aria-labelledby="userReservationListLabel" aria-hidden="true">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="userReservationListLabel">${reservationsList }</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			      	<table class="table table-hover">
						<thead>
							<tr>
							    	<th scope="col">${bookId }</th>
							     	<th scope="col">${bookTitle }</th>
							     	<th scope="col">${ bookAuthor}</th>
							      	<th scope="col">${ bookIsbn}</th>
							     	<th scope="col"> ${ bookEndDate}</th>
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
			        <button type="button" class="btn btn-secondary float-right shadow" data-dismiss="modal">${closeButton }</button>
			      </div>
			    </div>
			  </div>
		</div>

		<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25 mr-1 shadow" data-toggle="modal" data-target="#userBorrowedBookList" data-whatever="">${borrowedBooks } (${fn:length(borrowedBookList)})</button>
			<div class="modal fade" id="userBorrowedBookList" tabindex="-1" role="dialog" aria-labelledby="userBorrowedBookListLabel" aria-hidden="true">
			  <div class="modal-dialog modal-lg" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="userBorrowedBookListLabel">${borrowedBooks } </h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
						<table class="table table-hover">
							<thead>
							  	<tr>
							    	<th scope="col">${bookId }</th>
							     	<th scope="col">${bookTitle }</th>
							     	<th scope="col">${ bookAuthor}</th>
							     	<th scope="col">${ bookIsbn}</th>
							     	<th scope="col"> ${ bookEndDate}</th>
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
					<button type="button" class="btn btn-secondary float-right shadow" data-dismiss="modal">${closeButton }</button>			   	
			      </div>
			    </div>
			  </div>
		</div>

	<table class="table table-hover">
		<thead>
		  	<tr>
		    	<th scope="col">${ userId}</th>
		      	<th scope="col">${userFirstName }</th>
		      	<th scope="col">${userLastName }</th>
		      	<th scope="col">${userEmail }</th>
		      	<th scope="col">${userPesel }</th>
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


	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${selectedBooks }</h1>
		
	<form action="cancel-borrowed-book">
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 shadow"  type="submit">${cancelButton }</button>	
	</form>
	
	<form action="borrow-books">
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1 shadow"  type="submit">${borrowButton }</button>	
	</form>


		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">${bookId }</th>
		      <th scope="col">${bookTitle }</th>
		      <th scope="col">${bookAuthor }</th>
		      <th scope="col">${bookIsbn }</th>
		      <th scope="col">${bookAvailability }</th>
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
								<c:when test="${isAvailable}"> <td>${bookIsbn }</td> </c:when>
								<c:otherwise> <td>${unavailable }</td > </c:otherwise>
							</c:choose>
						</tr>			
				</c:forEach>
		  </tbody>
		</table>

	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${bookstore}</h1>
	<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25 shadow" data-toggle="modal" data-target="#searchBookModal" data-whatever="">${findBook }</button>
		 <form action ="clearBookSearchParameters" >
		 	<button class="btn btn-sm btn-secondary float-right mt-4 mr-1 btn-block w-25 shadow" type="submit">${clearSerPar }</button>
		 </form>
		 <div class="modal fade" id="searchBookModal" tabindex="-1" role="dialog" aria-labelledby="searchBookModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="searchBookModalLabel">${findBook }</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <form action="borrow-book-choose-books" method="post">
			          <div class="form-group">
			        	<input class="form-control" type="text" placeholder="${bookId }" name ="bookId" value = "<%=(session.getAttribute("borrowBookSeachParamId")==null) ? "" : session.getAttribute("borrowBookSeachParamId")%>"/>
						<input class="form-control" type="text" placeholder="${ bookTitle}" name="title" value = "<%=(session.getAttribute("borrowBookSeachParamTitle")==null) ? "" : session.getAttribute("borrowBookSeachParamTitle")%>"/>
						<input class="form-control" type="text" placeholder="${ bookAuthor}" name="author" value = "<%=(session.getAttribute("borrowBookSeachParamAuthor")==null) ? "" : session.getAttribute("borrowBookSeachParamAuthor") %>"/>
						<input class="form-control" type="text" placeholder="${bookPublisher }"  name="publisher" value = "<%=(session.getAttribute("borrowBookSeachParamPublisher")== null) ? "" : session.getAttribute("borrowBookSeachParamPublisher")%>"/>
						<input class="form-control" type="text" placeholder="${bookIsbn }"  name="isbn" value = "<%=(session.getAttribute("borrowBookSeachParamIsbn")==null) ? "" : session.getAttribute("borrowBookSeachParamIsbn")%>"/>
					 </div>
			         <button type="button" class="btn btn-secondary float-right shadow" data-dismiss="modal">${closeButton }</button>
			         <input class="btn btn-secondary float-right mr-2 shadow" type="submit" value="Szukaj">
			   		</form>  
			      </div>
			    </div>
			  </div>
		</div>
		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">${bookId}</th>
		      <th scope="col">${bookTitle }</th>
		      <th scope="col">${bookAuthor}</th>
		      <th scope="col">${bookIsbn }</th>
		      <th scope="col">${bookAvailability }</th>
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
									<td>${available }</td>
								</tr>
							</c:when>
							<c:otherwise> 
								<tr>
									<td>${tempBook.id }</td>
									<td>${tempBook.title }</td>
									<td>${tempBook.author }</td>
									<td>${tempBook.isbn }</td>
									<td>${unavailable }</td>
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
			    <li class="page-item"><p class="page-link text-dark" >${resultRange} - ${amountOfResults}</p></li>
			    <li class="page-item"><a class="page-link text-dark" href="${showMoreLink}"> >>> </a></li>
			  </ul>
		</nav>
	</div>	
	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>