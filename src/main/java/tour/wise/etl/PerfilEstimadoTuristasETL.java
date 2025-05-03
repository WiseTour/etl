package tour.wise.etl;

import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.perfil.DestinoDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.etl.fichas.sintese.FichaSinteseBrasilET;
import tour.wise.etl.fichas.sintese.Ficha_Sintese_Estado_ET;
import tour.wise.etl.fichas.sintese.FichaSintesePaisET;
import tour.wise.model.Perfil_Estimado_Turistas;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PerfilEstimadoTuristasETL {

    public void exe(String fonte_perfil, String fonte_chegadas, String fileNameFichaSintesePais, String fileNameFichaSinteseBrasil,String fileNameFichaSinteseEstado, String fileNameChegadas) throws IOException {


        // EXTRACT

        FichaSinteseBrasilET fichaSinteseBrasilET = new FichaSinteseBrasilET();

        List<FichaSinteseBrasilDTO> fichasSinteseBrasil = fichaSinteseBrasilET.extractTransform(fileNameFichaSinteseBrasil, 0, 0);

        FichaSintesePaisET fichaSintesePaisET = new FichaSintesePaisET();

        List<FichaSintesePaisDTO> fichasSintesePaisDTO = fichaSintesePaisET.extractTransformFicha_Sintese_Pais(fileNameFichaSintesePais,0, 0);

        Ficha_Sintese_Estado_ET fichaSinteseEstadoEt = new Ficha_Sintese_Estado_ET();

        List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO =  fichaSinteseEstadoEt.extractTransformFicha_Sintese_Estado(fileNameFichaSinteseEstado, 0, 0);

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

        for (FichaSinteseBrasilDTO fichaSinteseBrasilDTO : fichasSinteseBrasil) {
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

        List<Perfil_Estimado_Turistas>  perfiesEstimadoTuristas = new ArrayList<>();




        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegadaTuristasInternacionaisBrasilMensalDTO : chegadasTuristasInternacionaisBrasilMensalDTO) {

            Integer totalChegadas = chegadaTuristasInternacionaisBrasilMensalDTO.getQtdChegadas();
            String paisOrigem = chegadaTuristasInternacionaisBrasilMensalDTO.getPaisOrigem();
            String ufDesembarque = chegadaTuristasInternacionaisBrasilMensalDTO.getUfDestino();
            String viaAcesso = chegadaTuristasInternacionaisBrasilMensalDTO.getViaAcesso();
            Integer ano = chegadaTuristasInternacionaisBrasilMensalDTO.getAno();
            Integer mes = chegadaTuristasInternacionaisBrasilMensalDTO.getMes();



        }

//            for (FichaSintesePaisDTO ficha_sintese_pais : fichas_sintese_por_pais) {
//
//                        if(ficha_sintese_pais.getPais().equalsIgnoreCase(pais) && ficha_sintese_pais.getAno().equals(ano)){
//
//                            for (FonteInformacaoDTO fonte_informacao_DTO_pais : ficha_sintese_pais.getFontes_informacao()) {
//
//                                for (ComposicaoGrupoViagemDTO composicao_grupo_viagem_DTO_pais : ficha_sintese_pais.getComposicao_grupos_viagem()) {
//
//                                    for (UtilizacaaAgenciaViagemDTO utilizacaa_agencia_viagem_DTO_pais : ficha_sintese_pais.getUtilizacao_agencia_viagem()) {
//
//                                        for (MotivoViagemDTO motivo_viagem_DTO_pais : ficha_sintese_pais.getMotivos()) {
//
//                                            Double totalChegadasFiltrada = (
//                                                    totalChegadas // totalChegadas = TOTAL DE CHEGADAS DAQUELE ESTADO DAQUELA NACIONALIDADE (PESQUISAR NO BD)
//                                                            * fonte_informacao_DTO_pais.getPorcentagem()/100
//                                                            * composicao_grupo_viagem_DTO_pais.getPorcentagem()/100
//                                                            * utilizacaa_agencia_viagem_DTO_pais.getPorcentagem()/100
//                                                            * motivo_viagem_DTO_pais.getPorcentagem()
//                                            );
//
//                                            for (DestinosMaisVisitadosPorMotivoDTO destinosMaisVisistadosPorMotivo : ficha_sintese_pais.getDestinos_mais_visistados_por_motivo()) {
//                                                if(destinosMaisVisistadosPorMotivo.getMotivo().equalsIgnoreCase(motivo_viagem_DTO_pais.getMotivo())){
//                                                    for (DestinoMaisVisitadoDTO destino : destinosMaisVisistadosPorMotivo.getDestinos_mais_visistado()) {
//                                                        if(destino.getDestino().equalsIgnoreCase(uf_destino)){
//                                                            totalChegadasFiltrada *= destino.getPorcentagem()/100;
//                                                        }
//                                                    }
//                                                }
//                                            }
//
//                                            if(motivo_viagem_DTO_pais.getMotivo().equalsIgnoreCase("Lazer")){
//
//                                                for (MotivacaoViagemLazerDTO motivacao_viagem_lazer_DTO_pais : ficha_sintese_pais.getMotivacoes_viagem_lazer()) {
//
//                                                    totalChegadasFiltrada *= (motivacao_viagem_lazer_DTO_pais.getPorcentagem()/100);
//
//                                                    for (FichaSinteseEstadoDTO fichaSinteseEstado : fichas_sintese_por_estado) {
//
//                                                        if(fichaSinteseEstado.getDestino_princiapal().equalsIgnoreCase(uf_destino) && fichaSinteseEstado.getAno().equals(ficha_sintese_pais.getAno())){
//
//                                                            FonteInformacaoDTO fonte_informacao_DTO_estado = fichaSinteseEstado.getFontes_informacao().stream()
//                                                                    .filter(item -> item.getFonte().equalsIgnoreCase(fonte_informacao_DTO_pais.getFonte()))
//                                                                    .findFirst()
//                                                                    .orElse(null);
//
//                                                            ComposicaoGrupoViagemDTO composicao_grupo_viagem_DTO_estado = fichaSinteseEstado.getComposicao_grupos_viagem().stream()
//                                                                    .filter(item -> item.getComposicao().equalsIgnoreCase(composicao_grupo_viagem_DTO_pais.getComposicao()))
//                                                                    .findFirst()
//                                                                    .orElse(null);
//
//                                                            UtilizacaaAgenciaViagemDTO utilizacaa_agencia_viagem_DTO_estado = fichaSinteseEstado.getUtilizacao_agencia_viagem().stream()
//                                                                    .filter(item -> item.getTipo().equalsIgnoreCase(utilizacaa_agencia_viagem_DTO_pais.getTipo()))
//                                                                    .findFirst()
//                                                                    .orElse(null);
//
//                                                            MotivoViagemDTO motivo_viagem_DTO_estado = fichaSinteseEstado.getMotivos().stream()
//                                                                    .filter(item -> item.getMotivo().equalsIgnoreCase(motivo_viagem_DTO_pais.getMotivo()))
//                                                                    .findFirst()
//                                                                    .orElse(null);
//
//                                                            MotivacaoViagemLazerDTO motivacao_viagem_lazer_DTO_estado = fichaSinteseEstado.getMotivacoes_viagem_lazer().stream()
//                                                                    .filter(item -> item.getMotivacao().equalsIgnoreCase(motivacao_viagem_lazer_DTO_pais.getMotivacao()))
//                                                                    .findFirst()
//                                                                    .orElse(null);
//
//                                                            if(fonte_informacao_DTO_estado != null) {
//
//                                                                totalChegadasFiltrada *= (fonte_informacao_DTO_estado.getPorcentagem()/100);
//
//                                                            }
//
//                                                            if(composicao_grupo_viagem_DTO_estado != null) {
//
//                                                                totalChegadasFiltrada *= (composicao_grupo_viagem_DTO_estado.getPorcentagem()/100);
//
//                                                            }
//
//                                                            if(utilizacaa_agencia_viagem_DTO_estado != null) {
//
//                                                                totalChegadasFiltrada *= (utilizacaa_agencia_viagem_DTO_estado.getPorcentagem()/100);
//
//                                                            }
//
//                                                            if(motivo_viagem_DTO_estado != null) {
//
//                                                                totalChegadasFiltrada *= (motivo_viagem_DTO_estado.getPorcentagem()/100);
//
//                                                            }
//
//
//                                                            if(motivacao_viagem_lazer_DTO_estado != null) {
//
//                                                                totalChegadasFiltrada *= (
//                                                                        motivacao_viagem_lazer_DTO_pais.getPorcentagem()/100
//                                                                                * motivacao_viagem_lazer_DTO_estado.getPorcentagem()/100
//
//                                                                );
//
//                                                            }
//
//                                                            if((int) Math.round(totalChegadasFiltrada) > 0){
//                                                                perfis_estimado_turistas.add(
//                                                                        new Perfil_Estimado_Turistas(
//                                                                                fonte_perfil,
//                                                                                fonte_chegadas,
//                                                                                (int) Math.round(totalChegadasFiltrada),
//                                                                                ficha_sintese_pais.getAno(),
//                                                                                fichaSinteseEstado.getDestino_princiapal(),
//                                                                                ((FichaSintesePaisDTO) ficha_sintese_pais).getPais(),
//                                                                                null,
//                                                                                null,
//                                                                                composicao_grupo_viagem_DTO_pais.getComposicao(),
//                                                                                fonte_informacao_DTO_pais.getFonte(),
//                                                                                utilizacaa_agencia_viagem_DTO_pais.getTipo(),
//                                                                                motivo_viagem_DTO_pais.getMotivo(),
//                                                                                motivacao_viagem_lazer_DTO_pais.getMotivacao()
//                                                                        )
//                                                                );
//                                                            }
//                                                        }
//
//                                                    }
//
//                                                }
//
//                                            }else {
//
//                                                for (FichaSinteseEstadoDTO fichaSinteseEstado : fichas_sintese_por_estado) {
//
//                                                    if(fichaSinteseEstado.getDestino_princiapal().equalsIgnoreCase(uf_destino) && fichaSinteseEstado.getAno().equals(ficha_sintese_pais.getAno())){
//
//                                                        FonteInformacaoDTO fonte_informacao_DTO_estado = fichaSinteseEstado.getFontes_informacao().stream()
//                                                                .filter(item -> item.getFonte().equalsIgnoreCase(fonte_informacao_DTO_pais.getFonte()))
//                                                                .findFirst()
//                                                                .orElse(null);
//
//                                                        ComposicaoGrupoViagemDTO composicao_grupo_viagem_DTO_estado = fichaSinteseEstado.getComposicao_grupos_viagem().stream()
//                                                                .filter(item -> item.getComposicao().equalsIgnoreCase(composicao_grupo_viagem_DTO_pais.getComposicao()))
//                                                                .findFirst()
//                                                                .orElse(null);
//
//                                                        UtilizacaaAgenciaViagemDTO utilizacaa_agencia_viagem_DTO_estado = fichaSinteseEstado.getUtilizacao_agencia_viagem().stream()
//                                                                .filter(item -> item.getTipo().equalsIgnoreCase(utilizacaa_agencia_viagem_DTO_pais.getTipo()))
//                                                                .findFirst()
//                                                                .orElse(null);
//
//                                                        MotivoViagemDTO motivo_viagem_DTO_estado = fichaSinteseEstado.getMotivos().stream()
//                                                                .filter(item -> item.getMotivo().equalsIgnoreCase(motivo_viagem_DTO_pais.getMotivo()))
//                                                                .findFirst()
//                                                                .orElse(null);
//
//
//                                                        if(fonte_informacao_DTO_estado != null) {
//
//                                                            totalChegadasFiltrada *= (fonte_informacao_DTO_estado.getPorcentagem()/100);
//
//                                                        }
//
//                                                        if(composicao_grupo_viagem_DTO_estado != null) {
//
//                                                            totalChegadasFiltrada *= (composicao_grupo_viagem_DTO_estado.getPorcentagem()/100);
//
//                                                        }
//
//                                                        if(utilizacaa_agencia_viagem_DTO_estado != null) {
//
//                                                            totalChegadasFiltrada *= (utilizacaa_agencia_viagem_DTO_estado.getPorcentagem()/100);
//
//                                                        }
//
//                                                        if(motivo_viagem_DTO_estado != null) {
//
//                                                            totalChegadasFiltrada *= (motivo_viagem_DTO_estado.getPorcentagem()/100);
//
//                                                        }
//
//                                                        if((int) Math.round(totalChegadasFiltrada) > 0){
//                                                            perfis_estimado_turistas.add(
//                                                                    new Perfil_Estimado_Turistas(
//                                                                            fonte_perfil,
//                                                                            fonte_chegadas,
//                                                                            (int) Math.round(totalChegadasFiltrada),
//                                                                            ficha_sintese_pais.getAno(),
//                                                                            fichaSinteseEstado.getDestino_princiapal(),
//                                                                            ((FichaSintesePaisDTO) ficha_sintese_pais).getPais(),
//                                                                            null,
//                                                                            null,
//                                                                            composicao_grupo_viagem_DTO_pais.getComposicao(),
//                                                                            fonte_informacao_DTO_pais.getFonte(),
//                                                                            utilizacaa_agencia_viagem_DTO_pais.getTipo(),
//                                                                            motivo_viagem_DTO_pais.getMotivo(),
//                                                                            null
//                                                                    )
//                                                            );
//                                                        }
//                                                    }
//
//                                                }
//
//
//
//
//                                            }
//                                        }
//
//
//                                    }
//
//                                }
//
//                            }
//
//                        }
//
//                    }
//
//                    return perfis_estimado_turistas;
//                    }













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

    public List<PerfilDTO> transformPerfiesEstadoDTO(List<PerfilDTO> perfiesEstadoDTO, List<FichaSintesePaisDTO> fichasSintesePaisDTO){

        List<PerfilDTO> perfiesDTO = new ArrayList<>();

        for (PerfilDTO perfilEstadoDTO : perfiesEstadoDTO) {

            Double porcentagemTuristas = perfilEstadoDTO.getTaxaTuristas();

            for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {

                if(fichaSintesePaisDTO.getPais().equalsIgnoreCase(perfilEstadoDTO.getPaisesOrigem()) &&
                        fichaSintesePaisDTO.getAno().equals(perfilEstadoDTO.getAno())){

                    for (FonteInformacaoDTO fonteInformacaoPaisDTO : fichaSintesePaisDTO.getFontesInformacao()) {

                        if(fonteInformacaoPaisDTO.getFonte().equalsIgnoreCase(perfilEstadoDTO.getFontesInformacao())){

                            porcentagemTuristas *= fonteInformacaoPaisDTO.getPorcentagem();

                        }

                        for (ComposicaoGrupoViagemDTO composicaoGrupoViagemPaisDTO : fichaSintesePaisDTO.getComposicaoGruposViagem()) {

                            if(composicaoGrupoViagemPaisDTO.getComposicao().equalsIgnoreCase(perfilEstadoDTO.getComposicaoGruposViagem())){

                                porcentagemTuristas *= composicaoGrupoViagemPaisDTO.getPorcentagem();

                            }

                            for (UtilizacaaAgenciaViagemDTO utilizacaoAgenciaViagemPaisDTO : fichaSintesePaisDTO.getUtilizacaoAgenciaViagemDTO()) {

                                if(utilizacaoAgenciaViagemPaisDTO.getTipo().equalsIgnoreCase(perfilEstadoDTO.getUtilizacaoAgenciaViagemDTO())){

                                    porcentagemTuristas *= utilizacaoAgenciaViagemPaisDTO.getPorcentagem();

                                }

                                for (GeneroDTO generoPaisDto : fichaSintesePaisDTO.getGeneroDTO()) {

                                    if(generoPaisDto.getGenero().equalsIgnoreCase(perfilEstadoDTO.getGeneroDTO())){

                                        porcentagemTuristas *= generoPaisDto.getPorcentagem();

                                        for (FaixaEtariaDTO faixaEtariaPaisDTO : fichaSintesePaisDTO.getFaixaEtariaDTO()) {

                                            if(faixaEtariaPaisDTO.getFaixa_etaria().equalsIgnoreCase(perfilEstadoDTO.getFaixaEtariaDTO())){

                                                porcentagemTuristas *= faixaEtariaPaisDTO.getPorcentagem();

                                                for (GastoMedioPerCapitaMotivoDTO gastoMedioPerCapitaMotivoDTO : fichaSintesePaisDTO.getGastosMedioPerCapitaMotivo()) {

                                                    if(gastoMedioPerCapitaMotivoDTO.getMotivo().equalsIgnoreCase())

                                                }

                                                perfiesDTO.add(
                                                        new PerfilDTO(
                                                                porcentagemTuristas,
                                                                perfilEstadoDTO.getPaisesOrigem(),
                                                                perfilEstadoDTO.getAno(),
                                                                perfilEstadoDTO.getGeneroDTO(),
                                                                perfilEstadoDTO.getFaixaEtariaDTO(),
                                                                perfilEstadoDTO.getComposicaoGruposViagem(),
                                                                perfilEstadoDTO.getFontesInformacao(),
                                                                perfilEstadoDTO.getUtilizacaoAgenciaViagemDTO(),
                                                                perfilEstadoDTO.getMotivo(),
                                                                perfilEstadoDTO.getMotivacaoViagemLazer(),
                                                                perfilEstadoDTO.getGastosMedioPerCapitaMotivo(),


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

        return perfiesDTO;
    }

    public List<PerfilDTO> transformFichasSinteseEstadoDTO(List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO){

        List<PerfilDTO> PerfiesEstadoDTO = new ArrayList<>();

        for (FichaSinteseEstadoDTO fichaSinteseEstadoDTO : fichasSinteseEstadoDTO) {

            Double porcentagemTuristas = 0.0;

            for (PaisOrigemDTO paisOrigemDTO : fichaSinteseEstadoDTO.getPaisesOrigem()) {

                porcentagemTuristas += paisOrigemDTO.getPorcentagem()/100;

                for (FonteInformacaoDTO fonteInformacaoDTO : fichaSinteseEstadoDTO.getFontesInformacao()) {

                    porcentagemTuristas *= fonteInformacaoDTO.getPorcentagem()/100;

                    for (ComposicaoGrupoViagemDTO composicaoGrupoViagemDTO : fichaSinteseEstadoDTO.getComposicaoGruposViagem()) {

                        porcentagemTuristas *= composicaoGrupoViagemDTO.getPorcentagem()/100;

                        for (UtilizacaaAgenciaViagemDTO utilizacaoAgenciaViagemDTO : fichaSinteseEstadoDTO.getUtilizacaoAgenciaViagemDTO()) {

                            porcentagemTuristas *= utilizacaoAgenciaViagemDTO.getPorcentagem()/100;

                            for (GeneroDTO generoDTO : fichaSinteseEstadoDTO.getGeneroDTO()) {

                                porcentagemTuristas *= generoDTO.getPorcentagem()/100;

                                for (FaixaEtariaDTO faixaEtariaDTO : fichaSinteseEstadoDTO.getFaixaEtariaDTO()) {

                                    porcentagemTuristas *= faixaEtariaDTO.getPorcentagem()/100;

                                    for (MotivoViagemDTO motivo : fichaSinteseEstadoDTO.getMotivos()) {

                                        porcentagemTuristas *= motivo.getPorcentagem()/100;

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
                                                                        fichaSinteseEstadoDTO.getGastosMedioPerCapitaMotivo().getLast().getGasto(),
                                                                        Arrays.asList(
                                                                                new DestinoDTO(
                                                                                        fichaSinteseEstadoDTO.getDestinoPrincipal(),
                                                                                        fichaSinteseEstadoDTO.getPermanenciaMediaDTO().getLast().getProcentagem() * fichaSinteseEstadoDTO.getPermanenciaMediaDTODestinoPrincipal().getLast().getProcentagem() / 2
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
                                                                            fichaSinteseEstadoDTO.getPermanenciaMediaDTODestinoPrincipal().getLast().getProcentagem()
                                                                    )
                                                            );

                                                            for (DestinoMaisVisitadoDTO destino : destinos) {
                                                                destinosDTO.add(
                                                                        new DestinoDTO(
                                                                                destino.getDestino(),
                                                                                (fichaSinteseEstadoDTO.getPermanenciaMediaDTO().getLast().getProcentagem() - fichaSinteseEstadoDTO.getPermanenciaMediaDTODestinoPrincipal().getLast().getProcentagem()) / destinos.size()
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
                                                                            fichaSinteseEstadoDTO.getGastosMedioPerCapitaMotivo().getLast().getGasto(),
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
                                                                    fichaSinteseEstadoDTO.getGastosMedioPerCapitaMotivo().getLast().getGasto(),
                                                                    Arrays.asList(
                                                                            new DestinoDTO(
                                                                                    fichaSinteseEstadoDTO.getDestinoPrincipal(),
                                                                                    fichaSinteseEstadoDTO.getPermanenciaMediaDTO().getLast().getProcentagem() * fichaSinteseEstadoDTO.getPermanenciaMediaDTODestinoPrincipal().getLast().getProcentagem() / 2
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
                                                                        fichaSinteseEstadoDTO.getPermanenciaMediaDTODestinoPrincipal().getLast().getProcentagem()
                                                                )
                                                        );

                                                        for (DestinoMaisVisitadoDTO destino : destinos) {
                                                            destinosDTO.add(
                                                                    new DestinoDTO(
                                                                            destino.getDestino(),
                                                                            (fichaSinteseEstadoDTO.getPermanenciaMediaDTO().getLast().getProcentagem() - fichaSinteseEstadoDTO.getPermanenciaMediaDTODestinoPrincipal().getLast().getProcentagem()) / destinos.size()
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
                                                                        fichaSinteseEstadoDTO.getGastosMedioPerCapitaMotivo().getLast().getGasto(),
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

        return PerfiesEstadoDTO;
    }

    public List<List<DestinoMaisVisitadoDTO>> transformDestinosMaisVisitados (List<DestinosMaisVisitadosPorMotivoDTO> listaDestinosMaisVisitadosPorMotivoDTO, String motivo){

        List<List<DestinoMaisVisitadoDTO>> destinosMaisVisitados = new ArrayList<>();

        for (DestinosMaisVisitadosPorMotivoDTO destinosMaisVisitadosPorMotivoDTO : listaDestinosMaisVisitadosPorMotivoDTO) {
            if(motivo.equalsIgnoreCase(destinosMaisVisitadosPorMotivoDTO.getMotivo())){

                destinosMaisVisitados.add(
                        Arrays.asList(destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0))
                );
                destinosMaisVisitados.add(Arrays.asList(
                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0),
                        new DestinoMaisVisitadoDTO(
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1).getDestino(),
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1).getPorcentagem() *
                                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0).getPorcentagem())
                ));
                destinosMaisVisitados.add(Arrays.asList(
                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0),
                        new DestinoMaisVisitadoDTO(
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(2).getDestino(),
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(2).getPorcentagem() *
                                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0).getPorcentagem())
                ));
                destinosMaisVisitados.add(Arrays.asList(
                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0),
                        new DestinoMaisVisitadoDTO(
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1).getDestino(),
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1).getPorcentagem() *
                                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0).getPorcentagem()),
                        new DestinoMaisVisitadoDTO(
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(2).getDestino(),
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(2).getPorcentagem() *
                                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(0).getPorcentagem() *
                                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1).getPorcentagem())
                ));
                destinosMaisVisitados.add(
                        Arrays.asList(destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1))
                );
                destinosMaisVisitados.add(Arrays.asList(
                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1),
                        new DestinoMaisVisitadoDTO(
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(2).getDestino(),
                                destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(2).getPorcentagem() *
                                        destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(1).getPorcentagem())
                ));
                destinosMaisVisitados.add(
                        Arrays.asList( destinosMaisVisitadosPorMotivoDTO.getDestinos_mais_visistado().get(2))
                );

            }
        }

        return destinosMaisVisitados;
    };

    public boolean hasPais (List<FichaSintesePaisDTO> fichasSintesePaisDTO, String nomePais) {
        for (FichaSintesePaisDTO fichaSintesePaisDTO : fichasSintesePaisDTO) {
            if (fichaSintesePaisDTO.getPais().equalsIgnoreCase(nomePais)) {
                return true;
            }
        }
        return false;
    }


}
