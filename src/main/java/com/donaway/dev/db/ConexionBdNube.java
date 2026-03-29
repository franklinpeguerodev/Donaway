package com.donaway.dev.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
public class ConexionBdNube {
	private static Connection conn=null;
	private static Dotenv dotenv=Dotenv.load();
	public static Connection getConnection() {
		String driver = dotenv.get("DB_NUBE_DONAWAY_DRIVER");
		String url= dotenv.get("DB_NUBE_DONAWAY_URL");
		String user= dotenv.get("DB_NUBE_DONAWAY_USER");
		String password= dotenv.get("DB_NUBE_DONAWAY_PASSWORD");
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url,user,password);
			System.out.println("se conecto la de la nube");
	        
		}catch (ClassNotFoundException e){
			System.out.println("No se encontro el driver");
			e.printStackTrace();
		}catch (SQLException e) {
			System.out.println("Error sql" + e);
			e.printStackTrace();
		}
		
		return conn;
	}
}
