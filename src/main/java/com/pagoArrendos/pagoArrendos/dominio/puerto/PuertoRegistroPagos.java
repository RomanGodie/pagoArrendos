package com.pagoArrendos.pagoArrendos.dominio.puerto;

import com.pagoArrendos.pagoArrendos.dominio.dto.RespuestaRegistroPagosDto;
import com.pagoArrendos.pagoArrendos.dominio.modelo.RegistroPagos;

import java.util.List;

public interface PuertoRegistroPagos {
    void createRegistroPagosEnBaseDatos(RegistroPagos registroPagos);
    long createRegistroPagosEnBaseDatosRetornandoIdSimple(RegistroPagos registroPagos);
    long createRegistroPagosEnBaseDatosRetornandoIdComplejo(RegistroPagos registroPagos);
    RegistroPagos readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleConBean(RegistroPagos registroPagos);
    RegistroPagos readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleDirecto(RegistroPagos registroPagos);
    boolean readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleRetornandoCuantosHay(RegistroPagos registroPagos);
    List<RegistroPagos> readTodosLosRegistrosPagosEnBaseDatos(RegistroPagos registroPagos);
    int updateRegistroPagosEnBaseDatos(RegistroPagos registroPagos);
    boolean deleteRegistroPagosEnBaseDatos(RegistroPagos registroPagos);
}
