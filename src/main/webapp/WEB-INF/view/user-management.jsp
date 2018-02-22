<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html ">
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/management-style.css" />
		<title>Simple ABC Library -  Zarządzanie Użytkownikami</title>
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
		
			<form class="form-signin" action="user-management">	
				<a href="${pageContext.request.contextPath}/user/main"><img id="big-logo" src="<%=request.getContextPath()%>/resources/image/ABC_logo.png" alt="ABC Big Logo"></a>
				<h3 class="h3-heading">Zarządzanie Użytkownikami</h3>		
				<input class="form-control" placeholder="ID" type="text" name="userManagementUserId" value = "<%=(session.getAttribute("userManagementUserId")==null) ? "" : session.getAttribute("userManagementUserId")%>"></label>
				<input class="form-control" placeholder="Imię" type="text" name="userManagementFirstName" value = "<%=(session.getAttribute("userManagementFirstName")==null) ? "" : session.getAttribute("userManagementFirstName")%>"></label>
				<input class="form-control" placeholder="Nazwisko" type="text" name="userManagementLastName" value = "<%=(session.getAttribute("userManagementLastName")==null) ? "" : session.getAttribute("userManagementLastName")%>"></label>
				<input class="form-control" placeholder="Email" type="text" name="userManagementEmail" value = "<%=(session.getAttribute("userManagementEmail")==null) ? "" : session.getAttribute("userManagementEmail")%>"></label>
				<input class="form-control" placeholder="Pesel" type="text" name="userManagementPesel" value = "<%=(session.getAttribute("userManagementPesel")==null) ? "" : session.getAttribute("userManagementPesel")%>"></label>
				<button class="big-button" type="submit">Szukaj</button>			
			</form>
		
			<div class="return-container">			
				<form action ="clearUserSearchParameters" >
					<button class="big-button" type="submit">Wyczyść dane wyszukiwania</button>	
				</form>
			</div>
		
			<div class="container">
			
				<c:url var="showMoreLink" value="/user/user-management">					
					<c:param name="userManagementStartResult" value="${showMoreLinkValue}"/>					
				</c:url>
				<c:url var="showLessLink" value="/user/user-management">					
					<c:param name="userManagementStartResult" value="${showLessLinkValue}"/>					
				</c:url>
			
				<p id="result-paragraph">Znaleziono: ${amountOfResults}</p><br>
				<button class="nav-small-button" onclick="window.location.href='${showMoreLink}'"> >>> </button>
				<button class="nav-special-button"> ${resultRange} </button>
				<button class="nav-small-button" onclick="window.location.href='${showLessLink}'"> <<< </button>
				<br><br>
			
				<table>
					<caption>Użytkownicy</caption>
					<tr>
						<th id="id-column">Id</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Email</th>
						<th id="pesel-column">Pesel</th>
						<th id="action-column">Action</th>
					</tr>
					
					<c:forEach var="tempUser" items="${usersList}">
					
						<c:url var="userDetailsLink" value="/user/user-details">					
							<c:param name="userDetailsUserId" value="${tempUser.id}"/>	
							<c:param name="userDetailsWayBack" value="user-management"/>		
						</c:url>
					<tr>
						<td id="id-column">${tempUser.id }</td>
						<td>${tempUser.firstName }</td>
						<td>${tempUser.lastName }</td>
						<td>${tempUser.email }</td>
						<td id="pesel-column">${tempUser.pesel }</td>
						<td id="action-column"><button class="small-button" onclick="window.location.href='${userDetailsLink}'">Szczegóły</button></td>			
					</tr>
					</c:forEach>			
					
				</table>
			
			
			
			
			
			</div>		
		</div>

		
<a href="${pageContext.request.contextPath}/user/main">Powrót</a>
</body>
</html>