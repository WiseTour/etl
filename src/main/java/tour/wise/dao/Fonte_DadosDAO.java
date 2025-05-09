package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

public class Fonte_DadosDAO {

    private JdbcTemplate connection; // Conexão com o banco

    public Fonte_DadosDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public boolean insertIgnore(String titulo_arquivo_fonte, String edicao, String orgao_emissor, String url_origem, String observacoes) {
        System.out.println("Tentando inserir a fonte: " + titulo_arquivo_fonte);

        try {
            int rowsAffected = connection.update(
                    "INSERT IGNORE INTO Fonte_Dados (titulo_arquivo_fonte, edicao, orgao_emissor, url_origem, data_coleta, observacoes) VALUES (?, ?, ?, ?, NOW(), ?)",
                    titulo_arquivo_fonte,
                    edicao,
                    orgao_emissor,
                    url_origem,
                    observacoes
            );

            if (rowsAffected > 0) {
                System.out.println("Fonte inserida com sucesso: " + titulo_arquivo_fonte);
                return true;
            } else {
                System.out.println("Fonte já existente, nenhuma inserção feita: " + titulo_arquivo_fonte);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro ao inserir fonte no banco: " + titulo_arquivo_fonte);
            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                System.err.println("Erro SQL: " + sqlEx.getMessage());
                System.err.println("Código de erro SQL: " + sqlEx.getErrorCode());
                System.err.println("SQLState: " + sqlEx.getSQLState());
            }
            e.printStackTrace();
            throw e;
        }
    }



    public Integer getId(String titulo_arquivo_fonte) {
        String sqlFonte = "SELECT id_fonte_dados FROM Fonte_Dados WHERE titulo_arquivo_fonte = ?";
        Integer id_fonte_dados = null;

        try {
            id_fonte_dados = connection.queryForObject(sqlFonte, Integer.class, titulo_arquivo_fonte);

            if (id_fonte_dados == null) {
                System.out.println("Fonte não encontrada para o título: " + titulo_arquivo_fonte);
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar ID da fonte para o título: " + titulo_arquivo_fonte);

            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                System.err.println("Erro SQL: " + sqlEx.getMessage());
                System.err.println("Código de erro SQL: " + sqlEx.getErrorCode());
                System.err.println("SQLState: " + sqlEx.getSQLState());
            }

            e.printStackTrace();

            throw e;
        }

        return id_fonte_dados;
    }


}
