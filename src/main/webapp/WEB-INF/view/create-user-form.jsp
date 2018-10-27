<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html >
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/signin.css" />
	<title><spring:message code="view.create-user-form.title"/></title>
</head>

<body class="text-center">

		<spring:message var="firstNamePlaceholder" code="user.first.name"/>	
		<spring:message var="lastNamePlaceholder" code="user.last.name"/>	
	 	<spring:message var="emailPlaceholder" code="user.email"/>	
		<spring:message var="passwordPlaceholder" code="user.password"/>	
		<spring:message var="confirmPasswordPlaceholder" code="label.password.confirm"/>		
		<spring:message var="createNewAccount" code="button.new.account"/>	
		<spring:message var="passwordRules" code="label.password.rules"/>	
		<spring:message var="backButton" code="button.back"/>	

	<form:form class="form-signin" action="saveUser" modelAttribute="user" method="POST">
		<img class="mb-3" src="<%=request.getContextPath()%>/resources/image/ABC_new_mini_logo.png" alt="" width="200" height="200">	
		<h1 class="h3 mb-3 font-weight-normal"><spring:message code="view.create-user-form.heading"/></h1>
				
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("error","${systemMessage}");</script></c:if>	
					
		<label for="inputFirstName" class="sr-only">${firstNamePlaceholder}</label>
	    <form:input type="text" id="inputFirstName" class="form-control" path="firstName" placeholder="${firstNamePlaceholder}" required="required" />	
	    	

	 	<label for="inputLastName" class="sr-only">${lastNamePlaceholder }</label>
	    <form:input type="text" id="inputLastName" class="form-control" path="lastName" placeholder="${ lastNamePlaceholder}" required="required" />
	    
	    <label for="inputEmail" class="sr-only">${emailPlaceholder }</label>
	    <form:input type="text" id="inputEmail" class="form-control" path="email" placeholder="${ emailPlaceholder}" required="required" />		
					
		<label for="inputPassword" class="sr-only">${ passwordPlaceholder}</label>
	    <input type="password" id="inputPassword" class="form-control" name="passwordOne" placeholder="${passwordPlaceholder }" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
					   title="${passwordRules }" required >
		
	
		<label for="inputConfirmPassword" class="sr-only">${confirmPasswordPlaceholder }</label>
		<form:input class="form-control" style="margin-top: -10px;" type="password" path="password" placeholder="${ confirmPasswordPlaceholder}" required="required"/>
				
		<button class="btn btn-lg btn-secondary btn-block shadow" type="submit">${createNewAccount }</button>
		<a href="${pageContext.request.contextPath}/user/login-page" class="btn btn-lg btn-secondary btn-block shadow" role="button" aria-pressed="true">${backButton }</a>
				
	</form:form>
		
<%@ include file="/resources/parts/footer-starter.jsp" %> 	

</body>
</html>