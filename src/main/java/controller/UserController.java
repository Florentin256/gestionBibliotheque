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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		
		String action = "";
		if (req.getParameter("action") != null) {
			action = req.getParameter("action");
		}
		
		if(session.getAttribute("APP_USER") == null || action.equals("deconnexion")) {
			req.getRequestDispatcher("WEB-INF/login.jsp").forward(req,resp);
		} else {
			req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		if(session.getAttribute("APP_USER") == null) {
			try {
				if (req.getParameter("login") != null && req.getParameter("password") != null &&
						userDao.existUser(req.getParameter("login"))
						&& userDao.trueLoginPassword(req.getParameter("login"), req.getParameter("password"))) {
					session.setAttribute("APP_USER", userDao.getUserByLoginPassword(req.getParameter("login"), req.getParameter("password")));
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req, resp);
				} else {
					// Erreur Login / Password
					req.getRequestDispatcher("WEB-INF/login.jsp").forward(req, resp);
				}
			} catch (ServletException | IOException | DaoException e) {
				req.setAttribute("error", e);
				req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
			}
		} else {
			req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
		}
	}
}
