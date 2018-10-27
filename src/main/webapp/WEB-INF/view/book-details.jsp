<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title><spring:message code="view.add-book-form.title" /></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
		<spring:message var="heading" code="view.book-details.heading" />
		<spring:message var="id" code="book.id" />
		<spring:message var="title" code="book.title" />
		<spring:message var="author" code="book.author" />
		<spring:message var="language" code="book.language" />
		<spring:message var="publisher" code="book.publisher" />
		<spring:message var="pages" code="book.pages" />
		<spring:message var="isbn" code="book.isbn" />
		<spring:message var="availability" code="label.availability" />
		<spring:message var="available" code="label.available" />
		<spring:message var="unavailable" code="label.unavailable" />
		<spring:message var="reserveButton" code="button.reserve" />
		<spring:message var="updateButton" code="button.update" />
		<spring:message var="deleteButton" code="button.delete" />
		<spring:message var="prinLabelButton" code="button.label" />

	<div class="container w-50 pt-5 mt-4">	
	
		<c:if test="${not empty systemSuccessMessage}">
			<script>showToastrAlert("success","${systemSuccessMessage}");</script></c:if>
		<c:if test="${not empty systemErrorMessage}">
			<script>showToastrAlert("error","${systemErrorMessage}");</script></c:if>	
	
		<table class="table table-borderedless mb-0" style="text-align: center;">
			<tr>
			<td><h1 class="h3 mb-0 font-weight-normal ">${heading }</h1></td>
			</tr>
		</table>
		<table class="table table-sm table-bordered">
		  <tbody>
		    <tr>
		      <th scope="row">${id }</th>
		      <td>${tempBook.id}</td>	
		    </tr>
		    <tr>
		      <th scope="row">${title } </th>
		      <td>${tempBook.title}</td>	
		    </tr>
		    <tr>
		      <th scope="row">${author } </th>
		      <td>${tempBook.author}</td>	
		    </tr>
		    <tr>
		      <th scope="row">${language }</th>
		      <td>${tempBook.language}</td>	
		    </tr>		   
		    <tr>
		      <th scope="row">${publisher}</th>
		      <td>${tempBook.publisher}</td>	
		    </tr>		   
		    <tr>
		      <th scope="row">${pages } </th>
		      <td>${tempBook.pages}</td>	
		    </tr>	
		    <tr>
		      <th scope="row">${isbn } </th>
		      <td>${tempBook.isbn}</td>	
		    </tr>			    		   
		    <tr>
		      <th scope="row">${availability }</th>
		      <c:choose>
					<c:when test="${tempBook.isAvailable}"><td>${available }</td></c:when>
					<c:otherwise> <td>${unavailable }</td> </c:otherwise>
			</c:choose>		
		    </tr>			    
		  </tbody>
		</table>	
		
		<c:url var="reservationLink" value="/book/reservation">					
			<c:param name="bookId" value="${tempBook.id}"/>					
		</c:url>
		<c:url var="updateLink" value="/book/updateBook">					
			<c:param name="bookId" value="${tempBook.id}"/>					
		</c:url>							
		<c:url var="deleteLink" value="/book/deleteBook">					
			<c:param name="bookId" value="${tempBook.id}"/>					
		</c:url>		
		<c:url var="generateLabelLink" value="/book/generate-book-label">					
			<c:param name="bookId" value="${tempBook.id}"/>					
		</c:url>
		
		<a href="${reservationLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${ reserveButton}</a>
		<c:if test="${userAccessLevel eq 'Employee' or  userAccessLevel eq 'Administrator' }">	
			<a href="${updateLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${ updateButton}</a>
			<a href="${deleteLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${ deleteButton}</a>
			<a href="${generateLabelLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${prinLabelButton }</a>
		</c:if>
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
	
</body>
</html>