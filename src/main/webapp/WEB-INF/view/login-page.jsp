<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html>
<head>
	<link rel="icon"  type="image/x-icon" href="<%=request.getContextPath()%>/resources/image/favicon.ico">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Simple ABC Library</title>

	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/style.css" />
	
</head>
<body>
  <div class="wrapper">
	    <form class="form-signin" action="login" method="post" > 
	    <img id="logo" src="<%=request.getContextPath()%>/resources/image/ABC_mini_logo_v2.png" alt="ABC Logo">      
	      	<h2 class="form-signin-heading">Simple ABC Library</h2>
	      <input class="form-control" type="text"  name="email" placeholder="Email " required/>
	      <input class="form-control" type="password" name="password" placeholder="Hasło" required/> <br>   
	      <p class="error-message">${incorrectPasswordMessage}</p>  
	      <button class="big-button" type="submit">Zaloguj</button>
	    </form>
		<div class="container">
			<button class="big-button" onclick="window.location.href='${pageContext.request.contextPath}/user/add-user'">Utwórz nowe konto</button>
		</div>
	    	
  </div>

</body>
</html>