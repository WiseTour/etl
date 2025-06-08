package tour.wise.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import tour.wise.model.Pais;
import tour.wise.util.DataBaseConnection;
import java.util.List;

public class PaisDAO {

    private static final JdbcTemplate jdbcTemplate;

    static {
        try {
            jdbcTemplate = DataBaseConnection.getJdbcTemplate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar JdbcTemplate no PaisDAO: " + e.getMessage(), e);
        }
    }
    // RowMapper para mapear os resultados do banco para o objeto Pais
    private static final RowMapper<Pais> paisRowMapper = (rs, rowNum) -> {
        Pais pais = new Pais();
        pais.setIdPais(rs.getInt("id_pais"));
        pais.setNomePais(rs.getString("pais"));
        return pais;
    };

    // Insert IGNORE (para evitar duplicados no nome)
    public static int insertIgnore(Pais pais) {
        String sql = "INSERT IGNORE INTO pais (pais) VALUES (?)";
        return jdbcTemplate.update(sql, pais.getNomePais());
    }

    // Buscar todos os registros
    public static List<Pais> findAll() {
        String sql = "SELECT * FROM pais";
        return jdbcTemplate.query(sql, paisRowMapper);
    }

    // Buscar por ID
    public static Pais findById(int id) {
        String sql = "SELECT * FROM pais WHERE id_pais = ?";
        try {
            return jdbcTemplate.queryForObject(sql, paisRowMapper, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    // Buscar por Nome
    public static Pais findByNome(String nome) {
        String sql = "SELECT * FROM pais WHERE pais = ?";
        try {
            return jdbcTemplate.queryForObject(sql, paisRowMapper, nome);
        } catch (DataAccessException e) {
            return null;
        }
    }

    // Deletar por ID
    public static int deleteById(int id) {
        String sql = "DELETE FROM pais WHERE id_pais = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Atualizar pelo ID
    public static int update(Pais pais) {
        String sql = "UPDATE pais SET pais = ? WHERE id_pais = ?";
        return jdbcTemplate.update(sql, pais.getNomePais(), pais.getIdPais());
    }

    // Contar quantos registros existem
    public static int count() {
        String sql = "SELECT COUNT(*) FROM pais";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }


}
