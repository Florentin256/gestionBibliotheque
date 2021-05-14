package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import entites.*;

public class UtilisateurDAO {

	private Connection connect;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public UtilisateurDAO() {}
	
	public void init() throws NamingException, SQLException {
		InitialContext cxt = new InitialContext();
		DataSource ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/postgres");
		this.connect = ds.getConnection();
	}
	
	public void close() throws SQLException {
		rs.close();
		stmt.close();
		connect.close();
	}
	
	private ArrayList<Utilisateur> getUtilisateurs() throws NamingException, SQLException {
		init();
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM utilisateur");
		
		ArrayList<Utilisateur> listUtilisateurs = new ArrayList<Utilisateur>();
		while(this.rs.next()) {
			Utilisateur utilisateurTemp = new Utilisateur(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"));
			listUtilisateurs.add(utilisateurTemp);
		}
		rs.close();
		st.close();
		connect.close();
		return listUtilisateurs;
	}
	
	public boolean existeUtilisateur(String login) throws NamingException, SQLException {
		ArrayList<Utilisateur> listUtilisateurs = getUtilisateurs();
		for (int i=0; i<listUtilisateurs.size(); i++) {
			if (login.equals(listUtilisateurs.get(i).getLogin())) {
				return true;
			}
		}
		return false;
	}
	
	private Utilisateur getUtilisateurWithLogin(String login) throws SQLException, NamingException {
		init();
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "'");
		rs.next();
		Utilisateur utilisateur = new Utilisateur(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"));
		rs.close();
		st.close();
		connect.close();
		return utilisateur;
	}
	
	public Utilisateur getUtilisateurWithLoginPassword(String login, String password) throws SQLException, NamingException {
		init();
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "' AND password='" + password + "'");
		rs.next();
		Utilisateur utilisateur = new Utilisateur(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"));
		rs.close();
		st.close();
		connect.close();
		return utilisateur;
	}
	
	public boolean trueLoginPassword(String login, String password) throws SQLException, NamingException {
		Utilisateur utilisateur = getUtilisateurWithLogin(login);
		if (utilisateur.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
}
