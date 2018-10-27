<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html >
<html>
<head>	
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/signin.css" />
	<title><spring:message code="view.confirm-page.title"/></title>
</head>

	<spring:message var="heading" code="view.confirm-page.heading"/>
	<spring:message var="error" code="label.error"/>
	<spring:message var="hello1" code="label.hello1"/>
	<spring:message var="hello2" code="label.hello2"/>
	<spring:message var="printConfirmButton" code="button.print.confirm"/>
	<spring:message var="loginPage" code="label.login.page"/>

	<body class="container text-center">
		<div class="form-signin">
			 <img class="mb-2" src="<%=request.getContextPath()%>/resources/image/ABC_new_mini_logo.png" alt="" width="200" height="200">
		     <h1 class="h3 mb-3 font-weight-normal">${heading}</h1>
		     
		     <c:url var="accountConfirmationLink" value="/user/generateAccountConfirmation">					
				<c:param name="userId" value="${theUser.id}"/>					
			</c:url>
		     <c:set var="isNewAccount" scope="session" value="${isExist}" />
		     
		     <c:choose>
					<c:when test="${isNewAccount}">
					<div class="alert alert-danger" role="alert">
						<strong>${error}</strong>
					</div>
					</c:when>
					<c:otherwise>
						<div class="alert alert-success" role="alert">
							<strong>${hello1} ${theUser.firstName} ${theUser.lastName} ${hello2}</strong>
						</div>
						<a href="${accountConfirmationLink}" class="btn btn-lg btn-secondary btn-block shadow" role="button" aria-pressed="true">${printConfirmButton}</a>
	
					</c:otherwise>
				</c:choose>	
		     <a href="${pageContext.request.contextPath}/user/login-page" class="btn btn-lg btn-secondary btn-block shadow" role="button" aria-pressed="true">${loginPage}</a>
		     
		</div>
		<%@ include file="/resources/parts/footer-starter.jsp" %> 
	
	</body>
</html>