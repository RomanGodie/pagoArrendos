package com.pagoArrendos.pagoArrendos.dominio.excepcion;

public class ExcepcionDeLogicaDeDominio extends RuntimeException{
    public ExcepcionDeLogicaDeDominio(String mensaje) {
        super(mensaje);
    }
}
