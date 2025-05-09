package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Chegada_Turistas_Internacionais_Brasil_MensalDAO {
    private JdbcTemplate connection; // Conexão com o banco

    public Chegada_Turistas_Internacionais_Brasil_MensalDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    Unidade_Federativa_BrasilDAO unidade_federativa_brasil_dao = new Unidade_Federativa_BrasilDAO(connection);

    public boolean hasChegadaMensal(int mes, int ano, String siglaUfDestino, int idFonteDados, int idPaisOrigem) {
        String sql = """
        SELECT COUNT(*) 
        FROM Chegada_Turistas_Internacionais_Brasil_Mensal 
        WHERE mes = ? AND ano = ? 
          AND fk_uf_destino = ? 
          AND fk_fonte_dados = ? 
          AND fk_pais_origem = ?
    """;

        try {
            Integer count = connection.queryForObject(sql, Integer.class, mes, ano, siglaUfDestino, idFonteDados, idPaisOrigem);

            // Se count > 0, significa que a chegada já existe
            return count != null && count > 0;

        } catch (Exception e) {
            System.err.println("Erro ao verificar se a chegada já existe.");
            e.printStackTrace();
            throw e;  // se quiser que o erro pare a execução ou seja tratado em outro lugar
        }
    }

    public void insert(
            int mes,
            int ano,
            int chegadas,
            String viaAcesso,
            String siglaUfDestino,
            int idFonteDados,
            int idPaisOrigem
    ) {
        String sql = """
        INSERT INTO Chegada_Turistas_Internacionais_Brasil_Mensal 
        (mes, ano, chegadas, via_acesso, fk_uf_destino, fk_fonte_dados, fk_pais_origem)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try {
            int rows = connection.update(
                    sql,
                    mes,
                    ano,
                    chegadas,
                    viaAcesso,
                    siglaUfDestino,
                    idFonteDados,
                    idPaisOrigem
            );

            if (rows > 0) {
                System.out.println(LocalDateTime.now() + "Inserção bem-sucedida para " + siglaUfDestino + " (" + mes + "/" + ano + ")");
            } else {
                System.out.println(LocalDateTime.now() + "Nenhuma linha inserida.");
            }

        } catch (Exception e) {
            System.err.println(LocalDateTime.now() + "Erro ao inserir chegada mensal:");
            System.err.printf(LocalDateTime.now() + "Dados: %d/%d, %d chegadas, via %s, UF %s, Fonte %d, País %d%n",
                    mes, ano, chegadas, viaAcesso, siglaUfDestino, idFonteDados, idPaisOrigem
            );

            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                System.err.println("Erro SQL: " + sqlEx.getMessage());
                System.err.println("Código de erro SQL: " + sqlEx.getErrorCode());
                System.err.println("SQLState: " + sqlEx.getSQLState());
            }

            e.printStackTrace();

            throw e; // se quiser que a aplicação pare ou retorne erro ao chamador
        }
    }

    public void insertIgnore(
            int mes,
            int ano,
            int chegadas,
            String viaAcesso,
            String siglaUfDestino,
            int idFonteDados,
            int idPaisOrigem
    ) {
        // Verifica se já existe a chegada
        if (hasChegadaMensal(mes, ano, siglaUfDestino, idFonteDados, idPaisOrigem)) {
            System.out.println("A chegada já existe para " + siglaUfDestino + " (" + mes + "/" + ano + "). Nenhuma inserção feita.");
            return;
        }

        // Verifica se o número de chegadas é maior que zero
        if (chegadas <= 0) {
            System.out.println("Não há chegadas para o mês " + mes + "/" + ano + ". Nenhuma inserção feita.");
            return;
        }

        String sql = """
        INSERT INTO Chegada_Turistas_Internacionais_Brasil_Mensal 
        (mes, ano, chegadas, via_acesso, fk_uf_destino, fk_fonte_dados, fk_pais_origem)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try {
            int rows = connection.update(
                    sql,
                    mes,
                    ano,
                    chegadas,
                    viaAcesso,
                    siglaUfDestino,
                    idFonteDados,
                    idPaisOrigem
            );

            if (rows > 0) {
                System.out.println("Inserção bem-sucedida para " + siglaUfDestino + " (" + mes + "/" + ano + ")");
            } else {
                System.out.println("Nenhuma linha inserida.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao inserir chegada mensal:");
            System.err.printf("Dados: %d/%d, %d chegadas, via %s, UF %s, Fonte %d, País %d%n",
                    mes, ano, chegadas, viaAcesso, siglaUfDestino, idFonteDados, idPaisOrigem
            );

            Throwable cause = e.getCause();
            if (cause instanceof java.sql.SQLException sqlEx) {
                System.err.println("Erro SQL: " + sqlEx.getMessage());
                System.err.println("Código de erro SQL: " + sqlEx.getErrorCode());
                System.err.println("SQLState: " + sqlEx.getSQLState());
            }

            e.printStackTrace();

            throw e; // se quiser que a aplicação pare ou retorne erro ao chamador
        }
    }


    public void insertLote(List<Object[]> batchArgs) {
        String sql = """
        INSERT INTO Chegada_Turistas_Internacionais_Brasil_Mensal
        (mes, ano, chegadas, via_acesso, fk_uf_destino, fk_fonte_dados, fk_pais_origem)
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try {
            int[] rows = connection.batchUpdate(sql, batchArgs);
            System.out.println("Lote inserido: " + rows.length + " registros.");
        } catch (Exception e) {
            System.err.println("Erro ao inserir lote de chegadas:");
            e.printStackTrace();
            throw e;
        }
    }




}
