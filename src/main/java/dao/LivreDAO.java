package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

import beans.*;

public class LivreDAO {
	
	private Connection connect;
	private PreparedStatement prepStmt;
	private ResultSet rs;
	
	public LivreDAO(Connection connect) {
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
	
	public Livre getLivreWithId(int id) throws DAOException {
		Livre livreTemp = null;
		Statement st = null;
		try {
			st = connect.createStatement();
			this.rs = st.executeQuery("SELECT * FROM livre WHERE id=" + id);
			rs.next();
			AuteurDAO Adao = new AuteurDAO(connect);
			livreTemp = new Livre(id, rs.getString("titre"), Adao.getAuteurWithId(rs.getInt("auteur")), rs.getDate("date_parution"), getLivreTagWithId(id));
		} catch (SQLException | NamingException e) {
			throw new DAOException("Echec de la requête");
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
	
	public void addLivreTagWithId(int id, String tag) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("INSERT INTO tag VALUES (?,?)");
			this.prepStmt.setInt(1, id);
			this.prepStmt.setString(2, tag);
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}
	
	// Méthode private, utilise un Statement et un ResultSet séparé
	// (appelée dans une méthode de la classe this)
	private ArrayList<String> getLivreTagWithId(int id_livre) throws SQLException, NamingException {
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
	
	public ArrayList<Livre> getLivres() throws DAOException {
		ArrayList<Livre> listLivres = new ArrayList<Livre>();
		try {
			Statement st = connect.createStatement();
			this.rs = st.executeQuery("SELECT * FROM livre");
			while(this.rs.next()) {
				AuteurDAO Adao = new AuteurDAO(connect);
				int id = rs.getInt("id");
				Livre livreTemp = new Livre(id, rs.getString("titre"), Adao.getAuteurWithId(rs.getInt("auteur")),rs.getDate("date_parution"), getLivreTagWithId(id));
				listLivres.add(livreTemp);
			}
		} catch (SQLException | NamingException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
		return listLivres;
		
	}
	
	public ArrayList<Livre> getLivres(int offset) throws DAOException {
		ArrayList<Livre> listLivres = new ArrayList<Livre>();
		Statement st = null;
		try {
			st = connect.createStatement();
			this.rs = st.executeQuery("SELECT * FROM livre order by date_parution limit 10 offset " + offset*10);
			
			while(this.rs.next()) {
				AuteurDAO Adao = new AuteurDAO(connect);
				int id = rs.getInt("id");
				Livre livreTemp = new Livre(id, rs.getString("titre"), Adao.getAuteurWithId(rs.getInt("auteur")),rs.getDate("date_parution"), getLivreTagWithId(id));
				listLivres.add(livreTemp);
			}
		} catch (SQLException | NamingException e) {
			throw new DAOException("Echec de la requête");
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
	
	public void ajoutLivre(String titre, Date dateParution, int id_auteur) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("INSERT INTO livre (id, titre, date_parution, auteur) VALUES (DEFAULT,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			this.prepStmt.setString(1, titre);
			this.prepStmt.setDate(2, dateParution);
			this.prepStmt.setInt(3, id_auteur);
			this.prepStmt.executeUpdate();
			this.rs = prepStmt.getGeneratedKeys();
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}
	
	public void supprimerLivre(Livre livre) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("DELETE FROM tag WHERE id_livre=?");
			this.prepStmt.setInt(1, livre.getId());
			this.prepStmt.executeUpdate();
			this.prepStmt = connect.prepareStatement("DELETE FROM livre WHERE id=?");
			this.prepStmt.setInt(1, livre.getId());
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}
	
	public void modifierLivre(Livre livre) throws DAOException {
		try {
			this.prepStmt = connect.prepareStatement("UPDATE livre SET titre=?, date_parution=?, auteur=? WHERE id=?");
			this.prepStmt.setString(1, livre.getTitre());
			this.prepStmt.setDate(2, livre.getDateParution());
			this.prepStmt.setInt(3, livre.getAuteur().getId());
			this.prepStmt.setInt(4, livre.getId());
			this.prepStmt.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Echec de la requête");
		} finally {
			close();
		}
	}
}
