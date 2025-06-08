package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import tour.wise.model.Log;
import tour.wise.util.DataBaseConnection;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

public class LogDAO {

    private static final JdbcTemplate jdbcTemplate;

    static {
        try {
            jdbcTemplate = DataBaseConnection.getJdbcTemplate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar JdbcTemplate no LogDAO: " + e.getMessage(), e);
        }
    }

    private static final RowMapper<Log> rowMapper = (rs, rowNum) -> {
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

    public static void insert(Log log) {
        String sql = "INSERT INTO log (fk_log_categoria, fk_etapa, fk_origem_dados, fk_perfil_estimado_turistas, fk_pais_origem, fk_uf_entrada, mensagem, erro, data_hora) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, log.getFkLogCategoria());
            ps.setInt(2, log.getFkEtapa());

            ps.setObject(3, log.getFkOrigemDados(), Types.INTEGER);
            ps.setObject(4, log.getFkPerfilEstimadoTuristas(), Types.INTEGER);
            ps.setObject(5, log.getFkPaisOrigem(), Types.INTEGER);
            ps.setObject(6, log.getFkUfEntrada(), Types.VARCHAR);
            ps.setString(7, log.getMensagem());

            if (log.getErro() != null) {
                ps.setString(8, log.getErro());
            } else {
                ps.setNull(8, Types.VARCHAR);
            }

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

    public static void update(Log log) {
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

    public static void delete(int idLog) {
        String sql = "DELETE FROM log WHERE id_log = ?";
        jdbcTemplate.update(sql, idLog);
    }

    public static Log findById(int idLog) {
        String sql = "SELECT * FROM log WHERE id_log = ?";
        List<Log> logs = jdbcTemplate.query(sql, new Object[]{idLog}, rowMapper);
        return logs.isEmpty() ? null : logs.get(0);
    }

    public static List<Log> findAll() {
        String sql = "SELECT * FROM log";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
