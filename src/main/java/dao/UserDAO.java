package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.User;

public class UserDAO implements DAO<User, Integer> {

	/**
	 * Renvoie True si le login de l'utilisateur existe dans la BD.
	 * False sinon.
	 * 
	 * @param login
	 * @return
	 * @throws DaoException
	 */
	public boolean existUser(String login) throws DaoException {
		boolean res = false;
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM utilisateur where login=?")) {
			stmt.setString(1, login);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					res = true;
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la verification de l'existance du login", e);
		}
		return res;
	}
	
	/**
	 * Retourne l'utilisateur donne par son login.
	 * 
	 * @param login
	 * @return
	 * @throws DaoException
	 */
	private User getUserByLogin(String login) throws DaoException {
		User utilisateur = null;
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM utilisateur WHERE login=?")) {
			stmt.setString(1, login);
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la recuperation de l'utilisateur par son login", e);
		}
		return utilisateur;
	}
	
	/**
	 * Retourne l'utilisateur donne par son login et password.
	 * 
	 * @param login
	 * @param password
	 * @return
	 * @throws DaoException
	 */
	public User getUserByLoginPassword(String login, String password) throws DaoException {
		User utilisateur = null;
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM utilisateur WHERE login=? AND password=?")) {
			stmt.setString(1, login);
			stmt.setString(2, password);
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				utilisateur = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la recuperation de l'utilisateur par son login et password", e);
		}
		return utilisateur;
	}
	
	/**
	 * Renvoie True si le login et password de l'utilisateur son corrects.
	 * False sinon.
	 * 
	 * @param login
	 * @param password
	 * @return
	 * @throws DaoException
	 */
	public boolean trueLoginPassword(String login, String password) throws DaoException {
		User utilisateur = getUserByLogin(login);
		boolean res = false;
		if (utilisateur.getPassword().equals(password)) {
			res = true;
		}
		return res;
	}
	
	
	///////////////////////////////////////////////////////

	@Override
	public User getById(Integer id) throws DaoException {
		User userTemp = null;
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM utilisateur WHERE id=?")) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				userTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), id);
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la recuperation de l'utilisateur par son id", e);
		}
		return userTemp;
	}

	@Override
	public List<User> getAll(Pagination pagination) throws DaoException {
		ArrayList<User> listUsers = new ArrayList<>();
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM utilisateur limit ? offset ?")) {
			stmt.setInt(1, pagination.getLimit());
			stmt.setInt(2, pagination.getOffset()*pagination.getLimit());
			try (ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					User utilisateurTemp = new User(rs.getString("nom"), rs.getString("prenom"), rs.getString("login"), rs.getString("password"), rs.getInt("id"));
					listUsers.add(utilisateurTemp);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la recuperation de la liste des utilisateurs", e);
		}
		return listUsers;
	}

	@Override
	public void add(User entity) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO utilisateur (id, nom, prenom, login, password) VALUES (DEFAULT,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, entity.getNom());
			stmt.setString(2, entity.getPrenom());
			stmt.setString(3, entity.getLogin());
			stmt.setString(4, entity.getPassword());
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				entity.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de l'insertion de l'utilisateur dans la base", e);
		}
	}

	@Override
	public void remove(Integer id) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("DELETE FROM utilisateur WHERE id=?")) {
			stmt.setInt(1, (int)id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la suppression de l'utilisateur dans la base", e);
		}
	}

	@Override
	public void update(User entity) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("UPDATE utilisateur SET nom=?, prenom=?, login=?, password=? WHERE id=?")) {
			stmt.setString(1, entity.getNom());
			stmt.setString(2, entity.getPrenom());
			stmt.setString(3, entity.getLogin());
			stmt.setString(4, entity.getPassword());
			stmt.setInt(5, (int) entity.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la mise a jour de l'utilisateur dans la base", e);
		}
	}
	
}
