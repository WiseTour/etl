package tour.wise.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class Perfil_Estimado_Turistas_DAO {
    private JdbcTemplate connection; // Conexão com o banco

    public Perfil_Estimado_Turistas_DAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public void insertPerfilEstimadoTurista(
            int fkFonte,
            String composicaoGrupoFamiliar,
            String fonteInformacaoViagem,
            int servicoAgenciaTurismo,
            String motivoViagem,
            double gastoMedioPerCapita,
            int ano,
            int fkTotalChegadasMensal,
            String ufDestino,
            int fkFonteChegadasAnual,
            int fkPaisOrigem
    ) {
        String sql = """
        INSERT INTO Perfil_Estimado_Turistas (
            fk_fonte,
            genero,
            faixa_etaria,
            composicao_grupo_familiar,
            fonte_informacao_viagem,
            servico_agencia_turismo,
            motivo_viagem,
            gasto_media_percapita_em_reais,
            ano,
            fk_total_chegadas_turistas_internacionais_brasil_mensal,
            fk_uf_destino,
            fk_fonte_chegadas_turistas_brasil_anual,
            fk_pais_origem
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, )
    """;

        try {
            int linhas = connection.update(sql,
                    fkFonte,
                    null,
                    null,
                    composicaoGrupoFamiliar,
                    fonteInformacaoViagem,
                    servicoAgenciaTurismo,
                    motivoViagem,
                    gastoMedioPerCapita,
                    ano,
                    fkTotalChegadasMensal,
                    ufDestino,
                    fkFonteChegadasAnual,
                    fkPaisOrigem
            );

            System.out.println("[INFO] Perfil estimado de turista inserido com sucesso. Linhas afetadas: " + linhas);
        } catch (DataAccessException e) {
            System.err.println("[ERRO] Falha ao inserir perfil estimado de turista: " + e.getMessage());
        }
    }

}
