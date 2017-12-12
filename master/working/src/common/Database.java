/**
 * Database class used to access the .sqlite database.
 * Implements the singleton design principle
 *
 * @author Nexus
 *
 */
package common;

import customers.Customer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.JDBC;

public class Database //Singleton Class
{

	private static Database INSTANCE = null;		//Singleton construct
	private Connection connection = null;
	private Statement stm = null;

	private Database() {

	}

	public static Database getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Database();
		}
		return INSTANCE;
	}

	public boolean connect() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:res/GMDB.db");	//use this when our program is packaged as a .jar
			//connection = DriverManager.getConnection("jdbc:sqlite:GMDB.db");
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean closeConnection() {
		try {
			stm.close();
			connection.close();
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

	public ResultSet query(String sql) throws SQLException {
		ResultSet resultset = null;
		stm = connection.createStatement();
		resultset = stm.executeQuery(sql);
		return resultset;
	}

	public void update(String sql) throws SQLException {
		stm = connection.createStatement();
		stm.executeUpdate(sql);
	}

}
