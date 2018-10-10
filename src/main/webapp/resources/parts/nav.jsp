<%@ page pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-sm navbar-dark bg-dark">
		<a class="navbar-brand" href="${pageContext.request.contextPath}/user/main">Simple ABC Library</a>
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
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/user-details">Twoje Dane</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/users-books">Książki</a>
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/change-password-form">Zmiana hasła</a>
						<div class="dropdown-divider"></div>						
						<a class="dropdown-item" href="${pageContext.request.contextPath}/user/logout">Wyloguj</a>
					</div>
				</li>		
				<c:if test="${userAccessLevel eq 'Employee' or  userAccessLevel eq 'Administrator' }">
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle text-white" href="#" id="dropdown03" data-toggle="dropdown"
							 aria-haspopup="true" aria-expanded="false">Pracownik</a>
						<div class="dropdown-menu" aria-labelledby="dropdown03">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/book/add-book-form">Dodaj Książkę</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/borrow-book/borrow-book-choose-user">Wydaj Książkę</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/return-book/return-book-choose-user">Przyjmij Książkę</a>
							<div class="dropdown-divider"></div>	
							<a class="dropdown-item" href="${pageContext.request.contextPath}/user/user-management">Zarządzaj Użytkownikami</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/reservation/reservation-management">Zarządzaj Rezerwacjami</a>
						</div>
					</li>	
				</c:if>
				<c:if test="${userAccessLevel eq 'Administrator' }">	
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle" href="#" id="dropdown03" data-toggle="dropdown"
							 aria-haspopup="true" aria-expanded="false">Administrator</a>
						<div class="dropdown-menu" aria-labelledby="dropdown03">
							<a class="dropdown-item" href="${pageContext.request.contextPath}/adminstrator/errors-and-problems'">Błędy i Problemy</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/adminstrator/configuration">Konfiguracja</a>
						</div>
					</li>	
				</c:if>				
					
				<li class="nav-item active">
					<a class="nav-link" href="${pageContext.request.contextPath}/book/main-bookstore">Biblioteka
						<span class="sr-only">(current)</span>
					</a>
				</li>
				<li class="nav-item active">
					<a class="nav-link" href="${pageContext.request.contextPath}/message-module/message-box-inbox">Wiadomości
						<span class="sr-only">(current)</span>
					</a>
				</li>	
			</ul>			
		</div>
	</nav>