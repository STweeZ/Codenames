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

@WebFilter(filterName = "LoginFilter")
public class LoginFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        if (((HttpServletRequest) req).getSession().getAttribute("username") == null) {
            ((HttpServletResponse) res).sendRedirect(req.getServletContext().getContextPath() + "/login");
        } else {
            chain.doFilter(req, res);
        }
    }

}
