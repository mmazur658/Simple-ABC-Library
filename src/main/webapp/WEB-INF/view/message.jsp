<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html >
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main-small.css" />
		<title>Simple ABC Library - Message </title>
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
			
			<table>
				<tr>
					<td>Data: </td><td><fmt:formatDate value="${message.startDate }" pattern="HH:mm dd-MM"/></td>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${modelBoxType == 'sent' }">
							<td>Odbiorca: </td><td>${message.recipient.email}</td>
						</c:when>
						<c:otherwise>
							<td>Nadawca: </td><td>${message.sender.email}</td>
						</c:otherwise>
					</c:choose>					
				</tr>
				<tr>
					<td>Temat: </td><td>${message.subject}</td>
				</tr>
				
			</table>		
				<p id="message-text">${message.text}</p>
	
			<c:url var="closeMessageAndSetStatusUnreadLink" value="/message-module/close-message-and-set-status">					
				<c:param name="messageId" value="${message.id}"/>	
				<c:param name="boxType" value="${modelBoxType}"	/>
			</c:url>
			<c:url var="replyMessageLink" value="/message-module/reply-message">					
				<c:param name="messageId" value="${message.id}"/>
				<c:param name="boxType" value="${modelBoxType}"	/>					
			</c:url>
	
			<button class="big-button" onclick="window.location.href='${replyMessageLink}'">Odpowiedz </button>
			<button class="big-button" onclick="window.location.href='${closeMessageAndSetStatusUnreadLink}'">Nieprzeczytane </button>
		
			<c:choose>
				<c:when test="${modelBoxType == 'sent' }">
					<button class="small-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-sent'">Powrót </button>
				</c:when>
				<c:otherwise>
					<button class="small-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-inbox'">Powrót </button>
				</c:otherwise>
			</c:choose>

			</div>
		</div>

</body>
</html>