package tour.wise.etl;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.*;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.dto.perfil.PerfilDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ETL extends Util {

    Service service = new Service();
    Workbook workbook;
    tour.wise.util.Util util = new tour.wise.util.Util();

    public void createLoadPerfies(
            JdbcTemplate connection,
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

        Fonte_DadosDAO fonteDadosDAO = new Fonte_DadosDAO(connection);

        fonteDadosDAO.insertIgnore(
                tituloArquivoFonteFichasSinteses,
                edicaoFichasSinteses,
                orgaoEmissorFichasSinteses,
                urlFichasSinteses,
                null

        );

        // EXTRACT AND TRANSFORM

        // CHEGADAS
        try {
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO =
                    extractTransformChegadasTuristasInternacionaisBrasilMensal(
                            fileNameChegadas,
                            0,
                            0,
                            12,
                            List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric"),
                            orgaoEmissorChegadas,
                            edicaoChegadas

                    );

            fonteDadosDAO.insertIgnore(
                    tituloArquivoFonteChegadas,
                    edicaoChegadas,
                    orgaoEmissorChegadas,
                    urlChegadas,
                    null

            );

            Set<String> paisesUnicos = chegadasTuristasInternacionaisBrasilMensalDTO.stream()
                    .map(ChegadaTuristasInternacionaisBrasilMensalDTO::getPaisOrigem)  // Mapeia os países
                    .collect(Collectors.toSet());  // Coleta em um Set, que não permite repetição

            PaisDAO paisDAO = new PaisDAO(connection);

            paisesUnicos.forEach(pais -> {
                paisDAO.insertIgnore(pais); // Chama o método insertIgnorePais para cada país
            });

            Chegada_Turistas_Internacionais_Brasil_MensalDAO chegadaTuristasInternacionaisBrasilMensalDAO = new Chegada_Turistas_Internacionais_Brasil_MensalDAO(connection);

            Unidade_Federativa_BrasilDAO unidadeFederativaBrasilDAO = new Unidade_Federativa_BrasilDAO(connection);

            int BATCH_SIZE = 10000;

            List<Object[]> batchArgs = new ArrayList<>();

            for (ChegadaTuristasInternacionaisBrasilMensalDTO chegada : chegadasTuristasInternacionaisBrasilMensalDTO) {
                int mes = chegada.getMes();
                int ano = chegada.getAno();
                int qtd = chegada.getQtdChegadas();
                String via = chegada.getViaAcesso();
                String uf = chegada.getUfDestino();
                String pais = chegada.getPaisOrigem();

                int idFonte = fonteDadosDAO.getId(tituloArquivoFonteChegadas);
                int idPais = paisDAO.getId(pais);
                String siglaUf = unidadeFederativaBrasilDAO.getId(uf);

                // Valida se a chegada existe ou é inválida
                if (qtd <= 0 || chegadaTuristasInternacionaisBrasilMensalDAO.hasChegadaMensal(mes, ano, siglaUf, idFonte, idPais)) {
                    continue;
                }

                batchArgs.add(new Object[]{mes, ano, qtd, via, siglaUf, idFonte, idPais});

                // Executa o batch a cada 1000 registros
                if (batchArgs.size() == BATCH_SIZE) {
                    chegadaTuristasInternacionaisBrasilMensalDAO.insertLote(batchArgs);
                    batchArgs.clear();
                }
            }

            // Insere os que sobraram (menos de 1000)
            if (!batchArgs.isEmpty()) {
                chegadaTuristasInternacionaisBrasilMensalDAO.insertLote(batchArgs);
            }

            // FICHAS SÍNTESE
            FichaSinteseBrasilDTO fichasSinteseBrasilDTO = extractTransformFichaSinteseBrasil(fileNameFichaSinteseBrasil, 4);
            List<FichaSintesePaisDTO> fichasSintesePaisDTO = extractTransformFichasSintesePais(fileNameFichaSintesePais, 4);
            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = extractTransformFichasSinteseEstado(fileNameFichaSinteseEstado, 4);

            // CRIAÇÃO DOS PERFIES
            List<PerfilDTO> perfiesEstimadoTuristas = new ArrayList<>();

            Integer i = 1;

            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilAnualDTO =
                    chegadasTuristasInternacionaisBrasilMensalDTO.stream()
                            .collect(Collectors.groupingBy(m -> m.getAno() + "|" + m.getPaisOrigem() + "|" + m.getUfDestino() + "|" + m.getViaAcesso()))
                            .entrySet().stream()
                            .map(entry -> {
                                String[] chavePartes = entry.getKey().split("\\|");
                                Integer ano = Integer.parseInt(chavePartes[0]);
                                String paisOrigem = chavePartes[1];
                                String ufDestino = chavePartes[2];
                                String viaAcesso = chavePartes[3];

                                List<ChegadaTuristasInternacionaisBrasilMensalDTO> grupo = entry.getValue();

                                int totalChegadas = grupo.stream()
                                        .mapToInt(ChegadaTuristasInternacionaisBrasilMensalDTO::getQtdChegadas)
                                        .sum();

                                return new ChegadaTuristasInternacionaisBrasilMensalDTO(
                                        null,
                                        ano,
                                        totalChegadas,
                                        viaAcesso,
                                        ufDestino,
                                        paisOrigem
                                );
                            })
                            .collect(Collectors.toList());



            for (ChegadaTuristasInternacionaisBrasilMensalDTO chegada : chegadasTuristasInternacionaisBrasilAnualDTO) {

                try {
                    System.out.println("chegada - linha: " + i);
                    System.out.println("quantidade de perfies: " + perfiesEstimadoTuristas.size());
                    System.out.println("quantidade de chegadas: " + perfiesEstimadoTuristas.size());
                    System.out.println("chegadas restantes: " + (chegadasTuristasInternacionaisBrasilAnualDTO.size() - i));
                    System.out.println();
                    i++;

                    String paisOrigem = chegada.getPaisOrigem();
                    String ufDestino = chegada.getUfDestino();
                    Integer ano = chegada.getAno();

                    // Primeiro tenta encontrar na ficha estadual

                    Optional<FichaSinteseEstadoDTO> fichaEstadoOptional = fichasSinteseEstadoDTO.stream()
                            .filter(f -> f.getDestinoPrincipal().equalsIgnoreCase(ufDestino)
                                    && f.getAno().equals(ano) && f.getPaisesOrigem().stream()
                                    .anyMatch(p -> p.getPais().equalsIgnoreCase(paisOrigem)))
                            .findFirst();

                    if (fichaEstadoOptional.isPresent()) {

                        FichaSinteseEstadoDTO  fichaEstado = fichaEstadoOptional.get();

                        Double taxaTuristas = fichaEstado.getPaisesOrigem().stream()
                                .filter(p -> p.getPais().equalsIgnoreCase(paisOrigem))
                                .map(PaisOrigemDTO::getPorcentagem)
                                .findFirst()
                                .orElse(null);


                        List<PerfilDTO> perfiesDTOEstado = util.transformFichaSinteseCombinationsCreatePerfilDTO(fichaEstado);

                        for (PerfilDTO perfilDTOEstado : perfiesDTOEstado) {
                            perfilDTOEstado.setPaisesOrigem(paisOrigem);
                            perfilDTOEstado.setEstadoEntrada(ufDestino);
                            perfilDTOEstado.setAno(chegada.getAno());
                            perfilDTOEstado.setMes(chegada.getMes());
                            perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                            perfilDTOEstado.setTaxaTuristas(
                                    perfilDTOEstado.getTaxaTuristas() *
                                            taxaTuristas / 100
                            );
                            Double taxaAtualizada = perfilDTOEstado.getTaxaTuristas();
                            Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * taxaAtualizada)).intValue();
                            perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
                        }

                        perfiesDTOEstado.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || Math.round(perfil.getQuantidadeTuristas()) < 1);

                        perfiesEstimadoTuristas.addAll(perfiesDTOEstado);

                        perfiesDTOEstado.clear();

                        continue;
                    }

                    fichaEstadoOptional = fichasSinteseEstadoDTO.stream()
                            .filter(f -> f.getDestinoPrincipal().equalsIgnoreCase(ufDestino)
                                    && f.getAno().equals(ano))
                            .findFirst();

                    if (fichaEstadoOptional.isPresent()) {

                        FichaSinteseEstadoDTO  fichaEstado = fichaEstadoOptional.get();

                        Double taxaTuristas = 100.00;

                        for (PaisOrigemDTO paisOrigemDTO : fichaEstado.getPaisesOrigem()) {
                            taxaTuristas -= paisOrigemDTO.getPorcentagem();
                        }

                        long quantidadePaises = chegadasTuristasInternacionaisBrasilAnualDTO.stream()
                                .filter(f -> f.getUfDestino().equalsIgnoreCase(ufDestino)
                                        && f.getAno().equals(ano))
                                .count();

                        int quantidadePaisesFichas = fichaEstado.getPaisesOrigem().size();

                        taxaTuristas = taxaTuristas / (quantidadePaises - quantidadePaisesFichas);

                        List<PerfilDTO> perfiesDTOEstado = util.transformFichaSinteseCombinationsCreatePerfilDTO(fichaEstado);

                        for (PerfilDTO perfilDTOEstado : perfiesDTOEstado) {
                            perfilDTOEstado.setPaisesOrigem(paisOrigem);
                            perfilDTOEstado.setEstadoEntrada(ufDestino);
                            perfilDTOEstado.setAno(chegada.getAno());
                            perfilDTOEstado.setMes(chegada.getMes());
                            perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                            perfilDTOEstado.setTaxaTuristas(
                                    perfilDTOEstado.getTaxaTuristas() *
                                            taxaTuristas / 100
                            );
                            Double taxaAtualizada = perfilDTOEstado.getTaxaTuristas();
                            Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * taxaAtualizada)).intValue();
                            perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
                        }

                        perfiesDTOEstado.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || Math.round(perfil.getQuantidadeTuristas()) < 1);

                        perfiesEstimadoTuristas.addAll(perfiesDTOEstado);

                        perfiesDTOEstado.clear();

                        continue;
                    }

                    // Senão, tenta encontrar na ficha do país
                    Optional<FichaSintesePaisDTO> fichaPaisOptional = fichasSintesePaisDTO.stream()
                            .filter(f -> f.getPais().equalsIgnoreCase(paisOrigem)
                                    && f.getAno().equals(ano))
                            .findFirst();

                    if (fichaPaisOptional.isPresent()) {
                        FichaSintesePaisDTO fichaPais = fichaPaisOptional.get();

                        List<PerfilDTO> perfiesDTOPais = util.transformFichaSinteseCombinationsCreatePerfilDTO(fichaPais);

                        for (PerfilDTO perfilDTOEstado : perfiesDTOPais) {
                            perfilDTOEstado.setPaisesOrigem(paisOrigem);
                            perfilDTOEstado.setEstadoEntrada(ufDestino);
                            perfilDTOEstado.setAno(chegada.getAno());
                            perfilDTOEstado.setMes(chegada.getMes());
                            perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                            Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilDTOEstado.getTaxaTuristas() / 100)).intValue();
                            perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
                        }

                        // Remove todos os perfis com quantidadeTuristas < 1
                        perfiesDTOPais.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || Math.round(perfil.getQuantidadeTuristas()) < 1);

                        perfiesEstimadoTuristas.addAll(perfiesDTOPais);

                        perfiesDTOPais.clear();

                        continue;
                    }

                    // Senão, usa a ficha do Brasil
                    FichaSinteseBrasilDTO fichaBrasil = fichasSinteseBrasilDTO;

                    List<PerfilDTO> perfiesDTOBrasil = util.transformFichaSinteseCombinationsCreatePerfilDTO(fichaBrasil);

                    for (PerfilDTO perfilDTOEstado : perfiesDTOBrasil) {
                        perfilDTOEstado.setPaisesOrigem(paisOrigem);
                        perfilDTOEstado.setEstadoEntrada(ufDestino);
                        perfilDTOEstado.setAno(chegada.getAno());
                        perfilDTOEstado.setMes(chegada.getMes());
                        perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                        Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilDTOEstado.getTaxaTuristas() / 100)).intValue();
                        perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
                    }

                    // Remove todos os perfis com quantidadeTuristas < 1
                    perfiesDTOBrasil.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || (int) Math.round(perfil.getQuantidadeTuristas()) < 1);
                    perfiesEstimadoTuristas.addAll(perfiesDTOBrasil);
                    perfiesDTOBrasil.clear();

                } catch (Exception e) {
                    System.err.println("Erro ao processar a chegada de turistas:");
                    System.err.printf("Dados da chegada: País de Origem: %s, UF de Destino: %s, Ano: %d, Mês: %d%n",
                            chegada.getPaisOrigem(), chegada.getUfDestino(), chegada.getAno(), chegada.getMes());

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



            Perfil_Estimado_TuristasDAO perfilEstimadoTuristasDAO = new Perfil_Estimado_TuristasDAO(connection);
            Perfil_Estimado_Turista_FonteDAO perfilEstimadoTuristaFonteDAO = new Perfil_Estimado_Turista_FonteDAO(connection);

            batchArgs = new ArrayList<>();
            List<Integer> fkPaisesDoLote = new ArrayList<>();
            List<Object[]> batchFonteArgs = new ArrayList<>();
            int loteMaximo = 10000;

            for (PerfilDTO perfil : perfiesEstimadoTuristas) {
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

                if (batchArgs.size() == loteMaximo) {
                    List<Integer> idsInseridos = perfilEstimadoTuristasDAO.insertLoteRetornandoIds(batchArgs);
                    int fkFonte = fonteDadosDAO.getId(tituloArquivoFonteFichasSinteses);

                    for (int j = 0; j < idsInseridos.size(); j++) {
                        int fkPerfilEstimado = idsInseridos.get(j);
                        int fkPaisInserido = fkPaisesDoLote.get(j);
                        batchFonteArgs.add(new Object[]{fkFonte, fkPerfilEstimado, fkPaisInserido});
                    }

                    perfilEstimadoTuristaFonteDAO.insertLote(batchFonteArgs);

                    batchArgs.clear();
                    fkPaisesDoLote.clear();
                    batchFonteArgs.clear();
                }
            }

// Inserir o restante fora do loop
            if (!batchArgs.isEmpty()) {
                List<Integer> idsInseridos = perfilEstimadoTuristasDAO.insertLoteRetornandoIds(batchArgs);
                int fkFonte = fonteDadosDAO.getId(tituloArquivoFonteFichasSinteses);

                for (int j = 0; j < idsInseridos.size(); j++) {
                    int fkPerfilEstimado = idsInseridos.get(j);
                    int fkPaisInserido = fkPaisesDoLote.get(j);
                    batchFonteArgs.add(new Object[]{fkFonte, fkPerfilEstimado, fkPaisInserido});
                }

                perfilEstimadoTuristaFonteDAO.insertLote(batchFonteArgs);
            }


        } catch (Exception e) {
            System.err.println("Erro ao processar os dados:");
            System.err.println("Mensagem: " + e.getMessage());
            System.err.println("Tipo de exceção: " + e.getClass().getName());

            // Verifica se há causa
            Throwable cause = e.getCause();
            if (cause != null) {
                System.err.println("Causa raiz: " + cause.getMessage());
                System.err.println("Tipo da causa: " + cause.getClass().getName());

                // Se for SQLException, mostra detalhes adicionais
                if (cause instanceof java.sql.SQLException sqlEx) {
                    System.err.println("Erro SQL: " + sqlEx.getMessage());
                    System.err.println("Código de erro SQL: " + sqlEx.getErrorCode());
                    System.err.println("SQLState: " + sqlEx.getSQLState());
                }
            }

            // Stack trace completo
            e.printStackTrace();
        }




    }


    public List<ChegadaTuristasInternacionaisBrasilMensalDTO> extractTransformChegadasTuristasInternacionaisBrasilMensal(String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types, String fonte, String edicao) throws IOException {

        // EXTRACT

        List<List<Object>> chegadasTuristasInternacionaisBrasilMensalData = extractChegadasTuristasInternacionaisBrasilMensalData(fileName, sheetNumber, header, colluns, types);

        // TRANSFORM

        return transformChegadasTuristasInternacionaisBrasilMensal(chegadasTuristasInternacionaisBrasilMensalData, fonte, edicao);


    }

    public List<List<Object>> extractChegadasTuristasInternacionaisBrasilMensalData(String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types) {

        System.out.println("[INÍCIO] Extração de dados iniciada.");
        System.out.println("[INFO] Arquivo: " + fileName);
        System.out.println("[INFO] Planilha (sheet): " + sheetNumber);
        System.out.println("[INFO] Linha de cabeçalho: " + header);
        System.out.println("[INFO] Quantidade de colunas esperadas: " + colluns);
        System.out.println("[INFO] Tipos esperados: " + types);

        List<List<Object>> data = null;

        try {
            data = service.extract(fileName, sheetNumber, header, colluns, types);

            System.out.println("[SUCESSO] Extração finalizada com sucesso. Total de registros extraídos: " + (data != null ? data.size() : 0));
            System.out.println();

        } catch (Exception e) {
            System.out.println("[ERRO] Falha na extração dos dados: " + e.getMessage());
            e.printStackTrace();
        }



        return data;
    }

    public  List<ChegadaTuristasInternacionaisBrasilMensalDTO> transformChegadasTuristasInternacionaisBrasilMensal(List<List<Object>> data, String fonte, String edicao) {

        System.out.println("[INÍCIO] Transformação dos dados iniciada.");
        System.out.println("[INFO] Fonte: " + fonte + ", Edição: " + edicao);
        System.out.println("[INFO] Total de registros brutos recebidos: " + (data != null ? data.size() : 0));

        List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas_turistas_internacionais_brasil_mensal_dto = new ArrayList<>();

        int linha = 0;
        for (List<Object> datum : data) {
            try {
                linha++;
                String pais_origem = datum.get(2).toString();
                String uf_destino = datum.get(4).toString();
                String via_acesso = datum.get(6).toString();
                Integer ano = Double.valueOf(datum.get(8).toString()).intValue();
                Integer mes = Double.valueOf(datum.get(10).toString()).intValue();
                Integer chegada = Double.valueOf(datum.get(11).toString()).intValue();

                if(chegada > 0){
                    ChegadaTuristasInternacionaisBrasilMensalDTO chegada_turistas_internacionais_brasil_mensal_dto = new ChegadaTuristasInternacionaisBrasilMensalDTO(
                            mes, ano, chegada, via_acesso, uf_destino, pais_origem
                    );

                    chegadas_turistas_internacionais_brasil_mensal_dto.add(chegada_turistas_internacionais_brasil_mensal_dto);
                }

            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao transformar a linha " + linha + ": " + datum);
                e.printStackTrace();
            }
        }

        System.out.println("[FIM] Transformação concluída. Total de registros convertidos: " + chegadas_turistas_internacionais_brasil_mensal_dto.size());

        return chegadas_turistas_internacionais_brasil_mensal_dto;
    }

    public  FichaSinteseBrasilDTO extractTransformFichaSinteseBrasil(String fileName, Integer collun) throws IOException {

        // EXTRACT

        ZipSecureFile.setMinInflateRatio(0.0001);

        workbook = service.loadWorkbook(fileName);

        List<List<List<Object>>> data = extractFichaSinteseBrasilData(
                fileName,
                workbook,
                1,
                List.of(1, 3+collun),
                List.of(10, 12+collun),
                List.of("string", "numeric"));


        workbook.close();

        // TRANSFORM

        FichaSinteseBrasilDTO fichas_sintese_brasil = transformFichaSinteseBrasil(data);

        return fichas_sintese_brasil;

    }

    public  List<List<List<Object>>> extractFichaSinteseBrasilData(String fileName, Workbook workbook, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException {

        // Parâmetros das seções a serem lidas
        List<int[]> ranges = List.of(
                new int[]{5, 5}, // ano
                new int[]{7, 9}, // motivo
                new int[]{11, 16}, // motivação viagem lazer
                new int[]{28, 32}, // composição do grupo turístico
                new int[]{34, 36}, // Gasto médio per capita dia no Brasil
                new int[]{39, 41}, // Permanência média no Brasil
                new int[]{45, 49}, // Destinos mais visitados a lazer
                new int[]{51, 55}, // Destinos mais visitados a negócios, eventos e convenções
                new int[]{57, 61}, // Destinos mais visitados utros motivos
                new int[]{64, 71}, // Fonte de informação
                new int[]{73, 75} // Utilização de agência de viagem
        );

        // Lista para consolidar todos os blocos de dados
        List<List<List<Object>>> data = new ArrayList<>();
        data.add(List.of(List.of("Brasil")));

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            leftColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }

        // Parâmetros das seções a serem lidas
        ranges = List.of(
                new int[]{23, 24}, // Gênero
                new int[]{26, 31} // faixa etária
        );

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            rightColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }


        for (List<List<Object>> datum : data) {
            System.out.println(datum);
        }

        workbook.close();

        return data;
    }

    public FichaSinteseBrasilDTO transformFichaSinteseBrasil(List<List<List<Object>>> data) {
        return new FichaSinteseBrasilDTO(
                transformAno(data, 1),
                transformListGenero(data, 12),
                transformListFaixaEtaria(data, 13),
                transformListComposicoesGrupo(data, 4),
                transformListFontesInformacao(data, 10),
                transformListUtilizacaoAgenciaViagem(data, 11),
                transformListMotivosViagem(data, 2),
                transformListMotivacaoViagemLazer(data, 3),
                transformListGastosMedioMotivo(data, 5),
                transformListPermanenciaMediaMotivo(data, 6),
                transformListDestinosMaisVisitadosPorMotivo(data, 7)


        );
    }

    public List<FichaSintesePaisDTO> extractTransformFichasSintesePais(String fileName, Integer collun) throws IOException {

        // EXTRACT

        workbook = service.loadWorkbook(fileName);

        List<List<List<List<Object>>>> data = new ArrayList<>();
        for(Integer i = 1; i < service.getSheetNumber(fileName); i++ ){
            data.add(extractFichasSintesePaisData(
                    workbook,
                    fileName,
                    i,
                    List.of(1, 3+collun),
                    List.of(10, 12+collun),
                    List.of("string", "numeric")));


        }

        workbook.close();

        // TRANSFORM

        List<FichaSintesePaisDTO> fichas_sintese_por_pais = new ArrayList<>();

        for (List<List<List<Object>>> datum : data) {
            fichas_sintese_por_pais.add(transformFichasSintesePais(datum));
        }


        return fichas_sintese_por_pais;

    }

    public List<List<List<Object>>> extractFichasSintesePaisData(Workbook workbook, String fileName, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException  {

        String sheetName = service.getSheetName(fileName, sheetNumber);
        String pais = sheetName.split("\\s+", 2)[1]; // pais de origem

        // Parâmetros das seções a serem lidas
        List<int[]> ranges = List.of(
                new int[]{5, 5}, // ano
                new int[]{7, 9}, // motivo da viagem
                new int[]{11, 17}, // motivacao da vaigem a lazer
                new int[]{29, 33}, // composicao do grupo turístico
                new int[]{35, 37}, // gasto médio percapita dia no Brasil
                new int[]{40, 42}, // permanencia média no Brasil
                new int[]{46, 50}, // destinos mais visitados a lazer
                new int[]{52, 56}, // destinos mais visitados a negócios, eventos e convenções
                new int[]{58, 62}, // destinos mais visitados outros motivos
                new int[]{69, 76} // fonte de informação
        );

        // Lista para consolidar todos os blocos de dados
        List<List<List<Object>>> data = new ArrayList<>();
        data.add(List.of(List.of(pais)));

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            leftColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }

        // Parâmetros das seções a serem lidas
        ranges = List.of(
                new int[]{7, 9}, // utilização de agência de viagem
                new int[]{27, 28}, // gênero
                new int[]{30, 36} // faixa etária
        );

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            rightColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }


        for (List<List<Object>> datum : data) {
            System.out.println(datum);
        }

        workbook.close();


        return data;
    }

    public FichaSintesePaisDTO transformFichasSintesePais(List<List<List<Object>>> data) {
        return new FichaSintesePaisDTO(
                transformAno(data, 1),
                transformListGenero(data, 12),
                transformListFaixaEtaria(data, 13),
                transformListComposicoesGrupo(data, 4),
                transformListFontesInformacao(data, 10),
                transformListUtilizacaoAgenciaViagem(data, 11),
                transformListMotivosViagem(data, 2),
                transformListMotivacaoViagemLazer(data, 3),
                transformListGastosMedioMotivo(data, 5),
                transformListPermanenciaMediaMotivo(data, 6),
                transformListDestinosMaisVisitadosPorMotivo(data, 7),
                extractNomePais(data, 0)
        );
    }


    public List<FichaSinteseEstadoDTO> extractTransformFichasSinteseEstado(String fileName, Integer collun) throws IOException {

        // EXTRACT

        workbook = service.loadWorkbook(fileName);

        List<List<List<List<Object>>>> data = new ArrayList<>();
        for(Integer i = 1; i < service.getSheetNumber(fileName); i++ ){
            data.add(extractFichasSinteseEstadoData(
                    workbook,
                    fileName,
                    i,
                    List.of(1, 3+collun),
                    List.of(10, 12+collun),
                    List.of("string", "numeric")));

        }

        workbook.close();

        // TRANSFORM

        List<FichaSinteseEstadoDTO> fichasSinteseEstadoMacroDTO = new ArrayList<>();

        for (List<List<List<Object>>> datum : data) {
            fichasSinteseEstadoMacroDTO.add(transformFichasSinteseEstado(datum));
        }

        return fichasSinteseEstadoMacroDTO;

    }


    public List<List<List<Object>>> extractFichasSinteseEstadoData(Workbook workbook, String fileName, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException  {

        String sheetName = service.getSheetName(fileName, sheetNumber);
        String estado = sheetName.split("\\s+", 2)[1]; // UF de entrada

        // Parâmetros das seções a serem lidas
        List<int[]> ranges = List.of(
                new int[]{5, 5},  // ano
                new int[]{7, 16}, // país de residência
                new int[]{18, 20}, // motivo da viagem
                new int[]{22, 27}, // motivação da vaigem a lazer
                new int[]{39, 43}, // Composição do grupo turístico
                new int[]{45, 47}, //Gasto médio per capita dia no Brasil
                new int[]{50, 52}, //Permanência média no Brasil
                new int[]{55, 57}, //Permanência média na UF de entrada
                new int[]{71, 72}, // Destinos mais visitados de outras UFs - Lazer
                new int[]{74, 75}, // Destinos mais visitados de outras UFs - Negócios, eventos e convenções
                new int[]{77, 78}, // Destinos mais visitados de outras UFs - Outros motivos
                new int[]{81, 83} // Utilização de agência de viagem
        );

        // Lista para consolidar todos os blocos de dados
        List<List<List<Object>>> data = new ArrayList<>();
        data.add(List.of(List.of(estado)));

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            leftColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }

        // Parâmetros das seções a serem lidas
        ranges = List.of(
                new int[]{7, 14}, // Fonte de informação
                new int[]{32, 33}, // Gênero
                new int[]{35, 40} // Faixa etária
        );

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            rightColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }


        for (List<List<Object>> datum : data) {
            System.out.println(datum);
        }

        workbook.close();

        return data;
    }


    public FichaSinteseEstadoDTO transformFichasSinteseEstado(List<List<List<Object>>> data) {
        return new FichaSinteseEstadoDTO(
                transformAno(data, 1),
                transformListGenero(data, 14),
                transformListFaixaEtaria(data, 15),
                transformListComposicoesGrupo(data, 5),
                transformListFontesInformacao(data, 13),
                transformListUtilizacaoAgenciaViagem(data, 12),
                transformListMotivosViagem(data, 3),
                transformListMotivacaoViagemLazer(data, 4),
                transformListGastosMedioMotivo(data, 6),
                transformListPermanenciaMediaMotivo(data, 7),
                transformListDestinosMaisVisitadosPorMotivo(data, 9),
                trasnformListPaisesOrigem(data, 2),
                trasnformEstado(data, 0),
                transformListPermanenciaMediaMotivo(data, 8)


        );
    }





}
