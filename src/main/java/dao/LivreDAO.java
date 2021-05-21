package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import beans.*;

public class LivreDAO implements DAO<Livre, Integer> {
	
	private Connection connect;
	private PreparedStatement prepStmt;
	private ResultSet rs;
	
	public LivreDAO(Connection connect) {
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
		} catch (SQLException e) {
			throw new DaoException("Echec de fermeture des Statement et ResultSet");
		}
	}
	
	public void addTagToBookById(int id, String tag) throws DaoException {
		try {
			this.prepStmt = connect.prepareStatement("INSERT INTO tag VALUES (?,?)");
			this.prepStmt.setInt(1, id);
			this.prepStmt.setString(2, tag);
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}
	
	// Méthode private, utilise un Statement et un ResultSet séparé
	// (appelée dans une méthode de la classe this)
	private ArrayList<String> getTagOfBookById(int id_livre) throws SQLException, NamingException {
		Statement st = connect.createStatement();
		ResultSet rs2 = st.executeQuery("SELECT libelle FROM tag WHERE id_livre=" + id_livre);
		ArrayList<String> tags = new ArrayList<String>();
		while(rs2.next()) {
			tags.add(rs2.getString("libelle"));
		}
		st.close();
		rs2.close();
		return tags;
	}


	///////////////////////////////////////////
	
	@Override
	public Livre getById(Integer id) throws DaoException {
		Livre livreTemp = null;
		Statement st = null;
		try {
			st = connect.createStatement();
			this.rs = st.executeQuery("SELECT * FROM livre WHERE id=" + id);
			rs.next();
			AuteurDAO Adao = new AuteurDAO(connect);
			livreTemp = new Livre((int)id, rs.getString("titre"), Adao.getById(rs.getInt("auteur")), rs.getDate("date_parution"), getTagOfBookById((int)id));
		} catch (SQLException | NamingException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return livreTemp;
	}

	@Override
	public List<Livre> getAll(Pagination pagination) throws DaoException {
		ArrayList<Livre> listLivres = new ArrayList<Livre>();
		Statement st = null;
		try {
			st = connect.createStatement();
			String orderBy = "";
			if (pagination.getOrderBy() != null) {
				orderBy = "order by " + pagination.getOrderBy();
			}
			this.rs = st.executeQuery("SELECT * FROM livre " + orderBy + " limit " + pagination.getLimit() + " offset " + pagination.getOffset()*pagination.getLimit());
			
			while(this.rs.next()) {
				AuteurDAO Adao = new AuteurDAO(connect);
				int id = rs.getInt("id");
				Livre livreTemp = new Livre(id, rs.getString("titre"), Adao.getById(rs.getInt("auteur")),rs.getDate("date_parution"), getTagOfBookById(id));
				listLivres.add(livreTemp);
			}
		} catch (SQLException | NamingException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listLivres;
	}

	@Override
	public void add(Livre entity) throws DaoException {
		try {
			this.prepStmt = connect.prepareStatement("INSERT INTO livre (id, titre, date_parution, auteur) VALUES (DEFAULT,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			this.prepStmt.setString(1, entity.getTitre());
			this.prepStmt.setDate(2, entity.getDateParution());
			this.prepStmt.setInt(3, entity.getAuteur().getId());
			this.prepStmt.executeUpdate();
			this.rs = prepStmt.getGeneratedKeys();
			this.rs.next();
			entity.setId(this.rs.getInt(1));
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}

	@Override
	public void remove(Integer id) throws DaoException {
		try {
			this.prepStmt = connect.prepareStatement("DELETE FROM tag WHERE id_livre=?");
			this.prepStmt.setInt(1, (int)id);
			this.prepStmt.executeUpdate();
			this.prepStmt = connect.prepareStatement("DELETE FROM livre WHERE id=?");
			this.prepStmt.setInt(1, (int)id);
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}

	@Override
	public void update(Livre entity) throws DaoException {
		try {
			this.prepStmt = connect.prepareStatement("UPDATE livre SET titre=?, date_parution=?, auteur=? WHERE id=?");
			this.prepStmt.setString(1, entity.getTitre());
			this.prepStmt.setDate(2, entity.getDateParution());
			this.prepStmt.setInt(3, (int) entity.getAuteur().getId());
			this.prepStmt.setInt(4, (int) entity.getId());
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("Echec de la requête");
		} finally {
			close();
		}
	}
}
