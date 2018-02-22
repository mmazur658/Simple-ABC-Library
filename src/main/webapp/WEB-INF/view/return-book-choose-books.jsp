<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html ">
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/borrow-return-book-style.css" />
		<title>Simple ABC Library - Zwrot Książki - Wybierz Książki</title>
	</head>
	<body>
		
		<c:url var="userDetailsLink" value="/user/user-details">
			<c:param name="userDetailsUserId" value="${user.id}" />
			<c:param name="userDetailsWayBack" value="main" />
		</c:url>
	
		<header>	
			<button class="header-button" onclick="window.location.href='${userDetailsLink}'"> <%=session.getAttribute("userFirstName")%> <%=session.getAttribute("userLastName")%></button>
			<button class="header-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-inbox'">MessageBox</button>
			<button class="header-button" onclick="window.location.href='${pageContext.request.contextPath}/user/logout'">Wyloguj</button>
		</header>
		
		<c:if test="${not empty systemMessage}">
			<div class="system-message-container">
				<p id="system-message">Komunikat: ${systemMessage}</p>
			</div>
		</c:if>
		
		<div class="wrapper">
		
		
			<div class="container">
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
				<h3 class="h3-heading">Zwrot Książek - ${theUser.firstName} ${theUser.lastName}</h3>	
				
				<table>
					<caption>Zwracane Książki</caption>
					<tr>
						<th id="id-column">Id</th>
						<th>Title</th>
						<th>Author</th>
						<th id="action-column">Action</th>
					</tr>
					<c:forEach var="tempBook" items="${tempReturnedBookList}">
					
						<c:url var="deleteBookLink" value="/return-book/deleteReturnedBookFromList">					
								<c:param name="bookId" value="${tempBook.id}"/>					
						</c:url>
						
						<tr>
							<td id="id-column">${tempBook.id }</td>
							<td>${tempBook.title }</td>
							<td>${tempBook.author }</td>
							<td id="action-column"> <button class="small-button" onclick="window.location.href='${deleteBookLink}'">Usuń</button></td> 
						</tr>			
				
					</c:forEach>
				</table>
				 
				<form action="return-book">
					<button class="big-button" type="submit">Zwrot Książek</button>	
				</form>
				
				
				<table>
					<caption>Wypożyczone Książki</caption>
					<tr>
						<th id="id-column">Id</th>
						<th>Title</th>
						<th>Author</th>	
						<th id="action-column">Action</th>
					</tr>		
					
					<c:forEach var="tempBorrowedBook" items="${userBorrowedBooksList}">
	
						<c:url var="addBookLink" value="/return-book/addReturnedBookToList">					
							<c:param name="bookId" value="${tempBorrowedBook.book.id }"/>					
						</c:url>
							
						<tr>
							<td id="id-column">${tempBorrowedBook.book.id }</td>
							<td>${tempBorrowedBook.book.title }</td>
							<td>${tempBorrowedBook.book.author }</td>			
							<td id="action-column"><button class="small-button" onclick="window.location.href='${addBookLink}'">Dodaj</button></td>
									
						</tr>								
					</c:forEach>
				</table>
				<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/return-book/addAllBorrowedBookToList'">Dodaj Wszystkie</button>
				
			</div>
		
		</div>
		
</body>
</html>