package controleur;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

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
	private static Connection connect;
	
	public Controleur() {}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query  = request.getRequestURI();
		HttpSession session = request.getSession(true);

		if(session.getAttribute("APP_USER") != null) {
			
			// Initialisation des variables
			AuteurDAO Adao = new AuteurDAO(connect);
			ArrayList<Auteur> listAuteurs = null;
			try {
				listAuteurs = Adao.getAuteurs();
			} catch (NamingException | SQLException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			LivreDAO Ldao = new LivreDAO(connect);
			ArrayList<Livre> listLivres = null;
			try {
				listLivres = Ldao.getLivres();
			} catch (NamingException | SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Collections.sort(listLivres);
			request.setAttribute("auteurs", listAuteurs);
			request.setAttribute("livres", listLivres);
			
			//
			
			
			
			//
			
			if(query.contains("/ajoutAuteur")) {
				Auteur ajout = new Auteur(request.getParameter("nom"), request.getParameter("prenom"));
				try {
					Adao.ajoutAuteur(ajout);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionAuteur") && request.getParameter("submit").equals("supprimer")) {
				try {
					Auteur supp = Adao.getAuteurWithId(Integer.parseInt(request.getParameter("id")));
					Adao.supprimerAuteur(supp);
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionAuteur") && request.getParameter("submit").equals("modifier")) {
				Auteur mod = null;
				try {
					mod = Adao.getAuteurWithId(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("auteur", mod);
				request.getRequestDispatcher("/WEB-INF/modifAuteur.jsp").forward(request,response);
				

			} else if(query.contains("/modifAuteur")) {
				try {
					Auteur mod = Adao.getAuteurWithId(Integer.parseInt(request.getParameter("id")));
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
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);

				
			} else if(query.contains("/ajoutLivre")) {
				Auteur aut = null;
				try {
					aut = Adao.getAuteurWithId(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String dateStr = request.getParameter("dateParution");
				java.sql.Date date = java.sql.Date.valueOf(dateStr);
				Livre ajout = new Livre(request.getParameter("titre"), aut, date);
				try {
					Ldao.ajoutLivre(ajout);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("supprimer")) {
				try {
					Livre supp = Ldao.getLivreWithId(Integer.parseInt(request.getParameter("id")));
					Ldao.supprimerLivre(supp);
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("modifier")) {
				Livre mod = null;
				try {
					mod = Ldao.getLivreWithId(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("livre", mod);
				request.getRequestDispatcher("WEB-INF/modifLivre.jsp").forward(request,response);
				

			} else if(query.contains("/actionLivre") && request.getParameter("submit").equals("Ajouter des Tags")) {
				ArrayList<String> tags = null;
				try {
					tags = Ldao.getLivreTagWithId(Integer.parseInt(request.getParameter("id")));
				} catch (NumberFormatException | SQLException | NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("tagsLivre", tags);
				request.getRequestDispatcher("WEB-INF/ajouterTagLivre.jsp").forward(request,response);
				
				
			} else if(query.contains("/modifLivre")) {
				try {
					Livre mod = Ldao.getLivreWithId(Integer.parseInt(request.getParameter("id")));
					mod.setTitre(request.getParameter("titre"));
					mod.setAuteur(Adao.getAuteurWithId(Integer.parseInt(request.getParameter("id_auteur"))));
					// Parser Date (Simple Date Format)
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
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);

				
			} else if(query.contains("/ajoutTagLivre")) {
				try {
					Ldao.addLivreTagWithId(Integer.parseInt(request.getParameter("id")), request.getParameter("newTag"));
				} catch (NumberFormatException | NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);

				
			} else if(query.contains("/indexAuteur")) {
				request.setAttribute("indexChoix", "indexAuteur");
				request.setAttribute("numPageAuteurs", (int)0);
				ArrayList<Auteur> listAuteursOffset = null;
				try {
					listAuteursOffset = Adao.getAuteurs(0);
				} catch (NamingException | SQLException e3) {
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
					listAuteursOffset = Adao.getAuteurs(numPage);
				} catch (NamingException | SQLException e3) {
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
					listAuteursOffset = Adao.getAuteurs(numPage);
				} catch (NamingException | SQLException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				request.setAttribute("auteursOffset", listAuteursOffset);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			} else if(query.contains("/indexLivre")) {
				request.setAttribute("indexChoix", "indexLivre");
				request.setAttribute("numPageLivres", (int)0);
				ArrayList<Livre> listLivresOffset = null;
				try {
					listLivresOffset = Ldao.getLivres(0);
				} catch (NamingException | SQLException e) {
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
					listLivresOffset = Ldao.getLivres(numPage);
				} catch (NamingException | SQLException e) {
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
					listLivresOffset = Ldao.getLivres(numPage);
				} catch (NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				request.setAttribute("livresOffset", listLivresOffset);
				request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request,response);
				
				
			}
			
		} else {
			
			UtilisateurDAO Udao = new UtilisateurDAO(connect);
			try {
				if (request.getParameter("login") != null && request.getParameter("password") != null &&
						Udao.existeUtilisateur(request.getParameter("login"))
						&& Udao.trueLoginPassword(request.getParameter("login"), request.getParameter("password"))) {
					session.setAttribute("APP_USER", Udao.getUtilisateurWithLoginPassword(request.getParameter("login"), request.getParameter("password")));
					request.getRequestDispatcher("WEB-INF/Index.jsp").forward(request, response);
				} else {
					// Erreur Login / Password
					request.getRequestDispatcher("WEB-INF/login.jsp").forward(request, response);
				}
			} catch (NamingException | SQLException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		ConnectDAO Cdao = new ConnectDAO();
		connect = Cdao.connect();
	}
}
