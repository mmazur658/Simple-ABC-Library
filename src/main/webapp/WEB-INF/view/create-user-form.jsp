<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/signin.css" />
	<title>Simple ABC Library - Tworzenie Nowego Użytkownika</title>
</head>
<body class="text-center">

	<form:form class="form-signin" action="saveUser" modelAttribute="user" method="POST">
		<img class="mb-3" src="<%=request.getContextPath()%>/resources/image/ABC_new_mini_logo.png" alt="" width="200" height="200">	
		<h1 class="h3 mb-3 font-weight-normal"> Nowe Konto</h1>
				
		<c:if test="${not empty incorrectPasswordMessage}">
			<div class="alert alert-danger" role="alert">
		    	<strong>${systemMessage}</strong>
		  	</div>
		</c:if>
				
		<label for="inputFirstName" class="sr-only">Imię</label>
	    <form:input type="text" id="inputFirstName" class="form-control" path="firstName" placeholder="Imię" required="required" />		
		
		<label for="inputLastName" class="sr-only">Nazwisko</label>
	    <form:input type="text" id="inputLastName" class="form-control" path="lastName" placeholder="Nazwisko" required="required" />
	    
	    <label for="inputEmail" class="sr-only">Email</label>
	    <form:input type="text" id="inputEmail" class="form-control" path="email" placeholder="Email" required="required" />		
				
		<label for="inputPassword" class="sr-only">Hasło</label>
	    <input type="password" id="inputPassword" class="form-control" name="password" placeholder="Hasło" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
					   title="Twoje hasło musi zawierać conajmniej jedną cyfrę, jedną dużą i małą literę i musi mieć 8 albo więcej znaków" required >
		
		<label for="inputConfirmPassword" class="sr-only">Hasło</label>
		<form:input class="form-control" style="margin-top: -10px;" type="password" path="password" placeholder="Powtórz Hasło" required="required"/>
				
		<button class="btn btn-lg btn-secondary btn-block" type="submit">Utwórz Nowe Konto</button>
		<a href="${pageContext.request.contextPath}/user/login-page" class="btn btn-lg btn-secondary btn-block" role="button" aria-pressed="true">Powrót</a>
				
	</form:form>
		
<%@ include file="/resources/parts/footer-starter.jsp" %> 	

</body>
</html>