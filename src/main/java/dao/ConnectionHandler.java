package dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionHandler {
	
	private static Connection connect = null;
	
	private ConnectionHandler() {}
	
	static {
		try {
			InitialContext cxt = new InitialContext();
			DataSource ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/postgres");
			connect = ds.getConnection();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return connect;
	}
}
