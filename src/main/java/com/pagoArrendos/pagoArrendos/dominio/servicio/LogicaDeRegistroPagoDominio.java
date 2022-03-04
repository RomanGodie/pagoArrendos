package com.pagoArrendos.pagoArrendos.dominio.servicio;

import com.pagoArrendos.pagoArrendos.dominio.dto.RespuestaRegistroPagosDto;
import com.pagoArrendos.pagoArrendos.dominio.modelo.RegistroPagos;
import com.pagoArrendos.pagoArrendos.dominio.puerto.PuertoRegistroPagos;

public class LogicaDeRegistroPagoDominio {
    PuertoRegistroPagos puertoRegistroPagos;
    private final int VALOR_CANON_ARRENDAMIENTO = 1000000;

    public LogicaDeRegistroPagoDominio(PuertoRegistroPagos puertoRegistroPagos) {
        this.puertoRegistroPagos = puertoRegistroPagos;
    }

    public RespuestaRegistroPagosDto logicaDeNegocioRegistroPago(RegistroPagos registroPagos){
        return validarMonto(validarSiExisteArrendatarioEInmueble(registroPagos), registroPagos);
    }

    public boolean validarSiExisteArrendatarioEInmueble(RegistroPagos registroPagos) {
        if(puertoRegistroPagos.
                readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleRetornandoCuantosHay(registroPagos)){
            return true;
        }else{
            return false;
        }
    }

    public RespuestaRegistroPagosDto validarMonto (boolean arrendatarioExistente, RegistroPagos registroPagos) {
        int nuevoValorParaRegistrarEnBaseDatos = 0;
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
}
