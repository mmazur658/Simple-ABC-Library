<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title><spring:message code="view.return-book-choose-books.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="selectedUser" code="label.selected.user"/>
	<spring:message var="selectedBooks" code="label.selected.books"/>
	<spring:message var="userId" code="user.id"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="userLastName" code="user.last.name"/>
	<spring:message var="userEmail" code="user.email"/>
	<spring:message var="userPesel" code="user.pesel"/>
	<spring:message var="cancelButton" code="button.cancel"/>
	<spring:message var="returnBooksButton" code="button.return.book"/>
	<spring:message var="bookId" code="book.id"/>
	<spring:message var="bookTitle" code="book.title"/>
	<spring:message var="bookAuthor" code="book.author"/>
	<spring:message var="bookIsbn" code="book.isbn"/>
	<spring:message var="addAllButton" code="button.add.all"/>

	<div class="container  pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
		
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${selectedUser }</h1>			
	<table class="table table-hover">
		<thead>
		  	<tr>
		    	<th scope="col">${userId }</th>
		      	<th scope="col">${userFirstName }</th>
		      	<th scope="col">${userLastName }</th>
		      	<th scope="col">${userEmail }</th>
		      	<th scope="col">${userPesel }</th>
		    </tr>
		 </thead>
		 <tbody>
			<tr onclick="window.location.href='${pageContext.request.contextPath}/return-book/return-book-choose-user'">
		 		<td>${theUser.id}</td>
				<td>${theUser.firstName}</td>
				<td>${theUser.lastName}</td>
				<td>${theUser.email}</td>	
				<td>${theUser.pesel}</td>	
			</tr>
		 </tbody>
	</table>	

	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${selectedBooks }</h1>
	<form action="cancel-book-returning">
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 shadow"  type="submit">${cancelButton }</button>	
	</form>	
	<form action="return-book">	
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1 shadow"  type="submit">${returnBooksButton }</button>	
	</form>			
		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">${bookId }</th>
		      <th scope="col">${bookTitle }</th>
		      <th scope="col">${bookAuthor }</th>
		      <th scope="col">${bookIsbn }</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="tempBook" items="${tempReturnedBookList}">
		    
					<c:url var="deleteBookLink" value="/return-book/deleteReturnedBookFromList">					
							<c:param name="bookId" value="${tempBook.id}"/>					
					</c:url>
						
					<tr onclick="window.location.href='${deleteBookLink}'">
						<td>${tempBook.id }</td>
						<td>${tempBook.title }</td>
						<td>${tempBook.author }</td>
						<td>${tempBook.isbn }</td>
					</tr>			
			</c:forEach>
		  </tbody>
		</table>

	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${selectedBooks }</h1>	
	<a href="${pageContext.request.contextPath}/return-book/addAllBorrowedBookToList" class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1 shadow" role="button" aria-pressed="true" >${addAllButton }</a>
		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">${bookId }</th>
		      <th scope="col">${bookTitle }</th>
		      <th scope="col">${bookAuthor }</th>
		      <th scope="col">${bookIsbn }</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="tempBorrowedBook" items="${userBorrowedBooksList}">
		    
					<c:url var="addBookLink" value="/return-book/addReturnedBookToList">					
						<c:param name="bookId" value="${tempBorrowedBook.book.id }"/>					
					</c:url>
						
					<tr onclick="window.location.href='${addBookLink}'">
						<td>${tempBorrowedBook.book.id }</td>
						<td>${tempBorrowedBook.book.title }</td>
						<td>${tempBorrowedBook.book.author }</td>
						<td>${tempBorrowedBook.book.isbn }</td>
					</tr>			
			</c:forEach>
		  </tbody>
		</table>
	</div>
	
	<%@ include file="/resources/parts/footer.jsp" %> 
	
</body>
</html>