package com.donaway.model.persona;
import com.donaway.model.user.Persona;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.Sexo;
import com.donaway.utils.constants.TipoSangre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class Receptor extends Persona {
	private BigDecimal peso;
	private TipoSangre tipoSangre;
	private FactorRh factorRh;

	public Receptor(String documentId,String nombre,Sexo sexo,LocalDate fechaNacimiento,String telefono,String email,LocalDateTime fechaRegistro,
			BigDecimal peso,TipoSangre tipoSangre,FactorRh factorRh ) {
		super(documentId,nombre,sexo,fechaNacimiento,telefono,email,fechaRegistro);
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
		
	
	public TipoSangre getTipoSangre() {
		return this.tipoSangre;
	}
	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre=tipoSangre;
	}
	
	
	public FactorRh getFactorRh() {
		return this.factorRh;
	}
	public void setFactorRh (FactorRh factorRh) {
		this.factorRh=factorRh;
	}
}

