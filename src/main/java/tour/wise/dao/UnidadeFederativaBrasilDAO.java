package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import tour.wise.model.UnidadeFederativaBrasil;
import tour.wise.util.DataBaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UnidadeFederativaBrasilDAO {

    private static final JdbcTemplate jdbcTemplate;

    static {
        try {
            jdbcTemplate = DataBaseConnection.getJdbcTemplate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar JdbcTemplate no UnidadeFederativaBrasilDAO: " + e.getMessage(), e);
        }
    }

    // RowMapper para converter ResultSet em UnidadeFederativaBrasil
    private static final class UnidadeFederativaBrasilRowMapper implements RowMapper<UnidadeFederativaBrasil> {
        @Override
        public UnidadeFederativaBrasil mapRow(ResultSet rs, int rowNum) throws SQLException {
            UnidadeFederativaBrasil uf = new UnidadeFederativaBrasil();
            uf.setSigla(rs.getString("sigla"));
            uf.setUnidadeFederativa(rs.getString("unidade_federativa"));
            uf.setRegiao(rs.getString("regiao"));
            return uf;
        }
    }

    // Busca por sigla (PK)
    public UnidadeFederativaBrasil findBySigla(String sigla) {
        String sql = "SELECT sigla, unidade_federativa, regiao FROM unidade_federativa_brasil WHERE sigla = ?";
        List<UnidadeFederativaBrasil> result = jdbcTemplate.query(sql, new UnidadeFederativaBrasilRowMapper(), sigla);
        return result.isEmpty() ? null : result.get(0);
    }

    // Lista todos
    public static List<UnidadeFederativaBrasil> findAll() {
        String sql = "SELECT sigla, unidade_federativa, regiao FROM unidade_federativa_brasil";
        return jdbcTemplate.query(sql, new UnidadeFederativaBrasilRowMapper());
    }

    // Inserir nova unidade federativa
    public int insert(UnidadeFederativaBrasil uf) {
        String sql = "INSERT INTO unidade_federativa_brasil (sigla, unidade_federativa, regiao) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, uf.getSigla(), uf.getUnidadeFederativa(), uf.getRegiao());
    }

    // Atualizar existente
    public int update(UnidadeFederativaBrasil uf) {
        String sql = "UPDATE unidade_federativa_brasil SET unidade_federativa = ?, regiao = ? WHERE sigla = ?";
        return jdbcTemplate.update(sql, uf.getUnidadeFederativa(), uf.getRegiao(), uf.getSigla());
    }

    // Deletar pelo PK (sigla)
    public int deleteBySigla(String sigla) {
        String sql = "DELETE FROM unidade_federativa_brasil WHERE sigla = ?";
        return jdbcTemplate.update(sql, sigla);
    }
}
