package tour.wise.etl;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.*;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.etl.extract.Extract;
import tour.wise.etl.extract.S3ExcelReader;
import tour.wise.etl.load.Load;
import tour.wise.etl.extract.S3ExcelReader;


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
    S3ExcelReader s3Reader;
    String caminhoChegadaTurista;
    String caminhoFichaSinteseBrasil;
    String caminhoArquivoFichaSintesePaises;
    String caminhoArquivoFichaSinteseEstado;

    public ETL(String bucketname, String caminhoChegadaTurista, String caminhoFichaSinteseBrasil, String caminhoArquivoFichaSintesePaises, String caminhoArquivoFichaSinteseEstado) {
        this.dataBase = new DataBase();
        this.connection = dataBase.getConnection();;
        this.logDAO = new LogDAO(connection);
        this.s3Reader = new S3ExcelReader(bucketname);
        this.extract = new Extract(connection, bucketname, caminhoChegadaTurista, caminhoFichaSinteseBrasil, caminhoArquivoFichaSintesePaises);
        this.transform = new tour.wise.etl.transform.Transform(connection);
        this.load = new Load(connection);
        this.service = new Service(s3Reader);
        this.caminhoChegadaTurista = caminhoChegadaTurista;
        this.caminhoFichaSinteseBrasil = caminhoFichaSinteseBrasil;
        this.caminhoArquivoFichaSintesePaises = caminhoArquivoFichaSintesePaises;
        this.caminhoArquivoFichaSinteseEstado = caminhoArquivoFichaSinteseEstado;
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

            // CARREGAMENTO DA FONTE DAS CHEGADAS NA TABELA FONTE_DADOS

            load.loadFonte(tituloArquivoFonteChegadas,
                    edicaoChegadas,
                    orgaoEmissorChegadas,
                    urlChegadas,
                    null);

            Fonte_DadosDAO fonteDadosDAO = new Fonte_DadosDAO(connection);

            Integer fkFonteChegadas = fonteDadosDAO.getId(tituloArquivoFonteChegadas);

            // EXTRAÇÃO DAS CHEGADAS

            List<List<Object>> chegadasTuristasInternacionaisBrasilMensalData = extract.extractChegadasTuristasInternacionaisBrasilMensalData(
                    fkFonteChegadas,
                    "Chegada_Turistas_Internacionais_Brasil_Mensal",
                    fileNameChegadas,
                    0,
                    0,
                    12,
                    List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric")
            );

            // TRANSFORMAÇÃO DAS CHEGADAS

            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO =
                    transform.transformChegadasTuristasInternacionaisBrasilMensal(
                            chegadasTuristasInternacionaisBrasilMensalData,
                            orgaoEmissorChegadas,
                            edicaoChegadas);



            // CARREGAMENTO OS PAÍSES PRESENTES NA BASE CHEGADAS NO BANCO, CASO AINDA NÃO PERSISTAM

            load.loadPais(chegadasTuristasInternacionaisBrasilMensalDTO);

            // CARREGAMENTO DA FONTE DAS FICHAS SÍNTESE

            load.loadFonte(
                    tituloArquivoFonteFichasSinteses,
                    edicaoFichasSinteses,
                    orgaoEmissorFichasSinteses,
                    urlFichasSinteses,
                    null
            );


            // EXTRAÇÃO E TRASNFORMAÇÃO DA FICHA SÍNTESE BRASIL

            FichaSinteseBrasilDTO fichaSinteseBrasilDTO = transform.transformFichaSinteseBrasil(
                    extract.extractFichaSinteseBrasilData(
                            fileNameFichaSinteseBrasil, 1,
                            List.of(1, 7),
                            List.of(10, 16),
                            List.of("string", "numeric"))
            );

            // EXTRAÇÃO DAS FICHAS SÍNTESE POR PAÍS

            List<List<List<List<Object>>>> fichasSintesePaisData = new ArrayList<>();

            for (Integer i = 1; i < service.getSheetNumber(fileNameFichaSintesePais); i++) {
                fichasSintesePaisData.add(extract.extractFichasSintesePaisData(
                        fileNameFichaSintesePais,
                        i,
                        List.of(1, 7),
                        List.of(10, 16),
                        List.of("string", "numeric")));
            }

            // TRANFORMAÇÃO DAS FICHAS SÍNTESE POR PAÍS

            List<FichaSintesePaisDTO> fichasSintesePaisDTO = new ArrayList<>();

            for (List<List<List<Object>>> fichaSintesePaisData : fichasSintesePaisData) {
                fichasSintesePaisDTO.add(transform.transformFichasSintesePais(fichaSintesePaisData));
            }

            // EXTRAÇÃO DAS FICHAS SÍNTESE POR ESTADO

            List<List<List<List<Object>>>> fichasSinteseEstadosData = new ArrayList<>();

            for (Integer i = 1; i < service.getSheetNumber(fileNameFichaSinteseEstado); i++) {
                fichasSinteseEstadosData.add(extract.extractFichasSinteseEstadoData(
                        fileNameFichaSinteseEstado,
                        i,
                        List.of(1, 7),
                        List.of(10, 16),
                        List.of("string", "numeric")));
            }

            // TRANFORMAÇÃO DAS FICHAS SÍNTESE POR ESTADO

            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = new ArrayList<>();

            for (List<List<List<Object>>> fichaSinteseEstadoData : fichasSinteseEstadosData) {
                fichasSinteseEstadoDTO.add(transform.transformFichasSinteseEstado(fichaSinteseEstadoData));
            }

            // TRANSFORMAÇÃO E CARREGAMENTO DOS perfis

            List<PerfilDTO> perfisEstimadosTuristas;

            Perfil_Estimado_TuristasDAO perfilEstimadoTuristasDAO = new Perfil_Estimado_TuristasDAO(connection);
            Perfil_Estimado_Turista_FonteDAO perfilEstimadoTuristaFonteDAO = new Perfil_Estimado_Turista_FonteDAO(connection);
            PaisDAO paisDAO = new PaisDAO(connection);
            Unidade_Federativa_BrasilDAO unidadeFederativaBrasilDAO = new Unidade_Federativa_BrasilDAO(connection);


            List<Object[]> batchArgs = new ArrayList<>();
            List<Integer> fkPaisesDoLote = new ArrayList<>();
            List<Object[]> batchFonteArgs = new ArrayList<>();
            int batchMax = 50000;


            for (ChegadaTuristasInternacionaisBrasilMensalDTO chegadaTuristasInternacionaisBrasilMensalDTO : chegadasTuristasInternacionaisBrasilMensalDTO) {

                // TRANSFORMAÇÃO

                perfisEstimadosTuristas = transform.transformPerfis(
                        chegadaTuristasInternacionaisBrasilMensalDTO,
                        chegadasTuristasInternacionaisBrasilMensalDTO,
                        fichasSinteseEstadoDTO,
                        fichasSintesePaisDTO,
                        fichaSinteseBrasilDTO
                );

                // CARREGAMENTO

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

                // Inserir o restante fora do loop
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

                // Imprime no console
                System.out.println(LocalDateTime.now() + "[FIM] Criação e inserção dos perfis finalizada.");

                // Registra no log
                logDAO.insertLog(
                        fkFonteChegadas,  // fk_fonte (ajuste conforme necessário)
                        3,  // Categoria: Sucesso
                        1,  // Etapa: Extração (ajuste conforme necessário, ou utilize a etapa correta)
                        "Criação dos perfis finalizada.",
                        LocalDateTime.now(),
                        0,  // Quantidade lida (ou ajuste conforme necessário)
                        0,  // Quantidade inserida (ou ajuste conforme necessário)
                        "Perfil_Estimado"
                );

            }
        } catch (Exception e) {
            // Log no banco
            logDAO.insertLog(
                    1,
                    1, // Erro
                    1,
                    "Erro ao tentar transforma dados de chegada: " + e.getMessage(),
                    LocalDateTime.now(),
                    0,
                    0,
                    "Fonte_Dados"
            );
            throw e;
        }
 
    }


}
