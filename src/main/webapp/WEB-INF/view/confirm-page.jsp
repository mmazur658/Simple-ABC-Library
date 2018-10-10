<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html lang="en">
<head>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
	integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
	crossorigin="anonymous">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/signin.css" />

<link rel="shortcut icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico" type="image/x-icon">
<link rel="icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico" type="image/x-icon">


<title>Simple ABC Library</title>
</head>
<body class="container text-center">
	<div class="form-signin">
		 <img class="mb-2" src="<%=request.getContextPath()%>/resources/image/ABC_new_mini_logo.png" alt="" width="200" height="200">
	     <h1 class="h3 mb-3 font-weight-normal">Simple ABC Library</h1>
	     
	     <c:url var="accountConfirmationLink" value="/user/generateAccountConfirmation">					
			<c:param name="userId" value="${theUser.id}"/>					
		</c:url>
	     <c:set var="isNewAccount" scope="session" value="${isExist}" />
	     
	     <c:choose>
				<c:when test="${isNewAccount}">
				<div class="alert alert-danger" role="alert">
					<strong>Istnieje konto dla podanego adresu email!</strong>
				</div>
				</c:when>
				<c:otherwise>
					<div class="alert alert-success" role="alert">
						<strong>Witaj ${theUser.firstName} ${theUser.lastName}. Konto zostało utworzone!</strong>
					</div>
					<a href="${accountConfirmationLink}" class="btn btn-lg btn-secondary btn-block" role="button" aria-pressed="true">Potwierdzenie</a>

				</c:otherwise>
			</c:choose>	
	     <a href="${pageContext.request.contextPath}/user/login-page" class="btn btn-lg btn-secondary btn-block" role="button" aria-pressed="true">Przejdz do logowania</a>
	     
	</div>


</body>
</html>