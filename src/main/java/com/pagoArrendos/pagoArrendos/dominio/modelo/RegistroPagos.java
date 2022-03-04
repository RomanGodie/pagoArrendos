package com.pagoArrendos.pagoArrendos.dominio.modelo;

import com.pagoArrendos.pagoArrendos.dominio.excepcion.ExcepcionDeLogicaDeDominio;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class RegistroPagos {
    private int id;
    private int documentoIdentificacionArrendatario;
    private String codigoInmueble;
    private String valorPagado;
    private String fechaPago;
    private final String VALOR_PAGO_FUERA_RANGO = "Valor pagado fuera del rango";
    private final String EXCEPCION_FORMATO_DOCUMENTO_IDENTIFICACION_ARRENDATARIO = "Formato de documento de identificacion arrendatario incorrecto";
    private final String EXCEPCION_FORMATO_VALOR_PAGO = "Formato de valor pagado incorrecto";
    private final String EXCEPCION_FORMATO_FECHA = "Formato de fecha incorrecto";
    private final int RANGO_INICIAL_PAGO = 1;
    private final int RANGO_FINAL_PAGO = 1000000;


    public RegistroPagos(int id, String documentoIdentificacionArrendatario, String codigoInmueble, String valorPagado, String fechaPago) {
        this.id = id;
        validarTipoDeCaracteresEnDocumentoIdentificacionArrendatario(documentoIdentificacionArrendatario);
        this.documentoIdentificacionArrendatario = Integer.parseInt(documentoIdentificacionArrendatario);
        this.codigoInmueble = codigoInmueble;
        validarValorPagado(valorPagado);
        this.valorPagado = valorPagado;
        validarFormatoDeFecha(fechaPago);
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

    public void setDocumentoIdentificacionArrendatario(String documentoIdentificacionArrendatario) {
        this.documentoIdentificacionArrendatario = Integer.parseInt(documentoIdentificacionArrendatario);
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

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("documentoIdentificacionArrendatario", documentoIdentificacionArrendatario);
        values.put("codigoInmueble", codigoInmueble);
        values.put("valorPagado", valorPagado);
        values.put("fechaPago", fechaPago);
        return values;
    }

    private void validarTipoDeCaracteresEnDocumentoIdentificacionArrendatario(String documentoIdentificacionArrendatario){
        try{
            int documentoIdentificacionArrendatarioConvertidoNumerico = Integer.parseInt(documentoIdentificacionArrendatario);
        }catch (Exception error){
            throw new ExcepcionDeLogicaDeDominio(EXCEPCION_FORMATO_DOCUMENTO_IDENTIFICACION_ARRENDATARIO);
        }
    }

    private void validarValorPagado(String valorPagado){
        int valorPagadoConvertidoNumerico;
        try{
            valorPagadoConvertidoNumerico = Integer.parseInt(valorPagado);
        }catch (Exception error){
            throw new ExcepcionDeLogicaDeDominio(EXCEPCION_FORMATO_VALOR_PAGO);
        }
        if(!(valorPagadoConvertidoNumerico>=RANGO_INICIAL_PAGO && valorPagadoConvertidoNumerico <=RANGO_FINAL_PAGO)){
            throw new ExcepcionDeLogicaDeDominio(VALOR_PAGO_FUERA_RANGO);
        }
    }

    private void validarFormatoDeFecha(String fechaPago){
        try{
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fechaPago);
        }catch (Exception error){
            throw new ExcepcionDeLogicaDeDominio(EXCEPCION_FORMATO_FECHA);
        }
    }
}
