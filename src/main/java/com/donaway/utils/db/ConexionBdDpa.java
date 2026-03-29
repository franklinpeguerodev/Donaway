package com.donaway.utils.db;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
public class ConexionBdDpa {
	private static Connection conn=null;
	private static Dotenv dotenv = Dotenv.load();
	public static Connection getStaticConnection () {
		String driver= dotenv.get("DB_LOCAL_DPA_DRIVER");
		String url= dotenv.get("DB_LOCAL_DPA_URL");
		String user=dotenv.get("DB_LOCAL_DPA_USER");
		String password= dotenv.get("DB_LOCAL_DPA_PASSWORD");
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url,user,password);
			System.out.println("Se conecto");
		}catch (ClassNotFoundException e) {
			System.out.println("No se encontro el driver DB");
			e.printStackTrace();
		}catch(SQLException e) {
			System.out.println("Error sql Dpa");
			e.printStackTrace();
		}
		return conn;
	}
}
