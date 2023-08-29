package fr.univartois.ili.jai.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.univartois.ili.jai.object.Game;
import fr.univartois.ili.jai.object.Player;
import fr.univartois.ili.jai.object.Team;
import fr.univartois.ili.jai.persistance.TDGRegistry;

@WebListener
public class MainListener implements HttpSessionListener, ServletContextListener {

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		List<String> usernames = (List<String>) se.getSession().getServletContext().getAttribute("usernames");
		String username = (String) se.getSession().getAttribute("username");
		if (!usernames.isEmpty() && usernames.contains(username)) {
			usernames.remove(username);
		}
		List<Game> games = (List<Game>) se.getSession().getServletContext().getAttribute("games");
		for (Game game : games) {
			game.playerLeft(username);
			if (game.isGameEmpty()) {
				games.remove(game);
			}
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("games", new ArrayList<List<Game>>());
		sce.getServletContext().setAttribute("usernames", new ArrayList<List<String>>());
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (ClassNotFoundException e) {
			sce.getServletContext().log("Cannot load JDBC driver");
		}
		try {
			initDB();
		} catch (Exception e) {
			sce.getServletContext().log("Cannot create JDBC tables");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			Driver driver = DriverManager.getDriver("jdbc:derby:DEMODB");
			DriverManager.deregisterDriver(driver);
		} catch (SQLException e) {
			sce.getServletContext().log("Cannot deregister JDBC driver");
		}
	}

	private static void initDB() throws SQLException {
		TDGRegistry.findTDG(Player.class).createTable();
		TDGRegistry.findTDG(Team.class).createTable();
		TDGRegistry.findTDG(Game.class).createTable();
	}

}
