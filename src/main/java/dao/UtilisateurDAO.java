package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

import beans.*;

public class UtilisateurDAO {

	private Connection connect;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public UtilisateurDAO(Connection connect) {
		this.connect = connect;
	}
	
	public void close() throws SQLException {
		rs.close();
		stmt.close();
	
	}
	
	private ArrayList<User> getUtilisateurs() throws NamingException, SQLException {
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM utilisateur");
		
		ArrayList<User> listUtilisateurs = new ArrayList<User>();
		while(this.rs.next()) {
			User utilisateurTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
			listUtilisateurs.add(utilisateurTemp);
		}
		rs.close();
		st.close();
		return listUtilisateurs;
	}
	
	public boolean existeUtilisateur(String login) throws NamingException, SQLException {
		ArrayList<User> listUtilisateurs = getUtilisateurs();
		for (int i=0; i<listUtilisateurs.size(); i++) {
			if (login.equals(listUtilisateurs.get(i).getLogin())) {
				return true;
			}
		}
		return false;
	}
	
	private User getUtilisateurWithLogin(String login) throws SQLException, NamingException {
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "'");
		rs.next();
		User utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		rs.close();
		st.close();
		return utilisateur;
	}
	
	public User getUtilisateurWithLoginPassword(String login, String password) throws SQLException, NamingException {
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "' AND password='" + password + "'");
		rs.next();
		User utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		rs.close();
		st.close();
		return utilisateur;
	}
	
	public boolean trueLoginPassword(String login, String password) throws SQLException, NamingException {
		User utilisateur = getUtilisateurWithLogin(login);
		if (utilisateur.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
}
