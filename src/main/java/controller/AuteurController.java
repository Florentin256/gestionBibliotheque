package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Auteur;
import dao.AuteurDAO;
import dao.DaoException;
import dao.Pagination;

public class AuteurController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final AuteurDAO auteurDao = new AuteurDAO();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		if(session.getAttribute(UserController.APP_USER) != null) {
			synchroIndex(req, resp, 0);
			req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		if(session.getAttribute(UserController.APP_USER) != null) {
			
			String action = "";
			if (req.getParameter("action") != null) {
				action = req.getParameter("action");
			}
			
			if (action.equals("addAuteur")) {
				try {
					Auteur ajout = new Auteur(req.getParameter("nom"), req.getParameter("prenom"));
					auteurDao.add(ajout);
					synchroIndex(req, resp, 0);
					req.getRequestDispatcher("WEB-INF/Index.jsp?indexChoix=indexAuteur").forward(req,resp);
				} catch (DaoException | ServletException | IOException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("putAuteur")) {
				try {
					Auteur mod = auteurDao.getById(Integer.parseInt(req.getParameter("id")));
					mod.setNom(req.getParameter("nom"));
					mod.setPrenom(req.getParameter("prenom"));
					auteurDao.update(mod);
					synchroIndex(req, resp, 0);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("nextAuteurs")) {
				try {
					int numPage = Integer.parseInt(req.getParameter("numPageAuteurs")) + 1;
					synchroIndex(req, resp, numPage);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | ServletException | IOException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("previousAuteurs")) {
				try {
					int numPage = Integer.parseInt(req.getParameter("numPageAuteurs")) - 1;
					if (numPage < 0) {
						numPage = 0;
					}
					synchroIndex(req, resp, numPage);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | ServletException | IOException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("supprimer")) {
				try {
					auteurDao.remove(Integer.parseInt(req.getParameter("id")));
					synchroIndex(req, resp, 0);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("modifier")) {
				try {
					Auteur mod = auteurDao.getById(Integer.parseInt(req.getParameter("id")));
					req.setAttribute("auteur", mod);
					req.getRequestDispatcher("/WEB-INF/modifAuteur.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
			}
		}
	}
	
	private void synchroIndex(HttpServletRequest req, HttpServletResponse resp, int numPage) throws ServletException, IOException {
		try {
			req.setAttribute("indexChoix", "indexAuteur");
			req.setAttribute("numPageAuteurs", numPage);
			ArrayList<Auteur> listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(numPage, 10));
			req.setAttribute("auteursOffset", listAuteursOffset);
		} catch (DaoException e) {
			req.setAttribute("error", e);
			req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
		}
	}
}
