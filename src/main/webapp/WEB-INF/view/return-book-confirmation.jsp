<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - Potwierdzenie Przyjęcia Książek</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container w-25">	
		<h1 class="h3 m-2 font-weight-normal ">Książki zostały zwrócone</h1>
		
				<c:url var="generateReturnBookConfirmationLink" value="/return-book/generate-return-book-confirmation">					
					<c:param name="returnedBookInfo" value="${returnedBookInfo}"/>					
				</c:url>
	
		<a href="${generateReturnBookConfirmationLink}" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Wydrukuj Potwierdzenie</a>
		<a href="${pageContext.request.contextPath}/return-book/return-book-choose-user" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Nowy Zwrot</a>
		<a href="${pageContext.request.contextPath}/user/main" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Strona Główna</a>
		
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>