package dao;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Auteur;


public class AuteurDAO implements DAO<Auteur, Integer> {
	
	public AuteurDAO() {}

	@Override
	public Auteur getById(Integer id) throws DaoException {
		Auteur auteurTemp = null;
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM auteur WHERE id=?")) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), (int)id);
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la récupération de l'auteur par son id", e);
		}
		return auteurTemp;
	}
	
	@Override
	public List<Auteur> getAll(Pagination pagination) throws DaoException {
		ArrayList<Auteur> listAuteurs = new ArrayList<Auteur>();
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM auteur limit ? offset ?")) {
			stmt.setInt(1, pagination.getLimit());
			stmt.setInt(2, pagination.getOffset()*pagination.getLimit());
			try (ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					Auteur auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), rs.getInt("id"));
					listAuteurs.add(auteurTemp);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la récupération de la liste des auteurs", e);
		}
		return listAuteurs;
	}

	@Override
	public void add(Auteur entity) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO auteur (id, nom, prenom) VALUES (DEFAULT,?,?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, entity.getNom());
			stmt.setString(2, entity.getPrenom());
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				entity.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new DaoException("Echec d'insertion de l'auteur dans la base", e);
		}
	}

	/**
	 * @throws DaoException
	 * 			Si l'auteur a supprimer est une dependance d'un ou plusieurs livres encore dans la base
	 */
	@Override
	public void remove(Integer id) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("DELETE FROM auteur WHERE id=?")) {
			stmt.setInt(1, (int)id);
			try {
				stmt.executeUpdate();
			} catch (SQLException e) {
				// Ne peut pas etre supprime car dependance d'un livre
				throw new DaoException("L'auteur est une dépendance d'un livre, suppression impossible");
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la suppression de l'auteur dans la base", e);
		}
	}

	@Override
	public void update(Auteur entity) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("UPDATE auteur SET nom=?, prenom=? WHERE id=?")) {
			stmt.setString(1, entity.getNom());
			stmt.setString(2, entity.getPrenom());
			stmt.setInt(3, (int) entity.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la mise à jour de l'auteur dans la base", e);
		}
	}

}
