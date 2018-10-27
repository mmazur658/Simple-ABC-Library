<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title><spring:message code="view.create-new-message.title"/></title>
</head>

<body>

	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<spring:message var="heading" code="view.create-new-message.heading"/>
	<spring:message var="recipient" code="message.recipient"/>
	<spring:message var="subject" code="message.subject"/>
	<spring:message var="sendButton" code="button.send"/>
	
	<div class="container w-50  pt-5 mt-4">	

	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
	<form action="send-message" method="POST" id="messageForm" >
	<table class="table table-sm mt-2">
		<tr>
			<td colspan="2"><h1 class="h3 mb-1 mt-1 font-weight-bold float-left">${heading}</h1></td>	
		</tr>	
		<tr>
			<th class="message-th-w pt-2" scope="row">${recipient } </th>
			<td><input class="form-control" type="email" name="recipient" required value="${message.sender.email}" placeholder="${recipient }"></td>	
		</tr>
		<tr>
			<th class="message-th-w pt-2" scope="row">${subject } </th>
			<td><input class="form-control" type="text" name="subject" required value="${message.subject }" placeholder="${subject }"></td>
		<tr>
			<td class="message-td" colspan="2">
				<div class="message-div">
					<textarea class="message-textarea" name="textArea" form="messageForm"></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2"><button class="btn btn-sm btn-secondary btn-block mt-2 shadow" type="submit">${sendButton }</button></td>
		</tr>
	</table>
	</form>	
</div>

<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>