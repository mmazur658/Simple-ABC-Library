<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html ">
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/borrow-return-book-style.css" />
		<title>Simple ABC Library - Wydanie Książki - Wybierz Użytkownika</title>
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
	

					<form class="form-signin" action="borrow-book-choose-user">	
						<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
						<h3 class="h3-heading">Wyszukaj Użytkownika</h3>	
						<input class="form-control" placeholder="ID" type="text" name="borrowBookSelectedUserId" value = "<%=(session.getAttribute("borrowBookSelectedUserId")==null) ? "" : session.getAttribute("borrowBookSelectedUserId")%>">
			 			<input class="form-control" placeholder="Imię" type="text" name="borrowBookFirstName" value = "<%=(session.getAttribute("borrowBookFirstName")==null) ? "" : session.getAttribute("borrowBookFirstName")%>">			
						<input class="form-control" placeholder="Nazwisko" type="text" name="borrowBookLastName" value = "<%=(session.getAttribute("borrowBookLastName")==null) ? "" : session.getAttribute("borrowBookLastName")%>">	
						<input class="form-control" placeholder="Email" type="text" name="borrowBookEmail" value = "<%=(session.getAttribute("borrowBookEmail")==null) ? "" : session.getAttribute("borrowBookEmail")%>">		
						<button class="big-button" type="submit">Szukaj</button>
					</form>
			
			
					
			
			
						<div class="return-container">
							<form action ="clearUserSearchParameters" >
								<button class="big-button" type="submit">Wyczyść dane wyszukiwania</button>	
							</form>
							<button class="big-return-button" onclick="window.location.href='${pageContext.request.contextPath}/user/main'">Anuluj Wydanie</button>
						</div>		
					
					
					<div class="container">
					
						<c:url var="showMoreLink" value="/borrow-book/borrow-book-choose-user">					
							<c:param name="borrowBookStartResult" value="${showMoreLinkValue}"/>					
						</c:url>
						
						<c:url var="showLessLink" value="/borrow-book/borrow-book-choose-user">					
							<c:param name="borrowBookStartResult" value="${showLessLinkValue}"/>					
						</c:url>
					
	
								<p id="result-paragraph">Znaleziono: ${amountOfResults}</p><br>
								<button class="nav-small-button" onclick="window.location.href='${showMoreLink}'"> >>> </button>
								<button class="nav-special-button"> ${resultRange} </button>
								<button class="nav-small-button" onclick="window.location.href='${showLessLink}'"> <<< </button>
			
						
						<table>
							<tr>
								<th id="id-column">Id</th>
								<th id="firstname-column">First Name</th>
								<th id="lastname-column">Last Name</th>
								<th id="email-column">Email</th>
								<th id="action-column">Action</th>
							</tr>
							
							<c:forEach var="tempUser" items="${usersList}">
							
								<c:url var="addUserLink" value="/borrow-book/borrow-book-choose-books">					
									<c:param name="selectedUserId" value="${tempUser.id}"/>					
								</c:url>
								
								<tr>
									<td id="id-column">${tempUser.id }</td>
									<td id="firstname-column">${tempUser.firstName }</td>
									<td id="lastname-column">${tempUser.lastName }</td>
									<td id="email-column">${tempUser.email }</td>
									<td><button class="small-button" onclick="window.location.href='${addUserLink}'">Dodaj</button></td>			
								</tr>
							</c:forEach>
						</table>
				
					</div>
					
			</div>
		

</body>
</html>