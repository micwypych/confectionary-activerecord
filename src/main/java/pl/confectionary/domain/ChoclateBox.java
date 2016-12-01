package pl.confectionary.domain;

import static pl.confectionary.db.ActiveRecord.defaultAssociationTableNameFor;
import static pl.confectionary.db.ActiveRecord.defaultTableNameFor;
import static pl.confectionary.db.ActiveRecord.fieldList;
import static pl.confectionary.db.ActiveRecord.qualifiedFieldList;
import static pl.confectionary.db.ActiveRecord.stmtSqlQueryAliasFor;
import static pl.confectionary.db.QueryTemplates.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;

import pl.confectionary.db.ApplicationException;
import pl.confectionary.db.DB;
import pl.confectionary.db.IdNotFound;
import pl.confectionary.db.RecordNotFound;
import pl.confectionary.db.Registry;
import pl.confectionary.db.ResultSetExtended;

public class ChoclateBox {

	public static ChoclateBox find(long id) {
		Optional<ChoclateBox> oc = Registry.getChoclateBox(id);
		if ( oc.isPresent() ) {
			return oc.get();
		}
		ChoclateBox choclate;
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
	
	public void create() {
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSetExtended rs = null;
		try {
			stmt = DB.prepareStatement(INSERT_QUERY+"(NULL,?)");
			stmt.setString(1, name);
			stmt.executeUpdate();
			rs = new ResultSetExtended(stmt.getGeneratedKeys());
			if( !rs.next() ) {
				throw new IdNotFound();
			}
			long id = rs.getLong(1);
			this.id = id;
			
			Choclate.create(choclates);
			
			stmt2 = DB.prepareStatement(ASSOC_INSERT_QUERY+valuesTuple(choclates.size()));
			int index = 1;
			for(Choclate c: choclates) {
				index = setValuesOfStmtAndReturnNextIndex(stmt,id,c,index);
			}
			stmt2.executeUpdate();
			
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,rs);
			DB.cleanUp(stmt2);
		}
	}
	
	private static String valuesTuple(int size) {
		final String tuple = "(?,?)";
		return repeatTuples(tuple,size);
	}

	private int setValuesOfStmtAndReturnNextIndex(PreparedStatement stmt, long id, Choclate c, int index) throws SQLException {
		stmt.setLong(index++, id);
		stmt.setLong(index++, c.getId());
		return index;
	}

	private static ChoclateBox load(ResultSetExtended rs) {
		PreparedStatement stmt = null;
		ResultSetExtended innerRs = null;
		try {
			long id = rs.getLong(ID_FIELD);
			Optional<ChoclateBox> oc = Registry.getChoclateBox(id);
			if ( oc.isPresent() ) {
				return oc.get();
			}
			String name = rs.getString(NAME_FIELD);
			stmt = DB.prepareStatement(FIND_ALL_CHOCLATES_IN_A_SINGLE_BOX);
			stmt.setLong(1, id);
			
			ResultSet tmp = stmt.executeQuery();
			innerRs = new ResultSetExtended(tmp);
			List<Choclate> choclates = new ArrayList<>();
			while( innerRs.next() ) {
				Choclate choclate = Choclate.load(rs);
				choclates.add(choclate);
			}
			ChoclateBox result = new ChoclateBox(id,name,choclates);
			Registry.addChoclateBox(result);
			return result;
		} catch (SQLException e) {
			throw new ApplicationException(e);
		} finally {
			DB.cleanUp(stmt,innerRs);
		}
		
	}
	
	public ChoclateBox(long id, String name, List<Choclate> choclates) {
		this.id = id;
		this.name = name;
		this.choclates = choclates;
	}
	
	public ChoclateBox(String name, List<Choclate> choclates) {
		this(Long.MIN_VALUE,name,choclates);
	}

	public Long getId() {
		return id;
	}
	
	
	private long id;
	private String name;
	private List<Choclate> choclates;
	
	public final static String TABLE_NAME = defaultTableNameFor(ChoclateBox.class);
	public final static String Q_TABLE_NAME = stmtSqlQueryAliasFor(TABLE_NAME);
	public final static String ID_FIELD = "id";
	public final static String NAME_FIELD = "name";
	
	private final static String FIELDS = fieldList(ID_FIELD,NAME_FIELD);
	private final static String Q_FIELDS = qualifiedFieldList(TABLE_NAME,ID_FIELD,NAME_FIELD);
	
	
	public final static String ASSOC_TABLE_NAME = defaultAssociationTableNameFor(ChoclateBox.class, Choclate.class);
	public final static String Q_ASSOC_TABLE_NAME = stmtSqlQueryAliasFor(ASSOC_TABLE_NAME);
	public final static String ASSOC_TABLE_CHOCLATE_BOX_ID = "choclate_box_id";
	public final static String ASSOC_TABLE_CHOCLATE_ID = "choclate_id";
	
	private final static String ASSOC_FIELDS = fieldList(ASSOC_TABLE_CHOCLATE_BOX_ID,ASSOC_TABLE_CHOCLATE_ID);
	private final static String Q_ASSOC_FIELDS = qualifiedFieldList(ASSOC_TABLE_NAME,ASSOC_TABLE_CHOCLATE_BOX_ID,ASSOC_TABLE_CHOCLATE_ID);
	
	private final static String INSERT_QUERY = insertQuery(TABLE_NAME,FIELDS);
	private final static String FIND_SINGLE_QUERY = "SELECT "+FIELDS+" FROM "+TABLE_NAME+" WHERE "+ID_FIELD+" = ?";
	private final static String FIND_ALL_CHOCLATES_IN_A_SINGLE_BOX = "SELECT "+Choclate.FIELDS+" FROM "+ASSOC_TABLE_NAME+" JOIN "+Choclate.TABLE_NAME+" ON ("+ASSOC_TABLE_CHOCLATE_BOX_ID+"="+Choclate.ID_FIELD+") WHERE "+ASSOC_TABLE_CHOCLATE_BOX_ID+" = ?";
	
	private final static String ASSOC_INSERT_QUERY = insertQuery(ASSOC_TABLE_NAME,ASSOC_FIELDS);
	
	
}
