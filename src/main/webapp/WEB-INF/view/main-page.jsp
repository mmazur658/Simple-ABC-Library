<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!doctype html>
<html lang="pl">

<head>
	<%@ include file="/resources/parts/navbar-main-style.jsp" %> 
	<%@ include file="/resources/parts/header.jsp" %> 
	<title><spring:message code="view.main-page.title"/></title>
</head>

<body>
<script>document.body.className += ' fade-out';</script>
	<%@ include file="/resources/parts/nav.jsp" %>  	
	
	<spring:message var="previous" code="label.previous"/>
	<spring:message var="next" code="label.next"/>
	
	<div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
	  <div class="carousel-inner">
	    <div class="carousel-item active">
	      <img class="d-block w-100" src="<%=request.getContextPath()%>/resources/image/car1.jpg" alt="First slide">
	    </div>
	    <div class="carousel-item">
	      <img class="d-block w-100" src="<%=request.getContextPath()%>/resources/image/car2.jpg" alt="Second slide">
	    </div>
	    <div class="carousel-item">
	      <img class="d-block w-100" src="<%=request.getContextPath()%>/resources/image/car3.jpg" alt="Third slide">
	    </div>
	  </div>
	  <a class="carousel-control-prev" href="#carouselExampleControls" role="button" data-slide="prev">
	    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
	    <span class="sr-only">${previous }</span>
	  </a>
	  <a class="carousel-control-next" href="#carouselExampleControls" role="button" data-slide="next">
	    <span class="carousel-control-next-icon" aria-hidden="true"></span>
	    <span class="sr-only">${next }</span>
	  </a>
	</div>
	
	<%@ include file="/resources/parts/footer.jsp" %>  

<script>
	$(function(){
		$('.carousel').carousel({
			  interval: 3000;
		})
	})
</script>
		
<script>
$(function() {
    $('body').removeClass('fade-out');
});
</script>
</body>
</html>