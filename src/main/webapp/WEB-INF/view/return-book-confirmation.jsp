<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title><spring:message code="view.return-book-confirmation.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.return-book-confirmation.heading"/>
	<spring:message var="printConfirmButton" code="button.print.confirm"/>
	<spring:message var="nextReturnButton" code="button.next.return"/>
	<spring:message var="mainPage" code="label.main.page"/>
	
	<div class="container w-25">	
		<h1 class="h3 m-2 font-weight-normal ">${heading }</h1>
		
				<c:url var="generateReturnBookConfirmationLink" value="/return-book/generate-return-book-confirmation">					
					<c:param name="returnedBookInfo" value="${returnedBookInfo}"/>					
				</c:url>
	
		<a href="${generateReturnBookConfirmationLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${printConfirmButton }</a>
		<a href="${pageContext.request.contextPath}/return-book/return-book-choose-user" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${nextReturnButton }</a>
		<a href="${pageContext.request.contextPath}/user/main" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${mainPage }</a>
		
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>