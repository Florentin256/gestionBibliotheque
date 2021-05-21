package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import beans.*;

public class UserDAO implements StandardCRUD<User> {

	private Connection connect;
	private PreparedStatement prepStmt;
	private ResultSet rs;
	
	public UserDAO(Connection connect) {
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
		} catch (SQLException e) {
			throw new DAOException("Echec de fermeture des Statement et ResultSet");
		}
	}
	
	@Override
	public User getById(Object id) throws DAOException {
		User userTemp = null;
		try {
			Statement stmt = (Statement) connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE id=" + id);
			rs.next();
			userTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), (int)id);
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return userTemp;
	}

	@Override
	public ArrayList<User> getAll() throws DAOException {
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

	@Override
	public ArrayList<User> getAll(int offset) throws DAOException {
		ArrayList<User> listUsers = new ArrayList<User>();
		try {
			Statement stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM utilisateur limit 10 offset " + offset*10);
			
			while(this.rs.next()) {
				User utilisateurTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
				listUsers.add(utilisateurTemp);
			}
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return listUsers;
	}

	@Override
	public void add(User obj) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("INSERT INTO utilisateur (id, nom, prenom, login, password) VALUES (DEFAULT,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			this.prepStmt.setString(1, obj.getNom());
			this.prepStmt.setString(2, obj.getPrenom());
			this.prepStmt.setString(3, obj.getLogin());
			this.prepStmt.setString(4, obj.getPassword());
			this.prepStmt.executeUpdate();
			this.rs = prepStmt.getGeneratedKeys();
			this.rs.next();
			obj.setId(this.rs.getInt(1));
		} catch (SQLException e) {
			throw new DAOException("Echec d'insertion dans la base");
		} finally {
			close();
		}
	}

	@Override
	public void remove(User obj) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("DELETE FROM utilisateur WHERE id=?");
			this.prepStmt.setInt(1, (int) obj.getId());
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}

	@Override
	public void update(User obj) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("UPDATE utilisateur SET nom=?, prenom=?, login=?, password=? WHERE id=?");
			this.prepStmt.setString(1, obj.getNom());
			this.prepStmt.setString(2, obj.getPrenom());
			this.prepStmt.setString(3, obj.getLogin());
			this.prepStmt.setString(4, obj.getPassword());
			this.prepStmt.setInt(5, (int) obj.getId());
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}
	
	
	public boolean existUser(String login) throws DAOException {
		ArrayList<User> listUtilisateurs = getAll();
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
