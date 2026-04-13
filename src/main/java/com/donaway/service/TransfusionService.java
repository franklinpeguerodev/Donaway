package com.donaway.service;

import com.donaway.dao.impl.transfusion.TransfusionDaoImpl;
import com.donaway.dao.transfusion.ITransfusionDao;
import com.donaway.exception.ValidacionException;
import com.donaway.model.transfusion.Transfusion;
import com.donaway.utils.mensaje.Mensaje;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransfusionService {

    private ITransfusionDao transfusionDao = new TransfusionDaoImpl();

    ////////////////////////////////////////////////////////////////////////////
   
    // Ejecuta el procedimiento de transfusion
    public void ejecutarTransfusion(Transfusion transfusion) {
        
        
        if (transfusion.getIdSolicitud() == null || transfusion.getCodigoBarras() == null ||
            transfusion.getCantidadAdministrada() <= 0) {
            
            throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
        }

        try {
       
            if (transfusion.getIdTransfusion() == null || transfusion.getIdTransfusion().isEmpty()) {
                transfusion.setIdTransfusion(UUID.randomUUID().toString());
            }
            
            if (transfusion.getFechaTransfusion() == null) {
                transfusion.setFechaTransfusion(LocalDateTime.now());
            }

            //Verificacion de duplicidad (No transfundir la misma unidad dos veces)
            if (transfusionDao.obtenerPorUnidad(transfusion.getCodigoBarras()) != null) {
                throw new ValidacionException("Esta unidad ya ha sido registrada en un procedimiento previo.");
            }

            transfusionDao.registrar(transfusion);

        } catch (SQLException e) {
            throw new ValidacionException(Mensaje.ERR_CONEXION + ": No se pudo completar el ciclo de transfusion.");
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////////


    // Consultar historial clinico de transfusiones de un paciente
    
    public List<Transfusion> historialPaciente(String cedulaReceptor) {
        if (cedulaReceptor == null || cedulaReceptor.isEmpty()) {
            throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
        }
        
        try {
            return transfusionDao.obtenerHistorialReceptor(cedulaReceptor);
        } catch (SQLException e) {
            throw new ValidacionException(Mensaje.ERR_CONEXION);
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////////


    // Reporte para la institucion (Auditoria de pintas usadas)
    public List<Transfusion> reporteTransfusiones(String rnc, LocalDate inicio, LocalDate fin) {
        if (rnc == null || inicio == null || fin == null) {
            throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
        }

        if (inicio.isAfter(fin)) {
            throw new ValidacionException("La fecha de inicio no puede ser posterior a la fecha final.");
        }

        try {
            return transfusionDao.obtenerPorInstitucion(rnc, inicio, fin);
        } catch (SQLException e) {
            throw new ValidacionException(Mensaje.ERR_CONEXION);
        }
    }
    ////////////////////////////////////////////////////////////////////////////
}