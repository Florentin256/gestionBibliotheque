package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import entites.*;

public class LivreDAO {
	
	private Connection connect;
	private PreparedStatement stmt;
	private ResultSet rs;
	
	public LivreDAO() {}
	
	public void init() throws NamingException, SQLException {
		InitialContext cxt = new InitialContext();
		DataSource ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/postgres");
		this.connect = ds.getConnection();
	}
	
	public void close() throws SQLException {
		rs.close();
		stmt.close();
		connect.close();
	}
	
	public Livre getLivreWithId(int id) throws NamingException, SQLException {
		init();
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM livre WHERE id=" + id);
		rs.next();
		AuteurDAO Adao = new AuteurDAO();
		Livre livreTemp = new Livre(rs.getString("titre"), Adao.getAuteurWithId(rs.getInt("auteur")), rs.getDate("date_parution"));
		livreTemp.setId(id);
		rs.close();
		st.close();
		connect.close();
		return livreTemp;
	}
	
	public void addLivreTagWithId(int id, String tag) throws NamingException, SQLException {
		init();
		this.stmt = connect.prepareStatement("INSERT INTO tag VALUES (?,?)");
		this.stmt.setInt(1, id);
		this.stmt.setString(2, tag);
		this.stmt.executeUpdate();
		stmt.close();
		connect.close();
	}
	
	public ArrayList<String> getLivreTagWithId(int id_livre) throws SQLException, NamingException {
		init();
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT libelle FROM tag WHERE id_livre=" + id_livre);
		ArrayList<String> tags = new ArrayList<String>();
		while(this.rs.next()) {
			tags.add(rs.getString("libelle"));
		}
		rs.close();
		st.close();
		connect.close();
		return tags;
	}
	
	public ArrayList<Livre> getLivres() throws NamingException, SQLException {
		init();
		Statement st = connect.createStatement();
		this.rs = st.executeQuery("SELECT * FROM livre");
		
		ArrayList<Livre> listLivres = new ArrayList<Livre>();
		while(this.rs.next()) {
			AuteurDAO Adao = new AuteurDAO();
			Livre livreTemp = new Livre(rs.getString("titre"), Adao.getAuteurWithId(rs.getInt("auteur")),rs.getDate("date_parution"));
			livreTemp.setId(rs.getInt("id"));
			listLivres.add(livreTemp);
		}
		rs.close();
		st.close();
		connect.close();
		return listLivres;
		
	}
	
	public void ajoutLivre(Livre livre) throws Exception {
		init();
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
		init();
		this.stmt = connect.prepareStatement("DELETE FROM livre WHERE id=?");
		this.stmt.setInt(1, livre.getId());
		this.stmt.executeUpdate();
		close();
	}
	
	public void modifierLivre(Livre livre) throws Exception {
		init();
		this.stmt = connect.prepareStatement("UPDATE livre SET titre=?, date_parution=?, auteur=? WHERE id=?");
		this.stmt.setString(1, livre.getTitre());
		this.stmt.setDate(2, livre.getDateParution());
		this.stmt.setInt(3, livre.getAuteur().getId());
		this.stmt.setInt(4, livre.getId());
		this.stmt.executeUpdate();
		close();
	}
}
