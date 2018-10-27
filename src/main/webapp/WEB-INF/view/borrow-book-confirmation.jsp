<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title>	<spring:message code="view.borrow-book-confirmation.title"/></title>
</head>

<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.borrow-book-confirmation.heading"/>
	<spring:message var="printConfirmationButton" code="button.print.confirm"/>
	<spring:message var="borrowMoreBooks" code="button.borrow.more"/>
	<spring:message var="mainPage" code="label.main.page"/>
	
	<div class="container w-25 pt-5 mt-4">	
		<h1 class="h3 m-2 font-weight-normal ">${heading }</h1>
		
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("success","${systemMessage}");</script></c:if>
		
		<c:url var="generateBorrowedBookConfirmationLink" value="/borrow-book/generate-borrowed-book-confirmation">					
			<c:param name="borrowedBookInfo" value="${borrowedBookInfo}"/>					
		</c:url>
	
		<a href="${generateBorrowedBookConfirmationLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${printConfirmationButton }</a>
		<a href="${pageContext.request.contextPath}/borrow-book/borrow-book-choose-user" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${borrowMoreBooks }</a>
		<a href="${pageContext.request.contextPath}/user/main" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${mainPage }</a>
		
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>