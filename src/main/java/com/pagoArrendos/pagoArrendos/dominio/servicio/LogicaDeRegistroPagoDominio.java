package com.pagoArrendos.pagoArrendos.dominio.servicio;

import com.pagoArrendos.pagoArrendos.dominio.dto.RespuestaConsultaPagosDto;
import com.pagoArrendos.pagoArrendos.dominio.dto.RespuestaRegistroPagosDto;
import com.pagoArrendos.pagoArrendos.dominio.excepcion.ExcepcionDeLogicaDeDominio;
import com.pagoArrendos.pagoArrendos.dominio.modelo.RegistroPagos;
import com.pagoArrendos.pagoArrendos.dominio.puerto.PuertoRegistroPagos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogicaDeRegistroPagoDominio {
    PuertoRegistroPagos puertoRegistroPagos;
    private final int VALOR_CANON_ARRENDAMIENTO = 1000000;

    public LogicaDeRegistroPagoDominio(PuertoRegistroPagos puertoRegistroPagos) {
        this.puertoRegistroPagos = puertoRegistroPagos;
    }

    public RespuestaRegistroPagosDto logicaDeNegocioRegistroPago(RegistroPagos registroPagos){
        return validarMonto(validarSiExisteArrendatarioEInmueble(registroPagos), registroPagos);
    }

    public RespuestaConsultaPagosDto[] logicaDeNegocioConsultaPagos(){
        RespuestaConsultaPagosDto[] respuestaConsultaPagosDtosMultiple =
                new RespuestaConsultaPagosDto[puertoRegistroPagos.readTodosLosRegistrosPagosEnBaseDatosDirecto().size()];
        RegistroPagos registroPagos;
        for(int i = 0; i < puertoRegistroPagos.readTodosLosRegistrosPagosEnBaseDatosDirecto().size(); i++){
            registroPagos = puertoRegistroPagos.readTodosLosRegistrosPagosEnBaseDatosDirecto().get(i);
            respuestaConsultaPagosDtosMultiple[i] = new RespuestaConsultaPagosDto(registroPagos.getId(),
                    registroPagos.getDocumentoIdentificacionArrendatario(),
                    registroPagos.getCodigoInmueble(),
                    registroPagos.getValorPagado(),
                    registroPagos.getFechaPago());
        }
        return respuestaConsultaPagosDtosMultiple;
    }

    public boolean validarSiExisteArrendatarioEInmueble(RegistroPagos registroPagos) {
        if(puertoRegistroPagos.
                readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleRetornandoCuantosHay(registroPagos)){
            return true;
        }else{
            return false;
        }
    }

    public RespuestaRegistroPagosDto validarMonto(boolean arrendatarioExistente, RegistroPagos registroPagos) {
        int nuevoValorParaRegistrarEnBaseDatos = 0;
        validarFechaImpar(registroPagos);
        if (arrendatarioExistente) {
            int valorRegistradoEnBaseDatos = Integer.parseInt(puertoRegistroPagos.
                    readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleDirecto(registroPagos).
                    getValorPagado());
            nuevoValorParaRegistrarEnBaseDatos = Integer.parseInt(registroPagos.getValorPagado()) + valorRegistradoEnBaseDatos;
            if (nuevoValorParaRegistrarEnBaseDatos > VALOR_CANON_ARRENDAMIENTO) {
                registroPagos.setValorPagado(String.valueOf(VALOR_CANON_ARRENDAMIENTO));
                puertoRegistroPagos.updateRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto("Ya has cancelado la totalidad de tu arrendo " +
                        "tienes un saldo a favor de $" + (nuevoValorParaRegistrarEnBaseDatos - VALOR_CANON_ARRENDAMIENTO));
            } else if (nuevoValorParaRegistrarEnBaseDatos < VALOR_CANON_ARRENDAMIENTO) {
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                int registroActualizado = puertoRegistroPagos.updateRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto("gracias por tu abono, pero recuerda que aun te falta pagar: " +
                        "$" + (VALOR_CANON_ARRENDAMIENTO - nuevoValorParaRegistrarEnBaseDatos));
            }else{
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                puertoRegistroPagos.updateRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto("gracias por pagar todo tu arriendo");
            }
        }else{
            nuevoValorParaRegistrarEnBaseDatos = Integer.parseInt(registroPagos.getValorPagado());
            if (nuevoValorParaRegistrarEnBaseDatos > VALOR_CANON_ARRENDAMIENTO) {
                registroPagos.setValorPagado(String.valueOf(VALOR_CANON_ARRENDAMIENTO));
                puertoRegistroPagos.createRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto("Ya has cancelado la totalidad de tu arrendo " +
                        "tienes un saldo a favor de $" + (nuevoValorParaRegistrarEnBaseDatos - VALOR_CANON_ARRENDAMIENTO));
            } else if (nuevoValorParaRegistrarEnBaseDatos < VALOR_CANON_ARRENDAMIENTO) {
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                puertoRegistroPagos.createRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto("gracias por tu abono, pero recuerda que aun te falta pagar: " +
                        "$" + (VALOR_CANON_ARRENDAMIENTO - nuevoValorParaRegistrarEnBaseDatos));
            }else{
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                puertoRegistroPagos.createRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto("gracias por pagar todo tu arriendo");
            }
        }
    }

    public boolean validarFechaImpar(RegistroPagos registroPagos){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaIngresada = LocalDate.parse(registroPagos.getFechaPago(),formatter);
        int diaDeFechaIngresada =  fechaIngresada.getDayOfMonth();
        if(diaDeFechaIngresada % 2 == 0){
            throw new ExcepcionDeLogicaDeDominio("lo siento pero no se "+
                    "puede recibir el pago por decreto de administración");
        }else{
            return true;
        }
    }
}
