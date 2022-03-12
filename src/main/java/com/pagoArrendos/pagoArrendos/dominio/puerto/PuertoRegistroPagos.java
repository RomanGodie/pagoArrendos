package com.pagoArrendos.pagoArrendos.dominio.puerto;

import com.pagoArrendos.pagoArrendos.dominio.modelo.RegistroPagos;

import java.util.List;

public interface PuertoRegistroPagos {
    void createRegistroPagosEnBaseDatos(RegistroPagos registroPagos);
    long createRegistroPagosEnBaseDatosRetornandoIdSimple(RegistroPagos registroPagos);
    long createRegistroPagosEnBaseDatosRetornandoIdComplejo(RegistroPagos registroPagos);
    RegistroPagos readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleConBean(RegistroPagos registroPagos);
    RegistroPagos readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleDirecto(RegistroPagos registroPagos);
    RegistroPagos readreadUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatario(RegistroPagos registroPagos);
    RegistroPagos readreadUnSoloRegistroPagosEnBaseDatosConCodigoInmueble(RegistroPagos registroPagos);
    boolean readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleRetornandoCuantosHay(RegistroPagos registroPagos);
    boolean readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioRetornandoCuantosHay(RegistroPagos registroPagos);
    boolean readUnSoloRegistroPagosEnBaseDatosConCodigoInmuebleRetornandoCuantosHay(RegistroPagos registroPagos);
    List<RegistroPagos> readTodosLosRegistrosPagosEnBaseDatosConBean();
    List<RegistroPagos> readTodosLosRegistrosPagosEnBaseDatosDirecto();
    int updateRegistroPagosEnBaseDatos(RegistroPagos registroPagos);
    boolean deleteRegistroPagosEnBaseDatos(RegistroPagos registroPagos);
}
