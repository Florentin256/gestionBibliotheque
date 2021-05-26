package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import beans.Livre;

public class LivreDAO implements DAO<Livre, Integer> {
	
	public LivreDAO() {}
	
	/**
	 * Ajoute un tag à un livre donné par son id.
	 * 
	 * @param id
	 * 			L'id du livre dans la table tag
	 * @param tag
	 * 			Le tag à ajouter dans la BD
	 * @throws DaoException
	 */
	public void addTagToBookById(int id, String tag) throws DaoException {
		try (PreparedStatement prepStmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO tag VALUES (?,?)")) {
			prepStmt.setInt(1, id);
			prepStmt.setString(2, tag);
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec lors de l'insertion du tag du livre", e);
		}
	}
	
	// Méthode private, appelée dans une méthode de la classe this
	/**
	 * Retourne une liste de tags associés au livre donné par son id.
	 * 
	 * @param id_livre
	 * @return
	 * @throws SQLException
	 * @throws NamingException
	 * @throws DaoException 
	 */
	private ArrayList<String> getTagOfBookById(int id_livre) throws DaoException {
		ArrayList<String> tags = new ArrayList<String>();
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT libelle FROM tag WHERE id_livre=?")) {
			stmt.setInt(1, id_livre);
			try (ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					tags.add(rs.getString("libelle"));
				}
			}
		} catch(SQLException e) {
			throw new DaoException("Echec lors de la récupération des tags du livre", e);
		}
		return tags;
	}


	///////////////////////////////////////////
	
	@Override
	public Livre getById(Integer id) throws DaoException {
		Livre livreTemp = null;
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("SELECT * FROM livre WHERE id=?")) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				AuteurDAO Adao = new AuteurDAO();
				livreTemp = new Livre((int)id, rs.getString("titre"), Adao.getById(rs.getInt("auteur")), rs.getDate("date_parution"), getTagOfBookById((int)id));
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la récupération du livre par son id", e);
		}
		return livreTemp;
	}

	@Override
	public List<Livre> getAll(Pagination pagination) throws DaoException {
		ArrayList<Livre> listLivres = new ArrayList<Livre>();
		String query = "SELECT * FROM livre limit ? offset ?";
		if (pagination.getOrderBy() != null) {
			query = "SELECT * FROM livre order by " + pagination.getOrderBy() + " limit ? offset ? ";
		}
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement(query)) {
			stmt.setInt(1, pagination.getLimit());
			stmt.setInt(2, pagination.getOffset()*pagination.getLimit());
			try (ResultSet rs = stmt.executeQuery()) {
				while(rs.next()) {
					AuteurDAO Adao = new AuteurDAO();
					int id = rs.getInt("id");
					Livre livreTemp = new Livre(id, rs.getString("titre"), Adao.getById(rs.getInt("auteur")),rs.getDate("date_parution"), getTagOfBookById(id));
					listLivres.add(livreTemp);
				}
			}
		} catch (SQLException e) {
			throw new DaoException("Echec lors de la réupération de la liste des livres", e);
		}
		return listLivres;
	}

	@Override
	public void add(Livre entity) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO livre (id, titre, date_parution, auteur) VALUES (DEFAULT,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, entity.getTitre());
			stmt.setDate(2, entity.getDateParution());
			stmt.setInt(3, entity.getAuteur().getId());
			stmt.executeUpdate();
			try (ResultSet rs = stmt.getGeneratedKeys()) {
				rs.next();
				entity.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			throw new DaoException("Echec d'insertion du livre dans la base", e);
		}
	}

	/**
	 * La suppression d'un Livre entraine la suppression de tous les tags associés.
	 */
	@Override
	public void remove(Integer id) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("DELETE FROM livre WHERE id=?")) {
			stmt.setInt(1, (int)id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la suppression du livre dans la base", e);
		}
	}

	@Override
	public void update(Livre entity) throws DaoException {
		try (PreparedStatement stmt = ConnectionHandler.getConnection().prepareStatement("UPDATE livre SET titre=?, date_parution=?, auteur=? WHERE id=?")) {
			stmt.setString(1, entity.getTitre());
			stmt.setDate(2, entity.getDateParution());
			stmt.setInt(3, (int) entity.getAuteur().getId());
			stmt.setInt(4, (int) entity.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la mise à jour du livre dans la base", e);
		}
	}
}
