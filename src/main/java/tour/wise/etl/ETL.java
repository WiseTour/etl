package tour.wise.etl;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.*;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.etl.extract.Extract;
import tour.wise.etl.load.Load;
import tour.wise.slack.SlackWiseTour;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ETL {

    DataBase dataBase;
    JdbcTemplate connection;
    LogDAO logDAO;
    Extract extract;
    tour.wise.etl.transform.Transform transform;
    Load load;
    Service service;
    private final SlackWiseTour slackNotifier = new SlackWiseTour("https://hooks.slack.com/services/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");

    public ETL() {
        this.dataBase = new DataBase();
        this.connection = dataBase.getConnection();;
        this.logDAO = new LogDAO(connection);
        this.extract = new Extract(connection);
        this.transform = new tour.wise.etl.transform.Transform(connection);
        this.load = new Load(connection);
        this.service = new Service();
    }

    public void exe(
            String fileNameChegadas,
            String tituloArquivoFonteChegadas,
            String urlChegadas,
            String orgaoEmissorChegadas,
            String edicaoChegadas,
            String fileNameFichaSintesePais,
            String fileNameFichaSinteseBrasil,
            String fileNameFichaSinteseEstado,
            String tituloArquivoFonteFichasSinteses,
            String urlFichasSinteses,
            String orgaoEmissorFichasSinteses,
            String edicaoFichasSinteses
    ) throws IOException {

        try {
            // 1. INÍCIO DO PROCESSO
            slackNotifier.sendNotification(":gear: *PROCESSAMENTO INICIADO COM SUCESSO!* ");

            // 2. EXTRAÇÃO DOS DADOS DE CHEGADA
            slackNotifier.sendNotification("[WiseTour] :mag: Extração de dados iniciada");
            load.loadFonte(tituloArquivoFonteChegadas,
                    edicaoChegadas,
                    orgaoEmissorChegadas,
                    urlChegadas,
                    null);

            Fonte_DadosDAO fonteDadosDAO = new Fonte_DadosDAO(connection);
            Integer fkFonteChegadas = fonteDadosDAO.getId(tituloArquivoFonteChegadas);

            List<List<Object>> chegadasTuristasInternacionaisBrasilMensalData = extract.extractChegadasTuristasInternacionaisBrasilMensalData(
                    fkFonteChegadas,
                    "Chegada_Turistas_Internacionais_Brasil_Mensal",
                    fileNameChegadas,
                    0,
                    0,
                    12,
                    List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric")
            );
            slackNotifier.sendNotification("[WiseTour] :white_check_mark: Extração dos registros concluída!");

            // 3. TRANSFORMAÇÃO DOS DADOS
            slackNotifier.sendNotification("[WiseTour] :arrows_counterclockwise: Transformação dos dados em andamento...");
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO =
                    transform.transformChegadasTuristasInternacionaisBrasilMensal(
                            chegadasTuristasInternacionaisBrasilMensalData,
                            orgaoEmissorChegadas,
                            edicaoChegadas);
            slackNotifier.sendNotification("[WiseTour] :sparkles: Dados transformados com sucesso!");

            // 4. CARREGAMENTO INICIAL
            slackNotifier.sendNotification("[WiseTour] :floppy_disk: Inserção no banco de dados iniciada...");
            load.loadPais(chegadasTuristasInternacionaisBrasilMensalDTO);
            slackNotifier.sendNotification("[WiseTour] :card_file_box: Países carregados no banco!");

            // 5. PROCESSAMENTO DAS FICHAS SÍNTESE
            slackNotifier.sendNotification("[WiseTour] :page_facing_up: Processando fichas síntese...");
            load.loadFonte(
                    tituloArquivoFonteFichasSinteses,
                    edicaoFichasSinteses,
                    orgaoEmissorFichasSinteses,
                    urlFichasSinteses,
                    null
            );

            FichaSinteseBrasilDTO fichaSinteseBrasilDTO = transform.transformFichaSinteseBrasil(
                    extract.extractFichaSinteseBrasilData(
                            fileNameFichaSinteseBrasil, 1,
                            List.of(1, 7),
                            List.of(10, 16),
                            List.of("string", "numeric"))
            );

            List<List<List<List<Object>>>> fichasSintesePaisData = new ArrayList<>();
            for (Integer i = 1; i < service.getSheetNumber(fileNameFichaSintesePais); i++) {
                fichasSintesePaisData.add(extract.extractFichasSintesePaisData(
                        fileNameFichaSintesePais,
                        i,
                        List.of(1, 7),
                        List.of(10, 16),
                        List.of("string", "numeric")));
            }

            List<FichaSintesePaisDTO> fichasSintesePaisDTO = new ArrayList<>();
            for (List<List<List<Object>>> fichaSintesePaisData : fichasSintesePaisData) {
                fichasSintesePaisDTO.add(transform.transformFichasSintesePais(fichaSintesePaisData));
            }

            List<List<List<List<Object>>>> fichasSinteseEstadosData = new ArrayList<>();
            for (Integer i = 1; i < service.getSheetNumber(fileNameFichaSinteseEstado); i++) {
                fichasSinteseEstadosData.add(extract.extractFichasSinteseEstadoData(
                        fileNameFichaSinteseEstado,
                        i,
                        List.of(1, 7),
                        List.of(10, 16),
                        List.of("string", "numeric")));
            }

            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = new ArrayList<>();
            for (List<List<List<Object>>> fichaSinteseEstadoData : fichasSinteseEstadosData) {
                fichasSinteseEstadoDTO.add(transform.transformFichasSinteseEstado(fichaSinteseEstadoData));
            }
            slackNotifier.sendNotification("[WiseTour] :clipboard: Fichas processadas!");

            // 6. PROCESSAMENTO DE PERFIS
            slackNotifier.sendNotification("[WiseTour] :busts_in_silhouette: Gerando perfis de turistas...");
            List<PerfilDTO> perfisEstimadosTuristas;
            Perfil_Estimado_TuristasDAO perfilEstimadoTuristasDAO = new Perfil_Estimado_TuristasDAO(connection);
            Perfil_Estimado_Turista_FonteDAO perfilEstimadoTuristaFonteDAO = new Perfil_Estimado_Turista_FonteDAO(connection);
            PaisDAO paisDAO = new PaisDAO(connection);
            Unidade_Federativa_BrasilDAO unidadeFederativaBrasilDAO = new Unidade_Federativa_BrasilDAO(connection);

            List<Object[]> batchArgs = new ArrayList<>();
            List<Integer> fkPaisesDoLote = new ArrayList<>();
            List<Object[]> batchFonteArgs = new ArrayList<>();
            int batchMax = 10000;
            int totalPerfis = 0;

            for (ChegadaTuristasInternacionaisBrasilMensalDTO chegadaTuristasInternacionaisBrasilMensalDTO : chegadasTuristasInternacionaisBrasilMensalDTO) {
                perfisEstimadosTuristas = transform.transformPerfis(
                        chegadaTuristasInternacionaisBrasilMensalDTO,
                        chegadasTuristasInternacionaisBrasilMensalDTO,
                        fichasSinteseEstadoDTO,
                        fichasSintesePaisDTO,
                        fichaSinteseBrasilDTO
                );

                totalPerfis += perfisEstimadosTuristas.size();

                for (PerfilDTO perfil : perfisEstimadosTuristas) {
                    int fkPais = paisDAO.getId(perfil.getPaisesOrigem());
                    String fkUf = unidadeFederativaBrasilDAO.getId(perfil.getEstadoEntrada());

                    Object[] params = new Object[]{
                            fkPais,
                            fkUf,
                            perfil.getAno(),
                            perfil.getMes(),
                            perfil.getQuantidadeTuristas(),
                            perfil.getGeneroDTO(),
                            perfil.getFaixaEtariaDTO(),
                            perfil.getViaAcesso(),
                            perfil.getComposicaoGruposViagem(),
                            perfil.getFonteInformacao(),
                            perfil.getUtilizacaoAgenciaViagemDTO(),
                            perfil.getMotivo(),
                            perfil.getMotivacaoViagemLazer(),
                            perfil.getGastosMedioPerCapitaMotivo()
                    };

                    batchArgs.add(params);
                    fkPaisesDoLote.add(fkPais);

                    if (batchArgs.size() >= batchMax) {
                        load.loadPerfis(
                                batchArgs,
                                fkPaisesDoLote,
                                batchFonteArgs,
                                tituloArquivoFonteFichasSinteses
                        );
                        batchArgs.clear();
                        fkPaisesDoLote.clear();
                        batchFonteArgs.clear();
                    }
                }

                if (!batchArgs.isEmpty()) {
                    load.loadPerfis(
                            batchArgs,
                            fkPaisesDoLote,
                            batchFonteArgs,
                            tituloArquivoFonteFichasSinteses
                    );
                    batchArgs.clear();
                    fkPaisesDoLote.clear();
                    batchFonteArgs.clear();
                }
            }
            slackNotifier.sendNotification("[WiseTour] :tada: *PERFIS GERADOS COM SUCESSO!* (" + totalPerfis + " registros)");

            // 7. CONCLUSÃO
            slackNotifier.sendNotification("[WiseTour] :checkered_flag: *PROCESSO FINALIZADO!* Sua dashboard está atualizada! Entre e confira já!");

            logDAO.insertLog(
                    fkFonteChegadas,
                    3,
                    1,
                    "Criação dos perfis finalizada.",
                    LocalDateTime.now(),
                    0,
                    0,
                    "Perfil_Estimado"
            );

        } catch (Exception e) {
            slackNotifier.sendNotification("[WiseTour] :x: *ERRO NO PROCESSO* \nDetalhes: " + e.getMessage());
            logDAO.insertLog(
                    1,
                    1,
                    1,
                    "[WiseTour] Erro ao tentar transforma dados de chegada: " + e.getMessage(),
                    LocalDateTime.now(),
                    0,
                    0,
                    "Fonte_Dados"
            );
            throw e;
        }
    }
}