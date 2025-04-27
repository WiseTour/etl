package tour.wise.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class Unidade_Federativa_Brasil_DAO {
    private final JdbcTemplate connection;

    public Unidade_Federativa_Brasil_DAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public String getSiglaPorNome(String nomeUF) {
        String sql = "SELECT sigla FROM Unidade_Federativa_Brasil WHERE unidade_federativa = ?";
        String sigla = null;

        try {
            sigla = connection.queryForObject(sql, String.class, nomeUF);
            System.out.println("Sigla encontrada para '" + nomeUF + "': " + sigla);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("UF '" + nomeUF + "' não encontrada no banco de dados.");
        } catch (Exception e) {
            System.out.println("Erro ao buscar a sigla da UF '" + nomeUF + "': " + e.getMessage());
        }

        return sigla;
    }


}
