package tour.wise.util;

import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.perfil.DestinoDTO;
import tour.wise.dto.perfil.ListaDestinosDTO;
import tour.wise.dto.perfil.PerfilDTO;

import java.util.*;


public class Util {

    public List<PerfilDTO>transformFichaSinteseCombinationsCreatePerfilDTO(FichaSinteseBrasilDTO fichaSinteseBrasilDTO){
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


    public ListaDestinosDTO createListaDestinosDTO(List<DestinoMaisVisitadoDTO> destinosMaisVisitadosDTOCombination, Double permanenciaMediaMotivoDTO){
        List<DestinoDTO> destinosDTO = new ArrayList<>();

        for (DestinoMaisVisitadoDTO destinoMaisVisitadoDTO : destinosMaisVisitadosDTOCombination) {

            destinosDTO.add(new DestinoDTO(destinoMaisVisitadoDTO.getDestino()));

        }

        return new ListaDestinosDTO(destinosDTO, permanenciaMediaMotivoDTO);
    }

    public List<List<DestinoMaisVisitadoDTO>> createDestinosMaisVisitadosCombinations(
            DestinosMaisVisitadosPorMotivoDTO destinosMaisVisitadosPorMotivoDTO) {

        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations = new ArrayList<>();

        cretateConbinations(destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado(), destinosMaisVisitadosCombinations);

        return destinosMaisVisitadosCombinations;
    }

    public void cretateConbinations(
            List<DestinoMaisVisitadoDTO> destinosMaisVisitados,
            List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosCombinations) {

        int totalDestinos = destinosMaisVisitados.size();

        for (int tamanhoCombinacao = 1; tamanhoCombinacao <= totalDestinos; tamanhoCombinacao++) {
            combineDestinos(destinosMaisVisitados, destinosMaisVisitadosCombinations, tamanhoCombinacao, new ArrayList<>(), 0);
        }
    }

    public void combineDestinos(
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

    public void adicionarCombinacaoComPorcentagem(
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
