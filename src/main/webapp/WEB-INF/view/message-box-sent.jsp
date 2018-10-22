<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title>Simple ABC Library - Wiadomości</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container pt-5 mt-4">	
	
	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
	<h1 class="h3 mb-3 mt-3 font-weight-bold float-left">Elementy Wysłane</h1>
	<button class="btn btn-sm btn-block w-25 btn-secondary float-right mt-4 shadow" onclick="window.location.href='${pageContext.request.contextPath}/message-module/create-new-message'">Nowa wiadomość</button>
	<button class="btn btn-sm btn-block w-25 btn-secondary float-right mt-4 mr-1 shadow" onclick="window.location.href='${pageContext.request.contextPath}/message-module/message-box-inbox'">Odebrane</button>
	
	<table class="table table-hover table-sm">
		  <thead>
		    <tr>
		      <th scope="col">Data</th>
		      <th scope="col">Odbiorca</th>
		      <th scope="col">Temat</th>
		      <th scope="col">Akcja</th>
		    </tr>
		</thead>
		<tbody>
			
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
					<td class="font-weight-bold" onclick="window.location.href='${openMessageLink}'"><fmt:formatDate value="${tempMessage.startDate }" pattern="HH:mm dd-MM-yyyy"/></td>
					<td class="font-weight-bold" onclick="window.location.href='${openMessageLink}'">${tempMessage.recipient.email}</td>
					<td class="font-weight-bold" onclick="window.location.href='${openMessageLink}'">${tempMessage.subject }</td> 
				</c:when>
				<c:otherwise>
					<td onclick="window.location.href='${openMessageLink}'"><fmt:formatDate value="${tempMessage.startDate }" pattern="HH:mm dd-MM-yyyy"/></td>
					<td onclick="window.location.href='${openMessageLink}'">${tempMessage.recipient.email}</td>
					<td onclick="window.location.href='${openMessageLink}'">${tempMessage.subject }</td>
				</c:otherwise>
			</c:choose>
					
			<td>
				<button class="btn btn-secondary btn-sm shadow" onclick="window.location.href='${deleteMessageLink}'">Usuń</button>
				<button class="btn btn-secondary btn-sm shadow" onclick="window.location.href='${readUnreadMessageLink}'">P/N</button>
			</td>						
		</tr>
		
		</c:forEach>

	</table>

		<nav aria-label="Page navigation example">
			
				<c:url var="showMoreLink" value="/message-module/message-box-sent">					
					<c:param name="messageSentStartResult" value="${showMoreLinkValue}"/>					
				</c:url>
				<c:url var="showLessLink" value="/message-module/message-box-sent">					
					<c:param name="messageSentStartResult" value="${showLessLinkValue}"/>					
				</c:url>
			
			  <ul class="pagination justify-content-end">
			    <li class="page-item"><a class="page-link text-dark" href="${showLessLink}"> <<< </a></li>
			    <li class="page-item"><p class="page-link text-dark" >${resultRange} z ${amountOfResults}</p></li>
			    <li class="page-item"><a class="page-link text-dark" href="${showMoreLink}"> >>> </a></li>
			  </ul>
		</nav>

	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>