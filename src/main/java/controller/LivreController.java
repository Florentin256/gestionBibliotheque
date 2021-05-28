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
			synchroIndex(req, resp, 0);
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
				try {
					Auteur aut = auteurDao.getById(Integer.parseInt(req.getParameter("id")));
					String dateStr = req.getParameter("dateParution");
					java.sql.Date date = java.sql.Date.valueOf(dateStr);
					Livre ajout = new Livre(req.getParameter("titre"), aut, date);
					livreDao.add(ajout);
					synchroIndex(req, resp, 0);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if(action.equals("supprimer")) {
				try {
					livreDao.remove(Integer.parseInt(req.getParameter("id")));
					synchroIndex(req, resp, 0);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if(action.equals("modifier")) {
				try {
					Livre mod = livreDao.getById(Integer.parseInt(req.getParameter("id")));
					req.setAttribute("livre", mod);
					req.getRequestDispatcher("WEB-INF/modifLivre.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if(action.equals("Ajouter des Tags")) {
				try {
					ArrayList<String> tags = (ArrayList<String>) livreDao.getById(Integer.parseInt(req.getParameter("id"))).getTags();
					req.setAttribute("tagsLivre", tags);
					req.getRequestDispatcher("WEB-INF/ajouterTagLivre.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if(action.equals("putLivre")) {
				try {
					Livre mod = livreDao.getById(Integer.parseInt(req.getParameter("id")));
					mod.setTitre(req.getParameter("titre"));
					mod.setAuteur(auteurDao.getById(Integer.parseInt(req.getParameter("id_auteur"))));
					// Parser Date (Simple Date Format)
					java.sql.Date date = java.sql.Date.valueOf(req.getParameter("dateParution"));
					mod.setDateParution(date);
					livreDao.update(mod);
					synchroIndex(req, resp, 0);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}

			} else if(action.equals("ajoutTagLivre")) {
				try {
					livreDao.addTagToBookById(Integer.parseInt(req.getParameter("id")), req.getParameter("newTag"));
					synchroIndex(req, resp, 0);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | DaoException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if (action.equals("previousLivres")) {
				try {
					int numPage = Integer.parseInt(req.getParameter("numPageLivres")) - 1;
					if (numPage < 0) {
						numPage = 0;
					}
					synchroIndex(req, resp, numPage);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | ServletException | IOException e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
				
			} else if(action.equals("nextLivres")) {
				try {
					int numPage = Integer.parseInt(req.getParameter("numPageLivres")) + 1;
					synchroIndex(req, resp, numPage);
					req.getRequestDispatcher("WEB-INF/Index.jsp").forward(req,resp);
				} catch (NumberFormatException | ServletException | IOException  e) {
					req.setAttribute("error", e);
					req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
				}
			}	
		}
	}
	
	private void synchroIndex(HttpServletRequest req, HttpServletResponse resp, int numPage) throws ServletException, IOException {
		try {
			req.setAttribute("indexChoix", "indexLivre");
			req.setAttribute("numPageLivres", numPage);
			ArrayList<Livre> listLivresOffset = null;
			listLivresOffset = (ArrayList<Livre>) livreDao.getAll(new Pagination(numPage, 10, "date_parution"));
			req.setAttribute("livresOffset", listLivresOffset);
		} catch (DaoException e) {
			req.setAttribute("error", e);
			req.getRequestDispatcher("WEB-INF/errorPage.jsp").forward(req,resp);
		}
	}
}
