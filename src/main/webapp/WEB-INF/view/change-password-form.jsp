<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title><spring:message code="view.change-password-form.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>
	
	<spring:message var="heading" code="view.change-password-form.heading"/>
	<spring:message var="oldPassword" code="label.password.old"/>
	<spring:message var="newPassword" code="label.password.new"/>
	<spring:message var="passwordRules" code="label.password.rules"/>
	<spring:message var="confirmNewPassword" code="label.password.new.confirm"/>
	<spring:message var="changePasswordButton" code="button.change.password"/>
	
	<div class="container w-25 pt-5 mt-4">	
	
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("error","${systemMessage}");</script></c:if>
	
		<form class="form-signin" action="changePassword" method="POST">
			<h1 class="h3 mb-3 mt-2 font-weight-normal">${heading }</h1>			
			<input class="form-control" id="oldPassword" type="password" name="${oldPassword }" placeholder="Stare Hasło"/>
			<input class="form-control" id="password" type="password" id="password" placeholder="${newPassword }" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
				   title="${passwordRules }"
			       required >
			<input class="form-control" id="confirm_password" type="password" name="password" placeholder="${confirmNewPassword }" required="required"/>  
			<input class="form-control" type="hidden" name="changePasswordFormUserId" value="${changePasswordUserId }"/>   
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">${changePasswordButton }</button>
		</form>			

	</div>
	
<%@ include file="/resources/parts/footer.jsp" %> 

</body>
</html>