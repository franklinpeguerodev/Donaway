package com.donaway.model.inventario;

import java.time.LocalDate;
import com.donaway.utils.constants.EstadoUnidad;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.constants.TipoSangre;

public class UnidadHemoderivados {

    private String idHemoderivados;
    private String codigoBarras;
    private String idDonacion;
    private String rnc;
    private String codigoReferencia;
    private TipoComponente tipoComponenteObj;
    private TipoSangre tipoSangreObj; 
    private FactorRh factorRhObj;   
    private EstadoUnidad estadoUnidadObj;
    private LocalDate fechaCaducidad;
    
    // Constructor Completo
    public UnidadHemoderivados(String idHemoderivados, String codigoBarras, String idDonacion, String rnc, 
            String codigoReferencia, TipoComponente tipoComponenteObj, TipoSangre tipoSangreObj, 
            FactorRh factorRhObj, EstadoUnidad estadoUnidadObj, LocalDate fechaCaducidad) {
        this.idHemoderivados = idHemoderivados;
        this.codigoBarras = codigoBarras;
        this.idDonacion = idDonacion;
        this.rnc = rnc;
        this.codigoReferencia = codigoReferencia;
        this.tipoComponenteObj = tipoComponenteObj;
        this.tipoSangreObj = tipoSangreObj;
        this.factorRhObj = factorRhObj;
        this.estadoUnidadObj = estadoUnidadObj;
        this.fechaCaducidad = fechaCaducidad;
    }

    public UnidadHemoderivados() {
    }
    
    // Getters y Setters
    public String getIdHemoderivados() { return idHemoderivados; }
    public void setIdHemoderivados(String idHemoderivados) { this.idHemoderivados = idHemoderivados; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getIdDonacion() { return idDonacion; }
    public void setIdDonacion(String idDonacion) { this.idDonacion = idDonacion; }

    public String getRnc() { return rnc; }
    public void setRnc(String rnc) { this.rnc = rnc; }

    public String getCodigoReferencia() { return codigoReferencia; }
    public void setCodigoReferencia(String codigoReferencia) { this.codigoReferencia = codigoReferencia; }

    public TipoComponente getTipoComponenteObj() { return tipoComponenteObj; }
    public void setTipoComponenteObj(TipoComponente tipoComponenteObj) { this.tipoComponenteObj = tipoComponenteObj; }

    public TipoSangre getTipoSangreObj() { return tipoSangreObj; }
    public void setTipoSangreObj(TipoSangre tipoSangreObj) { this.tipoSangreObj = tipoSangreObj; }

    public FactorRh getFactorRhObj() { return factorRhObj; }
    public void setFactorRhObj(FactorRh factorRhObj) { this.factorRhObj = factorRhObj; }

    public EstadoUnidad getEstadoUnidadObj() { return estadoUnidadObj; }
    public void setEstadoUnidadObj(EstadoUnidad estadoUnidadObj) { this.estadoUnidadObj = estadoUnidadObj; }

    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}