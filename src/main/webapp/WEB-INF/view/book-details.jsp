<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Szczegóły Książki</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container">	
	
		<c:if test="${not empty systemSuccessMessage}">
			<div class="alert alert-success mt-2 w-50" role="alert">
		    	<strong>${systemSuccessMessage}</strong>
		  	</div>
		</c:if>	
		<c:if test="${not empty systemErrorMessage}">
			<div class="alert alert-danger mt-2 w-50" role="alert">
		    	<strong>${systemErrorMessage}</strong>
		  	</div>
		</c:if>	
	
		<table class="table table-borderedless w-50 mb-0" style="text-align: center;">
			<tr>
			<td><h1 class="h3 mb-0 font-weight-normal ">Szczegóły Książki</h1></td>
			</tr>
		</table>
		<table class="table table-bordered w-50">
		  <tbody>
		    <tr>
		      <th scope="row">Id: </th>
		      <td>${tempBook.id}</td>	
		    </tr>
		    <tr>
		      <th scope="row">Tytuł: </th>
		      <td>${tempBook.title}</td>	
		    </tr>
		    <tr>
		      <th scope="row">Autor: </th>
		      <td>${tempBook.author}</td>	
		    </tr>
		    <tr>
		      <th scope="row">Język: </th>
		      <td>${tempBook.language}</td>	
		    </tr>		   
		    <tr>
		      <th scope="row">Wydawca: </th>
		      <td>${tempBook.publisher}</td>	
		    </tr>		   
		    <tr>
		      <th scope="row">Strony: </th>
		      <td>${tempBook.pages}</td>	
		    </tr>	
		    <tr>
		      <th scope="row">ISBN: </th>
		      <td>${tempBook.isbn}</td>	
		    </tr>			    		   
		    <tr>
		      <th scope="row">Dostępność: </th>
		      <c:choose>
					<c:when test="${tempBook.isAvailable}"><td>Dostępna</td></c:when>
					<c:otherwise> <td>Niedostępna</td> </c:otherwise>
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
		
		<a href="${reservationLink}" class="btn btn-sm btn-secondary btn-block w-50" role="button" aria-pressed="true" >Rezerwuj</a>
		<c:if test="${userAccessLevel eq 'Employee' or  userAccessLevel eq 'Administrator' }">	
			<a href="${updateLink}" class="btn btn-sm btn-secondary btn-block w-50" role="button" aria-pressed="true" >Aktualizacja</a>
			<a href="${deleteLink}" class="btn btn-sm btn-secondary btn-block w-50" role="button" aria-pressed="true" >Usuń</a>
			<a href="${generateLabelLink}" class="btn btn-sm btn-secondary btn-block w-50" role="button" aria-pressed="true" >Etykieta</a>
		</c:if>
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>