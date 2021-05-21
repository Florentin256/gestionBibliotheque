package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Auteur;


public class AuteurDAO implements DAO<Auteur, Integer> {
	
	private Connection connect;
	private PreparedStatement prepStmt;
	private Statement stmt;
	private ResultSet rs;
	
	public AuteurDAO(Connection connect) {
		this.connect = connect;
	}
	
	private void close() throws DaoException {
		try {
			if (rs!=null) {
				rs.close();
			}
			if (prepStmt!=null) {
				prepStmt.close();
			}
			if (stmt!=null) {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de fermeture des Statement et ResultSet");
		}
	}
	
	public ArrayList<Auteur> getAuthors() throws DaoException {
		ArrayList<Auteur> listAuteurs = new ArrayList<Auteur>();
		try {
			stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur");
			
			while(this.rs.next()) {
				Auteur auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), rs.getInt("id"));
				listAuteurs.add(auteurTemp);
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
		return listAuteurs;
	}

/////////////////////////////////////////////////////


	@Override
	public Auteur getById(Integer id) throws DaoException {
		Auteur auteurTemp = null;
		try {
			stmt = (Statement) connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur WHERE id=" + id);
			rs.next();
			auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), (int)id);
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
		return auteurTemp;
	}
	
	@Override
	public List<Auteur> getAll(Pagination pagination) throws DaoException {
		ArrayList<Auteur> listAuteurs = new ArrayList<Auteur>();
		try {
			stmt = connect.createStatement();
			this.rs = stmt.executeQuery("SELECT * FROM auteur limit" + pagination.getLimit() + " offset " + pagination.getOffset()*pagination.getLimit());
			
			while(this.rs.next()) {
				Auteur auteurTemp = new Auteur(rs.getString("nom"), rs.getString("prenom"), rs.getInt("id"));
				listAuteurs.add(auteurTemp);
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
		return listAuteurs;
	}

	@Override
	public void add(Auteur entity) throws DaoException {
		try {
			this.prepStmt = connect.prepareStatement("INSERT INTO auteur (id, nom, prenom) VALUES (DEFAULT,?,?)", Statement.RETURN_GENERATED_KEYS);
			this.prepStmt.setString(1, entity.getNom());
			this.prepStmt.setString(2, entity.getPrenom());
			this.prepStmt.executeUpdate();
			this.rs = prepStmt.getGeneratedKeys();
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
			this.prepStmt = connect.prepareStatement("DELETE FROM auteur WHERE id=?");
			this.prepStmt.setInt(1, (int)id);
			try {
				this.prepStmt.executeUpdate();
			} catch (SQLException e) {
				// Ne peut pas etre supprime car dependance d'un livre
				throw new DaoException("L'auteur est une dépendance d'un livre, suppression impossible");
			}
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}

	@Override
	public void update(Auteur entity) throws DaoException {
		try {
			this.prepStmt = connect.prepareStatement("UPDATE auteur SET nom=?, prenom=? WHERE id=?");
			this.prepStmt.setString(1, entity.getNom());
			this.prepStmt.setString(2, entity.getPrenom());
			this.prepStmt.setInt(3, (int) entity.getId());
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}

}
