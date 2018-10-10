<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Księgozbiór</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container">	
	
		<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Księgozbiór</h1>
		 <button type="button" class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4" data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">Znajdź Książkę</button>
		 <form action ="clearSearchParameters" >
		 	<button class="btn btn-sm btn-secondary btn-block w-25 float-right mt-4 mr-1" type="submit">Wyczyść Dane Szukania</button>
		 </form>
		 <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="exampleModalLabel">Znajdź Książkę</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <form action="main-bookstore">
			          <div class="form-group">
			          <input class="form-control" placeholder="ID" type="text" name ="id" value = "<%=(session.getAttribute("id")==null) ? "" : session.getAttribute("id")%>"/>
						<input class="form-control" placeholder="Tytuł" type="text" name="title" value = "<%=(session.getAttribute("title")==null) ? "" : session.getAttribute("title")%>"/>
						<input class="form-control" placeholder="Autor" type="text" name="author" value = "<%=(session.getAttribute("author")==null) ? "" : session.getAttribute("author") %>"/>
						<input class="form-control" placeholder="Wydawca" type="text" name="publisher" value = "<%=(session.getAttribute("publisher")== null) ? "" : session.getAttribute("publisher")%>"/>
						<input class="form-control" placeholder="ISBN" type="text" name="isbn" value = "<%=(session.getAttribute("isbn")==null) ? "" : session.getAttribute("isbn")%>"/>	
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
		      <th scope="col">ID</th>
		      <th scope="col">Tytuł</th>
		      <th scope="col">Author</th>
		      <th scope="col">Dostępność</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="tempBook" items="${booksList}">
						
						<c:set var="isAvailable" value="${tempBook.isAvailable}"/>
						
						<c:url var="bookDetailsLink" value="/book/book-details">					
								<c:param name="bookId" value="${tempBook.id}"/>					
						</c:url>
						
						<tr onclick="window.location.href='${bookDetailsLink}'">
							<td>${tempBook.id }</td>
							<td>${tempBook.title }</td>
							<td>${tempBook.author }</td>
							<c:choose>
								<c:when test="${isAvailable}"> <td>Dostępna</td> </c:when>
								<c:otherwise> <td>Niedostępna</td > </c:otherwise>
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
			    <li class="page-item"><p class="page-link text-dark" >${resultRange} z ${amountOfResults}</p></li>
			    <li class="page-item"><a class="page-link text-dark" href="${showMoreLink}"> >>> </a></li>
			  </ul>
		</nav>
	
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>