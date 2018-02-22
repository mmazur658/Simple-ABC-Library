<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
		<title>Simple ABC Library - Zmiana hasła</title>
	</head>
	
	<body>

		<c:url var="userDetailsLink" value="/user/user-details">
			<c:param name="userDetailsUserId" value="${user.id}" />
			<c:param name="userDetailsWayBack" value="main" />
		</c:url>
	
		<header>	
			<button class="header-button" onclick="window.location.href='${userDetailsLink}'"> <%=session.getAttribute("userFirstName")%> <%=session.getAttribute("userLastName")%></button>
			<button class="header-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-inbox'">MessageBox</button>
			<button class="header-button" onclick="window.location.href='${pageContext.request.contextPath}/user/logout'">Wyloguj</button>
		</header>
		
		<c:if test="${not empty systemMessage}">
			<div class="system-message-container">
				<p id="system-message">Komunikat: ${systemMessage}</p>
			</div>
		</c:if>

		<div class="wrapper">
		
		<form class="form-signin" action="changePassword" method="POST">
			<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo-update-form" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
			<h3 class="form-signin-heading">Zmiana Hasła</h3>
			<input class="form-control" id="oldPassword" type="password" name="old-password" placeholder="Stare Hasło"/>
			<input class="form-control" id="password" type="password" id="password" placeholder="Nowe Hasło" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" 
				   title="Twoje hasło musi zawierać conajmniej jedną cyfrę, jedną dużą i małą literę i musi mieć 8 albo więcej znaków"
			       required >
			<input class="form-control" id="confirm_password" type="password" name="password" placeholder="Potwierdź Nowe Hasło" required="required"/>  
			<input class="form-control" type="hidden" name="changePasswordFormUserId" value="${changePasswordUserId }"/>   
			<button class="big-button" type="submit">Zmień Hasło</button>
		</form>

			<div class="return-container">
				<button class="small-button" onclick="history.back()">Wróć</button>
			</div>
		
		</div>


		<script>var password = document.getElementById("password"), confirm_password = document.getElementById("confirm_password");
			function validatePassword(){
				if(password.value != confirm_password.value) {
				  confirm_password.setCustomValidity("Passwords Don't Match");
				} else {
				  confirm_password.setCustomValidity('');
				}
			}
			password.onchange = validatePassword;
			confirm_password.onkeyup = validatePassword;
		</script>

	</body>
	
</html>