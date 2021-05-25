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
	private LivreDAO livreDao = new LivreDAO();
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query  = request.getRequestURI();
		HttpSession session = request.getSession(true);

		if(session.getAttribute("APP_USER") != null) {
			
			// Initialisation des variables
			AuteurDAO Adao = new AuteurDAO();
			
			if(query.contains("/ajoutLivre")) {
				Auteur aut = null;
				try {
					aut = Adao.getById(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException | DaoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String dateStr = request.getParameter("dateParution");
				java.sql.Date date = java.sql.Date.valueOf(dateStr);
				Livre ajout = new Livre(request.getParameter("titre"), aut, date);
				try {
					livreDao.add(ajout);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("supprimer")) {
				try {
					livreDao.remove(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException | DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("modifier")) {
				Livre mod = null;
				try {
					mod = livreDao.getById(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException | DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("livre", mod);
				request.getRequestDispatcher("WEB-INF/modifLivre.jsp").forward(request,response);
				

			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("Ajouter des Tags")) {
				ArrayList<String> tags = null;
				try {
					tags = (ArrayList<String>) livreDao.getById(Integer.parseInt(request.getParameter("id"))).getTags();
				} catch (NumberFormatException | DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("tagsLivre", tags);
				request.getRequestDispatcher("WEB-INF/ajouterTagLivre.jsp").forward(request,response);
				
				
			} else if(query.contains("/modifLivre")) {
				try {
					Livre mod = livreDao.getById(Integer.parseInt(request.getParameter("id")));
					mod.setTitre(request.getParameter("titre"));
					mod.setAuteur(Adao.getById(Integer.parseInt(request.getParameter("id_auteur"))));
					// Parser Date (Simple Date Format)
					java.sql.Date date = java.sql.Date.valueOf(request.getParameter("dateParution"));
					mod.setDateParution(date);
					livreDao.update(mod);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);

				
			} else if(query.contains("/ajoutTagLivre")) {
				try {
					livreDao.addTagToBookById(Integer.parseInt(request.getParameter("id")), request.getParameter("newTag"));
				} catch (NumberFormatException | DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);

				
			} else if(query.contains("/indexLivre")) {
				request.setAttribute("indexChoix", "indexLivre");
				request.setAttribute("numPageLivres", (int)0);
				ArrayList<Livre> listLivresOffset = null;
				try {
					listLivresOffset = (ArrayList<Livre>) livreDao.getAll(new Pagination(0, 10, "date_parution"));
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("livresOffset", listLivresOffset);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if (query.contains("/livresPrecedents")) {
				request.setAttribute("indexChoix", "indexLivre");
				int numPage = Integer.parseInt(request.getParameter("numPageLivres")) - 1;
				if (numPage < 0) {
					numPage = 0;
				}
				ArrayList<Livre> listLivresOffset = null;
				try {
					listLivresOffset = (ArrayList<Livre>)livreDao.getAll(new Pagination(numPage, 10, "date_parution"));
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("livresOffset", listLivresOffset);
				request.setAttribute("numPageLivres", numPage);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/livresSuivants")) {
				request.setAttribute("indexChoix", "indexLivre");
				int numPage = Integer.parseInt(request.getParameter("numPageLivres")) + 1;
				request.setAttribute("numPageLivres", numPage);
				
				ArrayList<Livre> listLivresOffset = null;
				try {
					listLivresOffset = (ArrayList<Livre>)livreDao.getAll(new Pagination(numPage, 10, "date_parution"));
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("livresOffset", listLivresOffset);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);	
			}	
		}
	}
}
