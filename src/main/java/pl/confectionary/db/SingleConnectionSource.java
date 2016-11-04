package pl.confectionary.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleConnectionSource extends ConnectionSource {

	@Override
	public Connection getConnection() {
		return connection;
	}

	@Override
	public void releaseConnection(Connection c) {
		
	}
	
	public SingleConnectionSource() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (SQLException | ClassNotFoundException e) {
			throw new ApplicationException(e);
		}
	}
	
	private Connection connection;

}
