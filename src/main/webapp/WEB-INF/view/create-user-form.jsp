<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Simple ABC Library - Tworzenie Nowego Użytkownika</title>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/style.css" />
	</head>
<body>

	<c:if test="${not empty systemMessage}">
		<div class="system-message-container">
			<p id="system-message">Komunikat: ${systemMessage}</p>
		</div>
	</c:if>


	<div class="wrapper">
	
		<form:form class="form-signin" action="saveUser" modelAttribute="user" method="POST">
		<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
			<h3 class="form-signin-heading">Tworzenie Nowego Konta</h3>
			<form:input class="form-control" type="text" path="firstName" placeholder="Imię" required="required"/>
			<form:input class="form-control" type="text" path="lastName" placeholder="Nazwisko" required="required"/>
			<form:input class="form-control" type="email" path="email" placeholder="Email" required="required"/>
			<input class="form-control" id="password" type="password" id="password" placeholder="Hasło" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
				   title="Twoje hasło musi zawierać conajmniej jedną cyfrę, jedną dużą i małą literę i musi mieć 8 albo więcej znaków"
			       required >
			<form:input class="form-control" id="confirm_password" type="password" path="password" placeholder="Powtórz Hasło" required="required"/>
			<button class="big-button" type="submit">Utwórz Nowe Konto</button>
		</form:form>
		
	<div class="container">
		<button class="small-button" onclick="window.location.href='${pageContext.request.contextPath}/user/login-page'">Wróć</button>
	</div>
	
	</div>

	<script>var password = document.getElementById("password"), confirm_password = document.getElementById("confirm_password");
		function validatePassword(){
			if(password.value != confirm_password.value) {
			  confirm_password.setCustomValidity("Hasła nie są takie same!");
			} else {
			  confirm_password.setCustomValidity('');
			}
		}
		password.onchange = validatePassword;
		confirm_password.onkeyup = validatePassword;
	</script>


</body>
</html>