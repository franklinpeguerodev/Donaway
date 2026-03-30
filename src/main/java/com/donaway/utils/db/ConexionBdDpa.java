package com.donaway.utils.db;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;//importacion del dotenv para leer las variables de entorno .env
public class ConexionBdDpa {
	private static Dotenv dotenv = Dotenv.load();
	private static String driver= dotenv.get("DB_LOCAL_DPA_DRIVER");//DB_LOCAL_DPA_DRIVER es el nombre de la variable de entorno
	private static String url= dotenv.get("DB_LOCAL_DPA_URL");
	private static String user= dotenv.get("DB_LOCAL_DPA_USER");
	private static String password=dotenv.get("DB_LOCAL_DPA_PASSWORD");

		static {
		try{
			Class.forName(driver);
			System.out.println("Se conecto");
		}catch (ClassNotFoundException e) {
			System.out.println("No se encontro el driver DB");
			e.printStackTrace();
		}}
		public static Connection getConnection() throws SQLException{
			return DriverManager.getConnection(url,user,password);
	}
}
