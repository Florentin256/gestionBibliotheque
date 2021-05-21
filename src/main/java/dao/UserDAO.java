package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.User;

public class UserDAO implements DAO<User, Integer> {

	private PreparedStatement stmt;
	private ResultSet rs;
	
	public UserDAO() {}
	
	private void close() throws DaoException {
		try {
			if (rs!=null) {
				rs.close();
			}
			if (stmt!=null) {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de fermeture des Statement et ResultSet");
		}
	}
	
	public boolean existUser(String login) throws DaoException {
		boolean res = false;
		try {
			Statement st = ConnectionHandler.getConnection().createStatement();
			this.rs = st.executeQuery("SELECT * FROM utilisateur where login='" + login +"'");
			if (this.rs.next()) {
				res = true;
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
		return res;
	}
	
	private User getUserByLogin(String login) throws DaoException {
		User utilisateur = null;
		try {
			Statement st = ConnectionHandler.getConnection().createStatement();
			this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "'");
			rs.next();
			utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
		return utilisateur;
	}
	
	public User getUserByLoginPassword(String login, String password) throws DaoException {
		User utilisateur = null;
		try {
			Statement st = ConnectionHandler.getConnection().createStatement();
			this.rs = st.executeQuery("SELECT * FROM utilisateur WHERE login='" + login + "' AND password='" + password + "'");
			rs.next();
			utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
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
		try {
			Statement stmt = (Statement) ConnectionHandler.getConnection().createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM utilisateur WHERE id=" + id);
			rs.next();
			userTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), (int)id);
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
		return userTemp;
	}

	@Override
	public List<User> getAll(Pagination pagination) throws DaoException {
		ArrayList<User> listUsers = new ArrayList<User>();
		try {
			Statement stmt = ConnectionHandler.getConnection().createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM utilisateur limit" + pagination.getLimit() + " offset " + pagination.getOffset()*pagination.getLimit());
			
			while(this.rs.next()) {
				User utilisateurTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
				listUsers.add(utilisateurTemp);
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
		return listUsers;
	}

	@Override
	public void add(User entity) throws DaoException {
		try {
			this.stmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO utilisateur (id, nom, prenom, login, password) VALUES (DEFAULT,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			this.stmt.setString(1, entity.getNom());
			this.stmt.setString(2, entity.getPrenom());
			this.stmt.setString(3, entity.getLogin());
			this.stmt.setString(4, entity.getPassword());
			this.stmt.executeUpdate();
			this.rs = stmt.getGeneratedKeys();
			this.rs.next();
			entity.setId(this.rs.getInt(1));
		} catch (SQLException e) {
			throw new DaoException("Echec d'insertion dans la base");
		} finally {
			close();
		}
	}

	@Override
	public void remove(Integer id) throws DaoException {
		try {
			this.stmt = ConnectionHandler.getConnection().prepareStatement("DELETE FROM utilisateur WHERE id=?");
			this.stmt.setInt(1, (int)id);
			this.stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}

	@Override
	public void update(User entity) throws DaoException {
		try {
			this.stmt = ConnectionHandler.getConnection().prepareStatement("UPDATE utilisateur SET nom=?, prenom=?, login=?, password=? WHERE id=?");
			this.stmt.setString(1, entity.getNom());
			this.stmt.setString(2, entity.getPrenom());
			this.stmt.setString(3, entity.getLogin());
			this.stmt.setString(4, entity.getPassword());
			this.stmt.setInt(5, (int) entity.getId());
			this.stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}
	
}
