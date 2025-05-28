package tour.wise.dao;

import tour.wise.model.OrigemDados;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.dao.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrigemDadosDAO {

    private final JdbcTemplate jdbcTemplate;

    public OrigemDadosDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper
    private final RowMapper<OrigemDados> origemDadosRowMapper = (rs, rowNum) -> {
        OrigemDados origem = new OrigemDados();
        origem.setIdOrigemDados(rs.getInt("id_origem_dados"));
        origem.setTitulo(rs.getString("titulo_arquivo_fonte"));
        origem.setEdicao(rs.getString("edicao"));
        origem.setOrgaoEmissor(rs.getString("orgao_emissor"));
        return origem;
    };

    // Insert IGNORE (ou equivalente em MySQL)
    public int insertIgnore(OrigemDados origemDados) {
        String sql = "INSERT IGNORE INTO origem_dados (titulo_arquivo_fonte, edicao, orgao_emissor) VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql,
                origemDados.getTitulo(),
                origemDados.getEdicao(),
                origemDados.getOrgaoEmissor()
        );
    }

    // Find All
    public List<OrigemDados> findAll() {
        String sql = "SELECT * FROM origem_dados";
        return jdbcTemplate.query(sql, origemDadosRowMapper);
    }

    // Find by ID
    public OrigemDados findById(int id) {
        String sql = "SELECT * FROM origem_dados WHERE id_origem_dados = ?";
        try {
            return jdbcTemplate.queryForObject(sql, origemDadosRowMapper, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    // Find by Título
    public OrigemDados findByTitulo(String titulo) {
        String sql = "SELECT * FROM origem_dados WHERE titulo_arquivo_fonte = ?";
        try {
            return jdbcTemplate.queryForObject(sql, origemDadosRowMapper, titulo);
        } catch (DataAccessException e) {
            return null;
        }
    }

    // Delete by ID
    public int deleteById(int id) {
        String sql = "DELETE FROM origem_dados WHERE id_origem_dados = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Update
    public int update(OrigemDados origemDados) {
        String sql = "UPDATE origem_dados SET titulo_arquivo_fonte = ?, edicao = ?, orgao_emissor = ? WHERE id_origem_dados = ?";
        return jdbcTemplate.update(sql,
                origemDados.getTitulo(),
                origemDados.getEdicao(),
                origemDados.getOrgaoEmissor(),
                origemDados.getIdOrigemDados()
        );
    }

    // Count
    public int count() {
        String sql = "SELECT COUNT(*) FROM origem_dados";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
