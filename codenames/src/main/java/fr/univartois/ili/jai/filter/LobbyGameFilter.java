package fr.univartois.ili.jai.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import fr.univartois.ili.jai.object.Game;

@WebFilter(filterName = "LobbyGameFilter")
public class LobbyGameFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String idGame = request.getPathInfo();
        if (idGame == null || idGame.substring(1).equals("")) {
            ((HttpServletResponse) res).sendRedirect(request.getServletContext().getContextPath() + "/jsp/404.jsp");
        } else {
            request.setAttribute("game", getGameById(request, idGame.substring(1)));
            chain.doFilter(req, res);
        }
    }

    private Game getGameById(HttpServletRequest request, String idG) {
        int idGame = Integer.valueOf(idG);
        List<Game> games = (List<Game>) request.getServletContext().getAttribute("games");
        for (Game game : games) {
            if (System.identityHashCode(game) == idGame)
                return game;
        }
        return null;
    }

}
