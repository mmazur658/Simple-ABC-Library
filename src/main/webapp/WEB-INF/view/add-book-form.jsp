<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title>Simple ABC Library - Dodawanie Książki</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  

	<div class="container w-25 pt-5 mt-4">	
	
		<c:if test="${not empty systemMessage}">
			<script>showToastrAlert("error","${systemMessage}");</script></c:if>
		
		<form:form class="form-signin" action="saveBook" modelAttribute="book" method="POST">
			<h1 class="h3 m-2 font-weight-normal ">Dodawanie Książki</h1>
			<form:input class="form-control" type="text" path="title" placeholder="Tytuł" required="required"/>
			<form:input class="form-control" type="text" path="author" placeholder="Autor" required="required"/>
			<form:input class="form-control" type="text" path="isbn" placeholder="ISBN" required="required"/>
			<form:input class="form-control" type="text" path="publisher" placeholder="Wydawnictwo" required="required"/>
			<form:input class="form-control" type="text" path="language" placeholder="Język" required="required"/>
			<form:input class="form-control" type="number" path="pages" placeholder="Stron" required="required"/>			
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">Zapisz</button>
		</form:form>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>