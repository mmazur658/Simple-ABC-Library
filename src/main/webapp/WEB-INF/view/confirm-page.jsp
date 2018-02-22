<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Simple ABC Library - Potwierdzenie Utworzenia Konta</title>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/style.css" />
</head>
<body>

	<c:url var="accountConfirmationLink" value="/user/generateAccountConfirmation">					
		<c:param name="userId" value="${theUser.id}"/>					
	</c:url>

	<c:set var="isNewAccount" scope="session" value="${isExist}" />
	<div class="wrapper">
		<div class="container">
		
			<c:choose>
				<c:when test="${isNewAccount}"><h3 class="form-signin-heading">Istnieje konto dla podanego adresu email.</h3></c:when>
				<c:otherwise>
					<h3 class="form-signin-heading">Konto zostało utworzone</h3>
					
						<table class="confirmation-table">
							<tr>
								<td>Imię: </td><td>${theUser.firstName}</td>
							</tr><tr>
								<td>Nazwisko: </td><td>${theUser.lastName}</td>
							</tr><tr>
								<td>Email: </td><td>${theUser.email}</td>
							</tr>
						</table>
						
						<button class="big-button" onclick="window.location.href='${accountConfirmationLink}'">Potwierdzenie</button>
				
				</c:otherwise>
			</c:choose>	
		
		
		
			<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/user/login-page'">Przejdź do logowania</button>
			
		</div>
	</div>



</body>
</html>