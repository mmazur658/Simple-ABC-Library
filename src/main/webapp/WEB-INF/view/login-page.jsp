<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html >
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/signin.css" />
	<style>		.btn{  border-radius: 0px;  }	</style>
	<title><spring:message code="view.login-page.title" /></title>
</head>

<body class="text-center">

<div class="container">
    <form class="form-signin" action="login" method="post">
      <img class="mb-3 shadow-lg" style="border-radius: 50%;" src="<%=request.getContextPath()%>/resources/image/ABC_new_mini_logo.png" alt="Simpel ABC Library Logo" width="200" height="200">
      <h1 class="h3 mb-3 font-weight-normal">Simple ABC Library</h1>
      
		<c:if test="${not empty incorrectPasswordMessage}">
			<script>showToastrAlert("error","${incorrectPasswordMessage}");</script></c:if>
		
      <label for="inputEmail" class="sr-only"><spring:message code="view.login-page.email"/></label>
      <input type="email" id="inputEmail" class="form-control" placeholder="<spring:message code="view.login-page.email"/>" name="email" required autofocus>
      <label for="inputPassword" class="sr-only"><spring:message code="view.login-page.password"/></label>
      <input type="password" id="inputPassword" class="form-control" name="password" placeholder="<spring:message code="view.login-page.password"/>" required>
      <button id="alert-target" class="btn btn-lg btn-secondary btn-block shadow" type="submit"><spring:message code="view.login-page.login" /></button>
      <a href="${pageContext.request.contextPath}/user/add-user" class="btn btn-lg btn-secondary btn-block shadow" role="button" aria-pressed="true"><spring:message code="view.login-page.newAccount" /></a>

    </form>
    	<a href="${pageContext.request.contextPath}/user/login-page?language=en_us">
    		<img class="mb-3" style="opacity: 0.7;" src="<%=request.getContextPath()%>/resources/image/united-kingdom-flag.png" alt="United Kingdom Flag" >
    	</a>
    	<a href="${pageContext.request.contextPath}/user/login-page?language=pl_pl">
    		<img class="mb-3" style="opacity: 0.7;" src="<%=request.getContextPath()%>/resources/image/poland-flag.png" alt="Poland Flag" >
    	</a>
    </div>

<%@ include file="/resources/parts/footer-starter.jsp" %> 

</body>
</html>