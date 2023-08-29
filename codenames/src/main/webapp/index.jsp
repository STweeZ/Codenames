<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ili" tagdir="/WEB-INF/tags"%>

<ili:page title="Home - Codenames">
	<jsp:body>
        <main>
	        <h1 class="p-4">Welcome on Codenames</h1>
	        <div class="p-5">
		        <c:if test="${sessionScope.username == null}">
		            <p class="lead">
			            <b>
			                You must login before creating or joining a game.
		                </b>
		            </p>
		        </c:if>
	        	<c:if test="${sessionScope.username != null}">
		            <p class="lead">
		                <a href="<c:url value="/create" />" class="btn btn-lg btn-light border-white bg-white">Create a game</a>
		            </p>
		            <p class="lead">
		                <a href="<c:url value="/join" />" class="btn btn-lg btn-light border-white bg-white">Join a game</a>
		            </p>
		        </c:if>
            </div>
	      </main>
    </jsp:body>
</ili:page>
