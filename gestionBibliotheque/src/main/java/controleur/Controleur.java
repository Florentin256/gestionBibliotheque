package controleur;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import entites.*;
import dao.*;

public class Controleur extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Controleur() {}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query  = request.getRequestURI();
		HttpSession session = request.getSession(true);

		if(session.getAttribute("APP_USER") != null) {
			
			if(query.contains("/ajoutAuteur")) {
				Auteur ajout = new Auteur(request.getParameter("nom"), request.getParameter("prenom"));
				AuteurDAO Adao = new AuteurDAO();
				try {
					Adao.ajoutAuteur(ajout);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(query.contains("/actionAuteur") && request.getParameter("submit").equals("supprimer")) {
				AuteurDAO Adao = new AuteurDAO();
				try {
					Auteur supp = Adao.getAuteurWithId(Integer.valueOf(request.getParameter("id")));
					Adao.supprimerAuteur(supp);
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(query.contains("/actionAuteur") && request.getParameter("submit").equals("modifier")) {
				request.getRequestDispatcher("modifAuteur.jsp").forward(request,response);
				

			} else if(query.contains("/modifAuteur")) {
				AuteurDAO Adao = new AuteurDAO();
				try {
					Auteur mod = Adao.getAuteurWithId(Integer.valueOf(request.getParameter("id")));
					mod.setNom(request.getParameter("nom"));
					mod.setPrenom(request.getParameter("prenom"));
					Adao.modifierAuteur(mod);
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if(query.contains("/ajoutLivre")) {
				AuteurDAO Adao = new AuteurDAO();
				Auteur aut = null;
				try {
					aut = Adao.getAuteurWithId(Integer.valueOf(request.getParameter("id")));
				} catch (NumberFormatException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String dateStr = request.getParameter("dateParution");
				java.sql.Date date = java.sql.Date.valueOf(dateStr);
				Livre ajout = new Livre(request.getParameter("titre"), aut, date);
				LivreDAO Ldao = new LivreDAO();
				try {
					Ldao.ajoutLivre(ajout);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("supprimer")) {
				LivreDAO Ldao = new LivreDAO();
				try {
					Livre supp = Ldao.getLivreWithId(Integer.valueOf(request.getParameter("id")));
					Ldao.supprimerLivre(supp);
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("modifier")) {
				request.getRequestDispatcher("modifLivre.jsp").forward(request,response);
				

			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("Ajouter des Tags")) {
				request.getRequestDispatcher("ajouterTagLivre.jsp").forward(request,response);
				
				
			} else if(query.contains("/modifLivre")) {
				LivreDAO Ldao = new LivreDAO();
				AuteurDAO Adao = new AuteurDAO();
				try {
					Livre mod = Ldao.getLivreWithId(Integer.valueOf(request.getParameter("id")));
					mod.setTitre(request.getParameter("titre"));
					mod.setAuteur(Adao.getAuteurWithId(Integer.valueOf(request.getParameter("id_auteur"))));
					java.sql.Date date = java.sql.Date.valueOf(request.getParameter("dateParution"));
					mod.setDateParution(date);
					Ldao.modifierLivre(mod);
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if(query.contains("/ajoutTagLivre")) {
				LivreDAO Ldao = new LivreDAO();
				try {
					Ldao.addLivreTagWithId(Integer.valueOf(request.getParameter("id")), request.getParameter("newTag"));
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if(query.contains("/indexAuteur")) {
				request.setAttribute("indexChoix", "indexAuteur");

			} else if(query.contains("/indexLivre")) {
				request.setAttribute("indexChoix", "indexLivre");

			}
			
			request.getRequestDispatcher("Index.jsp").forward(request,response);

		} else {
			
			UtilisateurDAO Udao = new UtilisateurDAO();
			try {
				if (request.getParameter("login") != null && request.getParameter("password") != null &&
						Udao.existeUtilisateur(request.getParameter("login"))
						&& Udao.trueLoginPassword(request.getParameter("login"), request.getParameter("password"))) {
					session.setAttribute("APP_USER", Udao.getUtilisateurWithLoginPassword(request.getParameter("login"), request.getParameter("password")));
					request.getRequestDispatcher("Index.jsp").forward(request,response);
				} else {
					// Erreur Login / Password
					request.getRequestDispatcher("login.jsp").forward(request,response);
				}
			} catch (NamingException | SQLException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
