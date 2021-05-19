package dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectDAO {
	
	private static Connection connect = null;
	
	public ConnectDAO() throws DAOException {
		try {
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/postgres");
			connect = ds.getConnection();
		} catch (NamingException | SQLException e) {
			throw new DAOException("Echec de connexion à la datasource");
		}
	}
	
	public Connection connect() {
		return connect;
	}
}
