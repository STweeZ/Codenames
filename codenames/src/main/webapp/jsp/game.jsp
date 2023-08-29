<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ili" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="codeili" uri="http://www.univ-artois.fr/master2ili/jai"%>

<ili:page title="Game ${game.getHashId()} - Codenames">
	<jsp:body>
		<main>
			<div class="container-fluid">
				<h1 class="pb-4">
					<c:choose>
						<c:when test="${game.isOver()}">
							<span class="${game.getBlueTeam().isWon() ? "blue-team" : "red-team"}">${game.getBlueTeam().isWon() ? "Blue" : "Red"}</span> team <span class="winner-announcement">won</span>
						</c:when>
						<c:otherwise>
							<span class="${game.isBlueTurn() ? "blue-team" : "red-team"}">${game.isBlueTurn() ? "Blue" : "Red"}</span> ${game.isSpyTurn() ? "spy" : "decoders"} turn
						</c:otherwise>
					</c:choose>
				</h1>
				<div class="row remaining-cards">
					<div class="col-md-3">
						<h3 class="text-left"><span class="blue-team">${game.blueCardsRemaining()} word${game.blueCardsRemaining() > 1 ? 's' : ''}</span> left</h3>
					</div>
					<div class="col-md-6">
					</div>
					<div class="col-md-3">
						<h3 class="text-left"><span class="red-team">${game.redCardsRemaining()} word${game.redCardsRemaining() > 1 ? 's' : ''}</span> left</h3>
					</div>
				</div>
				<div class="row">
					<div class="col-md-3 blue-team-composition">
						<c:if test="${game.isOver() || !game.playerInGameByUsername(username)}">
							<div class="col-auto my-1">
								<form method="post">
									<c:if test="${!game.getBlueTeam().fullDecoders() && !game.playerIsBlueDecoder(username)}">
										<button type="submit" class="btn btn-primary" value="blue-decoder" name="role-choice"><i class="bi bi-person-plus role-icon"></i>DECODER</button>
									</c:if>
									<c:if test="${game.getBlueTeam().getSpy() == null}">
										<button type="submit" class="btn btn-primary" value="blue-spy" name="role-choice"><i class="bi bi-search role-icon"></i>SPY</button>
									</c:if>
								</form>
							</div>
						</c:if>
						<div class="list-group m-4 players-distribution">
							<c:forEach var="player" items="${game.getBlueTeam().getPlayers()}">
								<a class="list-group-item list-group-item-action list-group-item-${player.isSpy() ? 'danger' : 'action'}">${player.getUsername()}</a>
							</c:forEach>
						</div>
					</div>
					<div class="col-md-6 game-board">
						<form method="post">
							<codeili:card var="card" items="${game.getGrid().getCards()}">
								<button type="submit" name="cardChoice" value="${card_name.toLowerCase()}" class="mb-1 col-md-card btn btn-light text-truncate card-${game.isSpy(username) || card_state || game.isOver() ? card_type  : 'hidden'} ${game.isOver() && !card_state ? 'disabled' : ''}">${card_name} ${game.isSpyTurn() || card_qty == 0 ? '' : card_qty }</button>
							</codeili:card>
						</form>
					</div>
					<div class="col-md-3 red-team-composition">
						<c:if test="${game.isOver() || !game.playerInGameByUsername(username)}">
							<div class="col-auto my-1">
								<form method="post">
									<c:if test="${!game.getRedTeam().fullDecoders() && !game.playerIsRedDecoder(username)}">
										<button type="submit" class="btn btn-danger" value="red-decoder" name="role-choice"><i class="bi bi-person-plus role-icon"></i>DECODER</button>
									</c:if>
									<c:if test="${game.getRedTeam().getSpy() == null}">
										<button type="submit" class="btn btn-danger" value="red-spy" name="role-choice"><i class="bi bi-search role-icon"></i>SPY</button>
									</c:if>
								</form>
							</div>
						</c:if>
						<div class="list-group m-4 players-distribution">
							<c:forEach var="player" items="${game.getRedTeam().getPlayers()}">
								<a class="list-group-item list-group-item-action list-group-item-${player.isSpy() ? 'danger' : 'action'}">${player.getUsername()}</a>
							</c:forEach>
						</div>
					</div>
				</div>
				<c:choose>
					<c:when test="${game.isSpy(username) && game.isMyTurn(username) && !game.isOver()}">
						<div id="input-clue-occurence" class="m-2">
							<form method="post">
								<div class="row mb-2">
									<div class="col-md-3"></div>
									<div class="col-md-3 mb-1">
										<input type="text" name="clue" class="form-control" placeholder="Your clue" required>
								  	</div>
								  	<div class="col-md-3">
								  		<input type="number" name="occurence" class="form-control"  placeholder="Occurence" required>
									</div>
									<div class="col-md-3"></div>
								</div>
								<div class="md-4">
									<button type="submit" class="btn btn-light">Give this clue</button>
								</div>
							</form>
						</div>
					</c:when>
					<c:when test="${!game.isSpyTurn() && !game.isOver()}">
						<div id="clue-occurence" class="m-2">
							<span>Clue : <b>${game.clue}</b> with <b>${game.occurence}</b> occurence${game.occurence > 1 ? 's' : ''}</span>
						</div>
						<c:if test="${game.isMyTurn(username)}">
							<div id="vote-end-round" class="m-5">
								<form method="post">
							      <button type="submit" name="voteEndRound" class="btn btn-light">Vote end Round (${game.getNumberVoteEndRound()})</button>
								</form>
							</div>
						</c:if>
					</c:when>
					<c:when test="${game.isOver() && game.amIMaster(username)}">
						<div id="new-game" class="m-5">
							<form method="post">
						      <button type="submit" name="newGame" class="btn btn-light">New Game</button>
							</form>
						</div>
					</c:when>
				</c:choose>
			</div>
		</main>
		<jsp:include page="websocket.jsp" />
    </jsp:body>
</ili:page>
