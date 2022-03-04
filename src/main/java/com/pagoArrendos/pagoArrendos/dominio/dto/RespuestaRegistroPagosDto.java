package com.pagoArrendos.pagoArrendos.dominio.dto;

public class RespuestaRegistroPagosDto {
    private String respuesta;

    public RespuestaRegistroPagosDto(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
