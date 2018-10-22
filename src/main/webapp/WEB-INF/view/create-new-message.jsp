<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title>Simple ABC Library - Nowa Wiadomość</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container w-50  pt-5 mt-4">	

	<c:if test="${not empty systemMessage}">
		<script>showToastrAlert("info","${systemMessage}");</script></c:if>
	
	<form action="send-message" method="POST" id="messageForm" >
	<table class="table table-sm mt-2">
		<tr>
			<td colspan="2"><h1 class="h3 mb-1 mt-1 font-weight-bold float-left">Nowa Wiadomość </h1></td>	
		</tr>	
		<tr>
			<th class="message-th-w pt-2" scope="row">Odbiorca: </th>
			<td><input class="form-control" type="email" name="recipient" required value="${message.sender.email}" placeholder="Odbiorca"></td>	
		</tr>
		<tr>
			<th class="message-th-w pt-2" scope="row">Temat: </th>
			<td><input class="form-control" type="text" name="subject" required value="${message.subject }" placeholder="Temat"></td>
		<tr>
			<td class="message-td" colspan="2">
				<div class="message-div">
					<textarea class="message-textarea" name="textArea" form="messageForm"></textarea>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="2"><button class="btn btn-sm btn-secondary btn-block mt-2 shadow" type="submit">Wyślij</button></td>
		</tr>
	</table>
	</form>	
</div>

<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>