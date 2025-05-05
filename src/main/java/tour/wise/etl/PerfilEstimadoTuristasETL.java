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
        List<PerfilDTO> perfiesEstimadoTuristasBrasil = new ArrayList<>(); // = transformFichaSinteseCombinationsCreatePerfilDTO(fichasSinteseBrasilDTO);
        List<PerfilDTO> perfiesEstimadoTuristasPais = new ArrayList<>();

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegada : chegadasTuristasInternacionaisBrasilMensalDTO) {

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
                // usar fichaEstado aqui...

                System.out.println(fichaEstado);

                continue;
            }

            // Senão, tenta encontrar na ficha do país
            Optional<FichaSintesePaisDTO> fichaPaisOptional = fichasSintesePaisDTO.stream()
                    .filter(f -> f.getPais().equalsIgnoreCase(paisOrigem)
                            && f.getAno().equals(ano))
                    .findFirst();

            if (fichaPaisOptional.isPresent()) {
                FichaSintesePaisDTO fichaPais = fichaPaisOptional.get();
                // usar fichaPais aqui...

                System.out.println(fichaPais);

                continue;
            }

            // Senão, usa a ficha do Brasil
            FichaSinteseBrasilDTO fichaBrasil = fichasSinteseBrasilDTO;
            // usar fichaBrasil aqui...

            System.out.println(fichaBrasil);

            break;
        }
    }

    public List<PerfilDTO> createPerfiesChegadaDTO(){
        List<PerfilDTO> perfiesEstado = new ArrayList<>();



        return perfiesEstado;
    }

    protected List<PerfilDTO>transformFichaSinteseCombinationsCreatePerfilDTO(FichaSinteseBrasilDTO fichaSinteseBrasilDTO){
        List<PerfilDTO> perfiesFichaSinteseBrasilDTO = new ArrayList<>();

        for (GeneroDTO generoDTO : fichaSinteseBrasilDTO.getGeneroDTO()) {

            for (FaixaEtariaDTO faixaEtariaDTO : fichaSinteseBrasilDTO.getFaixaEtariaDTO()) {

                for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemDTO : fichaSinteseBrasilDTO.getUtilizacaoAgenciaViagemDTO()) {

                    for (ComposicaoGrupoViagemDTO composicaoGrupoViagemDTO : fichaSinteseBrasilDTO.getComposicaoGruposViagem()) {

                        for (FonteInformacaoDTO fonteInformacaoDTO : fichaSinteseBrasilDTO.getFontesInformacao()) {

                            for (MotivoViagemDTO motivo : fichaSinteseBrasilDTO.getMotivos()) {

                                DestinosMaisVisitadosPorMotivoDTO destinosMaisVisitadosPorMotivoDTO = fichaSinteseBrasilDTO.getDestinosMaisVisistadosMotivo().stream()
                                        .filter(d -> d.getMotivo().equalsIgnoreCase(motivo.getMotivo()))
                                        .findFirst()
                                        .orElse(null);

                                List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitadosDTOCombinations = createDestinosMaisVisitadosCombinations(destinosMaisVisitadosPorMotivoDTO);

                                for (List<DestinoMaisVisitadoDTO> destinosMaisVisitadosDTOCombination : destinosMaisVisitadosDTOCombinations) {

                                    GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoDTO = fichaSinteseBrasilDTO.getGastosMedioPerCapitaMotivo().stream()
                                            .filter(g -> g.getMotivo().equalsIgnoreCase(motivo.getMotivo()))
                                            .findFirst()
                                            .orElse(null);

                                    PermanenciaMediaDTO permanenciaMediaMotivoDTO = fichaSinteseBrasilDTO.getPermanenciaMediaDTO().stream()
                                            .filter(p -> p.getMotivo().equalsIgnoreCase(motivo.getMotivo()))
                                            .findFirst()
                                            .orElse(null);


                                    List<MotivacaoViagemLazerDTO> motivacoesLazer = "Lazer".equalsIgnoreCase(motivo.getMotivo())
                                            ? fichaSinteseBrasilDTO.getMotivacoesViagemLazer()
                                            : Collections.emptyList();


                                    ListaDestinosDTO listaDestinosDTO = createListaDestinosDTO(destinosMaisVisitadosDTOCombination, permanenciaMediaMotivoDTO.getProcentagem());


                                    if(!motivacoesLazer.isEmpty()){

                                        for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaSinteseBrasilDTO.getMotivacoesViagemLazer()) {

                                            Double taxaTuristas = generoDTO.getPorcentagem()/100 *
                                                    faixaEtariaDTO.getPorcentagem()/100 *
                                                    utilizacaoAgenciaViagemDTO.getPorcentagem()/100 *
                                                    fonteInformacaoDTO.getPorcentagem()/100 *
                                                    composicaoGrupoViagemDTO.getPorcentagem()/100 *
                                                    motivo.getPorcentagem()/100 *
                                                    destinosMaisVisitadosDTOCombination.getLast().getPorcentagem() *
                                                    motivacaoViagemLazerDTO.getPorcentagem()/100;

                                            perfiesFichaSinteseBrasilDTO.add(
                                                    new PerfilDTO(
                                                            taxaTuristas,
                                                            fichaSinteseBrasilDTO.getAno(),
                                                            generoDTO.getGenero(),
                                                            faixaEtariaDTO.getFaixa_etaria(),
                                                            composicaoGrupoViagemDTO.getComposicao(),
                                                            fonteInformacaoDTO.getFonte(),
                                                            utilizacaoAgenciaViagemDTO.getTipo(),
                                                            motivo.getMotivo(),
                                                            motivacaoViagemLazerDTO.getMotivacao(),
                                                            gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                            listaDestinosDTO

                                                    )
                                            );

                                        }

                                    }else{

                                        Double taxaTuristas = generoDTO.getPorcentagem()/100 *
                                                faixaEtariaDTO.getPorcentagem()/100 *
                                                utilizacaoAgenciaViagemDTO.getPorcentagem()/100 *
                                                fonteInformacaoDTO.getPorcentagem()/100 *
                                                composicaoGrupoViagemDTO.getPorcentagem()/100 *
                                                motivo.getPorcentagem()/100 *
                                                destinosMaisVisitadosDTOCombination.getLast().getPorcentagem();

                                        perfiesFichaSinteseBrasilDTO.add(
                                                new PerfilDTO(
                                                        taxaTuristas,
                                                        fichaSinteseBrasilDTO.getAno(),
                                                        generoDTO.getGenero(),
                                                        faixaEtariaDTO.getFaixa_etaria(),
                                                        composicaoGrupoViagemDTO.getComposicao(),
                                                        fonteInformacaoDTO.getFonte(),
                                                        utilizacaoAgenciaViagemDTO.getTipo(),
                                                        motivo.getMotivo(),
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
        double porcentagemAcumulada = 1.0;

        for (DestinoMaisVisitadoDTO d : combinacaoAtual) {
            porcentagemAcumulada *= d.getPorcentagem();
            combinacaoComPorcentagem.add(new DestinoMaisVisitadoDTO(d.getDestino(), porcentagemAcumulada));
        }

        destinosMaisVisitadosCombinations.add(combinacaoComPorcentagem);
    }

}
