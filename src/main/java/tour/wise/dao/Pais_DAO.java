package tour.wise.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class Pais_DAO {

    private JdbcTemplate connection; // Conexão com o banco

    public Pais_DAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public Integer getPaisId(String nomePais) {
        String sqlIdPais = "SELECT idPais FROM Pais WHERE pais = ?";
        Integer idPais = null;

        try {
            idPais = connection.queryForObject(sqlIdPais, Integer.class, nomePais);
            System.out.println("[INFO] ID encontrado para o país '" + nomePais + "': " + idPais);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("[AVISO] País '" + nomePais + "' não encontrado no banco de dados.");
        } catch (Exception e) {
            System.out.println("[ERRO] Erro ao buscar ID do país '" + nomePais + "': " + e.getMessage());
        }

        return idPais;
    }

    // Método para inserir um novo país no banco
    public void insertPais(String nomePais) {
        String sqlInsertPais = "INSERT INTO Pais (pais) VALUES (?)";

        try {
            connection.update(sqlInsertPais, nomePais);
            System.out.println("[INSERÇÃO] Novo país inserido com sucesso: '" + nomePais + "'");

        } catch (Exception e) {
            System.out.println("[ERRO] Erro ao inserir novo país '" + nomePais + "': " + e.getMessage());
        }
    }
}
