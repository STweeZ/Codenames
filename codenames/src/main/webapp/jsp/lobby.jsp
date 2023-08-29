<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ili" tagdir="/WEB-INF/tags"%>

<ili:page title="Lobby ${game.getHashId()} - Codenames">
	<jsp:body>
        <main>
	        <div class="container-fluid">
		        <form method="post">
					<div class="row d-flex justify-content-around">
						<h1 class="pb-4">Game n°${game.getHashId()}</h1>
						<div class="col-md-4 blue-team">
							<div class="col-auto my-1">
								<c:if test="${!game.getBlueTeam().fullDecoders() && !game.playerIsBlueDecoder(username)}">
									<button type="submit" class="btn btn-primary" name="blue-decoder"><i class="bi bi-person-plus role-icon"></i>DECODER</button>
								</c:if>
								<c:if test="${game.getBlueTeam().getSpy() == null}">
									<button type="submit" class="btn btn-primary" name="blue-spy"><i class="bi bi-search role-icon"></i>SPY</button>
								</c:if>
							</div>
							<div class="list-group m-4 players-distribution">
								<c:forEach var="player" items="${game.getBlueTeam().getPlayers()}">
									<a class="list-group-item list-group-item-action list-group-item-${player.isSpy() ? 'danger' : 'action'} disabled">${player.getUsername()}</a>
								</c:forEach>
							</div>
						</div>
						<div class="col-md-4 red-team">
							<div class="col-auto my-1">
								<c:if  test="${!game.getRedTeam().fullDecoders() && !game.playerIsRedDecoder(username)}">
									<button type="submit" class="btn btn-danger" name="red-decoder"><i class="bi bi-person-plus role-icon"></i>DECODER</button>
								</c:if>
								<c:if  test="${game.getRedTeam().getSpy() == null}">
									<button type="submit" class="btn btn-danger" name="red-spy"><i class="bi bi-search role-icon"></i>SPY</button>
								</c:if>
							</div>
							<div class="list-group m-4 players-distribution">
								<c:forEach var="player" items="${game.getRedTeam().getPlayers()}">
									<a class="list-group-item list-group-item-action list-group-item-${player.isSpy() ? 'danger' : 'action'} disabled">${player.getUsername()}</a>
								</c:forEach>
							</div>
						</div>
					</div>
					<c:if test="${game.amIMaster(username)}">
						<button type="submit" class="btn btn-light" name="launch" ${game.gameRequirement() ? '' : 'disabled'}>Launch Game</button>
					</c:if>
					<c:if test="${username != null && game.playerInGameByUsername(username)}">
				    	<button type="submit" class="btn btn-light" name="quit">Quit Lobby</button>
				    </c:if>
				</form>
			</div>
	      </main>
		<jsp:include page="websocket.jsp" />
    </jsp:body>
</ili:page>
