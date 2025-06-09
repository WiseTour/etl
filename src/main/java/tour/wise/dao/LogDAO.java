package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import tour.wise.model.Log;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;

public class LogDAO {


    private static final RowMapper<Log> rowMapper = (rs, rowNum) -> {
        Log log = new Log();
        log.setIdLog(rs.getInt("id_log"));
        log.setFkLogCategoria(rs.getInt("fk_log_categoria"));
        log.setFkEtapa(rs.getInt("fk_etapa"));
        log.setFkOrigemDados(rs.getObject("fk_origem_dados") != null ? rs.getInt("fk_origem_dados") : null);
        log.setFkPerfilEstimadoTuristas(rs.getObject("fk_perfil_estimado_turistas") != null ? rs.getInt("fk_perfil_estimado_turistas") : null);
        log.setFkPaisOrigem(rs.getObject("fk_pais_origem") != null ? rs.getInt("fk_pais_origem") : null);
        log.setFkUfEntrada(rs.getString("fk_uf_entrada"));
        log.setMensagem(rs.getString("mensagem"));
        Timestamp ts = rs.getTimestamp("data_hora");
        if (ts != null) {
            log.setDataHora(ts.toLocalDateTime());
        }
        return log;
    };

    public static void insert(JdbcTemplate jdbcTemplate, Log log) {
        String sql = "INSERT INTO log (fk_log_categoria, fk_etapa, fk_origem_dados, fk_perfil_estimado_turistas, fk_pais_origem, fk_uf_entrada, mensagem, data_hora) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, log.getFkLogCategoria());
            ps.setInt(2, log.getFkEtapa());

            if (log.getFkOrigemDados() != null) {
                ps.setInt(3, log.getFkOrigemDados());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (log.getFkPerfilEstimadoTuristas() != null) {
                ps.setInt(4, log.getFkPerfilEstimadoTuristas());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            if (log.getFkPaisOrigem() != null) {
                ps.setInt(5, log.getFkPaisOrigem());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            if (log.getFkUfEntrada() != null) {
                ps.setString(6, log.getFkUfEntrada());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            ps.setString(7, log.getMensagem());

            if (log.getDataHora() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(log.getDataHora()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            log.setIdLog(key.intValue());
        }
    }



}
