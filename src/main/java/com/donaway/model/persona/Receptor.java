package com.donaway.model.persona;
import com.donaway.model.user.Persona;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Receptor extends Persona {
	private BigDecimal peso;
	private String tipoSangre;
	private String factorRh;

	public Receptor(String documentId,String nombre,String sexo,LocalDate fechaNacimientrto,String telefono,LocalDateTime fechaRegistro,
			BigDecimal peso,String tipoSangre,String factorRh ) {
		super();
		this.peso=peso;
		this.tipoSangre=tipoSangre;
		this.factorRh=factorRh;
	}
	
	public BigDecimal getPeso () {
		return this.peso;
	}
	public void setPeso(BigDecimal peso) {
		this.peso=peso;
	}
		
	
	public String getTipoSangre() {
		return this.tipoSangre;
	}
	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre=tipoSangre;
	}
	
	
	public String getFactorRh() {
		return this.factorRh;
	}
	public void setFactorRh (String factorRh) {
		this.factorRh=factorRh;
	}
}

