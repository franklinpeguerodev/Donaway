package com.donaway.dev.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.h2.tools.Server;
import com.donaway.utils.db.ConexionBdDonaway;
import com.donaway.utils.db.ConexionBdDpa;

public class WebServerBD {
		static {
		try {
    	Server.createWebServer("-web","-webPort","8084","-tcpAllowOthers").start();
    	System.out.println("Consola web lista: http://localhost:8084");
        Connection logicConn=ConexionBdDonaway.getConnection();
        Connection dpaConn=ConexionBdDpa.getStaticConnection();
        if (logicConn != null && dpaConn !=null) {
        	System.out.println("Se conecto jevi");
        }
        }catch (SQLException e){
     
        	System.out.println("error de H2: " + e.getMessage());
        	
        }catch (Exception e) {
        	System.out.println("Error inesperado: " + e);
        	e.printStackTrace();
        }
		}
	public static void inicializar () {
		
	}
	}

