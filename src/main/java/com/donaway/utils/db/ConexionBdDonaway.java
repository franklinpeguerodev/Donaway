package com.donaway.utils.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
public class ConexionBdDonaway {
	private static Dotenv dotenv = Dotenv.load();
	private static String driver =dotenv.get("DB_LOCAL_DONAWAY_DRIVER");
	private static String url =dotenv.get("DB_LOCAL_DONAWAY_URL");
	private static String user =dotenv.get("DB_LOCAL_DONAWAY_USER") ;
	private static String password =dotenv.get("DB_LOCAL_DONAWAY_PASSWORD");
			static {
			try {
				Class.forName(driver);
				System.out.println("Conexion con el driver exitoso");
			}catch (ClassNotFoundException e){
				System.out.println("Driver no econtrado");
				e.printStackTrace();
			}}
			public static Connection getConnection() throws SQLException{
			return DriverManager.getConnection(url,user,password);
	}
}
