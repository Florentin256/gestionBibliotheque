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
			req.setAttribute("indexChoix", "indexAuteur");
			req.setAttribute("numPageAuteurs", 0);
			ArrayList<Auteur> listAuteursOffset = null;
			try {
				listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(0, 10));
			} catch (DaoException e) {
				req.setAttribute("error", e);
				req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
			}
			req.setAttribute("auteursOffset", listAuteursOffset);
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
				Auteur ajout = new Auteur(req.getParameter("nom"), req.getParameter("prenom"));
				try {
					auteurDao.add(ajout);
				} catch (DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				
			} else if (action.equals("putAuteur")) {
				try {
					Auteur mod = auteurDao.getById(Integer.parseInt(req.getParameter("id")));
					mod.setNom(req.getParameter("nom"));
					mod.setPrenom(req.getParameter("prenom"));
					auteurDao.update(mod);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				
			} else if (action.equals("nextAuteurs")) {
				try {
					req.setAttribute("indexChoix", "indexAuteur");
					int numPage = Integer.parseInt(req.getParameter("numPageAuteurs")) + 1;
					req.setAttribute("numPageAuteurs", numPage);
					ArrayList<Auteur> listAuteursOffset = null;
					listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(numPage, 10));
					req.setAttribute("auteursOffset", listAuteursOffset);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (DaoException | NumberFormatException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("previousAuteurs")) {
				try {
					req.setAttribute("indexChoix", "indexAuteur");
					int numPage = Integer.parseInt(req.getParameter("numPageAuteurs")) - 1;
					if (numPage < 0) {
						numPage = 0;
					}
					ArrayList<Auteur> listAuteursOffset = null;
					listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(numPage, 10));
					req.setAttribute("numPageAuteurs", numPage);
					req.setAttribute("auteursOffset", listAuteursOffset);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (DaoException | NumberFormatException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("supprimer")) {
				try {
					auteurDao.remove(Integer.parseInt(req.getParameter("id")));
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				
			} else if (action.equals("modifier")) {
				Auteur mod = null;
				try {
					mod = auteurDao.getById(Integer.parseInt(req.getParameter("id")));
					req.setAttribute("auteur", mod);
					req.getRequestDispatcher("/WEB-INF/modifAuteur.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
			}
		}
	}
}
