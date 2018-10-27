<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
	
	<spring:message var="heading" code="view.book-update.heading" />
	<spring:message var="bookTitle" code="book.title" />
	<spring:message var="author" code="book.author" />
	<spring:message var="isbn" code="book.isbn" />
	<spring:message var="publisher" code="book.publisher" />
	<spring:message var="language" code="book.language" />
	<spring:message var="pages" code="book.pages" />
	<spring:message var="submitButton" code="button.save" />

	<div class="container pt-5 mt-4">	
	
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("error","${systemMessage}");</script></c:if>
		
		<form:form class="form-signin w-25" action="update-book" modelAttribute="book" method="Post">
			<h1 class="h3 m-2 font-weight-normal ">${heading }</h1>
			<form:hidden class="form-control" path="id"/>
			<form:input class="form-control" type="text" placeholder="${bookTitle }" path="title"/>
			<form:input class="form-control" type="text" placeholder="${author }" path="author"/>
			<form:input class="form-control" type="text" placeholder="${isbn }" path="isbn"/>
			<form:input class="form-control" type="text" placeholder="${publisher }" path="publisher"/>
			<form:input class="form-control" type="text" placeholder="${language }" path="language"/>
			<form:input class="form-control" type="number" placeholder="${pages }" path="pages"/>
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">${submitButton}</button>
		</form:form>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>