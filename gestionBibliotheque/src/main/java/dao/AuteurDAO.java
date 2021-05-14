package dao;

import java.sql.*;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.*;
import entites.*;

public class AuteurDAO {
	
	private Connection connect;
	private PreparedStatement prepStmt;
	private Statement stmt;
	private ResultSet rs;
	
	public AuteurDAO() {}
	
	private void init() throws NamingException, SQLException {
		InitialContext cxt = new InitialContext();
		DataSource ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/postgres");
		this.connect = ds.getConnection();
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
		if (connect!=null) {
			connect.close();
		}
	}
	
	public Auteur getAuteurWithId(int id) throws SQLException {
		Auteur auteurTemp = null;
		try {
			init();
			stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur WHERE id=" + id);
			rs.next();
			auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), id);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close();
		}
		return auteurTemp;
	}
	
	public ArrayList<Auteur> getAuteurs() throws NamingException, SQLException {
		init();
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
	
	public void ajoutAuteur(Auteur auteur) throws NamingException, SQLException {
		init();
		this.prepStmt = connect.prepareStatement("INSERT INTO auteur VALUES (DEFAULT,?,?)", Statement.RETURN_GENERATED_KEYS);
		this.prepStmt.setString(1, auteur.getNom());
		this.prepStmt.setString(2, auteur.getPrenom());
		this.prepStmt.executeUpdate();
		this.rs = prepStmt.getGeneratedKeys();
		this.rs.next();
		auteur.setId(this.rs.getInt(1));
		close();
	}
	
	public void supprimerAuteur(Auteur auteur) throws NamingException, SQLException {
		init();
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
		init();
		this.prepStmt = connect.prepareStatement("UPDATE auteur SET nom=?, prenom=? WHERE id=?");
		this.prepStmt.setString(1, auteur.getNom());
		this.prepStmt.setString(2, auteur.getPrenom());
		this.prepStmt.setInt(3, auteur.getId());
		this.prepStmt.executeUpdate();
		close();
	}
}
