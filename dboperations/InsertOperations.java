package com.dboperations;

import java.sql.SQLException;

public class InsertOperations {

	static java.sql.Statement stmt = null;
	static java.sql.Connection con = DbConnection.connect();
	/*
	 * Function to insert the data into mysql database
	 */

	public static void insert(String table, String user, String title, String publishDate, String description) {

		try {

			stmt = con.createStatement();
			String a = "anc\"d";
			String query = "INSERT INTO " + table + " values (\"" + user + "\",\"" + title + "\",\"" + publishDate
					+ "\",\"" + description + "\");";
			System.out.println("Query is " + query);
			stmt.executeUpdate(query);
			System.out.println("Insertion is success");
		} catch (SQLException e) {
			System.err.println("Duplicate user id");
		}

	}
	
	/*
	 * Insert polarity of a post
	 */
	
	public static void insertPolarity(String polarity,String user,String date,String table){
		
		try {
			java.sql.Statement stmt = null;
			java.sql.Connection con = DbConnection.connect();
			stmt = con.createStatement();
			String a = "anc\"d";
			String query = "UPDATE " + table + " set polarity =\""+polarity+"\" WHERE userid = \""+user+ "\" AND publishdate=\""+date+"\"  ;";
			System.out.println("Query is " + query);
			stmt.executeUpdate(query);
			System.out.println("Insertion is success");
			stmt.execute("commit;");
		} catch (SQLException e) {
			System.err.println("Duplicate user id");
		}finally{
			DbConnection.disconnect();
		}
		
	}
	
	/*
	 * Inserting rank
	 */
	
	public static void insertRank(double rank,String user,String date,String table){
		
		try {
			java.sql.Statement stmt = null;
			java.sql.Connection con = DbConnection.connect();
			stmt = con.createStatement();
			String a = "anc\"d";
			String query = "UPDATE " + table + " set rank =\""+rank+"\" WHERE userid = \""+user+ "\" AND publishdate=\""+date+"\"  ;";
			System.out.println("Query is " + query);
			stmt.executeUpdate(query);
			System.out.println("Insertion is success");
			stmt.execute("commit;");
		} catch (SQLException e) {
			System.err.println("Duplicate user id");
		}finally{
			DbConnection.disconnect();
		}
		
	}
	
	public static void insertCluster(String cluster,String user,String date,String table){
		
		try {
			java.sql.Statement stmt = null;
			java.sql.Connection con = DbConnection.connect();
			stmt = con.createStatement();
			String a = "anc\"d";
			String query = "UPDATE " + table + " set cluster_3 =\""+cluster+"\" WHERE userid = \""+user+ "\" AND publishdate=\""+date+"\"  ;";
			System.out.println("Query is " + query);
			stmt.executeUpdate(query);
			System.out.println("Insertion is success");
			stmt.execute("commit;");
		} catch (SQLException e) {
			System.err.println("Duplicate user id");
		}finally{
			DbConnection.disconnect();
		}
		
	}
	
}
