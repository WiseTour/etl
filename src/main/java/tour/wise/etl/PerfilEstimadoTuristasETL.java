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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerfilEstimadoTuristasETL {

    public void exe(String fonte_perfil, String fonte_chegadas, String fileNameFichaSintesePais, String fileNameFichaSinteseBrasil, String fileNameFichaSinteseEstado, String fileNameChegadas) throws IOException {


        // EXTRACT

        FichaSinteseBrasilET fichaSinteseBrasilET = new FichaSinteseBrasilET();

        List<FichaSinteseBrasilDTO> fichasSinteseBrasilDTO = fichaSinteseBrasilET.extractTransform(fileNameFichaSinteseBrasil, 0, 0);

        FichaSintesePaisET fichaSintesePaisET = new FichaSintesePaisET();

        List<FichaSintesePaisDTO> fichasSintesePaisDTO = fichaSintesePaisET.extractTransformFicha_Sintese_Pais(fileNameFichaSintesePais, 0, 0);

        Ficha_Sintese_Estado_ET fichaSinteseEstadoEt = new Ficha_Sintese_Estado_ET();

        List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO = fichaSinteseEstadoEt.extractTransformFicha_Sintese_Estado(fileNameFichaSinteseEstado, 0, 0);

        ChegadaTuristasInternacionaisBrasilMensalET chegadaTuristasInternacionaisBrasilMensalET = new ChegadaTuristasInternacionaisBrasilMensalET();

        List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO = chegadaTuristasInternacionaisBrasilMensalET.transform(chegadaTuristasInternacionaisBrasilMensalET.extract(
                        fileNameChegadas,
                        0,
                        0,
                        12,
                        List.of("String", "Numeric", "String", "Numeric", "String", "Numeric", "String", "Numeric", "Numeric", "String", "Numeric", "Numeric")
                ),
                "Ministério do Turismo",
                "2019"
        );


        System.out.println();
        System.out.println("Chegadas");

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegadaTuristasInternacionaisBrasilMensalDTO : chegadasTuristasInternacionaisBrasilMensalDTO) {
            System.out.println(chegadaTuristasInternacionaisBrasilMensalDTO);
        }

        System.out.println();
        System.out.println("Ficha Sintese Brasil");

        for (FichaSinteseBrasilDTO fichaSinteseBrasilDTO : fichasSinteseBrasilDTO) {
            System.out.println(fichaSinteseBrasilDTO);
        }

        System.out.println();
        System.out.println("Ficha Sintese por Estado");

        for (FichaSinteseEstadoDTO fichaSinteseEstadoDTOc : fichasSinteseEstadoDTO) {
            System.out.println(fichaSinteseEstadoDTOc);
        }

        System.out.println();
        System.out.println("Ficha Sintese por Pais");

        for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {
            System.out.println(fichaSintesePaisDTO);
        }


        // TRANSFORM

        List<PerfilDTO> perfiesEstimadoTuristas = new ArrayList<>();


        List<PerfilDTO> perfiesDTO = transformPerfiesEstadoDTO(
                transformFichasSinteseEstadoDTO(fichasSinteseEstadoDTO),
                fichasSintesePaisDTO,
                fichasSinteseBrasilDTO
        );

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegadaTuristasInternacionaisBrasilMensalDTO : chegadasTuristasInternacionaisBrasilMensalDTO) {

            Integer totalChegadas = chegadaTuristasInternacionaisBrasilMensalDTO.getQtdChegadas();
            String paisOrigem = chegadaTuristasInternacionaisBrasilMensalDTO.getPaisOrigem();
            String ufDesembarque = chegadaTuristasInternacionaisBrasilMensalDTO.getUfDestino();
            String viaAcesso = chegadaTuristasInternacionaisBrasilMensalDTO.getViaAcesso();
            Integer ano = chegadaTuristasInternacionaisBrasilMensalDTO.getAno();
            Integer mes = chegadaTuristasInternacionaisBrasilMensalDTO.getMes();

            if (hasPais(perfiesDTO, paisOrigem) && hasEstado(perfiesDTO, ufDesembarque) && hasAno(perfiesDTO, ano)) {

                for (PerfilDTO perfilDTO : perfiesDTO) {

                    if (perfilDTO.getDestinosDTO().getFirst().getDestino().equalsIgnoreCase(ufDesembarque) &&
                            perfilDTO.getPaisesOrigem().equalsIgnoreCase(paisOrigem) &&
                            perfilDTO.getAno().equals(ano)) {

                        perfiesEstimadoTuristas.add(
                                new PerfilDTO(
                                        perfilDTO.getTaxaTuristas(),
                                        ((Double) (totalChegadas * perfilDTO.getTaxaTuristas())).intValue(),
                                        perfilDTO.getPaisesOrigem(),
                                        perfilDTO.getAno(),
                                        mes,
                                        viaAcesso,
                                        ufDesembarque,
                                        perfilDTO.getGeneroDTO(),
                                        perfilDTO.getFaixaEtariaDTO(),
                                        perfilDTO.getComposicaoGruposViagem(),
                                        perfilDTO.getFonteInformacao(),
                                        perfilDTO.getUtilizacaoAgenciaViagemDTO(),
                                        perfilDTO.getMotivo(),
                                        perfilDTO.getMotivacaoViagemLazer(),
                                        perfilDTO.getGastosMedioPerCapitaMotivo(),
                                        perfilDTO.getDestinosDTO()

                                )
                        );

                    }

                }

            } else if (fichaSintesePaisDTOHasPais(fichasSintesePaisDTO, paisOrigem) && fichaSintesePaisDTOHasAno(fichasSintesePaisDTO, ano)) {

                List<PerfilDTO> perfiesOutrosEstados;

                for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {

                    if(fichaSintesePaisDTO.getPais().equalsIgnoreCase(paisOrigem) && fichaSintesePaisDTO.getAno().equals(ano)){


                        perfiesOutrosEstados = transformFichaSintesePais(fichaSintesePaisDTO, ufDesembarque);

                        for (PerfilDTO perfiesOutrosEstado : perfiesOutrosEstados) {

                            perfiesEstimadoTuristas.add(
                                    new PerfilDTO(
                                            perfiesOutrosEstado.getTaxaTuristas(),
                                            ((Double) (totalChegadas * perfiesOutrosEstado.getTaxaTuristas())).intValue(),
                                            perfiesOutrosEstado.getPaisesOrigem(),
                                            perfiesOutrosEstado.getAno(),
                                            mes,
                                            viaAcesso,
                                            perfiesOutrosEstado.getEstadoEntrada(),
                                            perfiesOutrosEstado.getGeneroDTO(),
                                            perfiesOutrosEstado.getFaixaEtariaDTO(),
                                            perfiesOutrosEstado.getComposicaoGruposViagem(),
                                            perfiesOutrosEstado.getFonteInformacao(),
                                            perfiesOutrosEstado.getUtilizacaoAgenciaViagemDTO(),
                                            perfiesOutrosEstado.getMotivo(),
                                            perfiesOutrosEstado.getMotivacaoViagemLazer(),
                                            perfiesOutrosEstado.getGastosMedioPerCapitaMotivo(),
                                            perfiesOutrosEstado.getDestinosDTO()

                                    )
                            );

                        }


                    }

                }


            } else {

                List<PerfilDTO> perfiesOutrosPaisesDTO;

                for (FichaSinteseBrasilDTO fichaSinteseBrasilDTO : fichasSinteseBrasilDTO) {

                    if (fichaSinteseBrasilDTO.getAno().equals(ano)) {

                        perfiesOutrosPaisesDTO = transformFichaSinteseBrasil(fichaSinteseBrasilDTO, ufDesembarque, paisOrigem);

                        for (PerfilDTO perfilOutrosPaisesDTO : perfiesOutrosPaisesDTO) {

                            perfiesEstimadoTuristas.add(
                                    new PerfilDTO(
                                            perfilOutrosPaisesDTO.getTaxaTuristas(),
                                            ((Double) (totalChegadas * perfilOutrosPaisesDTO.getTaxaTuristas())).intValue(),
                                            perfilOutrosPaisesDTO.getPaisesOrigem(),
                                            perfilOutrosPaisesDTO.getAno(),
                                            mes,
                                            viaAcesso,
                                            perfilOutrosPaisesDTO.getEstadoEntrada(),
                                            perfilOutrosPaisesDTO.getGeneroDTO(),
                                            perfilOutrosPaisesDTO.getFaixaEtariaDTO(),
                                            perfilOutrosPaisesDTO.getComposicaoGruposViagem(),
                                            perfilOutrosPaisesDTO.getFonteInformacao(),
                                            perfilOutrosPaisesDTO.getUtilizacaoAgenciaViagemDTO(),
                                            perfilOutrosPaisesDTO.getMotivo(),
                                            perfilOutrosPaisesDTO.getMotivacaoViagemLazer(),
                                            perfilOutrosPaisesDTO.getGastosMedioPerCapitaMotivo(),
                                            perfilOutrosPaisesDTO.getDestinosDTO()

                                    )
                            );

                        }


                    }

                }

            }

        }
    }

    //    public void load(
//            JdbcTemplate connection,
//            String orgao_emissor,
//            String edicao,
//            String titulo_edicao,
//            String url_fonte,
//            List<Perfil_Estimado_Turistas> perfis
//            ){
//
//        PerfilEstimadoTuristasDAO perfil_estimado_turistas_dao = new PerfilEstimadoTuristasDAO(connection);
//
//        PaisDAO pais_dao = new PaisDAO(connection);
//        FonteDAO fonte_dao = new FonteDAO(connection);
//        UnidadeFederativaBrasilDAO unidade_federativa_brasil_dao = new UnidadeFederativaBrasilDAO(connection);
//
//
////        for (Perfil_Estimado_Turistas perfil : perfis) {
////            Integer fkFonte = fonte_dao.getFonteId(titulo_edicao);
////            String composicaoGrupoFamiliar = perfil.getComposicao_grupo_familiar();
////            String fonteInformacaoViagem = perfil.getFonte_informacao_viagem();
////            Integer servicoAgenciaTurismo = perfil.getServico_agencia_turismo();
////            String motivoViagem = perfil.getMotivo_viagem();
////            Double gastoMedioPerCapita = perfil.get
////            Integer ano,
////            Integer fkTotalChegadasMensal,
////            String ufDestino,
////            Integer fkFonteChegadasAnual,
////            Integer fkPaisOrigem
////
////
////        }
//
//    }



    public List<PerfilDTO> transformFichaSinteseBrasil(FichaSinteseBrasilDTO fichaSinteseBrasilDTO, String ufDesembarque, String paisOrigem) {

        List<PerfilDTO> perfiesDTO = new ArrayList<>();

        Double porcentagemTuristas = 1.0;

        for (FonteInformacaoDTO fonteInformacaoDTO : fichaSinteseBrasilDTO.getFontesInformacao()) {

            porcentagemTuristas *= fonteInformacaoDTO.getPorcentagem();

            for (ComposicaoGrupoViagemDTO composicaoGrupoViagemDTO : fichaSinteseBrasilDTO.getComposicaoGruposViagem()) {

                porcentagemTuristas *= composicaoGrupoViagemDTO.getPorcentagem();

                for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemDTO : fichaSinteseBrasilDTO.getUtilizacaoAgenciaViagemDTO()) {

                    porcentagemTuristas *= utilizacaoAgenciaViagemDTO.getPorcentagem();

                    for (GeneroDTO generoDTO : fichaSinteseBrasilDTO.getGeneroDTO()) {

                        porcentagemTuristas *= generoDTO.getPorcentagem();

                        for (FaixaEtariaDTO faixaEtariaDTO : fichaSinteseBrasilDTO.getFaixaEtariaDTO()) {

                            porcentagemTuristas *= faixaEtariaDTO.getPorcentagem();

                            for (MotivoViagemDTO motivo : fichaSinteseBrasilDTO.getMotivos()) {

                                porcentagemTuristas *= motivo.getPorcentagem();

                                for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoDTO : fichaSinteseBrasilDTO.getGastosMedioPerCapitaMotivo()) {

                                    if(gastoMedioPerCapitaMotivoDTO.getMotivo().equalsIgnoreCase(motivo.getMotivo())){

                                        for (PermanenciaMediaDTO permanenciaMediaDTO : fichaSinteseBrasilDTO.getPermanenciaMediaDTO()) {

                                            if(permanenciaMediaDTO.getMotivo().equalsIgnoreCase(motivo.getMotivo())){

                                                for (int i = 0; i < 2; i++) {

                                                    List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados;

                                                    if(motivo.getMotivo().equalsIgnoreCase("Lazer")) {

                                                        for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaSinteseBrasilDTO.getMotivacoesViagemLazer()) {

                                                            porcentagemTuristas *= motivacaoViagemLazerDTO.getPorcentagem() / 100;

                                                            // i = 0 indica que só viajou para o estado principal
                                                            if(i == 0){

                                                                perfiesDTO.add(
                                                                        new PerfilDTO(
                                                                                porcentagemTuristas,
                                                                                paisOrigem,
                                                                                fichaSinteseBrasilDTO.getAno(),
                                                                                generoDTO.getGenero(),
                                                                                faixaEtariaDTO.getFaixa_etaria(),
                                                                                composicaoGrupoViagemDTO.getComposicao(),
                                                                                fonteInformacaoDTO.getFonte(),
                                                                                utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                motivo.getMotivo(),
                                                                                motivacaoViagemLazerDTO.getMotivacao(),
                                                                                gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                Arrays.asList(
                                                                                        new DestinoDTO(
                                                                                                ufDesembarque,
                                                                                                permanenciaMediaDTO.getProcentagem()
                                                                                        ))

                                                                        )
                                                                );

                                                            }

                                                            // i = 1 indica que viajou para mais estados
                                                            else if (i == 1){

                                                                destinosMaisVisitados = transformDestinosMaisVisitados(fichaSinteseBrasilDTO.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                                                for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {

                                                                    porcentagemTuristas *= destinos.getLast().getPorcentagem()/100;

                                                                    List<DestinoDTO> destinosDTO = new ArrayList<>();
                                                                    destinosDTO.add(
                                                                            new DestinoDTO(
                                                                                    ufDesembarque,
                                                                                    permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                            )
                                                                    );

                                                                    for (DestinoMaisVisitadoDTO destino : destinos) {
                                                                        destinosDTO.add(
                                                                                new DestinoDTO(
                                                                                        destino.getDestino(),
                                                                                        permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                                )
                                                                        );
                                                                    }

                                                                    perfiesDTO.add(
                                                                            new PerfilDTO(
                                                                                    porcentagemTuristas,
                                                                                    paisOrigem,
                                                                                    fichaSinteseBrasilDTO.getAno(),
                                                                                    generoDTO.getGenero(),
                                                                                    faixaEtariaDTO.getFaixa_etaria(),
                                                                                    composicaoGrupoViagemDTO.getComposicao(),
                                                                                    fonteInformacaoDTO.getFonte(),
                                                                                    utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                    motivo.getMotivo(),
                                                                                    motivacaoViagemLazerDTO.getMotivacao(),
                                                                                    gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                    destinosDTO

                                                                            )
                                                                    );

                                                                }

                                                            }

                                                        }

                                                    }else{
                                                        // i = 0 indica que só viajou para o estado principal
                                                        if(i == 0){

                                                            perfiesDTO.add(
                                                                    new PerfilDTO(
                                                                            porcentagemTuristas,
                                                                            paisOrigem,
                                                                            fichaSinteseBrasilDTO.getAno(),
                                                                            generoDTO.getGenero(),
                                                                            faixaEtariaDTO.getFaixa_etaria(),
                                                                            composicaoGrupoViagemDTO.getComposicao(),
                                                                            fonteInformacaoDTO.getFonte(),
                                                                            utilizacaoAgenciaViagemDTO.getTipo(),
                                                                            motivo.getMotivo(),
                                                                            null,
                                                                            gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                            Arrays.asList(
                                                                                    new DestinoDTO(
                                                                                            ufDesembarque,
                                                                                            permanenciaMediaDTO.getProcentagem()
                                                                                    ))

                                                                    )
                                                            );

                                                        }

                                                        // i = 1 indica que viajou para mais estados
                                                        else if (i == 1){

                                                            destinosMaisVisitados = transformDestinosMaisVisitados(fichaSinteseBrasilDTO.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                                            for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {

                                                                porcentagemTuristas *= destinos.getLast().getPorcentagem()/100;

                                                                List<DestinoDTO> destinosDTO = new ArrayList<>();
                                                                destinosDTO.add(
                                                                        new DestinoDTO(
                                                                                ufDesembarque,
                                                                                permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                        )
                                                                );

                                                                for (DestinoMaisVisitadoDTO destino : destinos) {
                                                                    destinosDTO.add(
                                                                            new DestinoDTO(
                                                                                    destino.getDestino(),
                                                                                    permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                            )
                                                                    );
                                                                }

                                                                perfiesDTO.add(
                                                                        new PerfilDTO(
                                                                                porcentagemTuristas,
                                                                                paisOrigem,
                                                                                fichaSinteseBrasilDTO.getAno(),
                                                                                generoDTO.getGenero(),
                                                                                faixaEtariaDTO.getFaixa_etaria(),
                                                                                composicaoGrupoViagemDTO.getComposicao(),
                                                                                fonteInformacaoDTO.getFonte(),
                                                                                utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                motivo.getMotivo(),
                                                                                null,
                                                                                gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                destinosDTO

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



                            }

                        }

                    }

                }

            }
        }

        return perfiesDTO;
    }

    public List<PerfilDTO> transformFichaSintesePais(FichaSintesePaisDTO fichaSintesePaisDTO, String ufDesembarque) {

        List<PerfilDTO> perfiesDTO = new ArrayList<>();

        Double porcentagemTuristas = 1.0;

            for (FonteInformacaoDTO fonteInformacaoDTO : fichaSintesePaisDTO.getFontesInformacao()) {

                porcentagemTuristas *= fonteInformacaoDTO.getPorcentagem();

                for (ComposicaoGrupoViagemDTO composicaoGrupoViagemDTO : fichaSintesePaisDTO.getComposicaoGruposViagem()) {

                    porcentagemTuristas *= composicaoGrupoViagemDTO.getPorcentagem();

                    for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemDTO : fichaSintesePaisDTO.getUtilizacaoAgenciaViagemDTO()) {

                        porcentagemTuristas *= utilizacaoAgenciaViagemDTO.getPorcentagem();

                        for (GeneroDTO generoDTO : fichaSintesePaisDTO.getGeneroDTO()) {

                            porcentagemTuristas *= generoDTO.getPorcentagem();

                            for (FaixaEtariaDTO faixaEtariaDTO : fichaSintesePaisDTO.getFaixaEtariaDTO()) {

                                porcentagemTuristas *= faixaEtariaDTO.getPorcentagem();

                                for (MotivoViagemDTO motivo : fichaSintesePaisDTO.getMotivos()) {

                                    porcentagemTuristas *= motivo.getPorcentagem();

                                    for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoDTO : fichaSintesePaisDTO.getGastosMedioPerCapitaMotivo()) {

                                        if(gastoMedioPerCapitaMotivoDTO.getMotivo().equalsIgnoreCase(motivo.getMotivo())){

                                            for (PermanenciaMediaDTO permanenciaMediaDTO : fichaSintesePaisDTO.getPermanenciaMediaDTO()) {

                                                if(permanenciaMediaDTO.getMotivo().equalsIgnoreCase(motivo.getMotivo())){

                                                    for (int i = 0; i < 2; i++) {

                                                        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados;

                                                        if(motivo.getMotivo().equalsIgnoreCase("Lazer")) {

                                                            for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaSintesePaisDTO.getMotivacoesViagemLazer()) {

                                                                porcentagemTuristas *= motivacaoViagemLazerDTO.getPorcentagem() / 100;

                                                                // i = 0 indica que só viajou para o estado principal
                                                                if(i == 0){

                                                                    perfiesDTO.add(
                                                                            new PerfilDTO(
                                                                                    porcentagemTuristas,
                                                                                    fichaSintesePaisDTO.getPais(),
                                                                                    fichaSintesePaisDTO.getAno(),
                                                                                    generoDTO.getGenero(),
                                                                                    faixaEtariaDTO.getFaixa_etaria(),
                                                                                    composicaoGrupoViagemDTO.getComposicao(),
                                                                                    fonteInformacaoDTO.getFonte(),
                                                                                    utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                    motivo.getMotivo(),
                                                                                    motivacaoViagemLazerDTO.getMotivacao(),
                                                                                    gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                    Arrays.asList(
                                                                                            new DestinoDTO(
                                                                                                    ufDesembarque,
                                                                                                    permanenciaMediaDTO.getProcentagem()
                                                                                            ))

                                                                            )
                                                                    );

                                                                }

                                                                // i = 1 indica que viajou para mais estados
                                                                else if (i == 1){

                                                                    destinosMaisVisitados = transformDestinosMaisVisitados(fichaSintesePaisDTO.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                                                    for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {

                                                                        porcentagemTuristas *= destinos.getLast().getPorcentagem()/100;

                                                                        List<DestinoDTO> destinosDTO = new ArrayList<>();
                                                                        destinosDTO.add(
                                                                                new DestinoDTO(
                                                                                        ufDesembarque,
                                                                                        permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                                )
                                                                        );

                                                                        for (DestinoMaisVisitadoDTO destino : destinos) {
                                                                            destinosDTO.add(
                                                                                    new DestinoDTO(
                                                                                            destino.getDestino(),
                                                                                            permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                                    )
                                                                            );
                                                                        }

                                                                        perfiesDTO.add(
                                                                                new PerfilDTO(
                                                                                        porcentagemTuristas,
                                                                                        fichaSintesePaisDTO.getPais(),
                                                                                        fichaSintesePaisDTO.getAno(),
                                                                                        generoDTO.getGenero(),
                                                                                        faixaEtariaDTO.getFaixa_etaria(),
                                                                                        composicaoGrupoViagemDTO.getComposicao(),
                                                                                        fonteInformacaoDTO.getFonte(),
                                                                                        utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                        motivo.getMotivo(),
                                                                                        motivacaoViagemLazerDTO.getMotivacao(),
                                                                                        gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                        destinosDTO

                                                                                )
                                                                        );

                                                                    }

                                                                }

                                                            }

                                                        }else{
                                                            // i = 0 indica que só viajou para o estado principal
                                                            if(i == 0){

                                                                perfiesDTO.add(
                                                                        new PerfilDTO(
                                                                                porcentagemTuristas,
                                                                                fichaSintesePaisDTO.getPais(),
                                                                                fichaSintesePaisDTO.getAno(),
                                                                                generoDTO.getGenero(),
                                                                                faixaEtariaDTO.getFaixa_etaria(),
                                                                                composicaoGrupoViagemDTO.getComposicao(),
                                                                                fonteInformacaoDTO.getFonte(),
                                                                                utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                motivo.getMotivo(),
                                                                                null,
                                                                                gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                Arrays.asList(
                                                                                        new DestinoDTO(
                                                                                                ufDesembarque,
                                                                                                permanenciaMediaDTO.getProcentagem()
                                                                                        ))

                                                                        )
                                                                );

                                                            }

                                                            // i = 1 indica que viajou para mais estados
                                                            else if (i == 1){

                                                                destinosMaisVisitados = transformDestinosMaisVisitados(fichaSintesePaisDTO.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                                                for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {

                                                                    porcentagemTuristas *= destinos.getLast().getPorcentagem()/100;

                                                                    List<DestinoDTO> destinosDTO = new ArrayList<>();
                                                                    destinosDTO.add(
                                                                            new DestinoDTO(
                                                                                    ufDesembarque,
                                                                                    permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                            )
                                                                    );

                                                                    for (DestinoMaisVisitadoDTO destino : destinos) {
                                                                        destinosDTO.add(
                                                                                new DestinoDTO(
                                                                                        destino.getDestino(),
                                                                                        permanenciaMediaDTO.getProcentagem() / destinos.size() + 1
                                                                                )
                                                                        );
                                                                    }

                                                                    perfiesDTO.add(
                                                                            new PerfilDTO(
                                                                                    porcentagemTuristas,
                                                                                    fichaSintesePaisDTO.getPais(),
                                                                                    fichaSintesePaisDTO.getAno(),
                                                                                    generoDTO.getGenero(),
                                                                                    faixaEtariaDTO.getFaixa_etaria(),
                                                                                    composicaoGrupoViagemDTO.getComposicao(),
                                                                                    fonteInformacaoDTO.getFonte(),
                                                                                    utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                    motivo.getMotivo(),
                                                                                    null,
                                                                                    gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                    destinosDTO

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



                                }

                            }

                        }

                    }

                }
            }




        return perfiesDTO;
    }

    public List<PerfilDTO> transformPerfiesEstadoDTO(List<PerfilDTO> perfiesEstadoDTO, List<FichaSintesePaisDTO> fichasSintesePaisDTO, List<FichaSinteseBrasilDTO> fichasSinteseBrasilDTO){

        List<PerfilDTO> perfiesDTO = new ArrayList<>();

        Double porcentagemTuristas = 1.0;

        for (PerfilDTO perfilEstadoDTO : perfiesEstadoDTO) {

            porcentagemTuristas *= perfilEstadoDTO.getTaxaTuristas();

            for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {

                if(fichaSintesePaisDTO.getPais().equalsIgnoreCase(perfilEstadoDTO.getPaisesOrigem()) &&
                        fichaSintesePaisDTO.getAno().equals(perfilEstadoDTO.getAno())){

                    for (FonteInformacaoDTO fonteInformacaoPaisDTO : fichaSintesePaisDTO.getFontesInformacao()) {

                        if(fonteInformacaoPaisDTO.getFonte().equalsIgnoreCase(perfilEstadoDTO.getFonteInformacao())){

                            porcentagemTuristas *= fonteInformacaoPaisDTO.getPorcentagem()/100;

                        }

                        for (ComposicaoGrupoViagemDTO composicaoGrupoViagemPaisDTO : fichaSintesePaisDTO.getComposicaoGruposViagem()) {

                            if(composicaoGrupoViagemPaisDTO.getComposicao().equalsIgnoreCase(perfilEstadoDTO.getComposicaoGruposViagem())){

                                porcentagemTuristas *= composicaoGrupoViagemPaisDTO.getPorcentagem()/100;

                            }

                            for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemPaisDTO : fichaSintesePaisDTO.getUtilizacaoAgenciaViagemDTO()) {

                                if(utilizacaoAgenciaViagemPaisDTO.getTipo().equalsIgnoreCase(perfilEstadoDTO.getUtilizacaoAgenciaViagemDTO())){

                                    porcentagemTuristas *= utilizacaoAgenciaViagemPaisDTO.getPorcentagem()/100;

                                }

                                for (GeneroDTO generoPaisDto : fichaSintesePaisDTO.getGeneroDTO()) {

                                    if(generoPaisDto.getGenero().equalsIgnoreCase(perfilEstadoDTO.getGeneroDTO())){

                                        porcentagemTuristas *= generoPaisDto.getPorcentagem()/100;

                                        for (FaixaEtariaDTO faixaEtariaPaisDTO : fichaSintesePaisDTO.getFaixaEtariaDTO()) {

                                            if(faixaEtariaPaisDTO.getFaixa_etaria().equalsIgnoreCase(perfilEstadoDTO.getFaixaEtariaDTO())){

                                                porcentagemTuristas *= faixaEtariaPaisDTO.getPorcentagem()/100;

                                                for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoPaisDTO : fichaSintesePaisDTO.getGastosMedioPerCapitaMotivo()) {

                                                    if(gastoMedioPerCapitaMotivoPaisDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())){

                                                        for (PermanenciaMediaDTO permanenciaMediaDTO : fichaSintesePaisDTO.getPermanenciaMediaDTO()) {

                                                            if(permanenciaMediaDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())){

                                                                for (MotivoViagemDTO motivo : fichaSintesePaisDTO.getMotivos()) {

                                                                    if(motivo.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())){

                                                                        porcentagemTuristas *= faixaEtariaPaisDTO.getPorcentagem()/100;

                                                                        if(motivo.getMotivo().equalsIgnoreCase("Lazer")){

                                                                            for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaSintesePaisDTO.getMotivacoesViagemLazer()) {

                                                                                if(motivacaoViagemLazerDTO.getMotivacao().equalsIgnoreCase(perfilEstadoDTO.getMotivacaoViagemLazer())){

                                                                                    porcentagemTuristas *= faixaEtariaPaisDTO.getPorcentagem()/100;

                                                                                    for (FichaSinteseBrasilDTO fichaSinteseBrasilDTO : fichasSinteseBrasilDTO) {

                                                                                        if(fichaSinteseBrasilDTO.getAno().equals(perfilEstadoDTO.getAno())){

                                                                                            for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoBrasilDTO : fichaSinteseBrasilDTO.getGastosMedioPerCapitaMotivo()) {

                                                                                                if(gastoMedioPerCapitaMotivoBrasilDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())){

                                                                                                    Double gastoMedioPercapitaEstimado = gastoMedioPerCapitaMotivoPaisDTO.getGasto()  *
                                                                                                            (perfilEstadoDTO.getGastosMedioPerCapitaMotivo() /gastoMedioPerCapitaMotivoBrasilDTO.getGasto());

                                                                                                    perfiesDTO.add(
                                                                                                            new PerfilDTO(
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
                                                                                                                    perfilEstadoDTO.getDestinosDTO()
                                                                                                            )
                                                                                                    );

                                                                                                }

                                                                                            }

                                                                                        }

                                                                                    }

                                                                                }

                                                                            }


                                                                        }else{

                                                                            for (FichaSinteseBrasilDTO fichaSinteseBrasilDTO : fichasSinteseBrasilDTO) {

                                                                                if(fichaSinteseBrasilDTO.getAno().equals(perfilEstadoDTO.getAno())){

                                                                                    for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoBrasilDTO : fichaSinteseBrasilDTO.getGastosMedioPerCapitaMotivo()) {

                                                                                        if(gastoMedioPerCapitaMotivoBrasilDTO.getMotivo().equalsIgnoreCase(perfilEstadoDTO.getMotivo())){

                                                                                            Double gastoMedioPercapitaEstimado = gastoMedioPerCapitaMotivoPaisDTO.getGasto()  *
                                                                                                    (perfilEstadoDTO.getGastosMedioPerCapitaMotivo() /gastoMedioPerCapitaMotivoBrasilDTO.getGasto());

                                                                                            perfiesDTO.add(
                                                                                                    new PerfilDTO(
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
                                                                                                            perfilEstadoDTO.getDestinosDTO()
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

    public List<PerfilDTO> transformFichasSinteseEstadoDTO(List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO){

        List<PerfilDTO> PerfiesEstadoDTO = new ArrayList<>();

        for (FichaSinteseEstadoDTO fichaSinteseEstadoDTO : fichasSinteseEstadoDTO) {

            Double porcentagemTuristas = 1.0;

            for (PaisOrigemDTO paisOrigemDTO : fichaSinteseEstadoDTO.getPaisesOrigem()) {

                porcentagemTuristas *= paisOrigemDTO.getPorcentagem()/100;

                for (FonteInformacaoDTO fonteInformacaoDTO : fichaSinteseEstadoDTO.getFontesInformacao()) {

                    porcentagemTuristas *= fonteInformacaoDTO.getPorcentagem()/100;

                    for (ComposicaoGrupoViagemDTO composicaoGrupoViagemDTO : fichaSinteseEstadoDTO.getComposicaoGruposViagem()) {

                        porcentagemTuristas *= composicaoGrupoViagemDTO.getPorcentagem()/100;

                        for (UtilizacaoAgenciaViagemDTO utilizacaoAgenciaViagemDTO : fichaSinteseEstadoDTO.getUtilizacaoAgenciaViagemDTO()) {

                            porcentagemTuristas *= utilizacaoAgenciaViagemDTO.getPorcentagem()/100;

                            for (GeneroDTO generoDTO : fichaSinteseEstadoDTO.getGeneroDTO()) {

                                porcentagemTuristas *= generoDTO.getPorcentagem()/100;

                                for (FaixaEtariaDTO faixaEtariaDTO : fichaSinteseEstadoDTO.getFaixaEtariaDTO()) {

                                    porcentagemTuristas *= faixaEtariaDTO.getPorcentagem()/100;

                                    for (MotivoViagemDTO motivo : fichaSinteseEstadoDTO.getMotivos()) {

                                        porcentagemTuristas *= motivo.getPorcentagem()/100;

                                        for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoDTO : fichaSinteseEstadoDTO.getGastosMedioPerCapitaMotivo()) {

                                            if (gastoMedioPerCapitaMotivoDTO.getMotivo().equalsIgnoreCase(motivo.getMotivo())){

                                                for (PermanenciaMediaDTO permanenciaMediaDTO : fichaSinteseEstadoDTO.getPermanenciaMediaDTO()) {

                                                    if(permanenciaMediaDTO.getMotivo().equalsIgnoreCase(motivo.getMotivo())){

                                                        for (PermanenciaMediaDTO permanenciaMediaDestinoPrincipalDTO : fichaSinteseEstadoDTO.getPermanenciaMediaDTODestinoPrincipal()) {

                                                            if(permanenciaMediaDestinoPrincipalDTO.getMotivo().equalsIgnoreCase(motivo.getMotivo())){

                                                                for (int i = 0; i < 2; i++) {

                                                                    List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados;

                                                                    if(motivo.getMotivo().equalsIgnoreCase("Lazer")) {

                                                                        for (MotivacaoViagemLazerDTO motivacaoViagemLazerDTO : fichaSinteseEstadoDTO.getMotivacoesViagemLazer()) {

                                                                            porcentagemTuristas *= motivacaoViagemLazerDTO.getPorcentagem() / 100;

                                                                            // i = 0 indica que só viajou para o estado principal
                                                                            if(i == 0){

                                                                                PerfiesEstadoDTO.add(
                                                                                        new PerfilDTO(
                                                                                                porcentagemTuristas,
                                                                                                paisOrigemDTO.getPais(),
                                                                                                fichaSinteseEstadoDTO.getAno(),
                                                                                                generoDTO.getGenero(),
                                                                                                faixaEtariaDTO.getFaixa_etaria(),
                                                                                                composicaoGrupoViagemDTO.getComposicao(),
                                                                                                fonteInformacaoDTO.getFonte(),
                                                                                                utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                                motivo.getMotivo(),
                                                                                                motivacaoViagemLazerDTO.getMotivacao(),
                                                                                                gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                                Arrays.asList(
                                                                                                        new DestinoDTO(
                                                                                                                fichaSinteseEstadoDTO.getDestinoPrincipal(),
                                                                                                                permanenciaMediaDTO.getProcentagem() * permanenciaMediaDestinoPrincipalDTO.getProcentagem() / 2
                                                                                                        ))

                                                                                        )
                                                                                );

                                                                            }

                                                                            // i = 1 indica que viajou para mais estados
                                                                            else if (i == 1){

                                                                                destinosMaisVisitados = transformDestinosMaisVisitados(fichaSinteseEstadoDTO.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                                                                for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {

                                                                                    porcentagemTuristas *= destinos.getLast().getPorcentagem()/100;

                                                                                    List<DestinoDTO> destinosDTO = new ArrayList<>();
                                                                                    destinosDTO.add(
                                                                                            new DestinoDTO(
                                                                                                    fichaSinteseEstadoDTO.getDestinoPrincipal(),
                                                                                                    permanenciaMediaDestinoPrincipalDTO.getProcentagem()
                                                                                            )
                                                                                    );

                                                                                    for (DestinoMaisVisitadoDTO destino : destinos) {
                                                                                        destinosDTO.add(
                                                                                                new DestinoDTO(
                                                                                                        destino.getDestino(),
                                                                                                        (permanenciaMediaDTO.getProcentagem() - permanenciaMediaDestinoPrincipalDTO.getProcentagem()) / destinos.size()
                                                                                                )
                                                                                        );
                                                                                    }

                                                                                    PerfiesEstadoDTO.add(
                                                                                            new PerfilDTO(
                                                                                                    porcentagemTuristas,
                                                                                                    paisOrigemDTO.getPais(),
                                                                                                    fichaSinteseEstadoDTO.getAno(),
                                                                                                    generoDTO.getGenero(),
                                                                                                    faixaEtariaDTO.getFaixa_etaria(),
                                                                                                    composicaoGrupoViagemDTO.getComposicao(),
                                                                                                    fonteInformacaoDTO.getFonte(),
                                                                                                    utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                                    motivo.getMotivo(),
                                                                                                    motivacaoViagemLazerDTO.getMotivacao(),
                                                                                                    gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                                    destinosDTO

                                                                                            )
                                                                                    );

                                                                                }

                                                                            }

                                                                        }

                                                                    }else{
                                                                        // i = 0 indica que só viajou para o estado principal
                                                                        if(i == 0){

                                                                            PerfiesEstadoDTO.add(
                                                                                    new PerfilDTO(
                                                                                            porcentagemTuristas,
                                                                                            paisOrigemDTO.getPais(),
                                                                                            fichaSinteseEstadoDTO.getAno(),
                                                                                            generoDTO.getGenero(),
                                                                                            faixaEtariaDTO.getFaixa_etaria(),
                                                                                            composicaoGrupoViagemDTO.getComposicao(),
                                                                                            fonteInformacaoDTO.getFonte(),
                                                                                            utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                            motivo.getMotivo(),
                                                                                            null,
                                                                                            gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                            Arrays.asList(
                                                                                                    new DestinoDTO(
                                                                                                            fichaSinteseEstadoDTO.getDestinoPrincipal(),
                                                                                                            permanenciaMediaDTO.getProcentagem() * permanenciaMediaDestinoPrincipalDTO.getProcentagem() / 2
                                                                                                    ))

                                                                                    )
                                                                            );

                                                                        }

                                                                        // i = 1 indica que viajou para mais estados
                                                                        else if (i == 1){

                                                                            destinosMaisVisitados = transformDestinosMaisVisitados(fichaSinteseEstadoDTO.getDestinosMaisVisistadosMotivo(), motivo.getMotivo());

                                                                            for (List<DestinoMaisVisitadoDTO> destinos : destinosMaisVisitados) {

                                                                                porcentagemTuristas *= destinos.getLast().getPorcentagem()/100;

                                                                                List<DestinoDTO> destinosDTO = new ArrayList<>();
                                                                                destinosDTO.add(
                                                                                        new DestinoDTO(
                                                                                                fichaSinteseEstadoDTO.getDestinoPrincipal(),
                                                                                                permanenciaMediaDestinoPrincipalDTO.getProcentagem()
                                                                                        )
                                                                                );

                                                                                for (DestinoMaisVisitadoDTO destino : destinos) {
                                                                                    destinosDTO.add(
                                                                                            new DestinoDTO(
                                                                                                    destino.getDestino(),
                                                                                                    (permanenciaMediaDTO.getProcentagem() - permanenciaMediaDestinoPrincipalDTO.getProcentagem()) / destinos.size()
                                                                                            )
                                                                                    );
                                                                                }

                                                                                PerfiesEstadoDTO.add(
                                                                                        new PerfilDTO(
                                                                                                porcentagemTuristas,
                                                                                                paisOrigemDTO.getPais(),
                                                                                                fichaSinteseEstadoDTO.getAno(),
                                                                                                generoDTO.getGenero(),
                                                                                                faixaEtariaDTO.getFaixa_etaria(),
                                                                                                composicaoGrupoViagemDTO.getComposicao(),
                                                                                                fonteInformacaoDTO.getFonte(),
                                                                                                utilizacaoAgenciaViagemDTO.getTipo(),
                                                                                                motivo.getMotivo(),
                                                                                                null,
                                                                                                gastoMedioPerCapitaMotivoDTO.getGasto(),
                                                                                                destinosDTO

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

        return PerfiesEstadoDTO;
    }

    public List<List<DestinoMaisVisitadoDTO>> transformDestinosMaisVisitados(
            List<DestinosMaisVisitadosPorMotivoDTO> listaDestinosMaisVisitadosPorMotivoDTO,
            String motivo) {

        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados = new ArrayList<>();

        for (DestinosMaisVisitadosPorMotivoDTO dto : listaDestinosMaisVisitadosPorMotivoDTO) {
            if (motivo.equalsIgnoreCase(dto.getMotivo())) {
                List<DestinoMaisVisitadoDTO> destinos = dto.getDestinos_mais_visistado();
                gerarCombinacoes(destinos, new ArrayList<>(), destinosMaisVisitados, 0);
            }
        }

        return destinosMaisVisitados;
    }

    private void gerarCombinacoes(
            List<DestinoMaisVisitadoDTO> destinosOriginais,
            List<DestinoMaisVisitadoDTO> combinacaoAtual,
            List<List<DestinoMaisVisitadoDTO>> resultados,
            int inicio) {

        if (!combinacaoAtual.isEmpty()) {
            // List para armazenar a combinação com porcentagens acumuladas
            List<DestinoMaisVisitadoDTO> combinacaoComPorcentagem = new ArrayList<>();
            double porcentagemAcumulada = 1.0;

            // Para cada destino na combinação, calculamos a porcentagem acumulada
            for (DestinoMaisVisitadoDTO d : combinacaoAtual) {
                porcentagemAcumulada *= d.getPorcentagem();  // Acumula a porcentagem
                // Cria um novo DestinoMaisVisitadoDTO com a porcentagem acumulada
                combinacaoComPorcentagem.add(new DestinoMaisVisitadoDTO(d.getDestino(), porcentagemAcumulada));
            }

            // Adiciona a combinação com porcentagens acumuladas no resultado final
            resultados.add(combinacaoComPorcentagem);
        }

        // Gera todas as combinações possíveis, usando recursão
        for (int i = inicio; i < destinosOriginais.size(); i++) {
            combinacaoAtual.add(destinosOriginais.get(i));
            gerarCombinacoes(destinosOriginais, combinacaoAtual, resultados, i + 1);
            combinacaoAtual.remove(combinacaoAtual.size() - 1);  // Volta para a combinação anterior
        }
    }


    public boolean hasPais (List<PerfilDTO> perfiesDTO, String nomePais) {

        for (PerfilDTO perfilDTO : perfiesDTO) {
            if (perfilDTO.getPaisesOrigem().equalsIgnoreCase(nomePais)) {
                return true;
            }
        }

        return false;
    }

    public boolean fichaSintesePaisDTOHasPais (List<FichaSintesePaisDTO> fichasSintesePaisDTO, String nomePais) {

        for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {
            if (fichaSintesePaisDTO.getPais().equalsIgnoreCase(nomePais)) {
                return true;
            }
        }

        return false;
    }

    public boolean fichaSintesePaisDTOHasAno (List<FichaSintesePaisDTO> fichasSintesePaisDTO, Integer ano) {

        for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {
            if (fichaSintesePaisDTO.getAno().equals(ano)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasEstado (List<PerfilDTO> perfiesDTO, String nomeEstado) {

        for (PerfilDTO perfilDTO : perfiesDTO) {
            if (perfilDTO.getDestinosDTO().getFirst().getDestino().equalsIgnoreCase(nomeEstado)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAno (List<PerfilDTO> perfiesDTO, Integer ano) {

        for (PerfilDTO perfilDTO : perfiesDTO) {
            if (perfilDTO.getAno().equals(ano)) {
                return true;
            }
        }

        return false;
    }


}
