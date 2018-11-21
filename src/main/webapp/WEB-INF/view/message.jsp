<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %> 
	<%@ include file="/resources/parts/navbar-style.jsp" %> 
	<title><spring:message code="view.message.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="date" code="message.date"/>
	<spring:message var="recipient" code="message.recipient"/>
	<spring:message var="sender" code="message.sender"/>
	<spring:message var="subject" code="message.subject"/>
	<spring:message var="reply" code="message.reply"/>
	<spring:message var="unread" code="message.unread"/>
	<spring:message var="back-button" code="button.back"/>
	
	<div class="container w-50 pt-5 mt-5">	

	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>

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
	  		<th scope="row">${date }:</th>
	  		<td><fmt:formatDate value="${message.startDate }" pattern="HH:mm dd-MM-yyyy"/></td>
		</tr>
		<tr>
			<c:choose>
				<c:when test="${modelBoxType == 'sent' }">
					<th class="message-th-w" scope="row">${recipient }: </th>
					<td>${message.recipient.email}</td>
				</c:when>
				<c:otherwise>
					<th class="message-th-w" scope="row">${sender }: </th>
					<td>${message.sender.email}</td>
				</c:otherwise>
			</c:choose>	
		</tr>
		<tr>
			<th class="message-th-w" scope="row">${subject }: </th>
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
			<td class="message-b-btn"><a href="${replyMessageLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${reply }</a></td>
			<td class="message-b-btn"><a href="${closeMessageAndSetStatusUnreadLink}" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true" >${unread }</a></td>
			<td class="message-b-btn">	
				<c:choose>
					<c:when test="${modelBoxType == 'sent' }">
						<a href="${pageContext.request.contextPath}/message-module/message-box-sent" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true">${back-button}</a>
					</c:when>
					<c:otherwise>
						<a href="${pageContext.request.contextPath}/message-module/message-box-inbox" class="btn btn-sm btn-secondary btn-block shadow" role="button" aria-pressed="true">${back-button}</a>
					</c:otherwise>
				</c:choose>
			</td>
		<tr>
	</table>
	
	</div>

<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>