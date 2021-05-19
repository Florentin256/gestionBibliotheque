package dao;

import java.sql.*;
import java.util.ArrayList;

import beans.*;

public class AuteurDAO {
	
	private Connection connect;
	private PreparedStatement prepStmt;
	private Statement stmt;
	private ResultSet rs;
	
	public AuteurDAO(Connection connect) {
		this.connect = connect;
	}
	
	private void close() throws DAOException {
		try {
			if (rs!=null) {
				rs.close();
			}
			if (prepStmt!=null) {
				prepStmt.close();
			}
			if (stmt!=null) {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new DAOException("Echec de fermeture des Statement et ResultSet");
		}
	}
	
	public Auteur getAuteurWithId(int id) throws DAOException {
		Auteur auteurTemp = null;
		try {
			stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur WHERE id=" + id);
			rs.next();
			auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), id);
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return auteurTemp;
	}
	
	public ArrayList<Auteur> getAuteurs() throws DAOException {
		ArrayList<Auteur> listAuteurs = new ArrayList<Auteur>();
		try {
			stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur");
			
			while(this.rs.next()) {
				Auteur auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), rs.getInt("id"));
				listAuteurs.add(auteurTemp);
			}
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return listAuteurs;
	}
	
	public ArrayList<Auteur> getAuteurs(int offset) throws DAOException {
		ArrayList<Auteur> listAuteurs = new ArrayList<Auteur>();
		try {
			stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur limit 10 offset " + offset*10);
			
			while(this.rs.next()) {
				Auteur auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), rs.getInt("id"));
				listAuteurs.add(auteurTemp);
			}
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return listAuteurs;
	}
	
	public void ajoutAuteur(String nom, String prenom) throws DAOException  {
		try {
			this.prepStmt = connect.prepareStatement("INSERT INTO auteur (id, nom, prenom) VALUES (DEFAULT,?,?)", Statement.RETURN_GENERATED_KEYS);
			this.prepStmt.setString(1, nom);
			this.prepStmt.setString(2, prenom);
			this.prepStmt.executeUpdate();
			this.rs = prepStmt.getGeneratedKeys();
		} catch (SQLException e) {
			throw new DAOException("Echec d'insertion dans la base");
		} finally {
			close();
		}
	}
	
	public void supprimerAuteur(Auteur auteur) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("DELETE FROM auteur WHERE id=?");
			this.prepStmt.setInt(1, auteur.getId());
			try {
				this.prepStmt.executeUpdate();
			} catch (org.postgresql.util.PSQLException e) {
				// Ne peut pas etre supprime car dependance d'un livre
				throw new DAOException("L'auteur est une dépendance d'un livre, suppression impossible");
			}
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}
	
	public void modifierAuteur(Auteur auteur) throws DAOException {
		try {
		this.prepStmt = connect.prepareStatement("UPDATE auteur SET nom=?, prenom=? WHERE id=?");
		this.prepStmt.setString(1, auteur.getNom());
		this.prepStmt.setString(2, auteur.getPrenom());
		this.prepStmt.setInt(3, auteur.getId());
		this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}
}
