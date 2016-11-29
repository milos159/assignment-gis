package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConnectPostGisDatabase {
	private Connection conn = null;
	private Statement stmt = null;
	
	public ConnectPostGisDatabase() throws SQLException{
		createDatabaseConnection();
	}
	
	private void createDatabaseConnection() throws SQLException{
		Properties connectionProps = new Properties();
		connectionProps.put("user", "postgres");
		connectionProps.put("password", "gmh012345");
		String connectionString = "jdbc:postgresql://localhost:5432/osm";
		conn = DriverManager.getConnection(connectionString, connectionProps);
		stmt = conn.createStatement();
	}
	
	public Statement getStatement(){
		return stmt;
	}
}
