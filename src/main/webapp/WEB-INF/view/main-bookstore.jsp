<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<%@ include file="/resources/parts/header.jsp" %>  
	<title><spring:message code="view.main-bookstore.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.main-bookstore.heading"/>
	<spring:message var="findBook" code="button.find.book"/>
	<spring:message var="clearSearchParameters" code="button.clear.ser.par"/>
	<spring:message var="bookIsbn" code="book.isbn"/>
	<spring:message var="bookPublisher" code="book.publisher"/>
	<spring:message var="bookAuthor" code="book.author"/>
	<spring:message var="bookTitle" code="book.title"/>
	<spring:message var="bookId" code="book.id"/>
	<spring:message var="closeButton" code="button.close"/>
	<spring:message var="availability" code="label.availability"/>
	<spring:message var="available" code="label.available"/>
	<spring:message var="unavailable" code="label.unavailable"/>
	
	<div class="container pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
		<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${heading}</h1>
		 <button type="button" class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 shadow" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">${findBook}</button>
		 <form action ="clearSearchParameters" >
		 	<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1 shadow" type="submit">${clearSearchParameters}</button>
		 </form>
		 <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="exampleModalLabel">${findBook}</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <form action="main-bookstore">
			          <div class="form-group">
			          <input class="form-control" placeholder="${bookId}" type="text" name ="id" value = "<%=(session.getAttribute("bookSeachParamId")==null) ? "" : session.getAttribute("bookSeachParamId")%>"/>
						<input class="form-control" placeholder="${bookTitle}" type="text" name="title" value = "<%=(session.getAttribute("bookSeachParamTitle")==null) ? "" : session.getAttribute("bookSeachParamTitle")%>"/>
						<input class="form-control" placeholder="${bookAuthor}" type="text" name="author" value = "<%=(session.getAttribute("bookSeachParamAuthor")==null) ? "" : session.getAttribute("bookSeachParamAuthor") %>"/>
						<input class="form-control" placeholder="${bookPublisher}" type="text" name="publisher" value = "<%=(session.getAttribute("bookSeachParamPublisher")== null) ? "" : session.getAttribute("bookSeachParamPublisher")%>"/>
						<input class="form-control" placeholder="${bookIsbn}" type="text" name="isbn" value = "<%=(session.getAttribute("bookSeachParamIsbn")==null) ? "" : session.getAttribute("bookSeachParamIsbn")%>"/>	
					  </div>
			         <button type="button" class="btn btn-secondary float-right shadow" data-dismiss="modal">${closeButton}</button>
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
		      <th scope="col">${bookTitle}</th>
		      <th scope="col">${bookAuthor}</th>
		      <th scope="col">${availability}</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="tempBook" items="${booksList}">
						
						<c:set var="isAvailable" value="${tempBook.isAvailable}"/>
						
						<c:url var="bookDetailsLink" value="/book/book-details">					
								<c:param name="bookId" value="${tempBook.id}"/>					
						</c:url>
						
						<tr onclick="window.location.href='${bookDetailsLink}'">
							<td>${tempBook.id}</td>
							<td>${tempBook.title}</td>
							<td>${tempBook.author}</td>
							<c:choose>
								<c:when test="${isAvailable}"> <td>${available}</td> </c:when>
								<c:otherwise> <td>${unavailable}</td > </c:otherwise>
							</c:choose>
							
						</tr>			
					
			</c:forEach>

		  </tbody>
		</table>
		
		<nav aria-label="Page navigation example">
			<c:url var="showMoreLink" value="/book/main-bookstore">					
				<c:param name="startResult" value="${showMoreLinkValue}"/>					
			</c:url>
			
			<c:url var="showLessLink" value="/book/main-bookstore">					
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