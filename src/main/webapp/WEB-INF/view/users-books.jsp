<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title>Simple ABC Library - Zmiana Hasła</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  

	<div class="container  pt-5 mt-4">
	

	<c:if test="${not empty systemSuccessMessage}">
		<script>showToastrAlert("success","${systemSuccessMessage}");</script></c:if>
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
		<h1 class="h3 mb-3 mt-3 font-weight-normal">Zarezerwowane Książki</h1>
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
						<td><button class="btn btn-secondary btn-sm shadow" onclick="window.location.href='${deleteReservationLink}'">Usuń</button></td>
					</tr>			  
			  </c:forEach>			  
			</tbody>
		</table>
		
		<h1 class="h3 mb-3 mt-3 font-weight-normal">Wypożyczone Książki</h1>
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