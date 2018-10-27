<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl"> 

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title><spring:message code="view.confirm-book-page.title"/></title>
</head>

<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
		
	<spring:message var="heading" code="view.confirm-book-page.heading"/>
	<spring:message var="printConfirm" code="button.print.confirm"/>
	<spring:message var="addNextBook" code="button.add.next.book"/>
	<spring:message var="mainPage" code="label.main.page"/>
	
	<div class="container w-25 pt-5 mt-4">	
	
	<c:if test="${not empty successMessage}">
		<script>showToastrAlert("success","${successMessage}");</script></c:if>
		
	<h1 class="h3 m-2 font-weight-normal ">${heading }</h1>
	
	<c:url var="generateBookLabelLink" value="/book/generate-book-label">					
		<c:param name="bookId" value="${tempBook.id}"/>					
	</c:url>
	
	<a href="${generateBookLabelLink }" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${printConfirm }</a>
	<a href="${pageContext.request.contextPath}/book/add-book-form" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${addNextBook }</a>
	<a href="${pageContext.request.contextPath}/user/main" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${mainPage }</a>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>

</html>