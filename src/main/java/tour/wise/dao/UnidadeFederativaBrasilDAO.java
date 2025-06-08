package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import tour.wise.model.UnidadeFederativaBrasil;
import tour.wise.util.DataBaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UnidadeFederativaBrasilDAO {

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


    // Lista todos
    public static List<UnidadeFederativaBrasil> findAll(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT sigla, unidade_federativa, regiao FROM unidade_federativa_brasil";
        return jdbcTemplate.query(sql, new UnidadeFederativaBrasilRowMapper());
    }


}
