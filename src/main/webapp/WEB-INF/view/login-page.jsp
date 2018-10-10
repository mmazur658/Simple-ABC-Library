<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/signin.css" />
	<title>Simple ABC Library</title>
</head>
<body class="text-center">

    <form class="form-signin" action="login" method="post">
      <img class="mb-3" src="<%=request.getContextPath()%>/resources/image/ABC_new_mini_logo.png" alt="" width="200" height="200">
      <h1 class="h3 mb-3 font-weight-normal">Simple ABC Library</h1>
      
		<c:if test="${not empty incorrectPasswordMessage}">
			<div class="alert alert-danger" role="alert">
	    		<strong>${incorrectPasswordMessage}</strong>
	  		</div>
		</c:if>
      <label for="inputEmail" class="sr-only">Email</label>
      <input type="email" id="inputEmail" class="form-control" placeholder="Email" name="email" required autofocus>
      <label for="inputPassword" class="sr-only">Hasło</label>
      <input type="password" id="inputPassword" class="form-control" name="password" placeholder="Hasło" required>
      <button class="btn btn-lg btn-secondary btn-block" type="submit">Zaloguj</button>
      <a href="${pageContext.request.contextPath}/user/add-user" class="btn btn-lg btn-secondary btn-block" role="button" aria-pressed="true">Utwórz nowe konto</a>

    </form>

<%@ include file="/resources/parts/footer-starter.jsp" %> 
	
</body>
</html>