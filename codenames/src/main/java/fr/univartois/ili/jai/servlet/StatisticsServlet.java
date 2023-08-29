package fr.univartois.ili.jai.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/statistics/*")
public class StatisticsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String filter = (String) request.getAttribute("filter");
		if (filter == null) {
			request.setAttribute("filter", "games");
		}
		request.getRequestDispatcher("/jsp/statistics.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String filter = request.getParameter("filter");
		if (filter != null) {
			request.setAttribute("filter", filter);
		}
		doGet(request, response);
	}

}
