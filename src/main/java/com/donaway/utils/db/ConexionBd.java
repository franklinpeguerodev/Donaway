package com.donaway.utils.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
public class ConexionBd {
	private static  Connection  conn=null ;
	private static  Dotenv dotenv = Dotenv.load();

	public static Connection getConnection() {
		String driver = dotenv.get("DB_DRIVER");
		String url = dotenv.get("DB_URL");
		String user = dotenv.get("DB_USER");
		String password = dotenv.get("DB_PASSWORD");
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url,user,password);
			System.out.println("Se conecto a la BD");
		}catch (ClassNotFoundException e){
			System.out.println("no se conecto a la base de datos");
			e.printStackTrace();
		}catch (SQLException e){
			System.out.println("Error de Sql");
			e.printStackTrace();
		}
	return conn;
}
}