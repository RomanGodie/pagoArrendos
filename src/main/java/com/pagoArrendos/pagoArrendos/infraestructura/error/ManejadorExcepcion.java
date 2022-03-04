package com.pagoArrendos.pagoArrendos.infraestructura.error;

import com.pagoArrendos.pagoArrendos.dominio.excepcion.ExcepcionDeLogicaDeDominio;
import com.pagoArrendos.pagoArrendos.infraestructura.dto.ExcepcionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
public class ManejadorExcepcion {
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger(ManejadorExcepcion.class);
    private static final String OCURRIO_UN_ERROR_FAVOR_CONTACTAR_AL_ADMINISTRADOR = "Ocurrió un error favor contactar al administrador.";
    private static final ConcurrentHashMap<String, Integer> CODIGOS_ESTADO = new ConcurrentHashMap<>();
    public ManejadorExcepcion() {
        CODIGOS_ESTADO.put(ExcepcionDeLogicaDeDominio.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
    }
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExcepcionDto> handleAllExceptions(Exception exception) {
        ResponseEntity<ExcepcionDto> resultado;

        String excepcionNombre = exception.getClass().getSimpleName();
        String mensaje = exception.getMessage();
        Integer codigo = CODIGOS_ESTADO.get(excepcionNombre);

        if (codigo != null) {
            ExcepcionDto error = new ExcepcionDto(mensaje);
            resultado = new ResponseEntity<>(error, HttpStatus.valueOf(codigo));
        } else {
            LOGGER_ERROR.error(excepcionNombre, exception);
            ExcepcionDto error = new ExcepcionDto(OCURRIO_UN_ERROR_FAVOR_CONTACTAR_AL_ADMINISTRADOR);
            resultado = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return resultado;
    }
}
