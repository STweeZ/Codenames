<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ili" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="codeili" uri="http://www.univ-artois.fr/master2ili/jai"%>

<ili:page title="Games - Codenames">
	<jsp:body>
        <main>
	        <div class="container-fluid">
				<div class="row d-flex justify-content-around">
					<div class="col-md-4">
						<h1 class="pb-4">Join a lobby</h1>
						<div class="list-group pb-4">
							<form method="post">
								<codeili:lobby var="game" items="${applicationScope.games}">
									<c:if test="${!game.hasStart()}">
										<button type="submit" name="gameHashID" value="${game_hash}" class="list-group-item list-group-item-action" ${game_style}>${game_hash} - ${game_qty} player(s)</button>
									</c:if>
								</codeili:lobby>
							</form>
						</div>
					</div>
					<div class="col-md-4">
						<h1 class="pb-4">Join a game</h1>
						<div class="list-group pb-4">
							<form method="post">
								<codeili:lobby var="game" items="${applicationScope.games}">
									<c:if test="${game.hasStart()}">
										<button type="submit" name="gameHashID" value="${game_hash}" class="list-group-item list-group-item-action" ${game_style}>${game_hash} - ${game_qty} player(s)</button>
									</c:if>
								</codeili:lobby>
							</form>
						</div>
					</div>
				</div>
			</div>
	      </main>
    </jsp:body>
</ili:page>
