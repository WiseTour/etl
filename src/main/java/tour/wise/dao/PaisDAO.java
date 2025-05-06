package tour.wise.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class PaisDAO {

    private JdbcTemplate connection; // Conexão com o banco

    public PaisDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public void insertIgnore(String pais) {
        System.out.println("Tentando inserir o país: " + pais);

        String sql = "INSERT IGNORE INTO Pais (pais) VALUES (?)";

        try {
            int rowsAffected = connection.update(sql, pais);

            if (rowsAffected > 0) {
                System.out.println("País inserido com sucesso: " + pais);
            } else {
                System.out.println("País já existente, nenhuma inserção feita: " + pais);
            }

        } catch (Exception e) {
            System.err.println("Erro ao inserir país no banco: " + pais);

            // Verifica se a causa da exceção é uma SQLException
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                System.err.println("Erro SQL: " + sqlEx.getMessage());
                System.err.println("Código de erro SQL: " + sqlEx.getErrorCode());
                System.err.println("SQLState: " + sqlEx.getSQLState());
            }

            e.printStackTrace(); // imprime o stack trace completo para depuração

            throw e; // lança a exceção para que a aplicação saiba que houve erro
        }
    }

    public Integer getId(String nomePais) {
        String sql = "SELECT id_pais FROM Pais WHERE pais = ?";

        try {
            Integer idPais = connection.queryForObject(sql, Integer.class, nomePais);

            if (idPais != null) {
                System.out.println("País encontrado: " + nomePais + " (ID: " + idPais + ")");
                return idPais;
            } else {
                System.out.println("País não encontrado: " + nomePais);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar ID do país: " + nomePais);
            e.printStackTrace();
            return null;
        }

    }


}
