<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Wybór Użytkownika</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container">	
	
	<c:if test="${not empty systemMessage}">
		<div class="alert alert-success" role="alert">
	    	<strong>${systemMessage}</strong>
	  	</div>
	</c:if>
	
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Użytkownicy</h1>
	<button type="button" class="btn btn-sm btn-secondary float-right mt-4" data-toggle="modal" data-target="#userSearchModal" data-whatever="">Znajdź Użytkownika</button>
	<form action ="clearReturnBookUserSearchParameters" >
		 <button class="btn btn-sm btn-secondary float-right mt-4 mr-1" type="submit">Wyczyść Dane Szukania</button>
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
			      <form action="return-book-choose-user">	
						<div class="form-group">
							<input class="form-control" placeholder="ID" type="text" name="returnBookSelectedUserId" value = "<%=(session.getAttribute("returnBookSelectedUserId")==null) ? "" : session.getAttribute("returnBookSelectedUserId")%>">
				 			<input class="form-control" placeholder="Imię" type="text" name="returnBookFirstName" value = "<%=(session.getAttribute("returnBookFirstName")==null) ? "" : session.getAttribute("returnBookFirstName")%>">			
							<input class="form-control" placeholder="Nazwisko" type="text" name="returnBookLastName" value = "<%=(session.getAttribute("returnBookLastName")==null) ? "" : session.getAttribute("returnBookLastName")%>">	
							<input class="form-control" placeholder="Email" type="text" name="returnBookEmail" value = "<%=(session.getAttribute("returnBookEmail")==null) ? "" : session.getAttribute("returnBookEmail")%>">		
							<input class="form-control" placeholder="PESEL" type="text" name="returnBookPesel" value = "<%=(session.getAttribute("returnBookPesel")==null) ? "" : session.getAttribute("returnBookPesel")%>">
						</div>
					<button type="button" class="btn btn-secondary float-right" data-dismiss="modal">Zamknij</button>
			        <input class="btn btn-secondary float-right mr-2" type="submit" value="Szukaj">
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
							
					<c:url var="addUserLink" value="/return-book/prepareForReturn">					
						<c:param name="userId" value="${tempUser.id}"/>					
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
			
			<c:url var="showMoreLink" value="/return-book/return-book-choose-user">					
				<c:param name="returnBookStartResult" value="${showMoreLinkValue}"/>					
			</c:url>
			<c:url var="showLessLink" value="/return-book/return-book-choose-user">					
				<c:param name="returnBookStartResult" value="${showLessLinkValue}"/>					
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