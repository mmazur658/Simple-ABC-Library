<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:message var="yourData" code="nav.your.data"/>
<spring:message var="books" code="nav.books"/>
<spring:message var="changePassword" code="nav.change-password"/>
<spring:message var="logout" code="nav.logout"/>
<spring:message var="addBook" code="nav.add.book"/>
<spring:message var="borrowBook" code="nav.borrow.book"/>
<spring:message var="returnBook" code="nav.return.book"/>
<spring:message var="userManagement" code="nav.user.management"/>
<spring:message var="reservationManagement" code="nav.reservation.management"/>
<spring:message var="employee" code="nav.employee"/>
<spring:message var="administrator" code="nav.administrator"/>
<spring:message var="errorAndProblems" code="nav.errors.and.problems"/>
<spring:message var="configuration" code="nav.configuration"/>
<spring:message var="library" code="nav.library"/>
<spring:message var="messagesModule" code="nav.messages"/>

<nav id="main-navbar" class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top shadow" >
		<a class="navbar-brand nav-item" href="${pageContext.request.contextPath}/user/main">Simple ABC Library</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"	data-target="#mainNavbar" aria-controls="mainNavbar"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="mainNavbar">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle text-white" href="#" id="dropdown03" data-toggle="dropdown"
						 aria-haspopup="true" aria-expanded="false"><%=session.getAttribute("userFirstName")%> <%=session.getAttribute("userLastName")%></a>
					<div class="dropdown-menu" aria-labelledby="dropdown03">
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/user-details">${yourData }</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/users-books">${books }</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/change-password-form">${changePassword }</a>
						<div class="dropdown-divider"></div>						
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/logout">${logout }</a>
					</div>
				</li>		
				<c:if test="${userAccessLevel eq 'Employee' or  userAccessLevel eq 'Administrator' }">
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle text-white" href="#" id="dropdown03" data-toggle="dropdown"
							 aria-haspopup="true" aria-expanded="false">${employee }</a>
						<div class="dropdown-menu" aria-labelledby="dropdown03">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/book/add-book-form">${addBook }</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/borrow-book/borrow-book-choose-user">${borrowBook }</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/return-book/return-book-choose-user">${returnBook }</a>
							<div class="dropdown-divider"></div>	
							<a class="dropdown-item" href="${pageContext.request.contextPath}/user/user-management">${userManagement }</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/reservation/reservation-management">${reservationManagement }</a>
						</div>
					</li>	
				</c:if>
				<c:if test="${userAccessLevel eq 'Administrator' }">	
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle text-white" href="#" id="dropdown03" data-toggle="dropdown"
							 aria-haspopup="true" aria-expanded="false">${administrator }</a>
						<div class="dropdown-menu" aria-labelledby="dropdown03">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/adminstrator/errors-and-problems'">${errorAndProblems }</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/adminstrator/configuration">${configuration }</a>
						</div>
					</li>	
				</c:if>				
					
				<li class="nav-item active">
					<a class="nav-link" href="${pageContext.request.contextPath}/book/main-bookstore">${library }
						<span class="sr-only">(current)</span>
					</a>
				</li>
				<li class="nav-item active">
					<a class="nav-link" href="${pageContext.request.contextPath}/message-module/message-box-inbox">${messagesModule }
						<span class="sr-only">(current)</span>
					</a>
				</li>	
			</ul>			
		</div>
		
		   	<a href="${pageContext.request.contextPath}/user/main?language=en_us">
    		<img class="mr-2" style="opacity: 0.7;" src="<%=request.getContextPath()%>/resources/image/united-kingdom-flag.png" alt="United Kingdom Flag" >
    	</a>
    	
    	<a href="${pageContext.request.contextPath}/user/main?language=pl_pl">
    		<img class="mr-5" style="opacity: 0.7;" src="<%=request.getContextPath()%>/resources/image/poland-flag.png" alt="Poland Flag" >
    	</a>
	</nav>