<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title><spring:message code="view.user-details.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="resetPasswordButton" code="button.password.reset"/>
	<spring:message var="booksButton" code="button.books"/>
	<spring:message var="changeAccessLevelButton" code="button.change.access.level"/>
	<spring:message var="heading" code="view.user-details.heading"/>
	<spring:message var="userId" code="user.id"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="userStartDate" code="user.last.name"/>
	<spring:message var="userBirthday" code="user.birthda"/>
	<spring:message var="userAccessLevel" code="user.access.level"/>
	<spring:message var="usercity" code="user.city"/>
	<spring:message var="userAddress" code="user.address"/>
	<spring:message var="userSex" code="user.sex"/>
	<spring:message var="userPesel" code="user.pesel"/>
	<spring:message var="userEmail" code="user.email"/>
	<spring:message var="userLastName" code="user.last.name"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="updateButton" code="button.update"/>
	
	<div class="container w-50  pt-5 mt-4">		
			
		<table class="table table-borderedless w-75 mb-0" style="text-align: center;">
			<tr>
				<td><h1 class="h3 mb-0 font-weight-normal ">${heading }</h1></td>			
			</tr>
		</table>
		
		<table class="table table-sm table-bordered w-75">
				  <tbody>
		    <tr>
		      <th scope="row">${ userId} </th>
		      <td>${theUser.id}</td>	
		    </tr>
		   	<tr>
		      <th scope="row">${ userFirstName} </th>
		      <td>${theUser.firstName}</td>	
		    </tr>
		   	<tr>
		      <th scope="row">${ userLastName} </th>
		      <td>${theUser.lastName}</td>	
		    </tr>
		   	<tr>
		      <th scope="row">${userEmail } </th>
		      <td>${theUser.email}</td>	
		    </tr>		    
		   	<tr>
		      <th scope="row">${userPesel } </th>
		      <td>${theUser.pesel}</td>	
		    </tr>
		    <tr>
		      <th scope="row">${ userSex} </th>
		      <td>${theUser.sex}</td>	
		    </tr>
		    <tr>
		      <th scope="row">${ userAddress}</th>
		      <td>${theUser.street} ${theUser.houseNumber }</td>	
		    </tr>
		    <tr>
		      <th scope="row">${ usercity} </th>
		      <td>${theUser.city} ${theUser.postalCode}</td>	
		    </tr>
		    <tr>
		      <th scope="row">${userAccessLevel} </th>
		      <td>${userAccessLevel}</td>	
		    </tr>
		    <tr>
		      <th scope="row">${ userBirthday} </th>
		      <td><fmt:formatDate value="${theUser.birthday}" pattern="yyyy-MM-dd"/></td>	
		    </tr>
		    <tr>
		      <th scope="row">${userStartDate } </th>
		      <td><fmt:formatDate value="${theUser.startDate}" pattern="yyyy-MM-dd"/></td>	
		    </tr>	  
		  </tbody>
		</table>	
		
		<c:url var="updateUserLink" value="/user/user-management-update-form">					
			<c:param name="userDetailsUserId" value="${theUser.id}"/>					
		</c:url>
	
		<a href="${updateUserLink}" class="btn btn-sm btn-secondary btn-block w-75 shadow" role="button" aria-pressed="true">${updateButton }</a>
		<a href="#" class="btn btn-sm btn-secondary btn-block w-75 shadow" role="button" aria-pressed="true">${resetPasswordButton }</a>
		<a href="#" class="btn btn-sm btn-secondary btn-block w-75 shadow" role="button" aria-pressed="true">${booksButton }</a>
		<a href="#" class="btn btn-sm btn-secondary btn-block w-75 mb-5 shadow" role="button" aria-pressed="true">${changeAccessLevelButton }</a>
	
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>