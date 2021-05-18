package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

import entites.*;

public class LivreDAO {
	
	private Connection connect;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public LivreDAO(Connection connect) {
		this.connect = connect;
	}
	
	public void close() throws SQLException {
		rs.close();
		stmt.close();
	}
	
	public Livre getLivreWithId(int id) throws NamingException, SQLException {
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM livre WHERE id=" + id);
		rs.next();
		AuteurDAO Adao = new AuteurDAO(connect);
		Livre livreTemp = new Livre(rs.getString("titre"), Adao.getAuteurWithId(rs.getInt("auteur")), rs.getDate("date_parution"), getLivreTagWithId(id));
		livreTemp.setId(id);
		rs.close();
		st.close();
		return livreTemp;
	}
	
	public void addLivreTagWithId(int id, String tag) throws NamingException, SQLException {
		this.stmt = connect.prepareStatement("INSERT INTO tag VALUES (?,?)");
		this.stmt.setInt(1, id);
		this.stmt.setString(2, tag);
		this.stmt.executeUpdate();
		stmt.close();
	}
	
	public ArrayList<String> getLivreTagWithId(int id_livre) throws SQLException, NamingException {
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
	
	public ArrayList<Livre> getLivres() throws NamingException, SQLException {
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM livre");
		
		ArrayList<Livre> listLivres = new ArrayList<Livre>();
		while(this.rs.next()) {
			AuteurDAO Adao = new AuteurDAO(connect);
			int id = rs.getInt("id");
			Livre livreTemp = new Livre(rs.getString("titre"), Adao.getAuteurWithId(rs.getInt("auteur")),rs.getDate("date_parution"), getLivreTagWithId(id));
			livreTemp.setId(id);
			listLivres.add(livreTemp);
		}
		rs.close();
		st.close();
		return listLivres;
		
	}
	
	public void ajoutLivre(Livre livre) throws Exception {
		this.stmt = connect.prepareStatement("INSERT INTO livre VALUES (DEFAULT,?,?,?)", Statement.RETURN_GENERATED_KEYS);
		this.stmt.setString(1, livre.getTitre());
		this.stmt.setDate(2, livre.getDateParution());
		this.stmt.setInt(3, livre.getAuteur().getId());
		this.stmt.executeUpdate();
		this.rs = stmt.getGeneratedKeys();
		this.rs.next();
		livre.setId(this.rs.getInt(1));
		close();
	}
	
	public void supprimerLivre(Livre livre) throws Exception {
		this.stmt = connect.prepareStatement("DELETE FROM tag WHERE id_livre=?");
		this.stmt.setInt(1, livre.getId());
		this.stmt.executeUpdate();
		this.stmt = connect.prepareStatement("DELETE FROM livre WHERE id=?");
		this.stmt.setInt(1, livre.getId());
		this.stmt.executeUpdate();
		close();
	}
	
	public void modifierLivre(Livre livre) throws Exception {
		this.stmt = connect.prepareStatement("UPDATE livre SET titre=?, date_parution=?, auteur=? WHERE id=?");
		this.stmt.setString(1, livre.getTitre());
		this.stmt.setDate(2, livre.getDateParution());
		this.stmt.setInt(3, livre.getAuteur().getId());
		this.stmt.setInt(4, livre.getId());
		this.stmt.executeUpdate();
		close();
	}
}
