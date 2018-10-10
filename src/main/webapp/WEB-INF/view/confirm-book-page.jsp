<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Potwierdzenie Dodania Książki</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container w-25">	
	
	<h1 class="h3 m-2 font-weight-normal ">Książka zastała dodana</h1>
	
	<c:url var="generateBookLabelLink" value="/book/generate-book-label">					
		<c:param name="bookId" value="${tempBook.id}"/>					
	</c:url>
	
	<a href="${generateBookLabelLink }" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Wydruk Etykiety</a>
	<a href="${pageContext.request.contextPath}/book/add-book-form" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Dodaj Następną Książkę</a>
	<a href="${pageContext.request.contextPath}/user/main" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Strona Główna</a>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>