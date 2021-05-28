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
	
	private final int LIMIT = 10;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		if(session.getAttribute(UserController.APP_USER) != null) {
			
			String action = req.getParameter("action");
			if (action == null) {
				action = "";
			}
			String numPageStr = req.getParameter("numPage");
			int numPage = 0;
			if (numPageStr != null) {
				numPage = Integer.parseInt(numPageStr);
			}
			
			switch (action) {
			case "next":
				int nbPages = 0;
				try {
					nbPages = auteurDao.nbTotalPages(LIMIT);
				} catch (DaoException e) {
					e.printStackTrace();
				}
				numPage += 1;
				if (numPage >= nbPages) {
					numPage = nbPages-1;
				}
				configParameterForIndex(req, resp, numPage);
				break;
			case "prev":
				numPage -= 1;
				if (numPage < 0) {
					numPage = 0;
				}
				configParameterForIndex(req, resp, numPage);
				break;
			default:
				configParameterForIndex(req, resp, 0);
			}
			req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);

		if(session.getAttribute(UserController.APP_USER) != null) {
			
			String action = req.getParameter("action");
			if (action == null) {
				action = "";
			}
			
			switch (action) {
			case "addAuteur":
				try {
					String nom = req.getParameter("nom");
					String prenom = req.getParameter("prenom");
					if (nom != null && prenom != null) {
						Auteur ajout = new Auteur(nom, prenom);
						auteurDao.add(ajout);
						configParameterForIndex(req, resp, 0);
						req.getRequestDispatcher("WEB-INF/Index.jsp?indexChoix=indexAuteur").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour l'ajout");
						req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
					}
				} catch (DaoException | ServletException | IOException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				break;
				
			case "putAuteur" :
				try {
					String idStr = req.getParameter("id");
					String nom = req.getParameter("nom");
					String prenom = req.getParameter("prenom");
					if (idStr != null && nom != null && prenom != null) {
						Auteur mod = auteurDao.getById(Integer.parseInt(idStr));
						mod.setNom(nom);
						mod.setPrenom(prenom);
						auteurDao.update(mod);
						configParameterForIndex(req, resp, 0);
						req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour la mise à jour");
						req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
					}
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				break;
				
			case "supprimer":
				try {
					String idStr = req.getParameter("id");
					if (idStr != null) {
						auteurDao.remove(Integer.parseInt(idStr));
						configParameterForIndex(req, resp, 0);
						req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour la suppression");
						req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
					}
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				break;
				
			case "modifier":
				try {
					String idStr = req.getParameter("id");
					if (idStr != null) {
						Auteur mod = auteurDao.getById(Integer.parseInt(idStr));
						req.setAttribute("auteur", mod);
						req.getRequestDispatcher("/WEB-INF/modifAuteur.jsp").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour la mise à jour");
						req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
					}
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				break;
			
			default:
				configParameterForIndex(req, resp, 0);
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
			}
		}
	}
	
	/**
	 * Configure les parametres a setter pour retourner sur l'index de la derniere entite choisie
	 * a la page indique par 'numPage'
	 * 
	 * @param req
	 * @param resp
	 * @param numPage
	 * 			numero de page a configurer
	 * @throws ServletException
	 * @throws IOException
	 */
	private void configParameterForIndex(HttpServletRequest req, HttpServletResponse resp, int numPage) throws ServletException, IOException {
		try {
			req.setAttribute("indexChoix", "indexAuteur");
			req.setAttribute("numPageAuteurs", numPage);
			ArrayList<Auteur> listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(numPage, LIMIT));
			req.setAttribute("auteursOffset", listAuteursOffset);
		} catch (DaoException e) {
			req.setAttribute("error", e);
			req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
		}
	}
}
