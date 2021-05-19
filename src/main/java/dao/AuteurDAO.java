package dao;

import java.sql.*;
import java.util.ArrayList;

import javax.naming.NamingException;

import beans.*;

public class AuteurDAO {
	
	private Connection connect;
	private PreparedStatement prepStmt;
	private Statement stmt;
	private ResultSet rs;
	
	public AuteurDAO(Connection connect) {
		this.connect = connect;
	}
	
	private void close() throws SQLException {
		if (rs!=null) {
			rs.close();
		}
		if (prepStmt!=null) {
			prepStmt.close();
		}
		if (stmt!=null) {
			stmt.close();
		}
	}
	
	public Auteur getAuteurWithId(int id) throws SQLException {
		Auteur auteurTemp = null;
		try {
			stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur WHERE id=" + id);
			rs.next();
			auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return auteurTemp;
	}
	
	public ArrayList<Auteur> getAuteurs() throws NamingException, SQLException {
		stmt = connect.createStatement();
		this.rs = stmt.executeQuery("SELECT * FROM auteur");
		
		ArrayList<Auteur> listAuteurs = new ArrayList<Auteur>();
		while(this.rs.next()) {
			Auteur auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), rs.getInt("id"));
			listAuteurs.add(auteurTemp);
		}
		close();
		return listAuteurs;
	}
	
	public ArrayList<Auteur> getAuteurs(int offset) throws NamingException, SQLException {
		stmt = connect.createStatement();
		this.rs = stmt.executeQuery("SELECT * FROM auteur limit 10 offset " + offset*10);
		
		ArrayList<Auteur> listAuteurs = new ArrayList<Auteur>();
		while(this.rs.next()) {
			Auteur auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), rs.getInt("id"));
			listAuteurs.add(auteurTemp);
		}
		close();
		return listAuteurs;
	}
	
	public void ajoutAuteur(String nom, String prenom) throws NamingException, SQLException {
		this.prepStmt = connect.prepareStatement("INSERT INTO auteur (id, nom, prenom) VALUES (DEFAULT,?,?)", Statement.RETURN_GENERATED_KEYS);
		this.prepStmt.setString(1, nom);
		this.prepStmt.setString(2, prenom);
		this.prepStmt.executeUpdate();
		this.rs = prepStmt.getGeneratedKeys();
		close();
	}
	
	public void supprimerAuteur(Auteur auteur) throws NamingException, SQLException {
		this.prepStmt = connect.prepareStatement("DELETE FROM auteur WHERE id=?");
		this.prepStmt.setInt(1, auteur.getId());
		try {
			this.prepStmt.executeUpdate();
		} catch (org.postgresql.util.PSQLException e) {
			// Ne peut pas être supprimé car dépendance d'un livre
		}
		close();
	}
	
	public void modifierAuteur(Auteur auteur) throws NamingException, SQLException {
		this.prepStmt = connect.prepareStatement("UPDATE auteur SET nom=?, prenom=? WHERE id=?");
		this.prepStmt.setString(1, auteur.getNom());
		this.prepStmt.setString(2, auteur.getPrenom());
		this.prepStmt.setInt(3, auteur.getId());
		this.prepStmt.executeUpdate();
		close();
	}
}
