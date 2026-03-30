package com.donaway.dev.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.h2.tools.Server;//Importacion del servidor web para BD
import com.donaway.utils.db.ConexionBdDonaway;//Importacion de la conexion de la BD Local Donaway
import com.donaway.utils.db.ConexionBdDpa;//Importacion de la conexion de la BD Local Division politica administrativa

public class WebServerBD {
		//se usa Static para llamar la clase de manera mas comoda
		static {
		try {
    	Server.createWebServer("-web","-webPort","8084","-tcpAllowOthers").start();//8084 es el puerto del servidor
    	System.out.println("Consola web lista: http://localhost:8084");
        Connection logicConn=ConexionBdDonaway.getConnection();
        Connection dpaConn=ConexionBdDpa.getConnection();
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

