<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ili" tagdir="/WEB-INF/tags"%>

<ili:page title="Authentification - Codenames">
	<jsp:body>
        <main>
	        <h1 class="pb-4">Authentification</h1>
	        <div>
		        <c:if test="${sessionScope.username == null}">
					<form method="post">
					  <div class="form-row center align-items-center">
					    <div class="mb-2">
					      <input type="text" name="username" class="login-form form-control" id="inlineFormInputName" placeholder="Your username" required>
					    </div>
					    <div class="col-auto my-1">
					      <button type="submit" class="btn btn-light">Submit</button>
					    </div>
					  </div>
					</form>
		        </c:if>
	        	<c:if test="${sessionScope.username != null}">
		            <p class="lead">
		            	You are already logged in.
		            </p>
		        </c:if>
            </div>
	      </main>
    </jsp:body>
</ili:page>
