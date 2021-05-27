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
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		
		if(session.getAttribute(UserController.APP_USER) != null) {
			synchroIndex(req, resp);
			req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		
		if(session.getAttribute(UserController.APP_USER) != null) {
			// Initialisation des variables
			AuteurDAO auteurDao = new AuteurDAO();
			
			String action = "";
			if (req.getParameter("action") != null) {
				action = req.getParameter("action");
			}
			
			if(action.equals("addLivre")) {
				Auteur aut = null;
				try {
					aut = auteurDao.getById(Integer.parseInt(req.getParameter("id")));
				} catch (NumberFormatException | DaoException e1) {
					e1.printStackTrace();
				}
				String dateStr = req.getParameter("dateParution");
				java.sql.Date date = java.sql.Date.valueOf(dateStr);
				Livre ajout = new Livre(req.getParameter("titre"), aut, date);
				try {
					livreDao.add(ajout);
				} catch (DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				synchroIndex(req, resp);
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				
			} else if(action.equals("supprimer")) {
				try {
					livreDao.remove(Integer.parseInt(req.getParameter("id")));
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				synchroIndex(req, resp);
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				
			} else if(action.equals("modifier")) {
				Livre mod = null;
				try {
					mod = livreDao.getById(Integer.parseInt(req.getParameter("id")));
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				req.setAttribute("livre", mod);
				req.getRequestDispatcher("WEB-INF/modifLivre.jsp").forward(req,resp);
				
			} else if(action.equals("Ajouter des Tags")) {
				ArrayList<String> tags = null;
				try {
					tags = (ArrayList<String>) livreDao.getById(Integer.parseInt(req.getParameter("id"))).getTags();
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				req.setAttribute("tagsLivre", tags);
				req.getRequestDispatcher("WEB-INF/ajouterTagLivre.jsp").forward(req,resp);
				
			} else if(action.equals("putLivre")) {
				try {
					Livre mod = livreDao.getById(Integer.parseInt(req.getParameter("id")));
					mod.setTitre(req.getParameter("titre"));
					mod.setAuteur(auteurDao.getById(Integer.parseInt(req.getParameter("id_auteur"))));
					// Parser Date (Simple Date Format)
					java.sql.Date date = java.sql.Date.valueOf(req.getParameter("dateParution"));
					mod.setDateParution(date);
					livreDao.update(mod);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				synchroIndex(req, resp);
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);

			} else if(action.equals("ajoutTagLivre")) {
				try {
					livreDao.addTagToBookById(Integer.parseInt(req.getParameter("id")), req.getParameter("newTag"));
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				synchroIndex(req, resp);
				req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				
			} else if (action.equals("previousLivres")) {
				try {
					req.setAttribute("indexChoix", "indexLivre");
					int numPage = Integer.parseInt(req.getParameter("numPageLivres")) - 1;
					if (numPage < 0) {
						numPage = 0;
					}
					ArrayList<Livre> listLivresOffset = null;
					listLivresOffset = (ArrayList<Livre>)livreDao.getAll(new Pagination(numPage, 10, "date_parution"));
					req.setAttribute("livresOffset", listLivresOffset);
					req.setAttribute("numPageLivres", numPage);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (DaoException | NumberFormatException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if(action.equals("nextLivres")) {
				try {
					req.setAttribute("indexChoix", "indexLivre");
					int numPage = Integer.parseInt(req.getParameter("numPageLivres")) + 1;
					req.setAttribute("numPageLivres", numPage);
					ArrayList<Livre> listLivresOffset = null;
					listLivresOffset = (ArrayList<Livre>)livreDao.getAll(new Pagination(numPage, 10, "date_parution"));
					req.setAttribute("livresOffset", listLivresOffset);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (DaoException | NumberFormatException  e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
			}	
		}
	}
	
	private void synchroIndex(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("indexChoix", "indexLivre");
		req.setAttribute("numPageLivres", 0);
		ArrayList<Livre> listLivresOffset = null;
		try {
			listLivresOffset = (ArrayList<Livre>) livreDao.getAll(new Pagination(0, 10, "date_parution"));
		} catch (DaoException e) {
			req.setAttribute("error", e);
			req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
		}
		req.setAttribute("livresOffset", listLivresOffset);
	}
}
