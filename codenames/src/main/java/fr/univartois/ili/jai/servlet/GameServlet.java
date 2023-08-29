package fr.univartois.ili.jai.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.Role;
import fr.univartois.ili.jai.websocket.Refresh;

@WebServlet("/game/*")
public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Game game = (Game) request.getAttribute("game");
		if (game != null) {
			if (!game.hasStart()) {
				response.sendRedirect(request.getServletContext().getContextPath() + "/lobby/" + game.getHashId());
			} else {
				request.getRequestDispatcher("/jsp/game.jsp").forward(request, response);
			}
		} else {
			response.sendRedirect(request.getServletContext().getContextPath() + "/jsp/404.jsp");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Game game = (Game) request.getAttribute("game");
		String username = (String) request.getSession().getAttribute("username");
		boolean haveToRefresh = false;

		if (game != null) {
			String role = request.getParameter("role-choice");
			if (role != null) {
				switch (role) {
					case "blue-decoder":
						game.playerJoinTeamInGame(game.getBlueTeam(), Role.DECODER, username);
						break;
					case "blue-spy":
						game.playerJoinTeamInGame(game.getBlueTeam(), Role.SPY, username);
						break;
					case "red-decoder":
						game.playerJoinTeamInGame(game.getRedTeam(), Role.DECODER, username);
						break;
					case "red-spy":
						game.playerJoinTeamInGame(game.getRedTeam(), Role.SPY, username);
						break;
				}
				haveToRefresh = true;
			}
			if (!game.isOver() && game.isMyTurn(username)) {
				String clue = request.getParameter("clue");
				String occurence = request.getParameter("occurence");
				String card = request.getParameter("cardChoice");
				String vote = request.getParameter("voteEndRound");
				if (game.isSpyTurn() && game.checkClueValidity(clue, occurence)) {
					game.spyTurn(clue, Integer.parseInt(occurence));
					haveToRefresh = true;
				} else if (!game.isSpyTurn() && game.checkCardVoteValidity(card)) {
					game.playerCardVote(username, card);
					haveToRefresh = true;
				} else if (!game.isSpyTurn() && vote != null) {
					game.addVoteEndRound(game.getPlayerByUsername(username));
					haveToRefresh = true;
				}
			} else if (game.isOver() && game.amIMaster(username)) {
				String newGame = request.getParameter("newGame");
				if (newGame != null && game.gameRequirement()) {
					game.resetGame();
					haveToRefresh = true;
				}
			}
			if(haveToRefresh) {
				refresh(game);
			}
		}
		doGet(request, response);
	}
	
	private void refresh(Game game) {
		Refresh.refresh(String.valueOf(game.getHashId()));
	}

}
