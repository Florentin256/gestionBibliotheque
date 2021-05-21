package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.User;

public class UserDAO implements DAO<User, Integer> {

	public UserDAO() {}
	
	
	public boolean existUser(String login) throws DaoException {
		boolean res = false;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHandler.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM utilisateur where login='" + login +"'");
			if (rs.next()) {
				res = true;
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
		return res;
	}
	
	private User getUserByLogin(String login) throws DaoException {
		User utilisateur = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHandler.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "'");
			rs.next();
			utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
		return utilisateur;
	}
	
	public User getUserByLoginPassword(String login, String password) throws DaoException {
		User utilisateur = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHandler.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "' AND password='" + password + "'");
			rs.next();
			utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
		return utilisateur;
	}
	
	public boolean trueLoginPassword(String login, String password) throws DaoException {
		User utilisateur = getUserByLogin(login);
		if (utilisateur.getPassword().equals(password)) {
			return true;
		}
		return false;
	}
	
	
	///////////////////////////////////////////////////////

	@Override
	public User getById(Integer id) throws DaoException {
		User userTemp = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = (Statement) ConnectionHandler.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE id=" + id);
			rs.next();
			userTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), (int)id);
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
		return userTemp;
	}

	@Override
	public List<User> getAll(Pagination pagination) throws DaoException {
		ArrayList<User> listUsers = new ArrayList<User>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHandler.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM utilisateur limit" + pagination.getLimit() + " offset " + pagination.getOffset()*pagination.getLimit());
			
			while(rs.next()) {
				User utilisateurTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
				listUsers.add(utilisateurTemp);
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
		return listUsers;
	}

	@Override
	public void add(User entity) throws DaoException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO utilisateur (id, nom, prenom, login, password) VALUES (DEFAULT,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, entity.getNom());
			stmt.setString(2, entity.getPrenom());
			stmt.setString(3, entity.getLogin());
			stmt.setString(4, entity.getPassword());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			entity.setId(rs.getInt(1));
		} catch (SQLException e) {
			throw new DaoException("Echec d'insertion dans la base");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
	}

	@Override
	public void remove(Integer id) throws DaoException {
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionHandler.getConnection().prepareStatement("DELETE FROM utilisateur WHERE id=?");
			stmt.setInt(1, (int)id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
	}

	@Override
	public void update(User entity) throws DaoException {
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionHandler.getConnection().prepareStatement("UPDATE utilisateur SET nom=?, prenom=?, login=?, password=? WHERE id=?");
			stmt.setString(1, entity.getNom());
			stmt.setString(2, entity.getPrenom());
			stmt.setString(3, entity.getLogin());
			stmt.setString(4, entity.getPassword());
			stmt.setInt(5, (int) entity.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
	}
	
}
