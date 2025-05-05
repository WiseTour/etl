package tour.wise.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class Unidade_Federativa_BrasilDAO {
    private final JdbcTemplate connection;

    public Unidade_Federativa_BrasilDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public String getId(String unidadeFederativa) {
        String sql = "SELECT sigla FROM Unidade_Federativa_Brasil WHERE unidade_federativa = ?";

        try {
            // Executa o select e obtém o valor da sigla correspondente
            String siglaResult = connection.queryForObject(sql, String.class, unidadeFederativa);

            if (siglaResult != null) {
                System.out.println("Unidade Federativa encontrada: " + siglaResult);
                return siglaResult;
            } else {
                System.out.println("Erro: Unidade Federativa não encontrada.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao consultar Unidade Federativa: " + e.getMessage());
            e.printStackTrace(); // Exibe o stack trace para ajudar na depuração
            return null;
        }
    }



}
