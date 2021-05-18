package dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectDAO {
	
	private static Connection connect = null;
	
	public ConnectDAO() {
		InitialContext cxt = null;
		try {
			cxt = new InitialContext();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataSource ds = null;
		try {
			ds = (DataSource)cxt.lookup("java:/comp/env/jdbc/postgres");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection connect = null;
		try {
			if (connect == null) {
				connect = ds.getConnection();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connection connect() {
		return connect;
	}
}
