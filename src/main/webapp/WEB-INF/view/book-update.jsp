<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title>Simple ABC Library - Aktualizaca Danych</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  

	<div class="container pt-5 mt-4">	
	
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("error","${systemMessage}");</script></c:if>
		
		<form:form class="form-signin w-25" action="update-book" modelAttribute="book" method="Post">
			<h1 class="h3 m-2 font-weight-normal ">Aktualizacja Danych</h1>
			<form:hidden class="form-control" path="id"/>
			<form:input class="form-control" type="text" placeholder="Tytuł" path="title"/>
			<form:input class="form-control" type="text" placeholder="Author" path="author"/>
			<form:input class="form-control" type="text" placeholder="ISBN" path="isbn"/>
			<form:input class="form-control" type="text" placeholder="Wydawnictwo" path="publisher"/>
			<form:input class="form-control" type="text" placeholder="Język" path="language"/>
			<form:input class="form-control" type="number" placeholder="Stron" path="pages"/>
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">Zapisz</button>
		</form:form>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>