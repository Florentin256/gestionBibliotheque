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
		PreparedStatement prepStmt = null;
		try {
			prepStmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO tag VALUES (?,?)");
			prepStmt.setInt(1, id);
			prepStmt.setString(2, tag);
			prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				prepStmt.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
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
	 */
	private ArrayList<String> getTagOfBookById(int id_livre) throws SQLException, NamingException {
		Statement stmt = ConnectionHandler.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery("SELECT libelle FROM tag WHERE id_livre=" + id_livre);
		ArrayList<String> tags = new ArrayList<String>();
		while(rs.next()) {
			tags.add(rs.getString("libelle"));
		}
		stmt.close();
		rs.close();
		return tags;
	}


	///////////////////////////////////////////
	
	@Override
	public Livre getById(Integer id) throws DaoException {
		Livre livreTemp = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHandler.getConnection().createStatement();
			rs = stmt.executeQuery("SELECT * FROM livre WHERE id=" + id);
			rs.next();
			AuteurDAO Adao = new AuteurDAO();
			livreTemp = new Livre((int)id, rs.getString("titre"), Adao.getById(rs.getInt("auteur")), rs.getDate("date_parution"), getTagOfBookById((int)id));
		} catch (SQLException | NamingException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
		return livreTemp;
	}

	@Override
	public List<Livre> getAll(Pagination pagination) throws DaoException {
		ArrayList<Livre> listLivres = new ArrayList<Livre>();
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = ConnectionHandler.getConnection().createStatement();
			String orderBy = "";
			if (pagination.getOrderBy() != null) {
				orderBy = "order by " + pagination.getOrderBy();
			}
			rs = stmt.executeQuery("SELECT * FROM livre " + orderBy + " limit " + pagination.getLimit() + " offset " + pagination.getOffset()*pagination.getLimit());
			
			while(rs.next()) {
				AuteurDAO Adao = new AuteurDAO();
				int id = rs.getInt("id");
				Livre livreTemp = new Livre(id, rs.getString("titre"), Adao.getById(rs.getInt("auteur")),rs.getDate("date_parution"), getTagOfBookById(id));
				listLivres.add(livreTemp);
			}
		} catch (SQLException | NamingException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				throw new DaoException("Echec de fermeture de Statement et ResultSet");
			}
		}
		return listLivres;
	}

	@Override
	public void add(Livre entity) throws DaoException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = ConnectionHandler.getConnection().prepareStatement("INSERT INTO livre (id, titre, date_parution, auteur) VALUES (DEFAULT,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, entity.getTitre());
			stmt.setDate(2, entity.getDateParution());
			stmt.setInt(3, entity.getAuteur().getId());
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			rs.next();
			entity.setId(rs.getInt(1));
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
	}

	/**
	 * La suppression d'un Livre entraine la suppression de tous les tags associés.
	 */
	@SuppressWarnings("resource")
	@Override
	public void remove(Integer id) throws DaoException {
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionHandler.getConnection().prepareStatement("DELETE FROM tag WHERE id_livre=?");
			stmt.setInt(1, (int)id);
			stmt.executeUpdate();
			stmt = ConnectionHandler.getConnection().prepareStatement("DELETE FROM livre WHERE id=?");
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
	public void update(Livre entity) throws DaoException {
		PreparedStatement stmt = null;
		try {
			stmt = ConnectionHandler.getConnection().prepareStatement("UPDATE livre SET titre=?, date_parution=?, auteur=? WHERE id=?");
			stmt.setString(1, entity.getTitre());
			stmt.setDate(2, entity.getDateParution());
			stmt.setInt(3, (int) entity.getAuteur().getId());
			stmt.setInt(4, (int) entity.getId());
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
