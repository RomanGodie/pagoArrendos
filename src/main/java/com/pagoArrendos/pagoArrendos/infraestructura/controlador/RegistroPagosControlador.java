package com.pagoArrendos.pagoArrendos.infraestructura.controlador;

import com.pagoArrendos.pagoArrendos.aplicacion.servicio.IntermediarioRegistroPagosServicioAplicacion;
import com.pagoArrendos.pagoArrendos.dominio.dto.RespuestaRegistroPagosDto;
import com.pagoArrendos.pagoArrendos.infraestructura.dto.RegistroPagosDtoInfraestructura;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class RegistroPagosControlador {
    private IntermediarioRegistroPagosServicioAplicacion intermediarioRegistroPagosServicioAplicacion;

    @PostMapping("/pagos")
    public RespuestaRegistroPagosDto registrarPago(@RequestBody RegistroPagosDtoInfraestructura registroPagosDtoInfraestructura){
        return intermediarioRegistroPagosServicioAplicacion.transaccionDeControladorADominio(registroPagosDtoInfraestructura);
    }

    @GetMapping("/pagos")
    public RespuestaRegistroPagosDto leerTodosLosPagos(){
        return intermediarioRegistroPagosServicioAplicacion.transaccionDeBaseDeDatosARespuestaRegistroPagosDtoDominio();
    }
}
