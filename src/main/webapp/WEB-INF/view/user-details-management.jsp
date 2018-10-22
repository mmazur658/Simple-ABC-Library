<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
	<%@ include file="/resources/parts/header.jsp" %>  
	<%@ include file="/resources/parts/navbar-style.jsp" %>
	<title>Simple ABC Library - Szczegóły Użytkwonika</title>
</head>
<body>
	<%@ include file="/resources/parts/nav.jsp" %>  
	
	<div class="container w-50  pt-5 mt-4">		
			
		<table class="table table-borderedless w-75 mb-0" style="text-align: center;">
			<tr>
				<td><h1 class="h3 mb-0 font-weight-normal ">Dane Użytkownika</h1></td>			
			</tr>
		</table>
		
		<table class="table table-sm table-bordered w-75">
		  <tbody>
		    <tr>
		      <th scope="row">Id: </th>
		      <td>${theUser.id}</td>	
		    </tr>
		   	<tr>
		      <th scope="row">Imię: </th>
		      <td>${theUser.firstName}</td>	
		    </tr>
		   	<tr>
		      <th scope="row">Nazwisko: </th>
		      <td>${theUser.lastName}</td>	
		    </tr>
		   	<tr>
		      <th scope="row">Email: </th>
		      <td>${theUser.email}</td>	
		    </tr>		    
		   	<tr>
		      <th scope="row">Pesel: </th>
		      <td>${theUser.pesel}</td>	
		    </tr>
		    <tr>
		      <th scope="row">Płeć: </th>
		      <td>${theUser.sex}</td>	
		    </tr>
		    <tr>
		      <th scope="row">Adres:</th>
		      <td>${theUser.street} ${theUser.houseNumber }</td>	
		    </tr>
		    <tr>
		      <th scope="row">Miasto: </th>
		      <td>${theUser.city} ${theUser.postalCode}</td>	
		    </tr>
		    <tr>
		      <th scope="row">Status: </th>
		      <td>${userAccessLevel}</td>	
		    </tr>
		    <tr>
		      <th scope="row">Urodziny: </th>
		      <td><fmt:formatDate value="${theUser.birthday}" pattern="yyyy-MM-dd"/></td>	
		    </tr>
		    <tr>
		      <th scope="row">Dołączył(a): </th>
		      <td><fmt:formatDate value="${theUser.startDate}" pattern="yyyy-MM-dd"/></td>	
		    </tr>	  
		  </tbody>
		</table>	
		
		<c:url var="updateUserLink" value="/user/user-management-update-form">					
			<c:param name="userDetailsUserId" value="${theUser.id}"/>					
		</c:url>
	
		<a href="${updateUserLink}" class="btn btn-sm btn-secondary btn-block w-75 shadow" role="button" aria-pressed="true">Aktualizaja Danych</a>
		<a href="#" class="btn btn-sm btn-secondary btn-block w-75 shadow" role="button" aria-pressed="true">Resetuj Hasło</a>
		<a href="#" class="btn btn-sm btn-secondary btn-block w-75 shadow" role="button" aria-pressed="true">Książki</a>
		<a href="#" class="btn btn-sm btn-secondary btn-block w-75 mb-5 shadow" role="button" aria-pressed="true">Zmiana Uprawnień</a>
	
	</div>

	<%@ include file="/resources/parts/footer.jsp" %> 
</body>
</html>