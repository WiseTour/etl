package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

public class Perfil_Estimado_Turista_FonteDAO {

    private final JdbcTemplate connection; // Conexão com o banco

    public Perfil_Estimado_Turista_FonteDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public boolean exist(int fkFonte, int fkPerfilEstimadoTuristas, int fkPaisOrigem) {
        String sql = """
            SELECT COUNT(*) FROM Perfil_Estimado_Turista_Fonte
            WHERE fk_fonte = ? AND fk_perfil_estimado_turistas = ? AND fk_pais_origem = ?
        """;

        Integer count = connection.queryForObject(sql, Integer.class, fkFonte, fkPerfilEstimadoTuristas, fkPaisOrigem);

        // Retorna true se já existe, caso contrário, false
        return count != null && count > 0;
    }

    public void insertIgnore(
            int fkFonte,
            int fkPerfilEstimadoTuristas,
            int fkPaisOrigem
    ) {
        // Verifica se o registro já existe na tabela Perfil_Estimado_Turista_Fonte
        if (exist(fkFonte, fkPerfilEstimadoTuristas, fkPaisOrigem)) {
            System.out.println(LocalDateTime.now() +  "Registro de Perfil_Estimado_Turista_Fonte já existe para os dados fornecidos, nenhuma inserção feita.");
            return; // Se existir, não faz a inserção
        }

        // Se não existir, insere o registro
        String sql = """
        INSERT INTO Perfil_Estimado_Turista_Fonte 
        (fk_fonte, fk_perfil_estimado_turistas, fk_pais_origem)
        VALUES (?, ?, ?)
    """;

        try {
            int rows = connection.update(
                    sql,
                    fkFonte,
                    fkPerfilEstimadoTuristas,
                    fkPaisOrigem
            );

            if (rows > 0) {
                System.out.println(LocalDateTime.now() + "Inserção bem-sucedida para o Perfil_Estimado_Turista_Fonte.");
            } else {
                System.out.println(LocalDateTime.now() + "Nenhuma linha inserida.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao inserir Perfil_Estimado_Turista_Fonte:");
            System.err.printf("Dados: Fonte %d, Perfil Estimado %d, País %d%n",
                    fkFonte, fkPerfilEstimadoTuristas, fkPaisOrigem);

            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                System.err.println("Erro SQL: " + sqlEx.getMessage());
                System.err.println("Código de erro SQL: " + sqlEx.getErrorCode());
                System.err.println("SQLState: " + sqlEx.getSQLState());
            }

            e.printStackTrace();
            throw e; // Se quiser que a aplicação pare ou retorne erro ao chamador
        }
    }


    public void insertLote(List<Object[]> batchArgs) {
        String sql = """
        INSERT INTO Perfil_Estimado_Turista_Fonte
        (fk_fonte, fk_perfil_estimado_turistas, fk_pais_origem)
        VALUES (?, ?, ?)
    """;

        try {
            int[] resultados = connection.batchUpdate(sql, batchArgs);
            System.out.println(LocalDateTime.now() + "Lote de associação fonte-perfil inserido: " + resultados.length);
        } catch (Exception e) {
            System.err.println(LocalDateTime.now() + "Erro ao inserir lote em Perfil_Estimado_Turista_Fonte:");
            e.printStackTrace();
            throw e;
        }
    }
}
