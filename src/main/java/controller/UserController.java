package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.DaoException;
import dao.UserDAO;

public class UserController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final UserDAO userDao = new UserDAO();
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query  = request.getRequestURI();
		HttpSession session = request.getSession(true);

		if(session.getAttribute("APP_USER") != null) {
			if(query.contains("/deconnexion")) {
				session.invalidate();
				request.getRequestDispatcher("WEB-INF/login.jsp").forward(request,response);
			}
		} else {
			try {
				if (request.getParameter("login") != null && request.getParameter("password") != null &&
						userDao.existUser(request.getParameter("login"))
						&& userDao.trueLoginPassword(request.getParameter("login"), request.getParameter("password"))) {
					session.setAttribute("APP_USER", userDao.getUserByLoginPassword(request.getParameter("login"), request.getParameter("password")));
					request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request, response);
				} else {
					// Erreur Login / Password
					request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
				}
			} catch (ServletException | IOException | DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
