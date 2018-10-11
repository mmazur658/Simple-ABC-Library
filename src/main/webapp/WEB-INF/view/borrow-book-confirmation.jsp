<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Potwierdzenie Wydania Książek</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container w-25">	
		<h1 class="h3 m-2 font-weight-normal ">Książki zostały wydane</h1>
		
		<c:url var="generateBorrowedBookConfirmationLink" value="/borrow-book/generate-borrowed-book-confirmation">					
			<c:param name="borrowedBookInfo" value="${borrowedBookInfo}"/>					
		</c:url>
	
		<a href="${generateBorrowedBookConfirmationLink}" class="btn btn-sm btn-secondary btn-block " role="button" aria-pressed="true" >Wydrukuj Potwierdzenie</a>
		<a href="${pageContext.request.contextPath}/borrow-book/borrow-book-choose-user" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Nowe Wydanie</a>
		<a href="${pageContext.request.contextPath}/user/main" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Strona Główna</a>
		
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>