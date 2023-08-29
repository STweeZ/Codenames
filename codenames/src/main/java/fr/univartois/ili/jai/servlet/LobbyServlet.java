package fr.univartois.ili.jai.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.Role;
import fr.univartois.ili.jai.websocket.Refresh;

@WebServlet("/lobby/*")
public class LobbyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Game game = (Game) request.getAttribute("game");
		if (game != null) {
			if (game.hasStart()) {
				response.sendRedirect(request.getServletContext().getContextPath() + "/game/" + game.getHashId());
			} else {
				request.getRequestDispatcher("/jsp/lobby.jsp").forward(request, response);
			}
		} else {
			response.sendRedirect(request.getServletContext().getContextPath() + "/");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Enumeration<String> e = request.getParameterNames();
		Game game = (Game) request.getAttribute("game");
		String username = (String) request.getSession().getAttribute("username");
		boolean haveToRefresh = false;
		if (game != null) {
			while (e.hasMoreElements()) {
				String code = e.nextElement();
				switch (code) {
					case "quit":
						game.playerLeft(username);
						checkEmptyGame(request, game);
						response.sendRedirect(request.getServletContext().getContextPath() + "/");
						refresh(game);
						return;
					case "blue-decoder":
						game.playerJoinTeam(game.getBlueTeam(), Role.DECODER, username);
						haveToRefresh = true;
						break;
					case "blue-spy":
						game.playerJoinTeam(game.getBlueTeam(), Role.SPY, username);
						haveToRefresh = true;
						break;
					case "red-decoder":
						game.playerJoinTeam(game.getRedTeam(), Role.DECODER, username);
						haveToRefresh = true;
						break;
					case "red-spy":
						game.playerJoinTeam(game.getRedTeam(), Role.SPY, username);
						haveToRefresh = true;
						break;
					case "launch":
						if (game.gameRequirement() && game.amIMaster(username)) {
							game.initGame();
							response.sendRedirect(
									request.getServletContext().getContextPath() + "/game/" + game.getHashId());
							refresh(game);
							return;
						}
						break;
				}
				if (haveToRefresh) {
					refresh(game);
				}
			}
		}
		doGet(request, response);
	}

	private void checkEmptyGame(HttpServletRequest request, Game game) {
		List<Game> games = (List<Game>) request.getServletContext().getAttribute("games");
		if (game.isGameEmpty()) {
			games.removeIf(g -> g.getHashId() == game.getHashId());
		}
	}

	private void refresh(Game game) {
		Refresh.refresh(String.valueOf(game.getHashId()));
	}
}
