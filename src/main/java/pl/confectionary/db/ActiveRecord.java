package pl.confectionary.db;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class ActiveRecord {
	
	private static final Nlp NLP = new Nlp();

	public static String defaultTableNameFor(Class<?> klass) {
		String dbName = javaToDbName(klass);
		List<String> dbTokenizedNames = new ArrayList<>(Splitter.on("_").splitToList(dbName));
		int lastIndex = dbTokenizedNames.size()-1;
		String lastName = dbTokenizedNames.get(lastIndex);
		String pluralizedLastname = NLP.plural(lastName);
		dbTokenizedNames.remove(lastIndex);
		dbTokenizedNames.add(pluralizedLastname);
		return Joiner.on("_").join(dbTokenizedNames);
	}
	
	public static String defaultAssociationTableNameFor(Class<?> table1,Class<?> table2) {
		String tableName1 = javaToDbName(table1);
		String tableName2 = defaultTableNameFor(table2);
		return Joiner.on("_").join(tableName1,tableName2);
	}
	
	private static String javaToDbName(Class<?> klass) {
		String simpleClassName = klass.getSimpleName();
		return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, simpleClassName);
	}
	
	public static String aliasForTable(String tableName) {
		Iterable<String> splitted = Splitter.on('_').split(tableName);
		StringBuilder sb = new StringBuilder();
		for (String string : splitted) {
			String alias = string.substring(0, 1);
			sb.append(alias);
		}
		return sb.toString();
	}

	public static String stmtSqlQueryAliasFor(String tableName) {
		return tableName + " AS " + aliasForTable(tableName);
	}

	public static String stmtSqlQueryAliasFor(String tableName, String fieldName) {
		return aliasForTable(tableName) + "." + fieldName;
	}

	public static String fieldList(String... fields) {
		return Joiner.on(", ").join(fields);
	}

	public static String qualifiedFieldList(String tableName, String... fields) {
		String[] qualifiedFields = toQualifiedFields(tableName, fields);
		return Joiner.on(", ").join(qualifiedFields);
	}

	public static String[] toQualifiedFields(String tableName, String... fields) {
		String[] qualifiedFields = new String[fields.length];
		int i = 0;
		for (String f : fields) {
			qualifiedFields[i++] = stmtSqlQueryAliasFor(tableName, f);
		}
		return qualifiedFields;
	}
}
