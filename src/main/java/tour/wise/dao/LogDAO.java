package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class LogDAO{

    private JdbcTemplate connection; // Conexão com o banco

    public LogDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public void insertLog(int fkFonte, int fkLogCategoria, int fkEtapa, String mensagem,
                          LocalDateTime dataHora, Integer quantidadeLida, Integer quantidadeInserida,
                          String tabelaDestino) {
        System.out.println(LocalDateTime.now() + "Tentando inserir log para a fonte ID: " + fkFonte);

        try {
            int rowsAffected = connection.update(
                    "INSERT INTO Log (fk_fonte, fk_log_categoria, fk_etapa, mensagem, data_hora, quantidade_lida, quantidade_inserida, tabela_destino) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    fkFonte,
                    fkLogCategoria,
                    fkEtapa,
                    mensagem,
                    Timestamp.valueOf(dataHora),
                    quantidadeLida,
                    quantidadeInserida,
                    tabelaDestino
            );

            if (rowsAffected > 0) {
                System.out.println(LocalDateTime.now() + "Log inserido com sucesso.");
            } else {
                System.out.println(LocalDateTime.now() + "Nenhuma linha foi inserida.");
            }
        } catch (Exception e) {
            System.err.println(LocalDateTime.now() + "Erro ao inserir log no banco.");

            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                System.err.println(LocalDateTime.now() + "Erro SQL: " + sqlEx.getMessage());
                System.err.println(LocalDateTime.now() + "Código de erro SQL: " + sqlEx.getErrorCode());
                System.err.println(LocalDateTime.now() + "SQLState: " + sqlEx.getSQLState());
            }

            e.printStackTrace();
            throw e;
        }
    }
}

