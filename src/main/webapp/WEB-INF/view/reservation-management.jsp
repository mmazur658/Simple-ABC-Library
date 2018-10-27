<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title><spring:message code="view.reservation-management.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.reservation-management.heading"/>
	<spring:message var="findReservation" code="button.find.reservation"/>
	<spring:message var="clearSerPar" code="button.clear.ser.par"/>
	<spring:message var="userId" code="user.id"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="userLastName" code="user.last.name"/>
	<spring:message var="userPesel" code="user.pesel"/>
	<spring:message var="bookId" code="book.id"/>
	<spring:message var="bookTitle" code="book.title"/>
	<spring:message var="closeButton" code="button.close"/>
	<spring:message var="findButton" code="button.find.book"/>
	<spring:message var="endDate" code="book.end.date"/>
	<spring:message var="action" code="label.action"/>
	<spring:message var="smallDeleteButton" code="button.delete.small"/>
	
	<div class="container pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
	<h1 class="h3 mb-3 mt-3 font-weight-normal float-left">${heading}</h1>

		<button type="button" class="btn btn-sm btn-secondary float-right mt-4 btn-block w-25 shadow" data-toggle="modal" data-target="#searchReservationModal" data-whatever="">${findReservation }</button>
		 <form action ="clearReservationSearchParameters" >
		 	<button class="btn btn-sm btn-secondary float-right mt-4 mr-1 btn-block w-25 shadow" type="submit">${clearSerPar }</button>
		 </form>
		 <div class="modal fade" id="searchReservationModal" tabindex="-1" role="dialog" aria-labelledby="searchReservationModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="searchReservationModalLabel">${findReservation }</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <form action="reservation-management" method="post">
			          <div class="form-group">	
			          	<input class="form-control" type="text" placeholder="${userId }" name="customerId" value="<%=(session.getAttribute("customerId")==null) ? "" : session.getAttribute("customerId")%>">
						<input class="form-control" type="text" placeholder="${userFirstName}" name="customerFirstName" value="<%=(session.getAttribute("customerFirstName")==null) ? "" : session.getAttribute("customerFirstName")%>">
						<input class="form-control" type="text" placeholder="${userLastName}" name="customerLastName" value="<%=(session.getAttribute("customerLastName")==null) ? "" : session.getAttribute("customerLastName")%>">
						<input class="form-control" type="text" placeholder="${userPesel}" name="customerPesel" value="<%=(session.getAttribute("customerPesel")==null) ? "" : session.getAttribute("customerPesel")%>">
						<input class="form-control" type="text" placeholder="${bookId}" name="bookId" value="<%=(session.getAttribute("bookId")==null) ? "" : session.getAttribute("bookId")%>">
						<input class="form-control" type="text" placeholder="${bookTitle}" name="bookTitle" value="<%=(session.getAttribute("bookTitle")==null) ? "" : session.getAttribute("bookTitle")%>">			   
					 </div>
			         <button type="button" class="btn btn-secondary float-right shadow" data-dismiss="modal">${closeButton }</button>
			         <input class="btn btn-secondary float-right mr-2 shadow" type="submit" value="${findButton }">
			   		</form>  
			      </div>
			    </div>
			  </div>
		</div>
	
		<table class="table table-hover">
			<thead>
				<tr>
					<th scope="col">${userId }</th>
					<th scope="col">${userFirstName} ${userLastName}</th>
					<th scope="col">${userPesel}</th>
					<th scope="col">${bookId}</th>
					<th scope="col">${bookTitle}</th>
					<th scope="col">${endDate}</th>
					<th scope="col">${action }</th>
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
			 				<button class="btn btn-secondary btn-sm shadow" onclick="window.location.href='${deleteReservationLink}'">${smallDeleteButton}</button>
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
			    <li class="page-item"><p class="page-link text-dark" >${resultRange} - ${amountOfResults}</p></li>
			    <li class="page-item"><a class="page-link text-dark" href="${showMoreLink}"> >>> </a></li>
			  </ul>
		</nav>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>