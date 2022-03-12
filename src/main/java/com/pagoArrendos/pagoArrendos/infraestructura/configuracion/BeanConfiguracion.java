package com.pagoArrendos.pagoArrendos.infraestructura.configuracion;

import com.pagoArrendos.pagoArrendos.dominio.puerto.PuertoRegistroPagos;
import com.pagoArrendos.pagoArrendos.dominio.servicio.LogicaDeRegistroPagoDominio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguracion {

    @Bean
    public LogicaDeRegistroPagoDominio inyeccionLogicaDeRegistroPagoDominio(PuertoRegistroPagos puertoRegistroPagos){
        return new LogicaDeRegistroPagoDominio(puertoRegistroPagos);
    }
}
