package com.dboperations;

import java.sql.*;

import com.crawler.Constants;

public class DbConnection {
	private static Connection connection;
	
	/*
	 * Constructor
	 */
	DbConnection(){
		//Constructor
	}
	
	/*
	 * COnnectiong to database
	 */
	public static Connection connect(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(Constants.url, Constants.username, Constants.password);
			if(!connection.isClosed() || connection !=null){
				System.out.println("Database connected!");
				return connection;
			}
		} catch (SQLException|ClassNotFoundException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		return connection;
	}
	
	/*
	 * Disconnecting from database
	 */
    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static  boolean status() {
		try{
			if(!connection.isClosed() || connection !=null){
				return true;
			}
		}catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
    }
}
