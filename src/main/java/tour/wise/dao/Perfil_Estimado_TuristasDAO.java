package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Perfil_Estimado_TuristasDAO {

    private JdbcTemplate connection; // Conexão com o banco

    public Perfil_Estimado_TuristasDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public boolean exist(
            int fkPaisOrigem,
            String fkUfEntrada,
            int ano,
            Integer mes,
            int quantidadeTuristas,
            String genero,
            String faixaEtaria,
            String viaAcesso,
            String composicaoGrupoFamiliar,
            String fonteInformacaoViagem,
            int servicoAgenciaTurismo,
            String motivoViagem,
            String motivacao_viagem_lazer,
            double gastoMedioPercapitaEmReais
    ) {
        // SQL para verificar se já existe um perfil estimado com os dados fornecidos, exceto o id.
        String sql = """
        SELECT COUNT(*) 
        FROM Perfil_Estimado_Turistas 
        WHERE fk_pais_origem = ? 
          AND fk_uf_entrada = ? 
          AND ano = ? 
          AND (mes = ? OR mes IS NULL)
          AND quantidade_turistas = ? 
          AND genero = ? 
          AND faixa_etaria = ? 
          AND via_acesso = ? 
          AND composicao_grupo_familiar = ? 
          AND fonte_informacao_viagem = ? 
          AND servico_agencia_turismo = ? 
          AND motivo_viagem = ? 
          AND motivacao_viagem_lazer = ?
          AND gasto_media_percapita_em_reais = ?
    """;

        try {
            // Executa a consulta para verificar a existência do registro
            int count = connection.queryForObject(sql, Integer.class,
                    fkPaisOrigem,
                    fkUfEntrada,
                    ano,
                    mes,
                    quantidadeTuristas,
                    genero,
                    faixaEtaria,
                    viaAcesso,
                    composicaoGrupoFamiliar,
                    fonteInformacaoViagem,
                    servicoAgenciaTurismo,
                    motivoViagem,
                    motivacao_viagem_lazer,
                    gastoMedioPercapitaEmReais
            );

            // Se count > 0, significa que o registro já existe
            return count > 0;
        } catch (Exception e) {
            System.err.println("Erro ao verificar existência do perfil estimado:");
            System.err.printf("Dados: País %d, UF %s, %02d/%d, %d turistas%n",
                    fkPaisOrigem, fkUfEntrada, mes != null ? mes : 0, ano, quantidadeTuristas);

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



    public void insert(
            int fkPaisOrigem,
            String fkUfEntrada,
            int ano,
            Integer mes,
            int quantidadeTuristas,
            String genero,
            String faixaEtaria,
            String viaAcesso,
            String composicaoGrupoFamiliar,
            String fonteInformacaoViagem,
            int servicoAgenciaTurismo,
            String motivoViagem,
            String motivacao_viagem_lazer,
            double gastoMedioPercapitaEmReais
    ) {
        String sql = """
        INSERT INTO Perfil_Estimado_Turistas (
            fk_pais_origem, fk_uf_entrada, ano, mes, quantidade_turistas,
            genero, faixa_etaria, via_acesso, composicao_grupo_familiar,
            fonte_informacao_viagem, servico_agencia_turismo,
            motivo_viagem, motivacao_viagem_lazer, gasto_media_percapita_em_reais
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try {
            int rowsAffected = connection.update(sql,
                    fkPaisOrigem,
                    fkUfEntrada,
                    ano,
                    mes,
                    quantidadeTuristas,
                    genero,
                    faixaEtaria,
                    viaAcesso,
                    composicaoGrupoFamiliar,
                    fonteInformacaoViagem,
                    servicoAgenciaTurismo,
                    motivoViagem,
                    motivacao_viagem_lazer,
                    gastoMedioPercapitaEmReais
            );

            if (rowsAffected > 0) {
                System.out.println("Perfil estimado inserido com sucesso.");
            } else {
                System.out.println("Nenhum dado foi inserido.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao inserir perfil estimado:");
            System.err.printf("Dados: País %d, UF %s, %02d/%d, %d turistas%n",
                    fkPaisOrigem, fkUfEntrada, mes != null ? mes : 0, ano, quantidadeTuristas);

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

    public void insertIgnore(
            int fkPaisOrigem,
            String fkUfEntrada,
            int ano,
            Integer mes,
            int quantidadeTuristas,
            String genero,
            String faixaEtaria,
            String viaAcesso,
            String composicaoGrupoFamiliar,
            String fonteInformacaoViagem,
            int servicoAgenciaTurismo,
            String motivoViagem,
            String motivacao_viagem_lazer,
            double gastoMedioPercapitaEmReais
    ) {
        // Verifica se o perfil estimado já existe
        if (exist(fkPaisOrigem, fkUfEntrada, ano, mes, quantidadeTuristas, genero, faixaEtaria, viaAcesso, composicaoGrupoFamiliar, fonteInformacaoViagem, servicoAgenciaTurismo, motivoViagem, motivacao_viagem_lazer, gastoMedioPercapitaEmReais)) {
            System.out.println("Perfil estimado já existe para os dados fornecidos, nenhuma inserção feita.");
            return; // Se existir, não faz a inserção
        }

        // Se não existir, insere o perfil estimado
        String sql = """
        INSERT INTO Perfil_Estimado_Turistas 
        (fk_pais_origem, fk_uf_entrada, ano, mes, quantidade_turistas, genero, faixa_etaria, via_acesso, composicao_grupo_familiar, fonte_informacao_viagem, servico_agencia_turismo, motivo_viagem, motivacao_viagem_lazer, gasto_media_percapita_em_reais)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try {
            int rows = connection.update(
                    sql,
                    fkPaisOrigem,
                    fkUfEntrada,
                    ano,
                    mes,
                    quantidadeTuristas,
                    genero,
                    faixaEtaria,
                    viaAcesso,
                    composicaoGrupoFamiliar,
                    fonteInformacaoViagem,
                    servicoAgenciaTurismo,
                    motivoViagem,
                    motivacao_viagem_lazer,
                    gastoMedioPercapitaEmReais
            );

            if (rows > 0) {
                System.out.println("Inserção bem-sucedida para o perfil estimado de turistas.");
            } else {
                System.out.println("Nenhuma linha inserida.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao inserir perfil estimado:");
            System.err.printf("Dados: País %d, UF %s, %02d/%d, %d turistas%n",
                    fkPaisOrigem, fkUfEntrada, mes != null ? mes : 0, ano, quantidadeTuristas);

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
        INSERT INTO Perfil_Estimado_Turistas 
        (fk_pais_origem, fk_uf_entrada, ano, mes, quantidade_turistas, 
         genero, faixa_etaria, via_acesso, composicao_grupo_familiar, 
         fonte_informacao_viagem, servico_agencia_turismo, 
         motivo_viagem, gasto_media_percapita_em_reais)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try {
            int[] rows = connection.batchUpdate(sql, batchArgs);
            System.out.println("Lote inserido: " + rows.length + " registros.");
        } catch (Exception e) {
            System.err.println("Erro ao inserir lote de perfil estimado de turistas:");
            e.printStackTrace();
            throw e;
        }
    }

    public int insertAndReturnId(Object[] params) {
        String sql = """
        INSERT INTO Perfil_Estimado_Turistas 
        (fk_pais_origem, fk_uf_entrada, ano, mes, quantidade_turistas, genero, faixa_etaria, via_acesso,
        composicao_grupo_viagem, fonte_informacao, utilizacao_agencia_viagem, motivo_viagem, gasto_medio_per_capita)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        connection.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue(); // Retorna o ID gerado
    }

    public List<Integer> insertLoteRetornandoIds(List<Object[]> batchArgs) {
        String sql = """
        INSERT INTO Perfil_Estimado_Turistas (
            fk_pais_origem, fk_uf_entrada, ano, mes, quantidade_turistas,
            genero, faixa_etaria, via_acesso, composicao_grupo_familiar,
            fonte_informacao_viagem, servico_agencia_turismo,
            motivo_viagem, motivacao_viagem_lazer, gasto_media_percapita_em_reais
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        List<Integer> idsGerados = new ArrayList<>();

        for (Object[] args : batchArgs) {
            KeyHolder holder = new GeneratedKeyHolder();
            connection.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
                return ps;
            }, holder);

            Number id = holder.getKey();
            if (id != null) {
                idsGerados.add(id.intValue());
            }
        }

        return idsGerados;
    }






}
