<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title>Simple ABC Library - Wybór Użytkownika</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("success","${systemMessage}");</script></c:if>

	
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Użytkownicy</h1>
	<button type="button" class="btn btn-sm btn-secondary float-right mt-4 shadow" data-toggle="modal" data-target="#userSearchModal" data-whatever="">Znajdź Użytkownika</button>
	<form action ="clearUserSearchParameters" >
		 <button class="btn btn-sm btn-secondary float-right mt-4 mr-1 shadow" type="submit">Wyczyść Dane Szukania</button>
	</form>
	<div class="modal fade" id="userSearchModal" tabindex="-1" role="dialog" aria-labelledby="userSearchModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="userSearchModalLabel">Znajdź Użytkownika</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			      <form action="borrow-book-choose-user">	
						<div class="form-group">
							<input class="form-control" placeholder="ID" type="text" name="borrowBookSelectedUserId" value = "<%=(session.getAttribute("borrowBookSelectedUserId")==null) ? "" : session.getAttribute("borrowBookSelectedUserId")%>">
				 			<input class="form-control" placeholder="Imię" type="text" name="borrowBookFirstName" value = "<%=(session.getAttribute("borrowBookFirstName")==null) ? "" : session.getAttribute("borrowBookFirstName")%>">			
							<input class="form-control" placeholder="Nazwisko" type="text" name="borrowBookLastName" value = "<%=(session.getAttribute("borrowBookLastName")==null) ? "" : session.getAttribute("borrowBookLastName")%>">	
							<input class="form-control" placeholder="Email" type="text" name="borrowBookEmail" value = "<%=(session.getAttribute("borrowBookEmail")==null) ? "" : session.getAttribute("borrowBookEmail")%>">		
							<input class="form-control" placeholder="PESEL" type="text" name="borrowBookPesel" value = "<%=(session.getAttribute("borrowBookPesel")==null) ? "" : session.getAttribute("borrowBookPesel")%>">
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
		      <th scope="col">Id</th>
		      <th scope="col">Imię</th>
		      <th scope="col">Nazwisko</th>
		      <th scope="col">Email</th>
		      <th scope="col">Pesel</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="tempUser" items="${usersList}">
					
					<c:url var="addUserLink" value="/borrow-book/borrow-book-choose-books">					
						<c:param name="selectedUserId" value="${tempUser.id}"/>					
					</c:url>
					
					<tr onclick="window.location.href='${addUserLink}'">
						<td>${tempUser.id}</td>
						<td>${tempUser.firstName}</td>
						<td>${tempUser.lastName}</td>
						<td>${tempUser.email}</td>	
						<td>${tempUser.pesel}</td>	
					</tr>
			</c:forEach>
		  </tbody>
		</table>
		
		<nav aria-label="Page navigation example">
			
			<c:url var="showMoreLink" value="/borrow-book/borrow-book-choose-user">					
				<c:param name="borrowBookStartResult" value="${showMoreLinkValue}"/>					
			</c:url>
						
			<c:url var="showLessLink" value="/borrow-book/borrow-book-choose-user">					
				<c:param name="borrowBookStartResult" value="${showLessLinkValue}"/>					
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