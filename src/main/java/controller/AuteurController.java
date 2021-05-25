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
	private AuteurDAO auteurDao = new AuteurDAO();
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query  = request.getRequestURI();
		HttpSession session = request.getSession(true);

		if(session.getAttribute("APP_USER") != null) {
			
			// Initialisation des variables
			
			if(query.contains("/ajoutAuteur")) {
				Auteur ajout = new Auteur(request.getParameter("nom"), request.getParameter("prenom"));
				try {
					auteurDao.add(ajout);
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionAuteur") && request.getParameter("submit").equals("supprimer")) {
				try {
					auteurDao.remove(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionAuteur") && request.getParameter("submit").equals("modifier")) {
				Auteur mod = null;
				try {
					mod = auteurDao.getById(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("auteur", mod);
				request.getRequestDispatcher("/WEB-INF/modifAuteur.jsp").forward(request,response);
				

			} else if(query.contains("/modifAuteur")) {
				try {
					Auteur mod = auteurDao.getById(Integer.parseInt(request.getParameter("id")));
					mod.setNom(request.getParameter("nom"));
					mod.setPrenom(request.getParameter("prenom"));
					auteurDao.update(mod);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);

				
			} else if(query.contains("/indexAuteur")) {
				request.setAttribute("indexChoix", "indexAuteur");
				request.setAttribute("numPageAuteurs", (int)0);
				ArrayList<Auteur> listAuteursOffset = null;
				try {
					listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(0, 10));
				} catch (DaoException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				request.setAttribute("auteursOffset", listAuteursOffset);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);

				
			} else if(query.contains("/auteursPrecedents")) {
				request.setAttribute("indexChoix", "indexAuteur");
				int numPage = Integer.parseInt(request.getParameter("numPageAuteurs")) - 1;
				if (numPage < 0) {
					numPage = 0;
				}
				ArrayList<Auteur> listAuteursOffset = null;
				try {
					listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(numPage, 10));
				} catch (DaoException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				request.setAttribute("numPageAuteurs", numPage);
				request.setAttribute("auteursOffset", listAuteursOffset);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/auteursSuivants")) {
				request.setAttribute("indexChoix", "indexAuteur");
				int numPage = Integer.parseInt(request.getParameter("numPageAuteurs")) + 1;
				request.setAttribute("numPageAuteurs", numPage);
				
				ArrayList<Auteur> listAuteursOffset = null;
				try {
					listAuteursOffset = (ArrayList<Auteur>) auteurDao.getAll(new Pagination(numPage, 10));
				} catch (DaoException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				request.setAttribute("auteursOffset", listAuteursOffset);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);	
			}
			
		}

	}
}
