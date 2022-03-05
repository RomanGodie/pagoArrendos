package com.pagoArrendos.pagoArrendos.dominio.dto;

public class RespuestaConsultaPagosDto {
    private int id;
    private int documentoIdentificacionArrendatario;
    private String codigoInmueble;
    private String valorPagado;
    private String fechaPago;

    public RespuestaConsultaPagosDto(int id, int documentoIdentificacionArrendatario, String codigoInmueble, String valorPagado, String fechaPago) {
        this.id = id;
        this.documentoIdentificacionArrendatario = documentoIdentificacionArrendatario;
        this.codigoInmueble = codigoInmueble;
        this.valorPagado = valorPagado;
        this.fechaPago = fechaPago;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentoIdentificacionArrendatario() {
        return documentoIdentificacionArrendatario;
    }

    public void setDocumentoIdentificacionArrendatario(int documentoIdentificacionArrendatario) {
        this.documentoIdentificacionArrendatario = documentoIdentificacionArrendatario;
    }

    public String getCodigoInmueble() {
        return codigoInmueble;
    }

    public void setCodigoInmueble(String codigoInmueble) {
        this.codigoInmueble = codigoInmueble;
    }

    public String getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(String valorPagado) {
        this.valorPagado = valorPagado;
    }

    public String getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(String fechaPago) {
        this.fechaPago = fechaPago;
    }
}
