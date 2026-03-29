package com.donaway.utils.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.h2.tools.Server;

import io.github.cdimascio.dotenv.Dotenv;
public class ConexionBdDonaway {
private static Connection conn=null;
private static Dotenv dotenv = Dotenv.load();
	public static Connection getConnection(){
		String driver = dotenv.get("DB_LOCAL_DONAWAY_DRIVER");
		String url= dotenv.get("DB_LOCAL_DONAWAY_URL");
		String user=dotenv.get("DB_LOCAL_DONAWAY_USER") ;
		String password=dotenv.get("DB_LOCAL_DONAWAY_PASSWORD");
		
		try {
		Class.forName(driver);
		conn= DriverManager.getConnection(url,user,password);
		System.out.println("Se creo la BD");
		}catch (ClassNotFoundException e){
			System.out.println("Error");
			e.printStackTrace();
		}catch (SQLException e){
			System.out.println("Error de sql donaway:" +e);
		}
		return conn;
	}
}
