package com.pagoArrendos.pagoArrendos.aplicacion.servicio;

import com.pagoArrendos.pagoArrendos.aplicacion.mapper.MapperRegistroPagoAplicacion;
import com.pagoArrendos.pagoArrendos.dominio.dto.RespuestaConsultaPagosDto;
import com.pagoArrendos.pagoArrendos.dominio.dto.RespuestaRegistroPagosDto;
import com.pagoArrendos.pagoArrendos.dominio.servicio.LogicaDeRegistroPagoDominio;
import com.pagoArrendos.pagoArrendos.infraestructura.dto.RegistroPagosDtoInfraestructura;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class IntermediarioRegistroPagosServicioAplicacion {
    private MapperRegistroPagoAplicacion mapperRegistroPagoAplicacion;
    private LogicaDeRegistroPagoDominio logicaDeRegistroPagoDominio;

    public RespuestaRegistroPagosDto transaccionDeControladorADominio(RegistroPagosDtoInfraestructura registroPagosDtoInfraestructura){
        return logicaDeRegistroPagoDominio.
                logicaDeNegocioRegistroPago(mapperRegistroPagoAplicacion.
                        conversorRegistroPagoDtoInfraestructuraARegistroPagoDeDominio(registroPagosDtoInfraestructura));
    }

    public RespuestaConsultaPagosDto[] transaccionDeBaseDeDatosARespuestaRegistroPagosDtoDominio(){
        return logicaDeRegistroPagoDominio.logicaDeNegocioConsultaPagos();
    }
}
