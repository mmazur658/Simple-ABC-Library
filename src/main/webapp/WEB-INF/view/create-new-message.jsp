<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
		<title>Simple ABC Library - Nowa Wiadomość </title>
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
				
				<form class="form-signin"  action="send-message" method="POST" id="messageForm" >
					<h3 class="h3-heading">Nowa Wiadomość</h3>
					<input class="form-control" type="email" name="recipient" required value="${message.sender.email}" placeholder="Odbiorca">
					<input class="form-control" type="text" name="subject" required value="${message.subject }" placeholder="Temat">	
					<textarea name="textArea" form="messageForm"></textarea>
					<button class="big-button" type="submit">Wyślij</button>
				</form>				

			<div class="return-container">
				<button class="small-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-inbox'">Powrót</button>
				
			</div>
		</div>

	</body>
</html>