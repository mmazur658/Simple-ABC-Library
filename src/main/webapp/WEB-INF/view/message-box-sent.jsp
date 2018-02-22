<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html >
<html>
	<head>
		<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/message-box-style.css" />
		<title>Simple ABC Library - Message Box - Wysłane</title>
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
		
				<c:url var="showMoreLink" value="/message-module/message-box-sent">					
					<c:param name="messageSentStartResult" value="${showMoreLinkValue}"/>					
				</c:url>
				<c:url var="showLessLink" value="/message-module/message-box-sent">					
					<c:param name="messageSentStartResult" value="${showLessLinkValue}"/>					
				</c:url>
		
				<div>
					<br><br>
					<h2 class="h2-heading">Wysłane - Wiadomości ${amountOfResults}</h2>
					
					<button class="nav-small-button" onclick="window.location.href='${showMoreLink}'"> >>> </button>
					<button class="nav-special-button"> ${resultRange} </button>
					<button class="nav-small-button" onclick="window.location.href='${showLessLink}'"> <<< </button>					
					<button class="nav-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/create-new-message'">Nowa Wiadomość</button>
					<button class="nav-button" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-inbox'">Skrzynka Odbiorcza</button>
					<br><br>
				</div>

				<table>
					<tr>
						<th id="date-column">Data</th>
						<th id="sender-column">Odbiorca</th>
						<th id="subject-column">Temat</th>
						<th id="action-column">Akcja</th>
					</tr>
					
					<c:forEach var="tempMessage" items="${userSentMessagesList}">
					
						<c:url var="deleteMessageLink" value="/message-module/deleteMessage">					
							<c:param name="messageId" value="${tempMessage.id}"/>
							<c:param name="boxType" value="sent"/>						
						</c:url>
						<c:url var="readUnreadMessageLink" value="/message-module/readUnreadMessage">					
							<c:param name="messageId" value="${tempMessage.id}"/>	
							<c:param name="boxType" value="sent"/>				
						</c:url>
						
						<c:url var="openMessageLink" value="/message-module/openMessage">					
							<c:param name="messageId" value="${tempMessage.id}"/>	
							<c:param name="boxType" value="sent"/>				
						</c:url>
					
					<tr>
										
						<c:choose>
							<c:when test="${tempMessage.senderIsRead != 'true'}">
								<td id="date-column" style="font-weight: bold"><a href="${openMessageLink}"><fmt:formatDate value="${tempMessage.startDate }" pattern="HH:mm dd-MM"/></a></td>
								<td id="sender-column" style="font-weight: bold"><a href="${openMessageLink}">${tempMessage.recipient.email}</a></td>
								<td id="subject-column" style="font-weight: bold"><a href="${openMessageLink}">${tempMessage.subject }</a></td> 
							</c:when>
							<c:otherwise>
								<td id="date-column"><a href="${openMessageLink}"><fmt:formatDate value="${tempMessage.startDate }" pattern="HH:mm dd-MM"/></a></td>
								<td id="sender-column"><a href="${openMessageLink}">${tempMessage.recipient.email}</a></td>
								<td id="subject-column"><a href="${openMessageLink}">${tempMessage.subject }</a></td>
							</c:otherwise>
						</c:choose>
								
						<td id="action-column">
								<button class="small-button" onclick="window.location.href='${deleteMessageLink}'">Usuń</button>
								<button class="small-button" onclick="window.location.href='${readUnreadMessageLink}'">Przeczyt./Nie prz.</button>
								</td>						
							</tr>
					
					</c:forEach>			
				</table>
			</div>
		</div>

</body>
</html>