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
    private final String TOTAL_ARRENDO_CANCELADO_MENSAJE = "Ya has cancelado la totalidad de tu arrendo ";
    private final String SALDO_A_FAVOR_MENSAJE = "tienes un saldo a favor de $";
    private final String GRACIAS_POR_ABONO_MENSAJE = "gracias por tu abono, pero recuerda que aun te falta pagar: $";
    private final String GRACIAS_POR_PAGAR_TODO_MENSAJE = "gracias por pagar todo tu arriendo";
    private final String LO_SENTIMOS_FECHA_INVALIDA_MENSAJE = "lo siento pero no se puede recibir el pago por decreto de administración";
    private final String EL_ARRENDATARIO_Y_APTO_EXISTEN_PERO_NO_LIGADOS_MENSAJE = "El Arrendatario y el apartamento existen pero no estan ligados";
    private final String ARRENDATARIO_EXISTE_PERO_RESPONDE_OTRO_APTO_MENSAJE = "El Arrendatario existe pero es responsable de otro apartamento";
    private final String APTO_EXISTE_PERO_RESPONDE_OTRO_ARRENDATARIO_MENSAJE = "El apartamento existe pero esta a cargo de otro arrendatario";

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
                readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioRetornandoCuantosHay(registroPagos)){
            if(puertoRegistroPagos.readUnSoloRegistroPagosEnBaseDatosConCodigoInmuebleRetornandoCuantosHay(registroPagos)){
                if(puertoRegistroPagos.
                        readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleRetornandoCuantosHay(registroPagos)){
                    return true;
                }else{
                    throw new ExcepcionDeLogicaDeDominio(EL_ARRENDATARIO_Y_APTO_EXISTEN_PERO_NO_LIGADOS_MENSAJE);
                }

            }else{
                throw  new ExcepcionDeLogicaDeDominio(ARRENDATARIO_EXISTE_PERO_RESPONDE_OTRO_APTO_MENSAJE);
            }
        }else{
            if(puertoRegistroPagos.readUnSoloRegistroPagosEnBaseDatosConCodigoInmuebleRetornandoCuantosHay(registroPagos)){
                throw new ExcepcionDeLogicaDeDominio(APTO_EXISTE_PERO_RESPONDE_OTRO_ARRENDATARIO_MENSAJE);
            }else{
                return false;
            }
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
                return new RespuestaRegistroPagosDto(TOTAL_ARRENDO_CANCELADO_MENSAJE +
                        SALDO_A_FAVOR_MENSAJE + (nuevoValorParaRegistrarEnBaseDatos - VALOR_CANON_ARRENDAMIENTO));
            } else if (nuevoValorParaRegistrarEnBaseDatos < VALOR_CANON_ARRENDAMIENTO) {
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                int registroActualizado = puertoRegistroPagos.updateRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto(GRACIAS_POR_ABONO_MENSAJE +
                        (VALOR_CANON_ARRENDAMIENTO - nuevoValorParaRegistrarEnBaseDatos));
            }else{
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                puertoRegistroPagos.updateRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto(GRACIAS_POR_PAGAR_TODO_MENSAJE);
            }
        }else{
            nuevoValorParaRegistrarEnBaseDatos = Integer.parseInt(registroPagos.getValorPagado());
            if (nuevoValorParaRegistrarEnBaseDatos > VALOR_CANON_ARRENDAMIENTO) {
                registroPagos.setValorPagado(String.valueOf(VALOR_CANON_ARRENDAMIENTO));
                puertoRegistroPagos.createRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto(TOTAL_ARRENDO_CANCELADO_MENSAJE +
                        SALDO_A_FAVOR_MENSAJE + (nuevoValorParaRegistrarEnBaseDatos - VALOR_CANON_ARRENDAMIENTO));
            } else if (nuevoValorParaRegistrarEnBaseDatos < VALOR_CANON_ARRENDAMIENTO) {
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                puertoRegistroPagos.createRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto(GRACIAS_POR_ABONO_MENSAJE +
                        (VALOR_CANON_ARRENDAMIENTO - nuevoValorParaRegistrarEnBaseDatos));
            }else{
                registroPagos.setValorPagado(String.valueOf(nuevoValorParaRegistrarEnBaseDatos));
                puertoRegistroPagos.createRegistroPagosEnBaseDatos(registroPagos);
                return new RespuestaRegistroPagosDto(GRACIAS_POR_PAGAR_TODO_MENSAJE);
            }
        }
    }

    public boolean validarFechaImpar(RegistroPagos registroPagos){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaIngresada = LocalDate.parse(registroPagos.getFechaPago(),formatter);
        int diaDeFechaIngresada =  fechaIngresada.getDayOfMonth();
        if(diaDeFechaIngresada % 2 == 0){
            throw new ExcepcionDeLogicaDeDominio(LO_SENTIMOS_FECHA_INVALIDA_MENSAJE);
        }else{
            return true;
        }
    }
}
