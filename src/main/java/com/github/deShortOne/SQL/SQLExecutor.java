package com.github.deShortOne.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.github.deShortOne.Engine.MoneyManager;

public class SQLExecutor {

	private static Connection connect = null;
	private static Statement statement = null;

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/money?"
							+ "user=root&password=pass"); 
			// need more secure login
			statement = connect.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * WILL NEED TO CLOSE RESULT SET
	 * 
	 * @param sqlQuery
	 * @return
	 * @throws SQLException
	 */
	// @Deprecated
	public static ResultSet getTable(String sqlQuery) throws SQLException {
		return statement.executeQuery(sqlQuery);
	}
	
	/**
	 * INSERT UPDATE OR DELETE ONLY
	 * @param sqlQuery
	 * @throws SQLException
	 */
	public static void changeTable(String sqlQuery) throws SQLException {
		statement.executeLargeUpdate(sqlQuery);
		MoneyManager.refreshTables();
	}

	// You need to close the resultSet
	public void close() {
		try {
			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

}