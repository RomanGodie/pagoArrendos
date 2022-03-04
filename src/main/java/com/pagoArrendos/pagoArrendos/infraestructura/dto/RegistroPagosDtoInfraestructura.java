package com.pagoArrendos.pagoArrendos.infraestructura.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistroPagosDtoInfraestructura {
    private int id;
    private String documentoIdentificacionArrendatario;
    private String codigoInmueble;
    private String valorPagado;
    private String fechaPago;
}
