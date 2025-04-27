package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class Fonte_DAO {

    private JdbcTemplate connection; // Conexão com o banco

    public Fonte_DAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public void insertFonteIgnore(String titulo_fonte, String edicao, String orgao_emissor, String url_origem) {
        System.out.println("Tentando inserir a fonte: " + titulo_fonte);

        int rows_affected = connection.update(
                "INSERT IGNORE INTO Fonte (titulo_arquivo_fonte, edicao, orgao_emissor, url_origem, data_coleta) VALUES (?, ?, ?, ?, NOW())",
                titulo_fonte,
                edicao,
                orgao_emissor,
                url_origem
        );

        if (rows_affected > 0) {
            System.out.println("Fonte inserida com sucesso: " + titulo_fonte);
        } else {
            System.out.println("Fonte já existente, nenhuma inserção feita: " + titulo_fonte);
        }
    }

    public Integer getFonteId(String titulo_edicao) {
        String sqlFonte = "SELECT id_fonte FROM Fonte WHERE titulo_edicao = ?";
        Integer fonte_id = connection.queryForObject(sqlFonte, Integer.class, titulo_edicao);

        if (fonte_id == null) {
            System.out.println("Erro: Fonte não encontrada.");
        }
        return fonte_id;
    }

}
