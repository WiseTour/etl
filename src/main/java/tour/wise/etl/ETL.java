package tour.wise.etl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.*;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.perfil.DestinoDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.etl.extract.Extract;
import tour.wise.etl.load.Load;
import tour.wise.model.Destino;
import tour.wise.model.OrigemDados;
import tour.wise.model.Pais;
import tour.wise.model.UnidadeFederativaBrasil;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ETL {

    private final DataBase dataBase;
    private final JdbcTemplate connection;
    private LogDAO logDAO;
    private final Extract extract;
    private tour.wise.etl.transform.Transform transform;
    private Load load;
    private Service service;


    public ETL() throws SQLException {
        this.dataBase = new DataBase();
        this.connection = dataBase.getJdbcTemplate();;
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
    ) throws IOException, SQLException {

        try {
            // CARREGAMENTO DA FONTE DAS CHEGADAS NA TABELA FONTE_DADOS
            load.loadOrigemDados(new OrigemDados(tituloArquivoFonteChegadas, edicaoChegadas, orgaoEmissorChegadas));
            dataBase.getConnection().commit();

            OrigemDadosDAO fonteDadosDAO = new OrigemDadosDAO(connection);
            Integer fkFonteChegadas = (fonteDadosDAO.findByTitulo(tituloArquivoFonteChegadas).getIdOrigemDados() == null) ? 0 : 1;

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

            chegadasTuristasInternacionaisBrasilMensalData.clear();

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

            // EXTRAÇÃO E TRANSFORMAÇÃO DA FICHA SÍNTESE BRASIL
            FichaSinteseBrasilDTO fichaSinteseBrasilDTO = transform.transformFichaSinteseBrasil(
                    extract.extractFichaSinteseBrasilData(
                            fileNameFichaSinteseBrasil, 1,
                            List.of(1, 7),
                            List.of(10, 16),
                            List.of("string", "numeric"))
            );

            // EXTRAÇÃO E TRANSFORMAÇÃO DAS FICHAS SÍNTESE POR PAÍS
            List<FichaSintesePaisDTO> fichasSintesePaisDTO = new ArrayList<>();

            System.out.printf(LocalDateTime.now() + "\n Iniciando leitura do arquivo %s\n%n", fileNameFichaSintesePais);
            Workbook workbook = service.loadWorkbook(fileNameFichaSintesePais);

            for (Integer indicePlanilha = 1; indicePlanilha < service.getSheetNumber(fileNameFichaSintesePais); indicePlanilha++) {
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
            System.out.println(LocalDateTime.now() + "\n Leitura finalizada\n");

            // EXTRAÇÃO E TRANSFORMAÇÃO DAS FICHAS SÍNTESE POR ESTADO
            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = new ArrayList<>();
            System.out.printf(LocalDateTime.now() + "\n Iniciando leitura do arquivo %s\n%n", fileNameFichaSinteseEstado);

            workbook = service.loadWorkbook(fileNameFichaSinteseEstado);
            for (Integer indicePlanilha = 1; indicePlanilha < service.getSheetNumber(fileNameFichaSinteseEstado); indicePlanilha++) {
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
            System.out.println(LocalDateTime.now() + "\n Leitura finalizada\n");

            // PREPARAR MAPAS PARA ACESSO RÁPIDO A PAÍSES E UFs
            PaisDAO paisDAO = new PaisDAO(connection);
            UnidadeFederativaBrasilDAO unidadeFederativaBrasilDAO = new UnidadeFederativaBrasilDAO(connection);

            paises = paisDAO.findAll();
            List<UnidadeFederativaBrasil> ufs = unidadeFederativaBrasilDAO.findAll();

            Map<String, Integer> mapaPaises = paises.stream()
                    .collect(Collectors.toMap(p -> p.getNomePais().toLowerCase(), Pais::getIdPais));

            Map<String, String> mapaUfs = ufs.stream()
                    .collect(Collectors.toMap(uf -> uf.getUnidadeFederativa().toLowerCase(), UnidadeFederativaBrasil::getSigla));

            // INICIANDO PROCESSAMENTO DOS PERFIS
            List<Object[]> batchArgs = new ArrayList<>();
            List<Integer> fkPaisesDoLote = new ArrayList<>();
            List<String> fkUfsDoLote = new ArrayList<>();
            List<Object[]> batchFonteArgs = new ArrayList<>();
            int batchMax = 10000;

            System.out.println(LocalDateTime.now() + " [INÍCIO] Criando perfis...");

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


                    // Aqui: se você usa destinosDoLote, deve decidir como tratar batch deles

                    batchArgs.add(params);
                    fkPaisesDoLote.add(fkPais);
                    fkUfsDoLote.add(fkUf);

                    if (batchArgs.size() >= batchMax) {
                        System.out.println(LocalDateTime.now() + " Inserindo perfis.");

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
                }

                iterator.remove();
            }

            if (!batchArgs.isEmpty()) {
                System.out.println(LocalDateTime.now() + " Inserindo perfis.");

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

            System.out.println(LocalDateTime.now() + " [FIM] Criação e inserção dos perfis finalizada.");

            dataBase.getConnection().close();

        } catch (Exception e) {
            // Log no banco (descomente se desejar usar)
//        logDAO.insertLog(
//                 "erro",
//                 "Erro na criação dos perfis do projeto",
//                 "exe",
//                 e.getMessage(),
//                 null
//        );
            e.printStackTrace();
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

