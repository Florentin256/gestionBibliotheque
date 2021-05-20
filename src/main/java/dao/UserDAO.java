package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.*;

public class UserDAO {

	private Connection connect;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public UserDAO(Connection connect) {
		this.connect = connect;
	}
	
	private void close() throws DAOException {
		try {
			if (rs!=null) {
				rs.close();
			}
			if (stmt!=null) {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new DAOException("Echec de fermeture des Statement et ResultSet");
		}
	}
	
	private ArrayList<User> getUsers() throws DAOException {
		ArrayList<User> listUtilisateurs = new ArrayList<User>();
		try {
			Statement st = connect.createStatement();
			this.rs = st.executeQuery("SELECT * FROM utilisateur");
			
			while(this.rs.next()) {
				User utilisateurTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
				listUtilisateurs.add(utilisateurTemp);
			}
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return listUtilisateurs;
	}
	
	public boolean existUser(String login) throws DAOException {
		ArrayList<User> listUtilisateurs = getUsers();
		for (int i=0; i<listUtilisateurs.size(); i++) {
			if (login.equals(listUtilisateurs.get(i).getLogin())) {
				return true;
			}
		}
		return false;
	}
	
	private User getUserByLogin(String login) throws DAOException {
		User utilisateur = null;
		try {
			Statement st = connect.createStatement();
			this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "'");
			rs.next();
			utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return utilisateur;
	}
	
	public User getUserByLoginPassword(String login, String password) throws DAOException {
		User utilisateur = null;
		try {
			Statement st = connect.createStatement();
			this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "' AND password='" + password + "'");
			rs.next();
			utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return utilisateur;
	}
	
	public boolean trueLoginPassword(String login, String password) throws DAOException {
		User utilisateur = getUserByLogin(login);
		if (utilisateur.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
}
