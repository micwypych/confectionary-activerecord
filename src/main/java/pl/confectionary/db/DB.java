package pl.confectionary.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

	public static PreparedStatement prepareStatement(String query) throws SQLException {
		Connection c = getConnection();
		PreparedStatement stmt = c.prepareStatement(query); 
		return stmt;
	}

	public static Connection getConnection() {
		return ConnectionSource.getInstance().getConnection();
	}

	public static void cleanUp(PreparedStatement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
	}

	public static void cleanUp(PreparedStatement stmt, ResultSetExtended rse) {
		ResultSet rs; 
		if ( rse == null ) {
			rs = null;
		} else {
			rs = rse.getResultSet();
		}
		cleanUp(stmt,rs);
	}

}
