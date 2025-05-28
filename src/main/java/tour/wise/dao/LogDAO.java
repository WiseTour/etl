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
import java.util.List;

public class LogDAO {

    private final JdbcTemplate jdbcTemplate;

    public LogDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Log> rowMapper = (rs, rowNum) -> {
        Log log = new Log();
        log.setIdLog(rs.getInt("id_log"));
        log.setFkLogCategoria(rs.getInt("fk_log_categoria"));
        log.setFkEtapa(rs.getInt("fk_etapa"));
        log.setFkOrigemDados(rs.getInt("fk_origem_dados"));
        log.setFkPerfilEstimadoTuristas(rs.getInt("fk_perfil_estimado_turistas"));
        log.setFkPaisOrigem(rs.getInt("fk_pais_origem"));
        log.setFkUfEntrada(rs.getString("fk_uf_entrada"));
        log.setMensagem(rs.getString("mensagem"));
        log.setErro(rs.getString("erro"));
        Timestamp ts = rs.getTimestamp("data_hora");
        if (ts != null) {
            log.setDataHora(ts.toLocalDateTime());
        }
        return log;
    };

    public void insert(Log log) {
        String sql = "INSERT INTO log (fk_log_categoria, fk_etapa, fk_origem_dados, fk_perfil_estimado_turistas, fk_pais_origem, fk_uf_entrada, mensagem, erro, data_hora) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // fk_log_categoria (obrigatório)
            ps.setInt(1, log.getFkLogCategoria());

            // fk_etapa (obrigatório)
            ps.setInt(2, log.getFkEtapa());

            // fk_origem_dados
            if (log.getFkOrigemDados() != null) {
                ps.setInt(3, log.getFkOrigemDados());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            // fk_perfil_estimado_turistas
            if (log.getFkPerfilEstimadoTuristas() != null) {
                ps.setInt(4, log.getFkPerfilEstimadoTuristas());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            // fk_pais_origem
            if (log.getFkPaisOrigem() != null) {
                ps.setInt(5, log.getFkPaisOrigem());
            } else {
                ps.setNull(5, Types.INTEGER);
            }

            // fk_uf_entrada
            if (log.getFkUfEntrada() != null) {
                ps.setString(6, log.getFkUfEntrada());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }

            // mensagem (obrigatório, pode ser vazio mas não nulo)
            ps.setString(7, log.getMensagem());

            // erro
            if (log.getErro() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(log.getErro()));
            } else {
                ps.setNull(8, Types.TIMESTAMP);
            }

            // data_hora (obrigatório)
            if (log.getDataHora() != null) {
                ps.setTimestamp(9, Timestamp.valueOf(log.getDataHora()));
            } else {
                ps.setNull(9, Types.TIMESTAMP);
            }

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            log.setIdLog(key.intValue());
        }
    }


    public void update(Log log) {
        String sql = "UPDATE log SET fk_log_categoria = ?, fk_etapa = ?, fk_origem_dados = ?, fk_perfil_estimado_turistas = ?, fk_pais_origem = ?, fk_uf_entrada = ?, mensagem = ?, erro = ?, data_hora = ? WHERE id_log = ?";
        jdbcTemplate.update(sql,
                log.getFkLogCategoria(),
                log.getFkEtapa(),
                log.getFkOrigemDados(),
                log.getFkPerfilEstimadoTuristas(),
                log.getFkPaisOrigem(),
                log.getFkUfEntrada(),
                log.getMensagem(),
                log.getErro(),
                Timestamp.valueOf(log.getDataHora()),
                log.getIdLog());
    }

    public void delete(int idLog) {
        String sql = "DELETE FROM log WHERE id_log = ?";
        jdbcTemplate.update(sql, idLog);
    }

    public Log findById(int idLog) {
        String sql = "SELECT * FROM log WHERE id_log = ?";
        List<Log> logs = jdbcTemplate.query(sql, new Object[]{idLog}, rowMapper);
        return logs.isEmpty() ? null : logs.get(0);
    }

    public List<Log> findAll() {
        String sql = "SELECT * FROM log";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
