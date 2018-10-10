<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Zmiana Hasła</title>
</head>
<body>

	<%@ include file="/resources/parts/nav.jsp" %>  

	<div class="container">
	
		<c:if test="${not empty systemSuccessMessage}">
			<div class="alert alert-success mt-2 w-50" role="alert">
		    	<strong>${systemSuccessMessage}</strong>
		  	</div>
		</c:if>		
	
		<h1 class="jumbotron-heading">Zarezerwowane Książki</h1>
		<table class="table table-hover">
			  <thead>
			    <tr>
			      <th scope="col">ID</th>
			      <th scope="col">Tytuł</th>
			      <th scope="col">Author</th>
			      <th scope="col">Data Końca</th>
			      <th scope="col">Akcja</th>
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
						<td><button class="btn btn-secondary btn-sm" onclick="window.location.href='${deleteReservationLink}'">Usuń</button></td>
					</tr>			  
			  </c:forEach>			  
			</tbody>
		</table>
		
		<h1 class="jumbotron-heading">Wypożyczone Książki</h1>
		<table class="table table-hover">
			  <thead>
			    <tr>
			      <th scope="col">ID</th>
			      <th scope="col">Tytuł</th>
			      <th scope="col">Author</th>
			      <th scope="col">Data Zwrotu</th>
			    </tr>
			  </thead>
			  <tbody>			  
			 	<c:forEach var="tempBookBorrowing" items="${bookBorrowingList}">
					<tr>
						<td>${tempBookBorrowing.book.id}</td>
						<td>${tempBookBorrowing.book.title}</td>
						<td>${tempBookBorrowing.book.author }</td>
						<td><fmt:formatDate value="${tempBookBorrowing.expectedEndDate}" pattern="hh:mm dd-MM"/></td>		
					</tr>
				</c:forEach>	
			</tbody>
		</table>		
	</div>
	
	
<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>