package pl.confectionary.db;

import java.sql.Connection;

public abstract class ConnectionSource {

	public abstract Connection getConnection();
	
	public abstract void releaseConnection(Connection c);
	
	public static ConnectionSource getInstance() {
		return cs ;
	}
	
	private static ConnectionSource cs = new SingleConnectionSource();
}
