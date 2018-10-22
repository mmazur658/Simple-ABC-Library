<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title>Simple ABC Library - Zmiana Hasła</title>
</head>
<body>

	<%@ include file="/resources/parts/nav.jsp" %>
	  
	<div class="container w-25 pt-5 mt-4">	
	
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("error","${systemMessage}");</script></c:if>
	
		<form class="form-signin" action="changePassword" method="POST">
			<h1 class="h3 mb-3 mt-2 font-weight-normal">Nowe Hasło</h1>			
			<input class="form-control" id="oldPassword" type="password" name="old-password" placeholder="Stare Hasło"/>
			<input class="form-control" id="password" type="password" id="password" placeholder="Nowe Hasło" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
				   title="Twoje hasło musi zawierać conajmniej jedną cyfrę, jedną dużą i małą literę i musi mieć 8 albo więcej znaków"
			       required >
			<input class="form-control" id="confirm_password" type="password" name="password" placeholder="Potwierdź Nowe Hasło" required="required"/>  
			<input class="form-control" type="hidden" name="changePasswordFormUserId" value="${changePasswordUserId }"/>   
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">Zmień Hasło</button>
		</form>			

	</div>
	
<%@ include file="/resources/parts/footer.jsp" %> 

</body>
</html>