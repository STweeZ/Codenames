package fr.univartois.ili.jai.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.Role;

@WebServlet("/create")
public class CreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = (String) request.getSession().getAttribute("username");
		Game game = new Game();
		game.playerJoinTeam(game.getBlueTeam(), Role.SPY, username);

		List<Game> games = (List<Game>) request.getServletContext().getAttribute("games");
		games.add(game);

		response.sendRedirect(request.getServletContext().getContextPath() + "/lobby/" + System.identityHashCode(game));
	}
}
