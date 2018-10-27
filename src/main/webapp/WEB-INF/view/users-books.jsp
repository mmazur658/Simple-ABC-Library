<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title><spring:message code="view.users-books.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading1" code="view.users-books.heading1"/>
	<spring:message var="heading2" code="view.users-books.heading2"/>
	<spring:message var="bookId" code="book.id"/>
	<spring:message var="bookTitle" code="book.title"/>
	<spring:message var="bookAuthor" code="book.author"/>
	<spring:message var="bookEndDate" code="book.end.date"/>
	<spring:message var="bookReturnDate" code="book.return.date"/>
	<spring:message var="bookAction" code="label.action"/>
	<spring:message var="smallDeleteButton" code="button.delete.small"/>

	<div class="container  pt-5 mt-4">

	<c:if test="${not empty systemSuccessMessage}">
		<script>showToastrAlert("success","${systemSuccessMessage}");</script></c:if>
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
		<h1 class="h3 mb-3 mt-3 font-weight-normal">${heading1 }</h1>
		<table class="table table-hover">
			  <thead>
			    <tr>
			      <th scope="col">${bookId}</th>
			      <th scope="col">${bookTitle }</th>
			      <th scope="col">${bookAuthor}</th>
			      <th scope="col">${bookEndDate }</th>
			      <th scope="col">${bookAction}</th>
			    </tr>
			  </thead>
			  <tbody>			  
			  <c:forEach var="tempReservation" items="${reservationList}">			  
			  		<c:url var="deleteReservationLink" value="/book/deleteReservation">					
						<c:param name="reservationId" value="${tempReservation.id}"/>
						<c:param name="deleteReservationWayBack" value="users-books"/> 		
					</c:url>
			  		<tr>
			  			<td>${tempReservation.book.id}</td>
						<td>${tempReservation.book.title}</td>
						<td>${tempReservation.book.author }</td>
						<td><fmt:formatDate value="${tempReservation.endDate }" pattern="hh:mm dd-MM"/></td>
						<td><button class="btn btn-secondary btn-sm shadow" onclick="window.location.href='${deleteReservationLink}'">${smallDeleteButton }</button></td>
					</tr>			  
			  </c:forEach>			  
			</tbody>
		</table>
		
		<h1 class="h3 mb-3 mt-3 font-weight-normal">${heading2 }</h1>
		<table class="table table-hover">
			  <thead>
			    <tr>
			      <th scope="col">${bookId }</th>
			      <th scope="col">${bookTitle }</th>
			      <th scope="col">${bookAuthor}</th>
			      <th scope="col">${bookReturnDate }</th>
			    </tr>
			  </thead>
			  <tbody>			  
			 	<c:forEach var="tempBorrowedBook" items="${borrowedBookList}">
					<tr>
						<td>${tempBorrowedBook.book.id}</td>
						<td>${tempBorrowedBook.book.title}</td>
						<td>${tempBorrowedBook.book.author }</td>
						<td><fmt:formatDate value="${tempBorrowedBook.expectedEndDate}" pattern="hh:mm dd-MM"/></td>		
					</tr>
				</c:forEach>	
			</tbody>
		</table>		
	</div>
	
<%@ include file="/resources/parts/footer.jsp" %> 

</body>
</html>