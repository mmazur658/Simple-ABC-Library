<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<title>Simple ABC Library - XYZ</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container w-50">	

	<c:if test="${not empty systemMessage}">
		<div class="alert alert-primary mt-2" role="alert">
		   <strong>${systemMessage}</strong>
		</div>
	</c:if>

	<c:url var="closeMessageAndSetStatusUnreadLink" value="/message-module/close-message-and-set-status">					
		<c:param name="messageId" value="${message.id}"/>	
		<c:param name="boxType" value="${modelBoxType}"	/>
	</c:url>
	<c:url var="replyMessageLink" value="/message-module/reply-message">					
		<c:param name="messageId" value="${message.id}"/>
		<c:param name="boxType" value="${modelBoxType}"	/>					
	</c:url>

	<table class="table table-sm mt-2">
		<tr>     
	  		<th scope="row">Data:</th>
	  		<td><fmt:formatDate value="${message.startDate }" pattern="HH:mm dd-MM-yyyy"/></td>
		</tr>
		<tr>
			<c:choose>
				<c:when test="${modelBoxType == 'sent' }">
					<th class="message-th-w" scope="row">Odbiorca: </th>
					<td>${message.recipient.email}</td>
				</c:when>
				<c:otherwise>
					<th class="message-th-w" scope="row">Nadawca: </th>
					<td>${message.sender.email}</td>
				</c:otherwise>
			</c:choose>	
		</tr>
		<tr>
			<th class="message-th-w" scope="row">Temat: </th>
			<td>${message.subject}</td>
		<tr>
			<td class="message-td" colspan="2">
				<div class="message-div">
					${message.text}
				</div>
			</td>
		</tr>
		
	</table>
	
	<table class="table table-sm mt-2">
		<tr>
			<td class="message-b-btn"><a href="${replyMessageLink}" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Odpowiedz</a></td>
			<td class="message-b-btn"><a href="${closeMessageAndSetStatusUnreadLink}" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true" >Nieprzeczytane</a></td>
			<td class="message-b-btn">	
				<c:choose>
					<c:when test="${modelBoxType == 'sent' }">
						<a href="${pageContext.request.contextPath}/message-module/message-box-sent" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true">Powrót</a>
					</c:when>
					<c:otherwise>
						<a href="${pageContext.request.contextPath}/message-module/message-box-inbox" class="btn btn-sm btn-secondary btn-block" role="button" aria-pressed="true">Powrót</a>
					</c:otherwise>
				</c:choose>
			</td>
		<tr>
	</table>
	
	</div>

<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>