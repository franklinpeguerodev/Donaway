package com.donaway.dev.db;

import java.sql.SQLException;
import org.h2.tools.Server;//Importacion del servidor web para BD


public class WebServerBD {
		//se usa Static para llamar la clase de manera mas comoda
		static {
		try {
    	Server.createWebServer("-web","-webPort","8084","-tcpAllowOthers").start();//8084 es el puerto del servidor
    	System.out.println("Consola web lista: http://localhost:8084");
       
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

