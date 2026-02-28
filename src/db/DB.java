package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
	public static Connection conn = null;
	
	public static Connection getConnection() {
		Properties prop = loadProperties();
		String url = prop.getProperty("dburl");
		
		try {
			conn = DriverManager.getConnection(url,prop);
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao carregar db.properties", e);
		}
		return conn;
		
	}
	
	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private static Properties loadProperties() {
	try(FileInputStream fis = new FileInputStream("db.properties")){
		Properties prop = new Properties();
		prop.load(fis);
		
		return prop;
	} 
	catch (IOException e) {
		throw new RuntimeException("Erro ao carregar db.properties", e);
	}}}
