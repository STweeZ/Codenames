package fr.univartois.ili.jai.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter(("username"));
		List<String> usernames = (List<String>) request.getServletContext().getAttribute("usernames");
		if (username.trim().isEmpty() || usernames.contains(username)) {
			doGet(request, response);
		} else {
			request.getSession().setAttribute("username", username);
			usernames.add(username);
			response.sendRedirect(request.getServletContext().getContextPath() + "/");
		}
	}

}
