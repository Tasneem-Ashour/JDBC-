package JDBC;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.SQLException;

public class CreateConnection {

	public static Connection con;
	public static String url = "jdbc:mysql://127.0.0.1:3306/itemstask";
	public static String userName = "root";
	public static String password = "";

	public static Connection getConnection() {

		try {
			con = DriverManager.getConnection(url, userName, password);
			//System.out.println("Connected With the database successfully");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;

	}

}
