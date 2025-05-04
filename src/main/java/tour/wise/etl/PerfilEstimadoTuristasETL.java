package tour.wise.etl;

import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.perfil.DestinoDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.etl.fichas.sintese.FichaSinteseBrasilET;
import tour.wise.etl.fichas.sintese.Ficha_Sintese_Estado_ET;
import tour.wise.etl.fichas.sintese.FichaSintesePaisET;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PerfilEstimadoTuristasETL {

    public void exe(String fonte_perfil, String fonte_chegadas, String fileNameFichaSintesePais, String fileNameFichaSinteseBrasil, String fileNameFichaSinteseEstado, String fileNameChegadas) throws IOException {

        // EXTRACT

        // CHEGADAS
        ChegadaTuristasInternacionaisBrasilMensalET chegadaTuristasInternacionaisBrasilMensalET = new ChegadaTuristasInternacionaisBrasilMensalET();

        List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO = chegadaTuristasInternacionaisBrasilMensalET.transform(
                chegadaTuristasInternacionaisBrasilMensalET.extract(
                        fileNameChegadas,
                        0,
                        0,
                        12,
                        List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric")
                ),
                "Ministério do Turismo",
                "2019"
        );

        // FICHA SÍNTESE
        FichaSinteseBrasilET fichaSinteseBrasilET = new FichaSinteseBrasilET();
        FichaSintesePaisET fichaSintesePaisET = new FichaSintesePaisET();
        Ficha_Sintese_Estado_ET fichaSinteseEstadoEt = new Ficha_Sintese_Estado_ET();


        // TRANSFORM

        // FICHAS SÍNTESE

        List<FichaSinteseBrasilDTO> fichasSinteseBrasilDTO = fichaSinteseBrasilET.extractTransform(fileNameFichaSinteseBrasil, 4, 4);
        List<FichaSintesePaisDTO> fichasSintesePaisDTO = fichaSintesePaisET.extractTransformFicha_Sintese_Pais(fileNameFichaSintesePais, 4, 4);
        List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = fichaSinteseEstadoEt.extractTransformFicha_Sintese_Estado(fileNameFichaSinteseEstado, 4, 4);


        // PERFIL
        List<PerfilDTO> perfiesEstimadoTuristas = new ArrayList<>();

        List<PerfilDTO> perfiesDTO;

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegadaTuristasInternacionaisBrasilMensalDTO : chegadasTuristasInternacionaisBrasilMensalDTO) {

            Integer totalChegadas = chegadaTuristasInternacionaisBrasilMensalDTO.getQtdChegadas();
            String paisOrigem = chegadaTuristasInternacionaisBrasilMensalDTO.getPaisOrigem();
            String ufDesembarque = chegadaTuristasInternacionaisBrasilMensalDTO.getUfDestino();
            String viaAcesso = chegadaTuristasInternacionaisBrasilMensalDTO.getViaAcesso();
            Integer ano = chegadaTuristasInternacionaisBrasilMensalDTO.getAno();
            Integer mes = chegadaTuristasInternacionaisBrasilMensalDTO.getMes();

            perfiesDTO = transformPerfiesEstadoDTO(
                    transformFichasSinteseEstadoDTO(fichasSinteseEstadoDTO, ufDesembarque, paisOrigem),
                    fichasSintesePaisDTO,
                    fichasSinteseBrasilDTO
            );

            for (PerfilDTO perfilDTO : perfiesDTO) {
                System.out.println(perfilDTO);
            }

//            if (hasPais(perfiesDTO, paisOrigem) && hasEstado(perfiesDTO, ufDesembarque) && hasAno(perfiesDTO, ano)) {
//                for (PerfilDTO perfilDTO : perfiesDTO) {
//
//                    Integer totalChecagadas = ((Double) (totalChegadas * perfilDTO.getTaxaTuristas())).intValue();
//
//                    if (perfilDTO.getDestinosDTO().getFirst().getDestino().equalsIgnoreCase(ufDesembarque) &&
//                            perfilDTO.getPaisesOrigem().equalsIgnoreCase(paisOrigem) &&
//                            perfilDTO.getAno().equals(ano) && totalChecagadas > 0){
//
//                        perfiesEstimadoTuristas.add(
//                                new PerfilDTO(
//                                        perfilDTO.getTaxaTuristas(),
//                                        totalChecagadas,
//                                        perfilDTO.getPaisesOrigem(),
//                                        perfilDTO.getAno(),
//                                        mes,
//                                        viaAcesso,
//                                        ufDesembarque,
//                                        perfilDTO.getGeneroDTO(),
//                                        perfilDTO.getFaixaEtariaDTO(),
//                                        perfilDTO.getComposicaoGruposViagem(),
//                                        perfilDTO.getFonteInformacao(),
//                                        perfilDTO.getUtilizacaoAgenciaViagemDTO(),
//                                        perfilDTO.getMotivo(),
//                                        perfilDTO.getMotivacaoViagemLazer(),
//                                        perfilDTO.getGastosMedioPerCapitaMotivo(),
//                                        perfilDTO.getDestinosDTO()
//                                )
//                        );
//
//                        System.out.println("Perfil adicionado [" + (perfiesEstimadoTuristas.size() - 1) + "]: " + perfiesEstimadoTuristas.get(perfiesEstimadoTuristas.size() - 1));
//                    }
//                }
//            } else if (fichaSintesePaisDTOHasPais(fichasSintesePaisDTO, paisOrigem) && fichaSintesePaisDTOHasAno(fichasSintesePaisDTO, ano)) {
//                List<PerfilDTO> perfiesOutrosEstados;
//
//                for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {
//                    if (fichaSintesePaisDTO.getPais().equalsIgnoreCase(paisOrigem) && fichaSintesePaisDTO.getAno().equals(ano)) {
//                        perfiesOutrosEstados = transformFichaSintesePais(fichaSintesePaisDTO, ufDesembarque);
//
//                        for (PerfilDTO perfiesOutrosEstado : perfiesOutrosEstados) {
//
//                            Integer totalChecagadas = ((Double) (totalChegadas * perfiesOutrosEstado.getTaxaTuristas())).intValue();
//
//                            if(totalChecagadas > 0){
//                                perfiesEstimadoTuristas.add(
//                                        new PerfilDTO(
//                                                perfiesOutrosEstado.getTaxaTuristas(),
//                                                totalChecagadas,
//                                                perfiesOutrosEstado.getPaisesOrigem(),
//                                                perfiesOutrosEstado.getAno(),
//                                                mes,
//                                                viaAcesso,
//                                                perfiesOutrosEstado.getEstadoEntrada(),
//                                                perfiesOutrosEstado.getGeneroDTO(),
//                                                perfiesOutrosEstado.getFaixaEtariaDTO(),
//                                                perfiesOutrosEstado.getComposicaoGruposViagem(),
//                                                perfiesOutrosEstado.getFonteInformacao(),
//                                                perfiesOutrosEstado.getUtilizacaoAgenciaViagemDTO(),
//                                                perfiesOutrosEstado.getMotivo(),
//                                                perfiesOutrosEstado.getMotivacaoViagemLazer(),
//                                                perfiesOutrosEstado.getGastosMedioPerCapitaMotivo(),
//                                                perfiesOutrosEstado.getDestinosDTO()
//                                        )
//                                );
//
//                                System.out.println("Perfil adicionado [" + (perfiesEstimadoTuristas.size() - 1) + "]: " + perfiesEstimadoTuristas.get(perfiesEstimadoTuristas.size() - 1));
//
//                            }
//
//                        }
//                    }
//                }
//            } else {
//                List<PerfilDTO> perfiesOutrosPaisesDTO;
//
//                for (FichaSinteseBrasilDTO fichaSinteseBrasilDTO : fichasSinteseBrasilDTO) {
//                    if (fichaSinteseBrasilDTO.getAno().equals(ano)) {
//                        perfiesOutrosPaisesDTO = transformFichaSinteseBrasil(fichaSinteseBrasilDTO, ufDesembarque, paisOrigem);
//
//                        for (PerfilDTO perfilOutrosPaisesDTO : perfiesOutrosPaisesDTO) {
//
//                            Integer totalChecagadas = ((Double) (totalChegadas * perfilOutrosPaisesDTO.getTaxaTuristas())).intValue();
//
//                            if(totalChecagadas > 0){
//
//                                perfiesEstimadoTuristas.add(
//                                        new PerfilDTO(
//                                                perfilOutrosPaisesDTO.getTaxaTuristas(),
//                                                totalChecagadas,
//                                                perfilOutrosPaisesDTO.getPaisesOrigem(),
//                                                perfilOutrosPaisesDTO.getAno(),
//                                                mes,
//                                                viaAcesso,
//                                                perfilOutrosPaisesDTO.getEstadoEntrada(),
//                                                perfilOutrosPaisesDTO.getGeneroDTO(),
//                                                perfilOutrosPaisesDTO.getFaixaEtariaDTO(),
//                                                perfilOutrosPaisesDTO.getComposicaoGruposViagem(),
//                                                perfilOutrosPaisesDTO.getFonteInformacao(),
//                                                perfilOutrosPaisesDTO.getUtilizacaoAgenciaViagemDTO(),
//                                                perfilOutrosPaisesDTO.getMotivo(),
//                                                perfilOutrosPaisesDTO.getMotivacaoViagemLazer(),
//                                                perfilOutrosPaisesDTO.getGastosMedioPerCapitaMotivo(),
//                                                perfilOutrosPaisesDTO.getDestinosDTO()
//                                        )
//                                );
//
//                                System.out.println("Perfil adicionado [" + (perfiesEstimadoTuristas.size() - 1) + "]: " + perfiesEstimadoTuristas.get(perfiesEstimadoTuristas.size() - 1));
//
//
//                            }
//
//                            }
//                    }
//                }
//            }
        }
    }


    public List<PerfilDTO> transformFichaSinteseBrasil(FichaSinteseBrasilDTO ficha, String ufDesembarque, String paisOrigem) {
        List<PerfilDTO> perfis = new ArrayList<>();

        // Mapas para acesso rápido
        Map<String, Double> gastosPorMotivo = ficha.getGastosMedioPerCapitaMotivo().stream()
                .collect(Collectors.toMap(GastoMedioPerCapitaMotivoDTO::getMotivo, GastoMedioPerCapitaMotivoDTO::getGasto));

        Map<String, Double> permanenciaPorMotivo = ficha.getPermanenciaMediaDTO().stream()
                .collect(Collectors.toMap(PermanenciaMediaDTO::getMotivo, PermanenciaMediaDTO::getProcentagem));

        for (FonteInformacaoDTO fonte : ficha.getFontesInformacao()) {
            for (ComposicaoGrupoViagemDTO grupo : ficha.getComposicaoGruposViagem()) {
                for (UtilizacaoAgenciaViagemDTO agencia : ficha.getUtilizacaoAgenciaViagemDTO()) {
                    for (GeneroDTO genero : ficha.getGeneroDTO()) {
                        for (FaixaEtariaDTO faixa : ficha.getFaixaEtariaDTO()) {
                            for (MotivoViagemDTO motivo : ficha.getMotivos()) {

                                double pctBase = fonte.getPorcentagem() * grupo.getPorcentagem() *
                                        agencia.getPorcentagem() * genero.getPorcentagem() *
                                        faixa.getPorcentagem() * motivo.getPorcentagem();

                                double gasto = gastosPorMotivo.getOrDefault(motivo.getMotivo(), 0.0);
                                double tempoTotal = permanenciaPorMotivo.getOrDefault(motivo.getMotivo(), 0.0);

                                // Obter destinos mais visitados
                                List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados =
                                        transformDestinosMaisVisitados(ficha.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                if (motivo.getMotivo().equalsIgnoreCase("Lazer")) {
                                    for (MotivacaoViagemLazerDTO motivacao : ficha.getMotivacoesViagemLazer()) {
                                        double pctMotivacao = pctBase * motivacao.getPorcentagem() / 100.0;

                                        // Destino principal
                                        perfis.add(new PerfilDTO(pctMotivacao, paisOrigem, ficha.getAno(),
                                                genero.getGenero(), faixa.getFaixa_etaria(), grupo.getComposicao(),
                                                fonte.getFonte(), agencia.getTipo(), motivo.getMotivo(),
                                                motivacao.getMotivacao(), gasto,
                                                List.of(new DestinoDTO(ufDesembarque, tempoTotal))));

                                        // Destinos adicionais
                                        adicionarComDestinos(perfis, paisOrigem, ficha.getAno(), ufDesembarque,
                                                motivo.getMotivo(), motivacao.getMotivacao(), gasto, tempoTotal,
                                                pctMotivacao, genero, faixa, grupo, fonte, agencia, destinosMaisVisitados);
                                    }
                                } else {
                                    // Sem motivação (não é lazer)
                                    perfis.add(new PerfilDTO(pctBase, paisOrigem, ficha.getAno(),
                                            genero.getGenero(), faixa.getFaixa_etaria(), grupo.getComposicao(),
                                            fonte.getFonte(), agencia.getTipo(), motivo.getMotivo(),
                                            null, gasto,
                                            List.of(new DestinoDTO(ufDesembarque, tempoTotal))));

                                    // Destinos adicionais
                                    adicionarComDestinos(perfis, paisOrigem, ficha.getAno(), ufDesembarque,
                                            motivo.getMotivo(), null, gasto, tempoTotal,
                                            pctBase, genero, faixa, grupo, fonte, agencia, destinosMaisVisitados);
                                }
                            }
                        }
                    }
                }
            }
        }

        return perfis;
    }


    private void adicionarComDestinos(List<PerfilDTO> perfis, String paisOrigem, int ano,
                                      String ufDesembarque, String motivo, String motivacao,
                                      double gasto, double tempoTotal, double pctBase,
                                      GeneroDTO genero, FaixaEtariaDTO faixa, ComposicaoGrupoViagemDTO grupo,
                                      FonteInformacaoDTO fonte, UtilizacaoAgenciaViagemDTO agencia,
                                      List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados) {

        for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {
            if (destinos.isEmpty()) continue;

            double pctFinal = pctBase;
            DestinoMaisVisitadoDTO ultimo = destinos.get(destinos.size() - 1);
            pctFinal *= (ultimo.getPorcentagem() / 100.0);

            List<DestinoDTO> destinosDTO = new ArrayList<>();
            int totalDestinos = destinos.size() + 1;
            double tempoPorDestino = tempoTotal / totalDestinos;

            // Destino principal
            destinosDTO.add(new DestinoDTO(ufDesembarque, tempoPorDestino));

            // Destinos adicionais
            for (DestinoMaisVisitadoDTO destino : destinos) {
                destinosDTO.add(new DestinoDTO(destino.getDestino(), tempoPorDestino));
            }

            // Adiciona o perfil completo
            perfis.add(new PerfilDTO(
                    pctFinal,
                    paisOrigem,
                    ano,
                    genero.getGenero(),
                    faixa.getFaixa_etaria(),
                    grupo.getComposicao(),
                    fonte.getFonte(),
                    agencia.getTipo(),
                    motivo,
                    motivacao,
                    gasto,
                    destinosDTO
            ));
        }
    }



    public List<PerfilDTO> transformFichaSintesePais(FichaSintesePaisDTO dto, String ufDesembarque) {
        List<PerfilDTO> perfis = new ArrayList<>();

        for (FonteInformacaoDTO fonte : dto.getFontesInformacao()) {
            for (ComposicaoGrupoViagemDTO grupo : dto.getComposicaoGruposViagem()) {
                for (UtilizacaoAgenciaViagemDTO agencia : dto.getUtilizacaoAgenciaViagemDTO()) {
                    for (GeneroDTO genero : dto.getGeneroDTO()) {
                        for (FaixaEtariaDTO faixa : dto.getFaixaEtariaDTO()) {
                            for (MotivoViagemDTO motivo : dto.getMotivos()) {

                                double basePct = fonte.getPorcentagem() / 100.0 *
                                        grupo.getPorcentagem() / 100.0 *
                                        agencia.getPorcentagem() / 100.0 *
                                        genero.getPorcentagem() / 100.0 *
                                        faixa.getPorcentagem() / 100.0 *
                                        motivo.getPorcentagem() / 100.0;

                                GastoMedioPerCapitaMotivoDTO gasto = dto.getGastosMedioPerCapitaMotivo().stream()
                                        .filter(g -> g.getMotivo().equalsIgnoreCase(motivo.getMotivo()))
                                        .findFirst().orElse(null);

                                PermanenciaMediaDTO permanencia = dto.getPermanenciaMediaDTO().stream()
                                        .filter(p -> p.getMotivo().equalsIgnoreCase(motivo.getMotivo()))
                                        .findFirst().orElse(null);

                                if (gasto == null || permanencia == null) continue;

                                for (int i = 0; i < 2; i++) {
                                    if (motivo.getMotivo().equalsIgnoreCase("Lazer")) {
                                        for (MotivacaoViagemLazerDTO motivacao : dto.getMotivacoesViagemLazer()) {
                                            double pct = basePct * (motivacao.getPorcentagem() / 100.0);

                                            if (i == 0) {
                                                perfis.add(criarPerfil(dto, ufDesembarque, genero, faixa, grupo, fonte, agencia,
                                                        motivo, motivacao.getMotivacao(), gasto, List.of(
                                                                new DestinoDTO(ufDesembarque, permanencia.getProcentagem())
                                                        ), pct));
                                            } else {
                                                adicionarPerfisComDestinos(perfis, dto, motivo, ufDesembarque, permanencia, pct,
                                                        genero, faixa, grupo, fonte, agencia, gasto, motivacao.getMotivacao());
                                            }
                                        }
                                    } else {
                                        if (i == 0) {
                                            perfis.add(criarPerfil(dto, ufDesembarque, genero, faixa, grupo, fonte, agencia,
                                                    motivo, null, gasto, List.of(
                                                            new DestinoDTO(ufDesembarque, permanencia.getProcentagem())
                                                    ), basePct));
                                        } else {
                                            adicionarPerfisComDestinos(perfis, dto, motivo, ufDesembarque, permanencia, basePct,
                                                    genero, faixa, grupo, fonte, agencia, gasto, null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return perfis;
    }

    private void adicionarPerfisComDestinos(List<PerfilDTO> perfis, FichaSintesePaisDTO dto, MotivoViagemDTO motivo,
                                            String ufDesembarque, PermanenciaMediaDTO permanencia, double pct,
                                            GeneroDTO genero, FaixaEtariaDTO faixa, ComposicaoGrupoViagemDTO grupo,
                                            FonteInformacaoDTO fonte, UtilizacaoAgenciaViagemDTO agencia,
                                            GastoMedioPerCapitaMotivoDTO gasto, String motivacao) {

        List<List<DestinoMaisVisitadoDTO>> destinosList = transformDestinosMaisVisitados(
                dto.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

        for (List<DestinoMaisVisitadoDTO> destinos : destinosList) {
            if (destinos.isEmpty()) continue;

            DestinoMaisVisitadoDTO ultimoDestino = destinos.get(destinos.size() - 1);
            double pctFinal = pct * (ultimoDestino.getPorcentagem() / 100.0);

            List<DestinoDTO> destinosDTO = new ArrayList<>();
            int totalDestinos = destinos.size() + 1;

            double tempoMedio = permanencia.getProcentagem() / totalDestinos;

            destinosDTO.add(new DestinoDTO(ufDesembarque, tempoMedio));
            for (DestinoMaisVisitadoDTO d : destinos) {
                destinosDTO.add(new DestinoDTO(d.getDestino(), tempoMedio));
            }

            perfis.add(criarPerfil(dto, ufDesembarque, genero, faixa, grupo, fonte, agencia, motivo,
                    motivacao, gasto, destinosDTO, pctFinal));
        }
    }

    private PerfilDTO criarPerfil(FichaSintesePaisDTO dto, String ufDesembarque,
                                  GeneroDTO genero, FaixaEtariaDTO faixa, ComposicaoGrupoViagemDTO grupo,
                                  FonteInformacaoDTO fonte, UtilizacaoAgenciaViagemDTO agencia,
                                  MotivoViagemDTO motivo, String motivacao,
                                  GastoMedioPerCapitaMotivoDTO gasto, List<DestinoDTO> destinos, double pct) {

        return new PerfilDTO(
                pct,
                dto.getPais(),
                dto.getAno(),
                genero.getGenero(),
                faixa.getFaixa_etaria(),
                grupo.getComposicao(),
                fonte.getFonte(),
                agencia.getTipo(),
                motivo.getMotivo(),
                motivacao,
                gasto.getGasto(),
                destinos
        );
    }


    public List<PerfilDTO> transformPerfiesEstadoDTO(List<PerfilDTO> perfiesEstadoDTO, List<FichaSintesePaisDTO> fichasSintesePaisDTO, List<FichaSinteseBrasilDTO> fichasSinteseBrasilDTO) {
        List<PerfilDTO> perfiesDTO = new ArrayList<>();

        // Mapear fichasSintesePaisDTO por ano e pais para buscar rapidamente durante os loops
        Map<Integer, Map<String, FichaSintesePaisDTO>> fichaPaisMap = fichasSintesePaisDTO.stream()
                .collect(Collectors.groupingBy(FichaSintesePaisDTO::getAno,
                        Collectors.toMap(FichaSintesePaisDTO::getPais, Function.identity())));

        // Mapear fichasSinteseBrasilDTO por ano para otimizar busca
        Map<Integer, List<FichaSinteseBrasilDTO>> fichaBrasilMap = fichasSinteseBrasilDTO.stream()
                .collect(Collectors.groupingBy(FichaSinteseBrasilDTO::getAno));

        for (PerfilDTO perfilEstadoDTO : perfiesEstadoDTO) {
            // Atualiza a porcentagem de turistas para o perfil do estado
            Double porcentagemTuristas = perfilEstadoDTO.getTaxaTuristas();

            // Verificar se a ficha correspondente ao país e ano existe
            FichaSintesePaisDTO fichaPais = fichaPaisMap.get(perfilEstadoDTO.getAno()) != null ?
                    fichaPaisMap.get(perfilEstadoDTO.getAno()).get(perfilEstadoDTO.getPaisesOrigem()) : null;

            if (fichaPais != null) {
                // Processa os dados de fontes de informação
                for (FonteInformacaoDTO fonteInformacaoPaisDTO : fichaPais.getFontesInformacao()) {
                    if (fonteInformacaoPaisDTO.getFonte().equalsIgnoreCase(perfilEstadoDTO.getFonteInformacao())) {
                        porcentagemTuristas *= fonteInformacaoPaisDTO.getPorcentagem() / 100;
                    }

                    // Processa os dados de composição do grupo de viagem
                    for (ComposicaoGrupoViagemDTO composicaoGrupoViagemPaisDTO : fichaPais.getComposicaoGruposViagem()) {
                        if (composicaoGrupoViagemPaisDTO.getComposicao().equalsIgnoreCase(perfilEstadoDTO.getComposicaoGruposViagem())) {
                            porcentagemTuristas *= composicaoGrupoViagemPaisDTO.getPorcentagem() / 100;
                        }

                        // Processa os dados de utilização de agência de viagem
                        for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemPaisDTO : fichaPais.getUtilizacaoAgenciaViagemDTO()) {
                            if (utilizacaoAgenciaViagemPaisDTO.getTipo().equalsIgnoreCase(perfilEstadoDTO.getUtilizacaoAgenciaViagemDTO())) {
                                porcentagemTuristas *= utilizacaoAgenciaViagemPaisDTO.getPorcentagem() / 100;
                            }

                            // Processa os dados de gênero
                            for (GeneroDTO generoPaisDto : fichaPais.getGeneroDTO()) {
                                if (generoPaisDto.getGenero().equalsIgnoreCase(perfilEstadoDTO.getGeneroDTO())) {
                                    porcentagemTuristas *= generoPaisDto.getPorcentagem() / 100;

                                    // Processa os dados de faixa etária
                                    for (FaixaEtariaDTO faixaEtariaPaisDTO : fichaPais.getFaixaEtariaDTO()) {
                                        if (faixaEtariaPaisDTO.getFaixa_etaria().equalsIgnoreCase(perfilEstadoDTO.getFaixaEtariaDTO())) {
                                            porcentagemTuristas *= faixaEtariaPaisDTO.getPorcentagem() / 100;

                                            // Processa os dados de gasto médio per capita
                                            for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoPaisDTO : fichaPais.getGastosMedioPerCapitaMotivo()) {
                                                if (gastoMedioPerCapitaMotivoPaisDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())) {

                                                    // Processa permanência média
                                                    for (PermanenciaMediaDTO permanenciaMediaDTO : fichaPais.getPermanenciaMediaDTO()) {
                                                        if (permanenciaMediaDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())) {

                                                            // Processa motivos de viagem
                                                            for (MotivoViagemDTO motivo : fichaPais.getMotivos()) {
                                                                if (motivo.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())) {
                                                                    porcentagemTuristas *= faixaEtariaPaisDTO.getPorcentagem() / 100;

                                                                    // Caso o motivo seja "Lazer"
                                                                    if (motivo.getMotivo().equalsIgnoreCase("Lazer")) {
                                                                        for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaPais.getMotivacoesViagemLazer()) {
                                                                            if (motivacaoViagemLazerDTO.getMotivacao().equalsIgnoreCase(perfilEstadoDTO.getMotivacaoViagemLazer())) {
                                                                                porcentagemTuristas *= faixaEtariaPaisDTO.getPorcentagem() / 100;

                                                                                // Buscar dados em fichaBrasilMap
                                                                                List<FichaSinteseBrasilDTO> fichaBrasilList = fichaBrasilMap.get(perfilEstadoDTO.getAno());
                                                                                if (fichaBrasilList != null) {
                                                                                    for (FichaSinteseBrasilDTO fichaBrasilDTO : fichaBrasilList) {
                                                                                        for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoBrasilDTO : fichaBrasilDTO.getGastosMedioPerCapitaMotivo()) {
                                                                                            if (gastoMedioPerCapitaMotivoBrasilDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())) {
                                                                                                Double gastoMedioPercapitaEstimado = gastoMedioPerCapitaMotivoPaisDTO.getGasto() *
                                                                                                        (perfilEstadoDTO.getGastosMedioPerCapitaMotivo() / gastoMedioPerCapitaMotivoBrasilDTO.getGasto());

                                                                                                perfiesDTO.add(new PerfilDTO(
                                                                                                        porcentagemTuristas,
                                                                                                        perfilEstadoDTO.getPaisesOrigem(),
                                                                                                        perfilEstadoDTO.getAno(),
                                                                                                        perfilEstadoDTO.getGeneroDTO(),
                                                                                                        perfilEstadoDTO.getFaixaEtariaDTO(),
                                                                                                        perfilEstadoDTO.getComposicaoGruposViagem(),
                                                                                                        perfilEstadoDTO.getFonteInformacao(),
                                                                                                        perfilEstadoDTO.getUtilizacaoAgenciaViagemDTO(),
                                                                                                        perfilEstadoDTO.getMotivo(),
                                                                                                        perfilEstadoDTO.getMotivacaoViagemLazer(),
                                                                                                        gastoMedioPercapitaEstimado,
                                                                                                        perfilEstadoDTO.getDestinosDTO()));
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        // Caso não seja Lazer
                                                                        List<FichaSinteseBrasilDTO> fichaBrasilList = fichaBrasilMap.get(perfilEstadoDTO.getAno());
                                                                        if (fichaBrasilList != null) {
                                                                            for (FichaSinteseBrasilDTO fichaBrasilDTO : fichaBrasilList) {
                                                                                for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoBrasilDTO : fichaBrasilDTO.getGastosMedioPerCapitaMotivo()) {
                                                                                    if (gastoMedioPerCapitaMotivoBrasilDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())) {
                                                                                        Double gastoMedioPercapitaEstimado = gastoMedioPerCapitaMotivoPaisDTO.getGasto() *
                                                                                                (perfilEstadoDTO.getGastosMedioPerCapitaMotivo() / gastoMedioPerCapitaMotivoBrasilDTO.getGasto());

                                                                                        perfiesDTO.add(new PerfilDTO(
                                                                                                porcentagemTuristas,
                                                                                                perfilEstadoDTO.getPaisesOrigem(),
                                                                                                perfilEstadoDTO.getAno(),
                                                                                                perfilEstadoDTO.getGeneroDTO(),
                                                                                                perfilEstadoDTO.getFaixaEtariaDTO(),
                                                                                                perfilEstadoDTO.getComposicaoGruposViagem(),
                                                                                                perfilEstadoDTO.getFonteInformacao(),
                                                                                                perfilEstadoDTO.getUtilizacaoAgenciaViagemDTO(),
                                                                                                perfilEstadoDTO.getMotivo(),
                                                                                                perfilEstadoDTO.getMotivacaoViagemLazer(),
                                                                                                gastoMedioPercapitaEstimado,
                                                                                                perfilEstadoDTO.getDestinosDTO()));
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return perfiesDTO;
    }


    public List<PerfilDTO> transformFichasSinteseEstadoDTO(List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO, String estadoFiltrado, String paisFiltrado) {

        List<PerfilDTO> perfisEstadoDTO = new ArrayList<>();

        for (FichaSinteseEstadoDTO ficha : fichasSinteseEstadoDTO) {

            if (ficha.getDestinoPrincipal().equalsIgnoreCase(estadoFiltrado)) {

                for (PaisOrigemDTO pais : ficha.getPaisesOrigem()) {

                    if (pais.getPais().equalsIgnoreCase(paisFiltrado)) {

                        for (FonteInformacaoDTO fonte : ficha.getFontesInformacao()) {
                            for (ComposicaoGrupoViagemDTO composicao : ficha.getComposicaoGruposViagem()) {
                                for (UtilizacaoAgenciaViagemDTO utilizacao : ficha.getUtilizacaoAgenciaViagemDTO()) {
                                    for (GeneroDTO genero : ficha.getGeneroDTO()) {
                                        for (FaixaEtariaDTO faixa : ficha.getFaixaEtariaDTO()) {
                                            for (MotivoViagemDTO motivo : ficha.getMotivos()) {
                                                for (GastoMedioPerCapitaMotivoDTO gasto : ficha.getGastosMedioPerCapitaMotivo()) {
                                                    if (gasto.getMotivo().equalsIgnoreCase(motivo.getMotivo())) {
                                                        for (PermanenciaMediaDTO permanencia : ficha.getPermanenciaMediaDTO()) {
                                                            if (permanencia.getMotivo().equalsIgnoreCase(motivo.getMotivo())) {
                                                                for (PermanenciaMediaDTO permanenciaDestino : ficha.getPermanenciaMediaDTODestinoPrincipal()) {
                                                                    if (permanenciaDestino.getMotivo().equalsIgnoreCase(motivo.getMotivo())) {

                                                                        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados;
                                                                        if (motivo.getMotivo().equalsIgnoreCase("Lazer")) {
                                                                            for (MotivacaoViagemLazerDTO motivacao : ficha.getMotivacoesViagemLazer()) {

                                                                                double porcentagemTuristas = 1.0;
                                                                                porcentagemTuristas *= pais.getPorcentagem() / 100;
                                                                                porcentagemTuristas *= fonte.getPorcentagem() / 100;
                                                                                porcentagemTuristas *= composicao.getPorcentagem() / 100;
                                                                                porcentagemTuristas *= utilizacao.getPorcentagem() / 100;
                                                                                porcentagemTuristas *= genero.getPorcentagem() / 100;
                                                                                porcentagemTuristas *= faixa.getPorcentagem() / 100;
                                                                                porcentagemTuristas *= motivo.getPorcentagem() / 100;
                                                                                porcentagemTuristas *= motivacao.getPorcentagem() / 100;

//                                                                                // Perfil principal
//                                                                                addPerfilDTO(perfisEstadoDTO, porcentagemTuristas, pais, ficha, genero, faixa, composicao, fonte, utilizacao, motivo, motivacao, gasto, permanencia, permanenciaDestino, null);

                                                                                // Perfis por destinos mais visitados
                                                                                destinosMaisVisitados = transformDestinosMaisVisitados(ficha.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());
                                                                                for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {
                                                                                    double porcentagemComDestinos = porcentagemTuristas * (destinos.get(destinos.size() - 1).getPorcentagem() / 100);
                                                                                    addPerfilDTO(perfisEstadoDTO, porcentagemComDestinos, pais, ficha, genero, faixa, composicao, fonte, utilizacao, motivo, motivacao, gasto, permanencia, permanenciaDestino, destinos);
                                                                                }
                                                                            }
                                                                        } else {
                                                                            double porcentagemTuristas = 1.0;
                                                                            porcentagemTuristas *= pais.getPorcentagem() / 100;
                                                                            porcentagemTuristas *= fonte.getPorcentagem() / 100;
                                                                            porcentagemTuristas *= composicao.getPorcentagem() / 100;
                                                                            porcentagemTuristas *= utilizacao.getPorcentagem() / 100;
                                                                            porcentagemTuristas *= genero.getPorcentagem() / 100;
                                                                            porcentagemTuristas *= faixa.getPorcentagem() / 100;
                                                                            porcentagemTuristas *= motivo.getPorcentagem() / 100;

//                                                                            // Perfil principal
//                                                                            addPerfilDTO(perfisEstadoDTO, porcentagemTuristas, pais, ficha, genero, faixa, composicao, fonte, utilizacao, motivo, null, gasto, permanencia, permanenciaDestino, null);

                                                                            // Perfis por destinos mais visitados
                                                                            destinosMaisVisitados = transformDestinosMaisVisitados(ficha.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                                                            for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {
                                                                                double porcentagemComDestinos = porcentagemTuristas * (destinos.get(destinos.size() - 1).getPorcentagem() / 100);
                                                                                addPerfilDTO(perfisEstadoDTO, porcentagemComDestinos, pais, ficha, genero, faixa, composicao, fonte, utilizacao, motivo, null, gasto, permanencia, permanenciaDestino, destinos);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        return perfisEstadoDTO;
    }


    // Método auxiliar para adicionar os dados na lista
    private void addPerfilDTO(List<PerfilDTO> perfis, double porcentagemTuristas, PaisOrigemDTO pais, FichaSinteseEstadoDTO ficha, GeneroDTO genero, FaixaEtariaDTO faixa, ComposicaoGrupoViagemDTO composicao,
                              FonteInformacaoDTO fonte, UtilizacaoAgenciaViagemDTO utilizacao, MotivoViagemDTO motivo, MotivacaoViagemLazerDTO motivacao, GastoMedioPerCapitaMotivoDTO gasto,
                              PermanenciaMediaDTO permanencia, PermanenciaMediaDTO permanenciaDestino, List<DestinoMaisVisitadoDTO> destinos) {

        List<DestinoDTO> destinosDTO = new ArrayList<>();
        if (destinos != null) {
            destinos.forEach(destino -> {
                destinosDTO.add(new DestinoDTO(destino.getDestino(), destino.getPorcentagem()));
            });
        } else {
            destinosDTO.add(new DestinoDTO(ficha.getDestinoPrincipal(), permanenciaDestino.getProcentagem() * permanencia.getProcentagem() / 2));
        }

        perfis.add(new PerfilDTO(
                porcentagemTuristas,
                pais.getPais(),
                ficha.getAno(),
                genero.getGenero(),
                faixa.getFaixa_etaria(),
                composicao.getComposicao(),
                fonte.getFonte(),
                utilizacao.getTipo(),
                motivo.getMotivo(),
                motivacao != null ? motivacao.getMotivacao() : null,
                gasto != null ? gasto.getGasto() : null,
                destinosDTO
        ));
    }


    public List<List<DestinoMaisVisitadoDTO>> transformDestinosMaisVisitados(
            List<DestinosMaisVisitadosPorMotivoDTO> listaDestinosMaisVisitadosPorMotivoDTO,
            String motivo) {

        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados = new ArrayList<>();

        for (DestinosMaisVisitadosPorMotivoDTO dto : listaDestinosMaisVisitadosPorMotivoDTO) {
            if (motivo.equalsIgnoreCase(dto.getMotivo())) {
                List<DestinoMaisVisitadoDTO> destinos = dto.getDestinos_mais_visistado();
                gerarCombinacoes(destinos, destinosMaisVisitados);
            }
        }

        return destinosMaisVisitados;
    }

    private void gerarCombinacoes(
            List<DestinoMaisVisitadoDTO> destinosOriginais,
            List<List<DestinoMaisVisitadoDTO>> resultados) {

        int totalDestinos = destinosOriginais.size();

        // Itera sobre todas as combinações possíveis
        for (int tamanhoCombinacao = 1; tamanhoCombinacao <= totalDestinos; tamanhoCombinacao++) {
            combinarDestinos(destinosOriginais, resultados, tamanhoCombinacao, new ArrayList<>(), 0);
        }
    }

    private void combinarDestinos(
            List<DestinoMaisVisitadoDTO> destinosOriginais,
            List<List<DestinoMaisVisitadoDTO>> resultados,
            int tamanhoCombinacao,
            List<DestinoMaisVisitadoDTO> combinacaoAtual,
            int inicio) {

        // Se a combinação tiver o tamanho desejado, processa e armazena a combinação
        if (combinacaoAtual.size() == tamanhoCombinacao) {
            adicionarCombinacaoComPorcentagem(combinacaoAtual, resultados);
            return;
        }

        // Gera as combinações de forma iterativa
        for (int i = inicio; i < destinosOriginais.size(); i++) {
            combinacaoAtual.add(destinosOriginais.get(i));
            combinarDestinos(destinosOriginais, resultados, tamanhoCombinacao, combinacaoAtual, i + 1);
            combinacaoAtual.remove(combinacaoAtual.size() - 1);  // Remove o último destino para tentar a próxima combinação
        }
    }

    private void adicionarCombinacaoComPorcentagem(
            List<DestinoMaisVisitadoDTO> combinacaoAtual,
            List<List<DestinoMaisVisitadoDTO>> resultados) {

        // Calcula a porcentagem acumulada
        List<DestinoMaisVisitadoDTO> combinacaoComPorcentagem = new ArrayList<>();
        double porcentagemAcumulada = 1.0;

        for (DestinoMaisVisitadoDTO d : combinacaoAtual) {
            porcentagemAcumulada *= d.getPorcentagem();  // Acumula a porcentagem
            combinacaoComPorcentagem.add(new DestinoMaisVisitadoDTO(d.getDestino(), porcentagemAcumulada));
        }

        // Adiciona a combinação com porcentagens acumuladas
        resultados.add(combinacaoComPorcentagem);
    }



    public boolean hasPais(List<PerfilDTO> perfiesDTO, String nomePais) {
        // Criar o conjunto para otimizar a busca
        Set<String> paisesSet = new HashSet<>();

        for (PerfilDTO perfilDTO : perfiesDTO) {
            // Adicionar o país ao conjunto
            paisesSet.add(perfilDTO.getPaisesOrigem());
        }

        // Verificar se o nomePais está no conjunto
        return paisesSet.contains(nomePais);
    }


    public boolean fichaSintesePaisDTOHasPais(List<FichaSintesePaisDTO> fichasSintesePaisDTO, String nomePais) {
        // Criar o conjunto para otimizar a busca
        Set<String> paisesSet = new HashSet<>();

        for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {
            // Adicionar o país ao conjunto
            paisesSet.add(fichaSintesePaisDTO.getPais());
        }

        // Verificar se o nomePais está no conjunto
        return paisesSet.contains(nomePais);
    }


    public boolean fichaSintesePaisDTOHasAno(List<FichaSintesePaisDTO> fichasSintesePaisDTO, Integer ano) {
        // Criar o conjunto para otimizar a busca
        Set<Integer> anosSet = new HashSet<>();

        for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {
            // Adicionar o ano ao conjunto
            anosSet.add(fichaSintesePaisDTO.getAno());
        }

        // Verificar se o ano está no conjunto
        return anosSet.contains(ano);
    }


    public boolean hasEstado(List<PerfilDTO> perfiesDTO, String nomeEstado) {
        // Criar o conjunto para otimizar a busca
        Set<String> destinosSet = new HashSet<>();

        for (PerfilDTO perfilDTO : perfiesDTO) {
            // Adiciona o destino ao conjunto
            destinosSet.add(perfilDTO.getDestinosDTO().getFirst().getDestino());
        }

        // Verificar se o nomeEstado está no conjunto
        return destinosSet.contains(nomeEstado);
    }


    public boolean hasAno(List<PerfilDTO> perfiesDTO, Integer ano) {
        // Criar o mapa para otimizar as buscas
        Map<Integer, PerfilDTO> anoMap = new HashMap<>();

        for (PerfilDTO perfilDTO : perfiesDTO) {
            anoMap.put(perfilDTO.getAno(), perfilDTO);
        }

        // Verificar se o ano existe no mapa
        return anoMap.containsKey(ano);
    }


}
