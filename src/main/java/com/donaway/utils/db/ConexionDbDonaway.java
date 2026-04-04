package com.donaway.utils.db;
import java.sql.Connection;

import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
public class ConexionDbDonaway {
	private static Dotenv dotenv = Dotenv.load();
	private static String driver =dotenv.get("DB_LOCAL_DONAWAY_DRIVER");
	private static String url =dotenv.get("DB_LOCAL_DONAWAY_URL");
	private static String user =dotenv.get("DB_LOCAL_DONAWAY_USER") ;
	private static String password =dotenv.get("DB_LOCAL_DONAWAY_PASSWORD");
	private static HikariDataSource pool ;
			static {
				try {
				HikariConfig config = new HikariConfig();
				
				config.setJdbcUrl(url);
				config.setUsername(user);
				config.setPassword(password);
				config.setDriverClassName(driver);
				
				config.setMaximumPoolSize(20);
				config.setMinimumIdle(2);
				config.setConnectionTimeout(20000);
				config.setIdleTimeout(300000);
				config.setMaxLifetime(1800000);
				
				pool = new HikariDataSource(config);
			}catch(Exception e) {
				System.err.println("El error es :");
				e.printStackTrace();
				
			}
				}
		private ConexionDbDonaway() {}	
		
		public static Connection getConnection() throws SQLException {
			
			return pool.getConnection();
		}
				
			
	
}
