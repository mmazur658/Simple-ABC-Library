<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title><spring:message code="view.add-book-form.title" /></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.add-book-form.heading" />
	<spring:message var="title" code="book.title" />
	<spring:message var="author" code="book.author" />
	<spring:message var="isbn" code="book.isbn" />
	<spring:message var="publisher" code="book.publisher" />
	<spring:message var="language" code="book.language" />
	<spring:message var="pages" code="book.pages" />
	<spring:message var="submit" code="button.save" />

	<div class="container w-25 pt-5 mt-4">	
	
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("error","${systemMessage}");</script></c:if>
		
		<form:form class="form-signin" action="saveBook" modelAttribute="book" method="POST">
			<h1 class="h3 m-2 font-weight-normal ">${heading}</h1>
			<form:input class="form-control" type="text" path="title" placeholder="${ title }" required="required"/>
			<form:input class="form-control" type="text" path="author" placeholder="${ author}" required="required"/>
			<form:input class="form-control" type="text" path="isbn" placeholder="${ isbn}" required="required"/>
			<form:input class="form-control" type="text" path="publisher" placeholder="${ publisher}" required="required"/>
			<form:input class="form-control" type="text" path="language" placeholder="${ language}" required="required"/>
			<form:input class="form-control" type="number" path="pages" placeholder="${pages }" required="required"/>			
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">${ submit}</button>
		</form:form>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>