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

	<div class="container w-25  pt-5 mt-4">	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("error","${systemMessage}");</script></c:if>
		
		<form:form class="form-signin" action="update-user" modelAttribute="user" method="POST">
			<h1 class="h3 m-2 font-weight-normal ">Aktualizacja Danych</h1>
			<form:hidden class="form-control" path="id"/>
			<form:input class="form-control" type="text" path="firstName" placeholder="Imię"/>
			<form:input class="form-control" type="text" path="lastName" placeholder="Nazwisko"/>
			<form:input class="form-control" type="text" path="email" placeholder="Email"/>
			<form:input class="form-control" type="text" path="pesel" placeholder="Pesel"/>
			<form:input class="form-control" type="text" path="street" placeholder="Ulica"/>
			<form:input class="form-control" type="text" path="houseNumber" placeholder="Numer Domu"/>
			<form:input class="form-control" type="text" path="city" placeholder="Miasto"/>
			<form:input class="form-control" type="text" path="postalCode" placeholder="Kod Pocztowy"/>
			<button class="btn btn-lg btn-secondary btn-block mt-2 shadow" type="submit">Zapisz</button>
		</form:form> 

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
	
</body>
</html>