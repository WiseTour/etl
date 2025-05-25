package tour.wise.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import tour.wise.model.Pais;

import java.time.LocalDateTime;
import java.util.List;

public class PaisDAO {

    private JdbcTemplate connection; // Conexão com o banco

    public PaisDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    // RowMapper para mapear os resultados do banco para o objeto Pais
    private final RowMapper<Pais> paisRowMapper = (rs, rowNum) -> {
        Pais pais = new Pais();
        pais.setIdPais(rs.getInt("id_pais"));
        pais.setNomePais(rs.getString("pais"));
        return pais;
    };

    // Insert IGNORE (para evitar duplicados no nome)
    public int insertIgnore(Pais pais) {
        String sql = "INSERT IGNORE INTO pais (pais) VALUES (?)";
        return connection.update(sql, pais.getNomePais());
    }

    // Buscar todos os registros
    public List<Pais> findAll() {
        String sql = "SELECT * FROM pais";
        return connection.query(sql, paisRowMapper);
    }

    // Buscar por ID
    public Pais findById(int id) {
        String sql = "SELECT * FROM pais WHERE id_pais = ?";
        try {
            return connection.queryForObject(sql, paisRowMapper, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    // Buscar por Nome
    public Pais findByNome(String nome) {
        String sql = "SELECT * FROM pais WHERE pais = ?";
        try {
            return connection.queryForObject(sql, paisRowMapper, nome);
        } catch (DataAccessException e) {
            return null;
        }
    }

    // Deletar por ID
    public int deleteById(int id) {
        String sql = "DELETE FROM pais WHERE id_pais = ?";
        return connection.update(sql, id);
    }

    // Atualizar pelo ID
    public int update(Pais pais) {
        String sql = "UPDATE pais SET pais = ? WHERE id_pais = ?";
        return connection.update(sql, pais.getNomePais(), pais.getIdPais());
    }

    // Contar quantos registros existem
    public int count() {
        String sql = "SELECT COUNT(*) FROM pais";
        return connection.queryForObject(sql, Integer.class);
    }


}
