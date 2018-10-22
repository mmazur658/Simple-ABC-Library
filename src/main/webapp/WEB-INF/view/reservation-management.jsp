<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title>Simple ABC Library - Zarządzanie Rezerwacjami</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
	<h1 class="h3 mb-3 mt-3 font-weight-normal float-left">Rezerwacje</h1>

		<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25 shadow" data-toggle="modal" data-target="#searchReservationModal" data-whatever="">Znajdź Rezerwację</button>
		 <form action ="clearReservationSearchParameters" >
		 	<button class="btn btn-sm btn-secondary float-right mt-4 mr-1 btn-block w-25 shadow" type="submit">Wyczyść Dane Szukania</button>
		 </form>
		 <div class="modal fade" id="searchReservationModal" tabindex="-1" role="dialog" aria-labelledby="searchReservationModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="searchReservationModalLabel">Znajdź Rezerwację</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <form action="reservation-management" method="post">
			          <div class="form-group">	
			          	<input class="form-control" type="text" placeholder="ID Użytkownika" name="customerId" value="<%=(session.getAttribute("customerId")==null) ? "" : session.getAttribute("customerId")%>">
						<input class="form-control" type="text" placeholder="Imię Użytkownika" name="customerFirstName" value="<%=(session.getAttribute("customerFirstName")==null) ? "" : session.getAttribute("customerFirstName")%>">
						<input class="form-control" type="text" placeholder="Nazwisko Użytkownika" name="customerLastName" value="<%=(session.getAttribute("customerLastName")==null) ? "" : session.getAttribute("customerLastName")%>">
						<input class="form-control" type="text" placeholder="PESEL Użytkwonika" name="customerPesel" value="<%=(session.getAttribute("customerPesel")==null) ? "" : session.getAttribute("customerPesel")%>">
						<input class="form-control" type="text" placeholder="Id Książki" name="bookId" value="<%=(session.getAttribute("bookId")==null) ? "" : session.getAttribute("bookId")%>">
						<input class="form-control" type="text" placeholder="Tytuł Książki" name="bookTitle" value="<%=(session.getAttribute("bookTitle")==null) ? "" : session.getAttribute("bookTitle")%>">			   
					 </div>
			         <button type="button" class="btn btn-secondary float-right shadow" data-dismiss="modal">Zamknij</button>
			         <input class="btn btn-secondary float-right mr-2 shadow" type="submit" value="Szukaj">
			   		</form>  
			      </div>
			    </div>
			  </div>
		</div>
	
		<table class="table table-hover">
			<thead>
				<tr>
					<th scope="col">Id Użyt.</th>
					<th scope="col">Imię i Nazwisko</th>
					<th scope="col">Pesel</th>
					<th scope="col">Id Ksi.</th>
					<th scope="col">Tytuł</th>
					<th scope="col">Data Końca</th>
					<th scope="col">Akcja</th>
				</tr>
			 </thead>
			 <tbody>
			 
			 	<c:forEach var="tempReservation" items="${reservationList}">
							
					<c:url var="deleteReservationLink" value="/reservation/delete-reservation">					
						<c:param name="reservationId" value="${tempReservation.id}"/>					
					</c:url>
				
					<c:url var="increaseExpDateLink" value="/reservation/increase-exp-date">					
						<c:param name="reservationId" value="${tempReservation.id}"/>					
					</c:url>
			 
			 		<tr>
			 			<td>${tempReservation.user.id}</td>
			 			<td>${tempReservation.user.firstName} ${tempReservation.user.lastName}</td>
			 			<td>${tempReservation.user.pesel}</td>
			 			<td>${tempReservation.book.id }</td>
			 			<td>${tempReservation.book.title }</td>
			 			<td><fmt:formatDate value="${tempReservation.endDate }" pattern="HH:mm dd-MM"/>	</td>
			 			<td>
			 				<button class="btn btn-secondary btn-sm shadow" onclick="window.location.href='${increaseExpDateLink}'">+24h</button>
			 				<button class="btn btn-secondary btn-sm shadow" onclick="window.location.href='${deleteReservationLink}'">Usuń</button>
			 			</td>
			 		</tr>			 
				 </c:forEach>
			</tbody> 
		</table>
		<nav aria-label="Page navigation example">
		
			<c:url var="showMoreLink" value="/reservation/reservation-management">					
				<c:param name="startResult" value="${showMoreLinkValue}"/>					
			</c:url>
			
			<c:url var="showLessLink" value="/reservation/reservation-management">					
				<c:param name="startResult" value="${showLessLinkValue}"/>					
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