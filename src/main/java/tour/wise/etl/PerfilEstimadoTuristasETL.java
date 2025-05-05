package tour.wise.etl;

import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.perfil.DestinoDTO;
import tour.wise.dto.perfil.ListaDestinosDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.etl.fichas.sintese.FichaSinteseBrasilET;
import tour.wise.etl.fichas.sintese.FichaSinteseEstadoET;
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
        FichaSinteseEstadoET fichaSinteseEstadoEt = new FichaSinteseEstadoET();

        // TRANSFORM

        // FICHAS SÍNTESE

        FichaSinteseBrasilDTO fichasSinteseBrasilDTO = fichaSinteseBrasilET.extractTransformFichaSinteseBrasil(fileNameFichaSinteseBrasil, 4);
        List<FichaSintesePaisDTO> fichasSintesePaisDTO = fichaSintesePaisET.extractTransformFichasSintesePais(fileNameFichaSintesePais, 4);
        List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = fichaSinteseEstadoEt.extractTransformFichasSinteseEstado(fileNameFichaSinteseEstado, 4);

        // PERFIES
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

            System.out.println("chegadas restantes: " + (chegadasTuristasInternacionaisBrasilAnualDTO.size() - i));
            System.out.println("chegada - linha: " + i);
            System.out.println("quantidade de perfies: " + perfiesEstimadoTuristas.size());

            for (PerfilDTO perfiesEstimadoTurista : perfiesEstimadoTuristas) {
                System.out.println(perfiesEstimadoTurista);
            }

            i++;

            String paisOrigem = chegada.getPaisOrigem();
            String ufDestino = chegada.getUfDestino();
            Integer ano = chegada.getAno();

            // Primeiro tenta encontrar na ficha estadual
            Optional<FichaSinteseEstadoDTO> fichaEstadoOptional = fichasSinteseEstadoDTO.stream()
                    .filter(f -> f.getPaisesOrigem().stream()
                            .anyMatch(p -> p.getPais().equalsIgnoreCase(paisOrigem)) // verifica se a lista contém o país
                            && f.getDestinoPrincipal().equalsIgnoreCase(ufDestino)
                            && f.getAno().equals(ano))
                    .findFirst();

            if (fichaEstadoOptional.isPresent()) {
                FichaSinteseEstadoDTO fichaEstado = fichaEstadoOptional.get();

                Double taxaTuristas = fichaEstado.getPaisesOrigem().stream()
                        .filter(p -> p.getPais().equalsIgnoreCase(paisOrigem))
                        .map(PaisOrigemDTO::getPorcentagem)
                        .findFirst()
                        .orElse(null);

                List<PerfilDTO> perfiesDTOEstado = transformFichaSinteseCombinationsCreatePerfilDTO(fichaEstado);

                for (PerfilDTO perfilDTOEstado : perfiesDTOEstado) {

                    perfilDTOEstado.setPaisesOrigem(paisOrigem);
                    perfilDTOEstado.setEstadoEntrada(ufDestino);
                    perfilDTOEstado.setAno(chegada.getAno());
                    perfilDTOEstado.setMes(chegada.getMes());
                    perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                    perfilDTOEstado.setTaxaTuristas(
                            perfilDTOEstado.getTaxaTuristas() *
                                    taxaTuristas/100
                            );
                    Double taxaAtualizada = perfilDTOEstado.getTaxaTuristas()/100;
                    Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * taxaAtualizada)).intValue();
                    perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);

                }


                // Remove todos os perfis com quantidadeTuristas < 1
                perfiesDTOEstado.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);

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

                List<PerfilDTO> perfiesDTOPais = transformFichaSinteseCombinationsCreatePerfilDTO(fichaPais);

                for (PerfilDTO perfilDTOEstado : perfiesDTOPais) {

                    perfilDTOEstado.setPaisesOrigem(paisOrigem);
                    perfilDTOEstado.setEstadoEntrada(ufDestino);
                    perfilDTOEstado.setAno(chegada.getAno());
                    perfilDTOEstado.setMes(chegada.getMes());
                    perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                    Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilDTOEstado.getTaxaTuristas()/100)).intValue();
                    perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
                }

                // Remove todos os perfis com quantidadeTuristas < 1
                perfiesDTOPais.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);

                perfiesEstimadoTuristas.addAll(perfiesDTOPais);

                perfiesDTOPais.clear();

                continue;
            }

            // Senão, usa a ficha do Brasil
            FichaSinteseBrasilDTO fichaBrasil = fichasSinteseBrasilDTO;

            List<PerfilDTO> perfiesDTOBrasil = transformFichaSinteseCombinationsCreatePerfilDTO(fichaBrasil);

            for (PerfilDTO perfilDTOEstado : perfiesDTOBrasil) {

                perfilDTOEstado.setPaisesOrigem(paisOrigem);
                perfilDTOEstado.setEstadoEntrada(ufDestino);
                perfilDTOEstado.setAno(chegada.getAno());
                perfilDTOEstado.setMes(chegada.getMes());
                perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilDTOEstado.getTaxaTuristas()/100)).intValue();
                perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
            }

            // Remove todos os perfis com quantidadeTuristas < 1
            perfiesDTOBrasil.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);
            perfiesEstimadoTuristas.addAll(perfiesDTOBrasil);
            perfiesDTOBrasil.clear();

        }


    }


    protected List<PerfilDTO>transformFichaSinteseCombinationsCreatePerfilDTO(FichaSinteseBrasilDTO fichaSinteseBrasilDTO){
        List<PerfilDTO> perfiesFichaSinteseBrasilDTO = new ArrayList<>();

        for (GeneroDTO generoDTO : fichaSinteseBrasilDTO.getGeneroDTO()) {

            for (FaixaEtariaDTO faixaEtariaDTO : fichaSinteseBrasilDTO.getFaixaEtariaDTO()) {

                for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemDTO : fichaSinteseBrasilDTO.getUtilizacaoAgenciaViagemDTO()) {

                    for (ComposicaoGrupoViagemDTO composicaoGrupoViagemDTO : fichaSinteseBrasilDTO.getComposicaoGruposViagem()) {

                        for (FonteInformacaoDTO fonteInformacaoDTO : fichaSinteseBrasilDTO.getFontesInformacao()) {

                            for (MotivoViagemDTO motivoDTO : fichaSinteseBrasilDTO.getMotivos()) {

                                DestinosMaisVisitadosPorMotivoDTO destinosMaisVisitadosPorMotivoDTO = fichaSinteseBrasilDTO.getDestinosMaisVisistadosMotivo().stream()
                                        .filter(d -> d.getMotivo().equalsIgnoreCase(motivoDTO.getMotivo()))
                                        .findFirst()
                                        .orElse(null);

                                List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosDTOCombinations = createDestinosMaisVisitadosCombinations(destinosMaisVisitadosPorMotivoDTO);

                                for (List<DestinoMaisVisitadoDTO> destinosMaisVisitadosDTOCombination : destinosMaisVisitadosDTOCombinations) {

                                    GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoDTO = fichaSinteseBrasilDTO.getGastosMedioPerCapitaMotivo().stream()
                                            .filter(g -> g.getMotivo().equalsIgnoreCase(motivoDTO.getMotivo()))
                                            .findFirst()
                                            .orElse(null);

                                    PermanenciaMediaDTO permanenciaMediaMotivoDTO = fichaSinteseBrasilDTO.getPermanenciaMediaDTO().stream()
                                            .filter(p -> p.getMotivo().equalsIgnoreCase(motivoDTO.getMotivo()))
                                            .findFirst()
                                            .orElse(null);


                                    List<MotivacaoViagemLazerDTO> motivacoesLazer = "Lazer".equalsIgnoreCase(motivoDTO.getMotivo())
                                            ? fichaSinteseBrasilDTO.getMotivacoesViagemLazer()
                                            : Collections.emptyList();


                                    ListaDestinosDTO listaDestinosDTO = createListaDestinosDTO(destinosMaisVisitadosDTOCombination, permanenciaMediaMotivoDTO.getDias());


                                    Double taxaTuristasDestino = (destinosMaisVisitadosDTOCombination.getFirst().getPorcentagem() > 0) ? destinosMaisVisitadosDTOCombination.getFirst().getPorcentagem() : 100;

                                    String genero = (generoDTO.getPorcentagem() > 0) ? generoDTO.getGenero() : null;
                                    Double gerenoPorcentagem = (generoDTO.getPorcentagem() > 0) ? generoDTO.getPorcentagem() : 100;
                                    String faixaEtaria = (faixaEtariaDTO.getPorcentagem() > 0) ? faixaEtariaDTO.getFaixa_etaria() : null;
                                    Double faixaEtariaPorcentagem = (faixaEtariaDTO.getPorcentagem() > 0) ? faixaEtariaDTO.getPorcentagem() : 100;
                                    String utilizacaoAgenciaViagem = (utilizacaoAgenciaViagemDTO.getPorcentagem() > 0) ? utilizacaoAgenciaViagemDTO.getTipo() : null;
                                    Double utilizacaoAgenciaViagemPorcentagem = (utilizacaoAgenciaViagemDTO.getPorcentagem() > 0) ? utilizacaoAgenciaViagemDTO.getPorcentagem() : 100;
                                    String composicaogrupoViagem =  (composicaoGrupoViagemDTO.getPorcentagem() > 0) ? composicaoGrupoViagemDTO.getComposicao() : null;
                                    Double composicaogrupoViagemPorcentagem = (composicaoGrupoViagemDTO.getPorcentagem() > 0) ? composicaoGrupoViagemDTO.getPorcentagem() : 100;
                                    String fonteInformacao = (fonteInformacaoDTO.getPorcentagem() > 0) ? fonteInformacaoDTO.getFonte() : null;
                                    Double fonteInformacaoPorcentagem = (fonteInformacaoDTO.getPorcentagem() > 0) ? fonteInformacaoDTO.getPorcentagem() : 100;
                                    String motivo = (motivoDTO.getPorcentagem() > 0) ? motivoDTO.getMotivo() : null;
                                    Double motivoPorcentagem = (motivoDTO.getPorcentagem() > 0) ? motivoDTO.getPorcentagem() : 100;


                                    if(!motivacoesLazer.isEmpty()){

                                        for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaSinteseBrasilDTO.getMotivacoesViagemLazer()) {

                                            String motivacaoViagemLazer = (motivacaoViagemLazerDTO.getPorcentagem() > 0) ? motivacaoViagemLazerDTO.getMotivacao() : null;
                                            Double motivacaoViagemLazerPorcentagem = (motivacaoViagemLazerDTO.getPorcentagem() > 0) ? motivacaoViagemLazerDTO.getPorcentagem() : 100;

                                            Double taxaTuristas = gerenoPorcentagem/100 *
                                                    faixaEtariaPorcentagem/100 *
                                                    utilizacaoAgenciaViagemPorcentagem/100 *
                                                    fonteInformacaoPorcentagem/100 *
                                                    composicaogrupoViagemPorcentagem/100 *
                                                    motivoPorcentagem/100 *
                                                    taxaTuristasDestino/100 *
                                                    motivacaoViagemLazerPorcentagem/100;

                                            perfiesFichaSinteseBrasilDTO.add(
                                                    new PerfilDTO(
                                                            taxaTuristas,
                                                            fichaSinteseBrasilDTO.getAno(),
                                                            genero,
                                                            faixaEtaria,
                                                            composicaogrupoViagem,
                                                            fonteInformacao,
                                                            utilizacaoAgenciaViagem,
                                                            motivo,
                                                            motivacaoViagemLazer,
                                                            gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                            listaDestinosDTO

                                                    )
                                            );

                                        }

                                    }else{

                                        Double taxaTuristas = gerenoPorcentagem/100 *
                                                faixaEtariaPorcentagem/100 *
                                                utilizacaoAgenciaViagemPorcentagem/100 *
                                                fonteInformacaoPorcentagem/100 *
                                                composicaogrupoViagemPorcentagem/100 *
                                                motivoPorcentagem/100 *
                                                taxaTuristasDestino/100;

                                        perfiesFichaSinteseBrasilDTO.add(
                                                new PerfilDTO(
                                                        taxaTuristas,
                                                        fichaSinteseBrasilDTO.getAno(),
                                                        genero,
                                                        faixaEtaria,
                                                        composicaogrupoViagem,
                                                        fonteInformacao,
                                                        utilizacaoAgenciaViagem,
                                                        motivo,
                                                        null,
                                                        gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                        listaDestinosDTO

                                                )
                                        );

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }

        return perfiesFichaSinteseBrasilDTO;

    };


    protected ListaDestinosDTO createListaDestinosDTO(List<DestinoMaisVisitadoDTO> destinosMaisVisitadosDTOCombination, Double permanenciaMediaMotivoDTO){
        List<DestinoDTO> destinosDTO = new ArrayList<>();

        for (DestinoMaisVisitadoDTO destinoMaisVisitadoDTO : destinosMaisVisitadosDTOCombination) {

            destinosDTO.add(new DestinoDTO(destinoMaisVisitadoDTO.getDestino()));

        }

        return new ListaDestinosDTO(destinosDTO, permanenciaMediaMotivoDTO);
    }

    protected List<List<DestinoMaisVisitadoDTO>> createDestinosMaisVisitadosCombinations(
            DestinosMaisVisitadosPorMotivoDTO destinosMaisVisitadosPorMotivoDTO) {

        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations = new ArrayList<>();

        cretateConbinations(destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado(), destinosMaisVisitadosCombinations);

        return destinosMaisVisitadosCombinations;
    }

    protected void cretateConbinations(
            List<DestinoMaisVisitadoDTO> destinosMaisVisitados,
            List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations) {

        int totalDestinos = destinosMaisVisitados.size();

        for (int tamanhoCombinacao = 1; tamanhoCombinacao <= totalDestinos; tamanhoCombinacao++) {
            combineDestinos(destinosMaisVisitados, destinosMaisVisitadosCombinations, tamanhoCombinacao, new ArrayList<>(), 0);
        }
    }

    protected void combineDestinos(
            List<DestinoMaisVisitadoDTO> destinosMaisVisitados,
            List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations,
            int tamanhoCombinacao,
            List<DestinoMaisVisitadoDTO> combinacaoAtual,
            int inicio) {

        if (combinacaoAtual.size() == tamanhoCombinacao) {
            adicionarCombinacaoComPorcentagem(combinacaoAtual, destinosMaisVisitadosCombinations);
            return;
        }

        for (int i = inicio; i < destinosMaisVisitados.size(); i++) {
            combinacaoAtual.add(destinosMaisVisitados.get(i));
            combineDestinos(destinosMaisVisitados, destinosMaisVisitadosCombinations, tamanhoCombinacao, combinacaoAtual, i + 1);
            combinacaoAtual.remove(combinacaoAtual.size() - 1);
        }
    }

    protected void adicionarCombinacaoComPorcentagem(
            List<DestinoMaisVisitadoDTO> combinacaoAtual,
            List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations) {

        List<DestinoMaisVisitadoDTO> combinacaoComPorcentagem = new ArrayList<>();
        double porcentagemAcumulada = 100.0;

        for (DestinoMaisVisitadoDTO d : combinacaoAtual) {
            porcentagemAcumulada *= d.getPorcentagem()/100;
            combinacaoComPorcentagem.add(new DestinoMaisVisitadoDTO(d.getDestino(), porcentagemAcumulada));
        }

        destinosMaisVisitadosCombinations.add(combinacaoComPorcentagem);
    }

}
