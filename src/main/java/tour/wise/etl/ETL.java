package tour.wise.etl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.Evento;
import tour.wise.config.ConfigLoader;
import tour.wise.dao.*;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.etl.extract.Extract;
import tour.wise.etl.load.Load;
import tour.wise.etl.transform.Transform;
import tour.wise.model.*;
import tour.wise.s3.S3;
import tour.wise.slack.SlackWiseTour;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class ETL {

    private final DataBase dataBase;
    private final JdbcTemplate connection;
    private final Extract extract;
    private final Transform transform;
    private final Load load;
    private final Service service;
    private final SlackWiseTour slackWiseTour;
    private final S3 s3;


    public ETL() throws IOException, SQLException {
        this.dataBase = new DataBase();
        this.connection = dataBase.getJdbcTemplate();
        this.extract = new Extract(connection);
        this.transform = new tour.wise.etl.transform.Transform(connection);
        this.load = new Load(connection);
        this.service = new Service();
        this.slackWiseTour = new SlackWiseTour(ConfigLoader.get("SLACK_URL"));
        this.s3 = new S3(slackWiseTour);

    }

    public void exe(
            String fileNameChegadas,
            String tituloArquivoFonteChegadas,
            String orgaoEmissorChegadas,
            String edicaoChegadas,
            String fileNameFichaSintesePais,
            String fileNameFichaSinteseBrasil,
            String fileNameFichaSinteseEstado,
            String tituloArquivoFonteFichasSinteses,
            String orgaoEmissorFichasSinteses,
            String edicaoFichasSinteses
    ) throws IOException, SQLException {

        try {

            // 1. INÍCIO DO PROCESSO
            String msg = "PROCESSAMENTO ETL INICIADO COM SUCESSO!";
            Evento.registrarEvento(ELogCategoria.INFO, EEtapa.INCIALIZACAO, msg, dataBase, slackWiseTour, "gear");

            // 2. EXTRAÇÃO DOS DADOS DE CHEGADA INICIADA
            msg = "Extração de dados de chegada iniciada";
            Evento.registrarEvento(ELogCategoria.INFO, EEtapa.INCIALIZACAO, msg, dataBase, slackWiseTour, "mag");

            // CARREGAMENTO DA FONTE DAS CHEGADAS NA TABELA FONTE_DADOS
            load.loadOrigemDados(new OrigemDados(tituloArquivoFonteChegadas, edicaoChegadas, orgaoEmissorChegadas));
            dataBase.getConnection().commit();

            OrigemDadosDAO fonteDadosDAO = new OrigemDadosDAO(connection);
            Integer fkFonteChegadas = (fonteDadosDAO.findByTitulo(tituloArquivoFonteChegadas).getIdOrigemDados() == null) ? 0 : 1;

            Workbook workbook = s3.readFile(fileNameChegadas);
            // EXTRAÇÃO DAS CHEGADAS
            List<List<Object>> chegadasTuristasInternacionaisBrasilMensalData = extract.extractChegadasTuristasInternacionaisBrasilMensalData(
                    workbook,
                    fkFonteChegadas,
                    "Chegada_Turistas_Internacionais_Brasil_Mensal",
                    fileNameChegadas,
                    0,
                    0,
                    12,
                    List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric")
            );
            
            // 2.1 EXTRAÇÃO DOS DADOS DE CHEGADA FINALIZADA
            msg = "Extração de dados de chegada finalizada";
            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.EXTRACAO, msg, dataBase, slackWiseTour, "white_check_mark");
            
            // 3. TRANSFORMAÇÃO DOS DADOS DE CHEGADAS INICIADA

            msg = "Transformação dos dados de chegadas em andamento...";
            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.TRANSFORMACAO, msg, dataBase, slackWiseTour, "arrows_counterclockwise");
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO =
                    transform.transformChegadasTuristasInternacionaisBrasilMensal(
                            chegadasTuristasInternacionaisBrasilMensalData,
                            orgaoEmissorChegadas,
                            edicaoChegadas);

            // 3.1. TRANSFORMAÇÃO DOS DADOS DE CHEGADAS FINALIZADA
            msg = "Dados de chegadas transformados com sucesso!";
            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.TRANSFORMACAO, msg, dataBase, slackWiseTour, "sparkles");

            chegadasTuristasInternacionaisBrasilMensalData.clear();

            // 4. PROCESSAMENTO DAS FICHAS SÍNTESE
            msg = "Dados de chegadas transformados com sucesso!";
            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.TRANSFORMACAO, msg, dataBase, slackWiseTour, "page_facing_up");

            msg = "Processando fichas síntese...";
            Evento.registrarEvento(ELogCategoria.INFO, EEtapa.EXTRACAO, msg, dataBase, slackWiseTour, "sparkles");

            // CARREGAMENTO OS PAÍSES PRESENTES NA BASE CHEGADAS NO BANCO, CASO AINDA NÃO PERSISTAM
            List<Pais> paises = listarPaisesNaoCadastrados(chegadasTuristasInternacionaisBrasilMensalDTO, new PaisDAO(connection).findAll());

            if (!paises.isEmpty()) {
                for (Pais pais : paises) {
                    load.loadPais(pais);
                }
                dataBase.getConnection().commit();
            }

            // CARREGAMENTO DA FONTE DAS FICHAS SÍNTESE
            load.loadOrigemDados(new OrigemDados(tituloArquivoFonteFichasSinteses, edicaoFichasSinteses, orgaoEmissorFichasSinteses));
            dataBase.getConnection().commit();

            workbook = s3.readFile(fileNameFichaSinteseBrasil);
            // EXTRAÇÃO E TRANSFORMAÇÃO DA FICHA SÍNTESE BRASIL
            FichaSinteseBrasilDTO fichaSinteseBrasilDTO = transform.transformFichaSinteseBrasil(
                    extract.extractFichaSinteseBrasilData(
                            workbook,
                            fileNameFichaSinteseBrasil, 1,
                            List.of(1, 7),
                            List.of(10, 16),
                            List.of("string", "numeric"))
            );

            // EXTRAÇÃO E TRANSFORMAÇÃO DAS FICHAS SÍNTESE POR PAÍS
            List<FichaSintesePaisDTO> fichasSintesePaisDTO = new ArrayList<>();

            System.out.printf(LocalDateTime.now() + "\n Iniciando leitura do arquivo %s\n%n", fileNameFichaSintesePais);
            workbook = s3.readFile(fileNameFichaSintesePais);

            for (Integer indicePlanilha = 1; indicePlanilha < service.getSheetNumber(workbook); indicePlanilha++) {
                fichasSintesePaisDTO.add(transform.transformFichasSintesePais(
                        extract.extractFichasSintesePaisData(
                                workbook,
                                fileNameFichaSintesePais,
                                indicePlanilha,
                                List.of(1, 7),
                                List.of(10, 16),
                                List.of("string", "numeric"))
                ));
            }
            workbook.close();
            

            // EXTRAÇÃO E TRANSFORMAÇÃO DAS FICHAS SÍNTESE POR ESTADO
            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = new ArrayList<>();
            System.out.printf(LocalDateTime.now() + "\n Iniciando leitura do arquivo %s\n%n", fileNameFichaSinteseEstado);

            workbook = s3.readFile(fileNameFichaSinteseEstado);
            for (Integer indicePlanilha = 1; indicePlanilha < service.getSheetNumber(workbook); indicePlanilha++) {
                fichasSinteseEstadoDTO.add(transform.transformFichasSinteseEstado(
                        extract.extractFichasSinteseEstadoData(
                                workbook,
                                fileNameFichaSinteseEstado,
                                indicePlanilha,
                                List.of(1, 7),
                                List.of(10, 16),
                                List.of("string", "numeric"))
                ));
            }
            workbook.close();

            msg = "Fichas processadas!";
            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.EXTRACAO, msg, dataBase, slackWiseTour, "clipboard");

            // PREPARAR MAPAS PARA ACESSO RÁPIDO A PAÍSES E UFs
            PaisDAO paisDAO = new PaisDAO(connection);
            UnidadeFederativaBrasilDAO unidadeFederativaBrasilDAO = new UnidadeFederativaBrasilDAO(connection);

            paises = paisDAO.findAll();
            List<UnidadeFederativaBrasil> ufs = unidadeFederativaBrasilDAO.findAll();

            Map<String, Integer> mapaPaises = paises.stream()
                    .collect(Collectors.toMap(p -> p.getNomePais().toLowerCase(), Pais::getIdPais));

            Map<String, String> mapaUfs = ufs.stream()
                    .collect(Collectors.toMap(uf -> uf.getUnidadeFederativa().toLowerCase(), UnidadeFederativaBrasil::getSigla));


            List<Object[]> batchArgs = new ArrayList<>();
            List<Integer> fkPaisesDoLote = new ArrayList<>();
            List<String> fkUfsDoLote = new ArrayList<>();
            List<Object[]> batchFonteArgs = new ArrayList<>();
            int batchMax = 10000;

            // 5. PROCESSAMENTO DE PERFIS

            msg = "Gerando perfis de turistas...";
            Evento.registrarEvento(ELogCategoria.INFO, EEtapa.TRANSFORMACAO, msg, dataBase, slackWiseTour, "busts_in_silhouette");

            msg = "[INÍCIO] Criando perfis...";
            Evento.registrarEvento(ELogCategoria.INFO, EEtapa.TRANSFORMACAO, msg, dataBase, slackWiseTour, "busts_in_silhouette");

            Iterator<ChegadaTuristasInternacionaisBrasilMensalDTO> iterator = chegadasTuristasInternacionaisBrasilMensalDTO.iterator();

            while (iterator.hasNext()) {

                ChegadaTuristasInternacionaisBrasilMensalDTO chegada = iterator.next();

                // TRANSFORMAÇÃO
                List<PerfilDTO> perfisEstimadosTuristas = transform.transformPerfis(
                        chegada,
                        chegadasTuristasInternacionaisBrasilMensalDTO,
                        fichasSinteseEstadoDTO,
                        fichasSintesePaisDTO,
                        fichaSinteseBrasilDTO
                );

                for (PerfilDTO perfil : perfisEstimadosTuristas) {
                    int fkPais = mapaPaises.getOrDefault(perfil.getPaisesOrigem().toLowerCase(), -1);
                    String fkUf = mapaUfs.getOrDefault(perfil.getEstadoEntrada().toLowerCase(), null);

                    if (fkUf == null || fkPais == -1) {
                        // ignorar perfil inválido e continuar
                        continue;
                    }

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
                            perfil.getMotivo(),
                            perfil.getMotivacaoViagemLazer(),
                            perfil.getGastosMedioPerCapitaMotivo()
                    };

                    batchArgs.add(params);
                    fkPaisesDoLote.add(fkPais);
                    fkUfsDoLote.add(fkUf);

                    if (batchArgs.size() >= batchMax) {

                        // 5.1 CARREGANDO DE PERFIS
                        msg = "Inserindo perfis de turistas no banco...";
                        Evento.registrarEvento(ELogCategoria.INFO, EEtapa.CARREGAMENTO, msg, dataBase, slackWiseTour, "busts_in_silhouette");

                        load.loadPerfis(
                                batchArgs,
                                fkPaisesDoLote,
                                fkUfsDoLote,
                                batchFonteArgs,
                                tituloArquivoFonteFichasSinteses
                        );

                        dataBase.getConnection().commit();

                        batchArgs.clear();
                        fkPaisesDoLote.clear();
                        fkUfsDoLote.clear();
                        batchFonteArgs.clear();

                        // 5. PROCESSAMENTO DE PERFIS
                        msg = " Gerando perfis de turistas...";
                        Evento.registrarEvento(ELogCategoria.INFO, EEtapa.TRANSFORMACAO, msg, dataBase, slackWiseTour, "busts_in_silhouette");
                    }
                }

                iterator.remove();
            }

            if (!batchArgs.isEmpty()) {
                msg = "Inserindo perfis de turistas no banco...";
                Evento.registrarEvento(ELogCategoria.INFO, EEtapa.CARREGAMENTO, msg, dataBase, slackWiseTour, "busts_in_silhouette");

                load.loadPerfis(
                        batchArgs,
                        fkPaisesDoLote,
                        fkUfsDoLote,
                        batchFonteArgs,
                        tituloArquivoFonteFichasSinteses
                );

                dataBase.getConnection().commit();

                batchArgs.clear();
                fkPaisesDoLote.clear();
                fkUfsDoLote.clear();
                batchFonteArgs.clear();
            }

            msg = "[FIM] Criação e inserção dos perfis finalizada.";
            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.TRANSFORMACAO, msg, dataBase, slackWiseTour, "checkered_flag");

            // 7. CONCLUSÃO
            msg = "Sua dashboard está atualizada! Entre e confira já!";
            Evento.registrarEvento(ELogCategoria.INFO, EEtapa.FINALIZACAO, msg, dataBase, slackWiseTour, "checkered_flag");

            dataBase.getConnection().close();

        } catch (Exception e) {

            String msg = "Erro para rodar o ETL.";
            Evento.registrarEvento(ELogCategoria.INFO, EEtapa.FINALIZACAO, msg, dataBase, slackWiseTour, "checkered_flag");

            throw e;
        }
    }


    static List<Pais> listarPaisesNaoCadastrados(
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas,
            List<Pais> paisesExistentes
    ) {
        // Mapa para pesquisa rápida: nomes existentes em minúsculo
        Set<String> nomesPaisesExistentes = paisesExistentes.stream()
                .map(p -> p.getNomePais().toLowerCase())
                .collect(Collectors.toSet());

        // Usar lista para acumular e evitar streams intermediários
        List<Pais> paisesNovos = new ArrayList<>();

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegada : chegadas) {
            String paisOrigem = chegada.getPaisOrigem();
            if (paisOrigem != null) {
                String paisOrigemLower = paisOrigem.toLowerCase();
                if (!nomesPaisesExistentes.contains(paisOrigemLower)) {
                    paisesNovos.add(new Pais(paisOrigem));
                    // Opcional: adicionar ao set para evitar repetição de mesmo nome
                    nomesPaisesExistentes.add(paisOrigemLower);
                }
            }
        }
        return paisesNovos;
    }


}
