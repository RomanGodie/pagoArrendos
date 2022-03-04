package com.pagoArrendos.pagoArrendos.aplicacion.mapper;

import com.pagoArrendos.pagoArrendos.dominio.modelo.RegistroPagos;
import com.pagoArrendos.pagoArrendos.infraestructura.dto.RegistroPagosDtoInfraestructura;
import org.springframework.stereotype.Component;

@Component
public class MapperRegistroPagoAplicacion {
    public RegistroPagos conversorRegistroPagoDtoInfraestructuraARegistroPagoDeDominio(RegistroPagosDtoInfraestructura registroPagosDtoInfraestructura){
        RegistroPagos registroPagos = new RegistroPagos(
        registroPagosDtoInfraestructura.getId(),
        registroPagosDtoInfraestructura.getDocumentoIdentificacionArrendatario(),
        registroPagosDtoInfraestructura.getCodigoInmueble(),
        registroPagosDtoInfraestructura.getValorPagado(),
        registroPagosDtoInfraestructura.getFechaPago());
        return registroPagos;
    }
}
