<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
<%@ include file="/resources/parts/header.jsp" %>  		
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title><spring:message code="view.user-update.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.user-update.heading"/>
	<spring:message var="userFirstName" code="user.first.name"/>
	<spring:message var="userLastName" code="user.last.name"/>				
	<spring:message var="userEmail" code="user.email"/>
	<spring:message var="userPesel" code="user.pesel"/>
	<spring:message var="userStreet" code="user.street"/>
	<spring:message var="userHouseNumber" code="user.house.number"/>
	<spring:message var="userCity" code="user.city"/>
	<spring:message var="userPostalCode" code="user.postal.code"/>
	<spring:message var="saveButton" code="button.save"/>

	<div class="container w-25  pt-5 mt-4">	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("error","${systemMessage}");</script></c:if>
		
		<form:form class="form-signin" action="update-user" modelAttribute="user" method="POST">
			<h1 class="h3 m-2 font-weight-normal ">${heading }</h1>
			<form:hidden class="form-control" path="id"/>
			<form:input class="form-control" type="text" path="firstName" placeholder="${userFirstName }"/>
			<form:input class="form-control" type="text" path="lastName" placeholder="${userLastName }"/>
			<form:input class="form-control" type="text" path="email" placeholder="${userEmail }"/>
			<form:input class="form-control" type="text" path="pesel" placeholder="${userPesel }"/>
			<form:input class="form-control" type="text" path="street" placeholder="${ userStreet}"/>
			<form:input class="form-control" type="text" path="houseNumber" placeholder="${userHouseNumber }"/>
			<form:input class="form-control" type="text" path="city" placeholder="${userCity}"/>
			<form:input class="form-control" type="text" path="postalCode" placeholder="${userPostalCode } "/>
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">${saveButton }</button>
		</form:form> 

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
	
</body>
</html>