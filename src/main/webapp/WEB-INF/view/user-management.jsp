<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title>	<spring:message code="view.user-management.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.user-management.heading"/>
	<spring:message var="findUser" code="button.find.user"/>
	<spring:message var="clearSerPar" code="button.clear.ser.par"/>
	<spring:message var="userId" code="user.id"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="userLastName" code="user.last.name"/>
	<spring:message var="userEmail" code="user.email"/>
	<spring:message var="userPesel" code="user.pesel"/>
	<spring:message var="closeButton" code="button.close"/>
	<spring:message var="searchButton" code="button.search"/>
	
	<div class="container  pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">${heading }</h1>
	<button type="button" class="btn btn-sm btn-secondary float-right mt-4 shadow" data-toggle="modal" data-target="#userSearchModal" data-whatever="">${findUser }</button>
	<form action ="clearUserSearchParameters" >
		 <button class="btn btn-sm btn-secondary float-right mt-4 mr-1 shadow" type="submit">${clearSerPar }</button>
	</form>
	<div class="modal fade" id="userSearchModal" tabindex="-1" role="dialog" aria-labelledby="userSearchModalLabel" aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content">
			      <div class="modal-header">
			        <h5 class="modal-title" id="userSearchModalLabel">${findUser }</h5>
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body">
			        <form action="user-management">
			          <div class="form-group">
						<input class="form-control" placeholder="${userId }" type="text" name="userManagementUserId" value = "<%=(session.getAttribute("userManagementUserId")==null) ? "" : session.getAttribute("userManagementUserId")%>">
						<input class="form-control" placeholder="${userFirstName }" type="text" name="userManagementFirstName" value = "<%=(session.getAttribute("userManagementFirstName")==null) ? "" : session.getAttribute("userManagementFirstName")%>">
						<input class="form-control" placeholder="${userLastName }" type="text" name="userManagementLastName" value = "<%=(session.getAttribute("userManagementLastName")==null) ? "" : session.getAttribute("userManagementLastName")%>">
						<input class="form-control" placeholder="${userEmail }" type="text" name="userManagementEmail" value = "<%=(session.getAttribute("userManagementEmail")==null) ? "" : session.getAttribute("userManagementEmail")%>">
						<input class="form-control" placeholder="${userPesel }" type="text" name="userManagementPesel" value = "<%=(session.getAttribute("userManagementPesel")==null) ? "" : session.getAttribute("userManagementPesel")%>">		          
					  </div>
			         <button type="button" class="btn btn-secondary float-right shadow" data-dismiss="modal">${closeButton }</button>
			         <input class="btn btn-secondary float-right mr-2 shadow" type="submit" value="${searchButton }">
			   		</form>  
			      </div>
			    </div>
			  </div>
		</div>
	
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
		    <c:forEach var="tempUser" items="${usersList}">
					
					<c:url var="userDetailsLink" value="/user/user-details-management">					
						<c:param name="userDetailsUserId" value="${tempUser.id}"/>		
					</c:url>
					<tr onclick="window.location.href='${userDetailsLink}'">
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
			
				<c:url var="showMoreLink" value="/user/user-management">					
					<c:param name="userManagementStartResult" value="${showMoreLinkValue}"/>					
				</c:url>
				<c:url var="showLessLink" value="/user/user-management">					
					<c:param name="userManagementStartResult" value="${showLessLinkValue}"/>					
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