package pl.confectionary.domain;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;

import org.javamoney.moneta.Money;

import pl.confectionary.db.ApplicationException;
import pl.confectionary.db.DB;
import pl.confectionary.db.IdNotFound;
import pl.confectionary.db.RecordNotFound;
import pl.confectionary.db.Registry;
import pl.confectionary.db.ResultSetExtended;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;



public class Choclate {
	
	
	public Choclate(long id, String name, MonetaryAmount cost) {
		this.id = id;
		this.name = name;
		this.cost = cost;
	}

	public Choclate(String name, Money cost) {
		this(Long.MIN_VALUE,name,cost);
	}

	public static Choclate find(long id) {
		Optional<Choclate> oc = Registry.getChoclate(id);
		if ( oc.isPresent() ) {
			return oc.get();
		}
		Choclate choclate;
		PreparedStatement stmt = null;
		ResultSetExtended rs = null;
		try {
			stmt = DB.prepareStatement(FIND_SINGLE_QUERY);
			stmt.setLong(1, id);
			ResultSet tmp = stmt.executeQuery();
			rs = new ResultSetExtended(tmp);
			if( !rs.next() ) {
				throw new RecordNotFound();
			}
			choclate = load(rs);
			return choclate;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,rs);
		}
	}
	
	public static List<Choclate> find(List<Long> ids) {
		List<Choclate> choclates = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSetExtended rs = null;
		try {
			Joiner j = Joiner.on(", ").skipNulls();
			String idsList = "("+j.join(ids)+")";
			stmt = DB.prepareStatement(FIND_MULTIPLE_QUERY.replaceAll("?", idsList));
			ResultSet tmp = stmt.executeQuery();
			rs = new ResultSetExtended(tmp);
			while( rs.next() ) {
				Choclate c = load(rs);
				choclates.add(c);
			}
			return choclates;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,rs);
		}
	}
	
	public static Collection<Choclate> all() {
		Collection<Choclate> choclates = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSetExtended rs = null;
		try {
			stmt = DB.prepareStatement(ALL_QUERY);
			ResultSet tmp = stmt.executeQuery();
			rs = new ResultSetExtended(tmp);
			while( rs.next() ) {
				Choclate c = load(rs);
				choclates.add(c);
			}
			return choclates;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,rs);
		}
	}
	
	public static Choclate first() {
		Choclate choclate;
		PreparedStatement stmt = null;
		ResultSetExtended rs = null;
		try {
			stmt = DB.prepareStatement(FIRST_QUERY);
			stmt.executeQuery();
			rs = new ResultSetExtended(stmt.getResultSet());
			if( !rs.next() ) {
				throw new RecordNotFound();
			}
			choclate = load(rs);
			return choclate;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,rs);
		}
	}
	
	//public static Choclate findBy(Map<String,Object> arg)
	public void create() {
		PreparedStatement stmt = null;
		ResultSetExtended rs = null;
		try {
			stmt = DB.prepareStatement(INSERT_QUERY);
			stmt.setString(1, name);
			stmt.setBigDecimal(2, cost.getNumber().numberValue(BigDecimal.class));
			stmt.setString(3, cost.getCurrency().toString());
			stmt.executeUpdate();
			rs = new ResultSetExtended(stmt.getGeneratedKeys());
			if( !rs.next() ) {
				throw new IdNotFound();
			}
			long id = rs.getLong(1);
			this.id = id;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,rs);
		}
	}
	
	public void update() {
		PreparedStatement stmt = null;
		ResultSetExtended rs = null;
		try {
			stmt = DB.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, name);
			stmt.setBigDecimal(2, cost.getNumber().numberValue(BigDecimal.class));
			stmt.setString(3, cost.getCurrency().toString());
			stmt.setLong(4, id);
			stmt.executeUpdate();
			rs = new ResultSetExtended(stmt.getResultSet());
			if( !rs.next() ) {
				throw new RecordNotFound();
			}
			long id = rs.getLong(1);
			this.id = id;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,rs);
		}
	}
	
	
	public long getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("Choclate{id=%d, name=%s, cost=%s}",id,name,MonetaryFormats.getAmountFormat(Locale.getDefault()).format(cost));
	}
	
	private static Choclate load(ResultSetExtended rs) {
		try {
			long id = rs.getLong(ID_FIELD);
			Optional<Choclate> oc = Registry.getChoclate(id);
			if ( oc.isPresent() ) {
				return oc.get();
			}
			String name = rs.getString(NAME_FIELD);
			MonetaryAmount amount = rs.getMonetaryAmount(COST_AMOUNT_FIELD,COST_CURRENCY_FIELD); 
			Choclate result = new Choclate(id,name,amount);
			Registry.addChoclate(result);
			return result;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		}
		
	}
	
	private long id;
	private String name;
	private MonetaryAmount cost;
	
	
	private final static String TABLE_NAME = "choclates";
	private final static String ID_FIELD = "id";
	private static final String NAME_FIELD = "name";
	private static final String COST_AMOUNT_FIELD = "cost_amount";
	private static final String COST_CURRENCY_FIELD = "cost_currency";
	private final static String FIELDS = Joiner.on(", ").join(ID_FIELD,NAME_FIELD,COST_AMOUNT_FIELD,COST_CURRENCY_FIELD);
	private final static String ALL_QUERY = "SELECT "+FIELDS+" FROM "+TABLE_NAME;
	private final static String FIRST_QUERY = "SELECT "+FIELDS+" FROM "+TABLE_NAME+" LIMIT 1";
	private final static String FIND_SINGLE_QUERY = "SELECT "+FIELDS+" FROM "+TABLE_NAME+"WHERE id = ?";
	private final static String FIND_MULTIPLE_QUERY = "SELECT "+FIELDS+" FROM "+TABLE_NAME+"WHERE id IN ?";
	private final static String INSERT_QUERY = "INSERT INTO "+TABLE_NAME+"("+FIELDS+") VALUES (NULL,?,?,?)";
	private final static String UPDATE_QUERY = "UPDATE "+TABLE_NAME+" SET "+NAME_FIELD+" = ?, "+COST_AMOUNT_FIELD+" = ?, "+COST_CURRENCY_FIELD+" = ? WHERE id = ?";
}
