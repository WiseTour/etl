package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import tour.wise.model.Pais;
import java.util.List;

public class PaisDAO {

    // RowMapper para mapear os resultados do banco para o objeto Pais
    private static final RowMapper<Pais> paisRowMapper = (rs, rowNum) -> {
        Pais pais = new Pais();
        pais.setIdPais(rs.getInt("id_pais"));
        pais.setNomePais(rs.getString("pais"));
        return pais;
    };

    // Insert IGNORE (para evitar duplicados no nome)
    public static int insertIgnore(JdbcTemplate jdbcTemplate, Pais pais) {
        String sql = "INSERT IGNORE INTO pais (pais) VALUES (?)";
        return jdbcTemplate.update(sql, pais.getNomePais());
    }

    public static List<Pais> findAll(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT * FROM pais";
        return jdbcTemplate.query(sql, paisRowMapper);
    }




}
