package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Auteur;
import beans.Livre;
import dao.AuteurDAO;
import dao.DaoException;
import dao.LivreDAO;
import dao.Pagination;

public class LivreController extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final LivreDAO livreDao = new LivreDAO();
	
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
					nbPages = livreDao.nbTotalPages(LIMIT);
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
			// Initialisation des variables
			AuteurDAO auteurDao = new AuteurDAO();
			
			String action = req.getParameter("action");
			if (action == null) {
				action = "";
			}
			
			switch (action) {
			case "addLivre":
				try {
					String idStr = req.getParameter("id");
					String dateStr = req.getParameter("dateParution");
					if (idStr != null && dateStr != null) {
						Auteur aut = auteurDao.getById(Integer.parseInt(idStr));
						java.sql.Date date = java.sql.Date.valueOf(dateStr);
						Livre ajout = new Livre(req.getParameter("titre"), aut, date);
						livreDao.add(ajout);
						configParameterForIndex(req, resp, 0);
						req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour l'ajout");
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
						livreDao.remove(Integer.parseInt(idStr));
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
						Livre mod = livreDao.getById(Integer.parseInt(idStr));
						req.setAttribute("livre", mod);
						req.getRequestDispatcher("WEB-INF/modifLivre.jsp").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour la mise à jour");
						req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
					}
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				break;
				
			case "Ajouter des Tags":
				try {
					String idStr = req.getParameter("id");
					if (idStr != null) {
						ArrayList<String> tags = (ArrayList<String>) livreDao.getById(Integer.parseInt(idStr)).getTags();
						req.setAttribute("tagsLivre", tags);
						req.getRequestDispatcher("WEB-INF/ajouterTagLivre.jsp").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour l'ajout de tags");
						req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
					}
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				break;
				
			case "putLivre":
				try {
					String idStr = req.getParameter("id");
					String idAuteurStr = req.getParameter("id_auteur");
					String titre = req.getParameter("titre");
					String dateParution = req.getParameter("dateParution");
					if (idStr != null && idAuteurStr != null && titre != null && dateParution != null) {
						Livre mod = livreDao.getById(Integer.parseInt(idStr));
						mod.setTitre(titre);
						mod.setAuteur(auteurDao.getById(Integer.parseInt(idAuteurStr)));
						// Parser Date (Simple Date Format)
						java.sql.Date date = java.sql.Date.valueOf(dateParution);
						mod.setDateParution(date);
						livreDao.update(mod);
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

			case "ajoutTagLivre":
				try {
					String idStr = req.getParameter("id");
					String newTag = req.getParameter("newTag");
					if (idStr != null && newTag != null) {
						livreDao.addTagToBookById(Integer.parseInt(idStr), newTag);
						configParameterForIndex(req, resp, 0);
						req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
					} else {
						req.setAttribute("error", "Paramètre(s) manquant(s) pour l'ajout de tags");
						req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
					}
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				break;
			
			default :
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
			req.setAttribute("indexChoix", "indexLivre");
			if (numPage < 0) {
				numPage = 0;
			}
			req.setAttribute("numPageLivres", numPage);
			ArrayList<Livre> listLivresOffset = null;
			listLivresOffset = (ArrayList<Livre>) livreDao.getAll(new Pagination(numPage, LIMIT, "date_parution"));
			req.setAttribute("livresOffset", listLivresOffset);
		} catch (DaoException e) {
			req.setAttribute("error", e);
			req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
		}
	}
}
