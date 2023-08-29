<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="title"%>

<!DOCTYPE html>
<html lang="fr" class="h-100">
	<head>
		<title><c:out value="${title}" /></title>
		<meta charset="utf-8" />
		<link rel="icon" type="image/png" href="<c:url value="/img/icon.png" />"/>
		<link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet" integrity="sha384-Zenh87qX5JnK2Jl0vWa8Ck2rdkQ2Bzep5IDxbcnCeuOxjzrPF/et3URy9Bv1WTRi" crossorigin="anonymous">
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css" />"/>
		<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.2/font/bootstrap-icons.css"/>
	    <script src="<c:url value="/js/bootstrap.min.js" />" integrity="sha384-Atwg2Pkwv9vp0ygtn1JAojH0nYbwNJLPhwyoVbhoPwBhjQPR5VtM2+xf0Uwh9KtT" crossorigin="anonymous"></script>
	</head>
	<body class="d-flex h-all text-center text-bg-dark vsc-initialized">
    <div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
      <header class="mb-5">
        <div>
          <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container">
              <h3>
                <a class="nav-link py-1 px-0" href="<c:url value="/" />">
                  <img src="<c:url value="/img/spy.png" />" alt="Logo" width="30" height="24">
                  Codenames
                </a>
              </h3>
              <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarToggle" aria-controls="navbarToggle" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
              </button>
              <div class="collapse navbar-collapse justify-content-end" id="navbarToggle">
                <ul class="navbar-nav">
                  <li class="nav-item">
                    <a class="nav-link active fw-bold" aria-current="page" href="<c:url value="/" />">Home</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/statistics" />">Statistics</a>
                  </li>
                  <li class="nav-item">
                    <a class="nav-link" href="<c:url value="/jsp/rules.jsp" />">Rules</a>
                  </li>
                  <c:if test="${sessionScope.username == null}">
                    <a class="nav-link" href="<c:url value="/login" />">Login</a>
		              </c:if>
                  <c:if test="${sessionScope.username != null}">
		                <li class="nav-item dropdown">
				            <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown" aria-expanded="false">${sessionScope.username}</a>
				            <ul class="dropdown-menu">
				              <li><a class="dropdown-item" href="<c:url value="/logout" />">Logout</a></li>
				            </ul>
			          	</li>
		              </c:if>
                </ul>
              </div>
            </div>
          </nav>
        </div>
      </header>
      
      <jsp:doBody />
    </div>
	</body>
</html>
