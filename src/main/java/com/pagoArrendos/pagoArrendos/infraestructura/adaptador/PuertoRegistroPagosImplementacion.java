package com.pagoArrendos.pagoArrendos.infraestructura.adaptador;

import com.pagoArrendos.pagoArrendos.dominio.modelo.RegistroPagos;
import com.pagoArrendos.pagoArrendos.dominio.puerto.PuertoRegistroPagos;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
@Transactional
@AllArgsConstructor
public class PuertoRegistroPagosImplementacion implements PuertoRegistroPagos {
    private JdbcTemplate jdbcTemplate;
    private final int FILA_ELIMINADA = 0;

    @Override
    public void createRegistroPagosEnBaseDatos(RegistroPagos registroPagos) {
        String consulta = "INSERT INTO pagos (documentoIdentificacionArrendatario, codigoInmueble, valorPagado, fechaPago)"+
                "values (?,?,?,?)";
        jdbcTemplate.update(consulta,
                registroPagos.getDocumentoIdentificacionArrendatario(),
                registroPagos.getCodigoInmueble(),
                registroPagos.getValorPagado(),
                registroPagos.getFechaPago());
    }

    @Override
    public long createRegistroPagosEnBaseDatosRetornandoIdSimple(RegistroPagos registroPagos){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("pagos")
                .usingGeneratedKeyColumns("id");
        return simpleJdbcInsert.executeAndReturnKey(registroPagos.toMap()).longValue();
    }

    @Override
    public long createRegistroPagosEnBaseDatosRetornandoIdComplejo(RegistroPagos registroPagos){
        String sql = "INSERT INTO pagos (documentoIdentificacionArrendatario, codigoInmueble, valorPagado, fechaPago)"+
                " VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setInt(1,registroPagos.getDocumentoIdentificacionArrendatario());
            stmt.setString(2,registroPagos.getCodigoInmueble());
            stmt.setString(3,registroPagos.getValorPagado());
            stmt.setString(4,registroPagos.getFechaPago());
            return stmt;},keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public RegistroPagos readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleConBean(RegistroPagos registroPagos) {
        //La busqueda puede realizarce con cualquiera de los campos solo se debe cambiar
        String consulta = "SELECT * FROM pagos WHERE documentoIdentificacionArrendatario = ? AND codigoInmueble LIKE ?";
        return (RegistroPagos) jdbcTemplate.queryForObject(consulta,
                new Object[]{registroPagos.getDocumentoIdentificacionArrendatario(), "%"+registroPagos.getCodigoInmueble()+"%"},
                new BeanPropertyRowMapper<>(RegistroPagos.class));
    }

    @Override
    public RegistroPagos readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleDirecto(RegistroPagos registroPagos){
        String consulta = "SELECT * FROM pagos WHERE documentoIdentificacionArrendatario = ? AND codigoInmueble LIKE ?";
        return jdbcTemplate.queryForObject(consulta,
                new Object[]{registroPagos.getDocumentoIdentificacionArrendatario(), "%"+registroPagos.getCodigoInmueble()+"%"},
                (rs, rowNum) -> new RegistroPagos(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)));
    }

    @Override
    public boolean readUnSoloRegistroPagosEnBaseDatosConDocumentoIdentificacionArrendatarioYCodigoInmuebleRetornandoCuantosHay(RegistroPagos registroPagos){
        String sql = "SELECT COUNT(documentoIdentificacionArrendatario)"+
                "FROM pagos WHERE documentoIdentificacionArrendatario = ? AND codigoInmueble LIKE ?";
        int existe = jdbcTemplate.queryForObject(sql,
                new Object[]{registroPagos.getDocumentoIdentificacionArrendatario(), "%"+registroPagos.getCodigoInmueble()+"%"},
                Integer.class);
        return (existe >= 1);
    }

    @Override
    public List<RegistroPagos> readTodosLosRegistrosPagosEnBaseDatos(RegistroPagos registroPagos) {
        String consulta = "SELECT documentoIdentificacionArrendatario, codigoInmueble, valorPagado, fechaPago FROM pagos";
        List<RegistroPagos> todosLosRegistros = jdbcTemplate.query(consulta, new BeanPropertyRowMapper<>(RegistroPagos.class));
        return todosLosRegistros;
    }

    @Override
    public int updateRegistroPagosEnBaseDatos(RegistroPagos registroPagos) {
        String consulta = "UPDATE pagos SET documentoIdentificacionArrendatario = ?, " +
                "codigoInmueble = ?, valorPagado = ?, fechaPago = ? " +
                "WHERE documentoIdentificacionArrendatario = ? AND codigoInmueble LIKE ?";
        return jdbcTemplate.update(consulta,
                registroPagos.getDocumentoIdentificacionArrendatario(),
                registroPagos.getCodigoInmueble(),
                registroPagos.getValorPagado(),
                registroPagos.getFechaPago(),
                registroPagos.getDocumentoIdentificacionArrendatario(),
                registroPagos.getCodigoInmueble());
    }

    @Override
    public boolean deleteRegistroPagosEnBaseDatos(RegistroPagos registroPagos) {
        String consulta = "DELETE FROM pagos WHERE documentoIdentificacionArrendatario = ?";
        return jdbcTemplate.update(consulta, registroPagos.getDocumentoIdentificacionArrendatario())>FILA_ELIMINADA;
    }
}
