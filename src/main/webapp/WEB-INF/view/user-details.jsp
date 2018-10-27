<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
		
	<spring:message var="heading" code="view.user-details.heading"/>
	<spring:message var="userId" code="user.id"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="userStartDate" code="user.last.name"/>
	<spring:message var="userBirthday" code="user.birthday"/>
	<spring:message var="userAccessLevel" code="user.access.level"/>
	<spring:message var="usercity" code="user.city"/>
	<spring:message var="userAddress" code="user.address"/>
	<spring:message var="userSex" code="user.sex"/>
	<spring:message var="userPesel" code="user.pesel"/>
	<spring:message var="userEmail" code="user.email"/>
	<spring:message var="userLastName" code="user.last.name"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="updateButton" code="button.update"/>
	
	<div class="container w-50 pt-5 mt-4" >
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
			
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
		<a href="${pageContext.request.contextPath}/user/user-update-form" class="btn btn-sm btn-secondary btn-block w-75 shadow" role="button" aria-pressed="true" >${updateButton }</a>
	</div>
	
	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>