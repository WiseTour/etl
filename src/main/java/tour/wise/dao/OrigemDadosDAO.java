package tour.wise.dao;

import tour.wise.model.OrigemDados;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.DataAccessException;

public class OrigemDadosDAO {


    // RowMapper
    private static final RowMapper<OrigemDados> origemDadosRowMapper = (rs, rowNum) -> {
        OrigemDados origem = new OrigemDados();
        origem.setIdOrigemDados(rs.getInt("id_origem_dados"));
        origem.setTitulo(rs.getString("titulo_arquivo_fonte"));
        origem.setEdicao(rs.getString("edicao"));
        origem.setOrgaoEmissor(rs.getString("orgao_emissor"));
        return origem;
    };

    // Insert IGNORE (ou equivalente em MySQL)
    public static void insertIgnore(JdbcTemplate jdbcTemplate, OrigemDados origemDados) {
        String sql = "INSERT IGNORE INTO origem_dados (titulo_arquivo_fonte, edicao, orgao_emissor) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                origemDados.getTitulo(),
                origemDados.getEdicao(),
                origemDados.getOrgaoEmissor()
        );
    }


    // Find by Título
    public static OrigemDados findByTitulo(JdbcTemplate jdbcTemplate, String titulo) {
        String sql = "SELECT * FROM origem_dados WHERE titulo_arquivo_fonte = ?";
        try {
            return jdbcTemplate.queryForObject(sql, origemDadosRowMapper, titulo);
        } catch (DataAccessException e) {
            return null;
        }
    }



}
