package com.donaway.dao.impl.ubicacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.donaway.dao.ubicacion.IUbicacionDao;
import com.donaway.model.ubicacion.Barrio;
import com.donaway.model.ubicacion.Distrito;
import com.donaway.model.ubicacion.Municipio;
import com.donaway.model.ubicacion.Provincia;
import com.donaway.model.ubicacion.Region;
import com.donaway.model.ubicacion.Seccion;
import com.donaway.model.ubicacion.SubBarrio;
import com.donaway.model.ubicacion.Ubicacion;
import com.donaway.utils.db.ConexionDbDonaway;

public class UbicacionImpl implements IUbicacionDao {

	////////////////////////////////////////////////////////////////////////
	
	// metodo para buscar las regiones
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Region> listaRegiones() {
		// TODO Auto-generated method stub
		
		
		List <Region> listaRegion = new ArrayList<>();
		String sql = "SELECT * FROM regiones ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
			
			while (rs.next()) {
				
				int id = rs.getInt("id");
				String nombre = rs.getString("nombre");
				
				Region regionEncontrada = new Region(id, nombre);
				
				listaRegion.add(regionEncontrada);
				
			}
			System.out.println("Se obtuvieron las regiones");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaRegion;
	}

	
	////////////////////////////////////////////////////////////////////////
	
	// metodo para buscar y filtrar las provincias
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Provincia> listaProvinciasPorRegion(int idRegion) {
		// TODO Auto-generated method stub
		
		List <Provincia> listaProvincias = new ArrayList<>();
		String sql = "SELECT id, nombre FROM provincias WHERE regionId = ? ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

				ps.setInt(1, idRegion);
		
				try (ResultSet rs = ps.executeQuery()) {
						
					while (rs.next()) {
					
						int id = rs.getInt("id");
						String nombre = rs.getString("nombre");
							
						Provincia provinciaEncontrada = new Provincia(id, nombre);
						
						listaProvincias.add(provinciaEncontrada);
					
					}
			}
			System.out.println("Se obtuvieron las provincias");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaProvincias;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	// metodo para buscar y filtrar los municipios
	
	////////////////////////////////////////////////////////////////////////
	

	@Override
	public List<Municipio> listaMunicipioPorProvincia(int idProvincia) {
		// TODO Auto-generated method stub
		
		List <Municipio> listaMunicipio = new ArrayList<>();
		String sql = "SELECT id, nombre FROM municipios WHERE provinciaId = ? ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idProvincia);
	
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					
					int id = rs.getInt("id");
					String nombre = rs.getString("nombre");
					
					Municipio municipioEncontrado = new Municipio(id, nombre);
					
					listaMunicipio.add(municipioEncontrado);
					
				}
				
			}
			
			System.out.println("Se obtuvieron los municipios");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaMunicipio;
	}

	////////////////////////////////////////////////////////////////////////
		
	// metodo para buscar y filtrar los distritos
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Distrito> listaDistritoPorMunicipio(int idMunicipio) {
		// TODO Auto-generated method stub
		
		List <Distrito> listaDistrito = new ArrayList<>();
		String sql = "SELECT id, nombre FROM distritos WHERE municipioId = ? ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idMunicipio);
	
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					
					int id = rs.getInt("id");
					String nombre = rs.getString("nombre");
					
					Distrito distritoEncontrado = new Distrito(id, nombre);
					
					listaDistrito.add(distritoEncontrado);
					
				}
				
			}
			
			System.out.println("Se obtuvieron los distritos");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaDistrito;
		
	}
	
	////////////////////////////////////////////////////////////////////////
		
	// metodo para buscar y filtrar las seccion por municipios
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Seccion> listaSeccionPorMunicipio(int idMunicipio) {
		// TODO Auto-generated method stub
		
		
		List <Seccion> listaSeccion = new ArrayList<>();
		String sql = "SELECT id, nombre FROM secciones WHERE municipioId = ? AND distritoId IS NULL ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idMunicipio);
	
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					
					int id = rs.getInt("id");
					String nombre = rs.getString("nombre");
					
					Seccion seccionEncontrada = new Seccion(id, nombre);
					
					listaSeccion.add(seccionEncontrada);
					
				}
				
			}
			
			System.out.println("Se obtuvieron las secciones");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaSeccion;
		
	}
	
	////////////////////////////////////////////////////////////////////////
		
	// metodo para buscar y filtrar las seccion por distritos
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Seccion> listaSeccionPorDistrito(int idDistrito) {
		// TODO Auto-generated method stub
		
		List <Seccion> listaSeccion = new ArrayList<>();
		String sql = "SELECT id, nombre FROM secciones WHERE distritoId = ? ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idDistrito);
	
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					
					int id = rs.getInt("id");
					String nombre = rs.getString("nombre");
					
					Seccion seccionEncontrada = new Seccion(id, nombre);
					
					listaSeccion.add(seccionEncontrada);
					
				}
				
			}
			
			System.out.println("Se obtuvieron las secciones");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaSeccion;

		
	}

	////////////////////////////////////////////////////////////////////////
		
	// metodo para buscar y filtrar los barrios 
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<Barrio> listaBarrioPorSeccion(int idSeccion) {
		// TODO Auto-generated method stub
		
		List <Barrio> listaBarrio = new ArrayList<>();
		String sql = "SELECT id, nombre FROM barrios WHERE seccionId = ? ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idSeccion);
	
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					
					int id = rs.getInt("id");
					String nombre = rs.getString("nombre");
					
					Barrio barrioEncontrado = new Barrio(id, nombre);
					
					listaBarrio.add(barrioEncontrado);
					
				}
				
			}
			
			System.out.println("Se obtuvieron los barrios");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaBarrio;
		
	}

	////////////////////////////////////////////////////////////////////////
	
	// metodo para buscar y filtrar los sub barrios 
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public List<SubBarrio> listaSubBarrioPorBarrio(int idBarrio) {
		// TODO Auto-generated method stub
		
		
		List <SubBarrio> listaSubBarrio = new ArrayList<>();
		String sql = "SELECT id, nombre FROM sub_barrios WHERE barrioId = ? ORDER BY nombre ASC";
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, idBarrio);
	
			try (ResultSet rs = ps.executeQuery()) {
				
				while (rs.next()) {
					
					int id = rs.getInt("id");
					String nombre = rs.getString("nombre");
					
					SubBarrio subBarrioEncontrado = new SubBarrio(id, nombre);
					
					listaSubBarrio.add(subBarrioEncontrado);
					
				}
				
			}
			
			System.out.println("Se obtuvieron los barrios");
			
		} catch (SQLException e) {
			
			System.out.println("Error en el sql: " + e);
			e.printStackTrace();
			
		}
		
		return listaSubBarrio;
		
	}

	////////////////////////////////////////////////////////////////////////
	
	// metodo para registrar una ubicacion 
	
	////////////////////////////////////////////////////////////////////////
	
	@Override
	public int registrarUbicacion(Ubicacion ubicacion) {
		// TODO Auto-generated method stub
		
		String sql = "INSERT INTO ubicacion (id_region,id_provincia,id_municipio,id_distrito,id_seccion,id_barrio,id_sub_barrio,direccion)"
				+ "VALUES (?,?,?,?,?,?,?,?)";
		
		int retornarPk = 0;
		
		try (Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			ps.setInt(1, ubicacion.getIdRegion());
			ps.setInt(2, ubicacion.getIdProvincia());
			ps.setInt(3, ubicacion.getIdMunicipio());
			
			if (ubicacion.getIdDistrito() > 0) { 
				ps.setInt(4, ubicacion.getIdDistrito());			
			} else {
				ps.setNull(4, java.sql.Types.INTEGER);
			}
			
			ps.setInt(5, ubicacion.getIdSeccion());
			ps.setInt(6, ubicacion.getIdBarrio());
			
			if (ubicacion.getIdSubBarrio() > 0) {
				ps.setInt(7, ubicacion.getIdSubBarrio());
			} else {
				ps.setNull(7,  java.sql.Types.INTEGER);
			}
			
			ps.setString(8, ubicacion.getDireccion());
			
			
			int optenerPk = ps.executeUpdate();
			
			if (optenerPk > 0) {
				
				try (ResultSet rs = ps.getGeneratedKeys()) {
					
					if (rs.next()) {
					
						retornarPk = rs.getInt(1);
					}
				}
			}
			
			System.out.println("se registro una nueva ubicacion");
			
			
		} catch (SQLException e){
			
			System.out.print("Error de sql" + e);
			e.printStackTrace();
		} 
		
		return retornarPk;
	}	
	
}