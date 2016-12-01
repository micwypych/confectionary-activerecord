package pl.confectionary.db;

import java.util.Collections;

import com.google.common.base.Joiner;

public class QueryTemplates {
	private final static String DEFAULT_VALUE = "NULL";
	private final static ActiveRecord RECORD = new ActiveRecord();
	
	public static String insertQuery(String tableName,String fields) {
		return "INSERT INTO "+tableName+" ("+fields+") VALUES ";
	}
	
	public static String selectAllQuery(String tableName,String fields) {
		return "SELECT "+fields+" FROM "+tableName;
	}
	
	public static String repeatTuples(String tuple,int times) {
		return Joiner.on(",").join(Collections.nCopies(times, tuple).iterator());
	}
	
	public static String createTuple(int size) {
		return "("+Joiner.on(",").join(Collections.nCopies(size, "?").iterator())+")";
	}
	
	public static String createTupleWithFristDefaultArg(int size) {
		if (size > 0) {
			return "("+DEFAULT_VALUE+","+Joiner.on(",").join(Collections.nCopies(size, "?").iterator())+")";
		} else {
			return "("+DEFAULT_VALUE+")";
		}
	}
	
}
