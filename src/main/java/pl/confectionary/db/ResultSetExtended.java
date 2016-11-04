package pl.confectionary.db;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;


public class ResultSetExtended {

	public ResultSetExtended(ResultSet rs) {
		this.rs = rs;
	}

	public long getLong(String columnLabel) throws SQLException {
		return rs.getLong(columnLabel);
	}

	public String getString(String columnLabel) throws SQLException {
		return rs.getString(columnLabel);
	}

	public MonetaryAmount getMonetaryAmount(String amountColumnLabel, String currencyColumnLabel) throws SQLException {
		String currencyCode = rs.getString(currencyColumnLabel);
		BigDecimal amount = rs.getBigDecimal(amountColumnLabel);
		MonetaryAmount result = Money.of(amount, currencyCode);
		return result;
	}

	public boolean next() throws SQLException {
		return rs.next();
	}

	public ResultSet getResultSet() {
		return rs;
	}
	
	private ResultSet rs;

	public long getLong(int columnIndex) throws SQLException {
		return rs.getLong(columnIndex);
	}
}
