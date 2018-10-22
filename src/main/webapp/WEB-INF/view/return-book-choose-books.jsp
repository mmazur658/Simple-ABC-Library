<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title>Simple ABC Library - Wybór Książki</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  

	<div class="container  pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
		
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Wybrany Użytkownik</h1>			
	<table class="table table-hover">
		<thead>
		  	<tr>
		    	<th scope="col">Id</th>
		      	<th scope="col">Imię</th>
		      	<th scope="col">Nazwisko</th>
		      	<th scope="col">Email</th>
		      	<th scope="col">Pesel</th>
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

	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Wybrane Książki</h1>
	<form action="cancel-book-returning">
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 shadow"  type="submit">Anuluj Wydanie</button>	
	</form>	
	<form action="return-book">	
		<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1 shadow"  type="submit">Zwrot Książek</button>	
	</form>			
		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">ID</th>
		      <th scope="col">Tytuł</th>
		      <th scope="col">Author</th>
		      <th scope="col">ISBN</th>
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

	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Wybrane Książki</h1>	
	<a href="${pageContext.request.contextPath}/return-book/addAllBorrowedBookToList" class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1 shadow" role="button" aria-pressed="true" >Dodaj Wszystkie</a>
		<table class="table table-hover">
		  <thead>
		    <tr>
		      <th scope="col">ID</th>
		      <th scope="col">Tytuł</th>
		      <th scope="col">Author</th>
		      <th scope="col">ISBN</th>
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