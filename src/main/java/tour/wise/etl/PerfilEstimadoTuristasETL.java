package tour.wise.etl;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.FonteDAO;
import tour.wise.dao.PaisDAO;
import tour.wise.dao.PerfilEstimadoTuristasDAO;
import tour.wise.dao.UnidadeFederativaBrasilDAO;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.etl.fichas.sintese.FichaSinteseBrasilET;
import tour.wise.etl.fichas.sintese.Ficha_Sintese_Estado_ET;
import tour.wise.etl.fichas.sintese.FichaSintesePaisET;
import tour.wise.model.Perfil_Estimado_Turistas;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;

import java.io.IOException;
import java.util.ArrayList;
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

        List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadasTuristasInternacionaisBrasilMensalDTO = chegadaTuristasInternacionaisBrasilMensalET.transform(chegada_turistas_internacionais_brasil_Mensal_ET.extract(
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

//        List<String> Chegada_Turistas_Internacionais_Brasil_Mensal_DTO_Paises = new ArrayList<>();
//
//        for (Chegada_Turistas_Internacionais_Brasil_Mensal_DTO dto : chegadas_turistas_internacionais_brasil_mensal_dto) {
//            String pais = dto.getPais_origem();
//            if (!Chegada_Turistas_Internacionais_Brasil_Mensal_DTO_Paises.contains(pais)) {
//                Chegada_Turistas_Internacionais_Brasil_Mensal_DTO_Paises.add(pais);
//            }
//        }
//
//        List<Chegada_Turistas_Internacionais_Brasil_Anual_DTO> chegadas_turistas_internacionais_brasil_anual_dto = new ArrayList<>();
//
//        for (Chegada_Turistas_Internacionais_Brasil_Mensal_DTO chegada_turistas_internacionais_brasil_mensal_dto : chegadas_turistas_internacionais_brasil_mensal_dto) {
//            Integer mes = chegada_turistas_internacionais_brasil_mensal_dto.getMes();
//            Integer ano = chegada_turistas_internacionais_brasil_mensal_dto.getAno();
//            String via_acesso = chegada_turistas_internacionais_brasil_mensal_dto.getVia_acesso();
//            String pais_origem = chegada_turistas_internacionais_brasil_mensal_dto.getPais_origem();
//            Integer qtd = chegada_turistas_internacionais_brasil_mensal_dto.getQtdChegadas();
//
//            boolean founded = false;
//
//            for (Chegada_Turistas_Internacionais_Brasil_Anual_DTO chegada_turistas_internacionais_brasil_anual_dto : chegadas_turistas_internacionais_brasil_anual_dto) {
//                if (chegada_turistas_internacionais_brasil_anual_dto.getMes().equals(mes) &&
//                        chegada_turistas_internacionais_brasil_anual_dto.getAno().equals(ano) &&
//                        chegada_turistas_internacionais_brasil_anual_dto.getVia_acesso().equals(via_acesso)
//                        && chegada_turistas_internacionais_brasil_anual_dto.getPais_origem().equals(pais_origem)) {
//
//                    // Soma as chegadas
//                    chegada_turistas_internacionais_brasil_anual_dto.setQtdChegadas(chegada_turistas_internacionais_brasil_anual_dto.getQtdChegadas() + qtd);
//                    founded = true;
//                    break;
//                }
//            }
//
//            if (!founded) {
//                // Cria nova entrada se ainda não existe
//                Chegada_Turistas_Internacionais_Brasil_Anual_DTO chegada_turistas_internacionais_brasil_anual_dto = new Chegada_Turistas_Internacionais_Brasil_Anual_DTO(
//                        mes, ano, qtd, via_acesso, pais_origem // país de origem é ignorado aqui
//                );
//                chegadas_turistas_internacionais_brasil_anual_dto.add(chegada_turistas_internacionais_brasil_anual_dto);
//            }
//        }
//
//        System.out.println();
//        System.out.println("Chegadas");
//
//        for (Chegada_Turistas_Internacionais_Brasil_Anual_DTO chegada_turistas_internacionais_brasil_anual_dto : chegadas_turistas_internacionais_brasil_anual_dto) {
//            System.out.println(chegada_turistas_internacionais_brasil_anual_dto);
//        }


        List<Perfil_Estimado_Turistas>  perfiesEstimadoTuristas = new ArrayList<>();

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegadaTuristasInternacionaisBrasilMensalDTO : chegadasTuristasInternacionaisBrasilMensalDTO) {

            Integer totalChegadas = chegadaTuristasInternacionaisBrasilMensalDTO.getQtdChegadas();
            String pais = chegadaTuristasInternacionaisBrasilMensalDTO.getPaisOrigem();
            String ufDestino = chegadaTuristasInternacionaisBrasilMensalDTO.getUfDestino();
            Integer ano = chegadaTuristasInternacionaisBrasilMensalDTO.getAno();

//            private Integer id_perfil_estimado_visistantes;
//            private Integer fk_pais_origem; //
//            private String fk_uf_entrada;
//            private Integer ano;
//            private Integer mes;
//            private Integer quantida_turistas;
//            private String genero;
//            private String faixa_etaria;
//            private String via_acesso;
//            private String composicao_grupo_familiar; //
//            private String fonte_informacao_viagem; //
//            private String servico_agencia_turismo;
//            private String motivo_viagem;
//            private Double gasto_medio_percapita_em_reais;



            for (FichaSintesePaisDTO ficha_sintese_pais : fichas_sintese_por_pais) {

                        if(ficha_sintese_pais.getPais().equalsIgnoreCase(pais) && ficha_sintese_pais.getAno().equals(ano)){

                            for (FonteInformacaoDTO fonte_informacao_DTO_pais : ficha_sintese_pais.getFontes_informacao()) {

                                for (ComposicaoGrupoViagemDTO composicao_grupo_viagem_DTO_pais : ficha_sintese_pais.getComposicao_grupos_viagem()) {

                                    for (UtilizacaaAgenciaViagemDTO utilizacaa_agencia_viagem_DTO_pais : ficha_sintese_pais.getUtilizacao_agencia_viagem()) {

                                        for (MotivoViagemDTO motivo_viagem_DTO_pais : ficha_sintese_pais.getMotivos()) {

                                            Double totalChegadasFiltrada = (
                                                    totalChegadas // totalChegadas = TOTAL DE CHEGADAS DAQUELE ESTADO DAQUELA NACIONALIDADE (PESQUISAR NO BD)
                                                            * fonte_informacao_DTO_pais.getPorcentagem()/100
                                                            * composicao_grupo_viagem_DTO_pais.getPorcentagem()/100
                                                            * utilizacaa_agencia_viagem_DTO_pais.getPorcentagem()/100
                                                            * motivo_viagem_DTO_pais.getPorcentagem()
                                            );

                                            for (DestinosMaisVisitadosPorMotivoDTO destinosMaisVisistadosPorMotivo : ficha_sintese_pais.getDestinos_mais_visistados_por_motivo()) {
                                                if(destinosMaisVisistadosPorMotivo.getMotivo().equalsIgnoreCase(motivo_viagem_DTO_pais.getMotivo())){
                                                    for (DestinoMaisVisitadoDTO destino : destinosMaisVisistadosPorMotivo.getDestinos_mais_visistado()) {
                                                        if(destino.getDestino().equalsIgnoreCase(uf_destino)){
                                                            totalChegadasFiltrada *= destino.getPorcentagem()/100;
                                                        }
                                                    }
                                                }
                                            }

                                            if(motivo_viagem_DTO_pais.getMotivo().equalsIgnoreCase("Lazer")){

                                                for (MotivacaoViagemLazerDTO motivacao_viagem_lazer_DTO_pais : ficha_sintese_pais.getMotivacoes_viagem_lazer()) {

                                                    totalChegadasFiltrada *= (motivacao_viagem_lazer_DTO_pais.getPorcentagem()/100);

                                                    for (FichaSinteseEstadoDTO fichaSinteseEstado : fichas_sintese_por_estado) {

                                                        if(fichaSinteseEstado.getDestino_princiapal().equalsIgnoreCase(uf_destino) && fichaSinteseEstado.getAno().equals(ficha_sintese_pais.getAno())){

                                                            FonteInformacaoDTO fonte_informacao_DTO_estado = fichaSinteseEstado.getFontes_informacao().stream()
                                                                    .filter(item -> item.getFonte().equalsIgnoreCase(fonte_informacao_DTO_pais.getFonte()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            ComposicaoGrupoViagemDTO composicao_grupo_viagem_DTO_estado = fichaSinteseEstado.getComposicao_grupos_viagem().stream()
                                                                    .filter(item -> item.getComposicao().equalsIgnoreCase(composicao_grupo_viagem_DTO_pais.getComposicao()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            UtilizacaaAgenciaViagemDTO utilizacaa_agencia_viagem_DTO_estado = fichaSinteseEstado.getUtilizacao_agencia_viagem().stream()
                                                                    .filter(item -> item.getTipo().equalsIgnoreCase(utilizacaa_agencia_viagem_DTO_pais.getTipo()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            MotivoViagemDTO motivo_viagem_DTO_estado = fichaSinteseEstado.getMotivos().stream()
                                                                    .filter(item -> item.getMotivo().equalsIgnoreCase(motivo_viagem_DTO_pais.getMotivo()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            MotivacaoViagemLazerDTO motivacao_viagem_lazer_DTO_estado = fichaSinteseEstado.getMotivacoes_viagem_lazer().stream()
                                                                    .filter(item -> item.getMotivacao().equalsIgnoreCase(motivacao_viagem_lazer_DTO_pais.getMotivacao()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            if(fonte_informacao_DTO_estado != null) {

                                                                totalChegadasFiltrada *= (fonte_informacao_DTO_estado.getPorcentagem()/100);

                                                            }

                                                            if(composicao_grupo_viagem_DTO_estado != null) {

                                                                totalChegadasFiltrada *= (composicao_grupo_viagem_DTO_estado.getPorcentagem()/100);

                                                            }

                                                            if(utilizacaa_agencia_viagem_DTO_estado != null) {

                                                                totalChegadasFiltrada *= (utilizacaa_agencia_viagem_DTO_estado.getPorcentagem()/100);

                                                            }

                                                            if(motivo_viagem_DTO_estado != null) {

                                                                totalChegadasFiltrada *= (motivo_viagem_DTO_estado.getPorcentagem()/100);

                                                            }


                                                            if(motivacao_viagem_lazer_DTO_estado != null) {

                                                                totalChegadasFiltrada *= (
                                                                        motivacao_viagem_lazer_DTO_pais.getPorcentagem()/100
                                                                                * motivacao_viagem_lazer_DTO_estado.getPorcentagem()/100

                                                                );

                                                            }

                                                            if((int) Math.round(totalChegadasFiltrada) > 0){
                                                                perfis_estimado_turistas.add(
                                                                        new Perfil_Estimado_Turistas(
                                                                                fonte_perfil,
                                                                                fonte_chegadas,
                                                                                (int) Math.round(totalChegadasFiltrada),
                                                                                ficha_sintese_pais.getAno(),
                                                                                fichaSinteseEstado.getDestino_princiapal(),
                                                                                ((FichaSintesePaisDTO) ficha_sintese_pais).getPais(),
                                                                                null,
                                                                                null,
                                                                                composicao_grupo_viagem_DTO_pais.getComposicao(),
                                                                                fonte_informacao_DTO_pais.getFonte(),
                                                                                utilizacaa_agencia_viagem_DTO_pais.getTipo(),
                                                                                motivo_viagem_DTO_pais.getMotivo(),
                                                                                motivacao_viagem_lazer_DTO_pais.getMotivacao()
                                                                        )
                                                                );
                                                            }
                                                        }

                                                    }

                                                }

                                            }else {

                                                for (FichaSinteseEstadoDTO fichaSinteseEstado : fichas_sintese_por_estado) {

                                                    if(fichaSinteseEstado.getDestino_princiapal().equalsIgnoreCase(uf_destino) && fichaSinteseEstado.getAno().equals(ficha_sintese_pais.getAno())){

                                                        FonteInformacaoDTO fonte_informacao_DTO_estado = fichaSinteseEstado.getFontes_informacao().stream()
                                                                .filter(item -> item.getFonte().equalsIgnoreCase(fonte_informacao_DTO_pais.getFonte()))
                                                                .findFirst()
                                                                .orElse(null);

                                                        ComposicaoGrupoViagemDTO composicao_grupo_viagem_DTO_estado = fichaSinteseEstado.getComposicao_grupos_viagem().stream()
                                                                .filter(item -> item.getComposicao().equalsIgnoreCase(composicao_grupo_viagem_DTO_pais.getComposicao()))
                                                                .findFirst()
                                                                .orElse(null);

                                                        UtilizacaaAgenciaViagemDTO utilizacaa_agencia_viagem_DTO_estado = fichaSinteseEstado.getUtilizacao_agencia_viagem().stream()
                                                                .filter(item -> item.getTipo().equalsIgnoreCase(utilizacaa_agencia_viagem_DTO_pais.getTipo()))
                                                                .findFirst()
                                                                .orElse(null);

                                                        MotivoViagemDTO motivo_viagem_DTO_estado = fichaSinteseEstado.getMotivos().stream()
                                                                .filter(item -> item.getMotivo().equalsIgnoreCase(motivo_viagem_DTO_pais.getMotivo()))
                                                                .findFirst()
                                                                .orElse(null);


                                                        if(fonte_informacao_DTO_estado != null) {

                                                            totalChegadasFiltrada *= (fonte_informacao_DTO_estado.getPorcentagem()/100);

                                                        }

                                                        if(composicao_grupo_viagem_DTO_estado != null) {

                                                            totalChegadasFiltrada *= (composicao_grupo_viagem_DTO_estado.getPorcentagem()/100);

                                                        }

                                                        if(utilizacaa_agencia_viagem_DTO_estado != null) {

                                                            totalChegadasFiltrada *= (utilizacaa_agencia_viagem_DTO_estado.getPorcentagem()/100);

                                                        }

                                                        if(motivo_viagem_DTO_estado != null) {

                                                            totalChegadasFiltrada *= (motivo_viagem_DTO_estado.getPorcentagem()/100);

                                                        }

                                                        if((int) Math.round(totalChegadasFiltrada) > 0){
                                                            perfis_estimado_turistas.add(
                                                                    new Perfil_Estimado_Turistas(
                                                                            fonte_perfil,
                                                                            fonte_chegadas,
                                                                            (int) Math.round(totalChegadasFiltrada),
                                                                            ficha_sintese_pais.getAno(),
                                                                            fichaSinteseEstado.getDestino_princiapal(),
                                                                            ((FichaSintesePaisDTO) ficha_sintese_pais).getPais(),
                                                                            null,
                                                                            null,
                                                                            composicao_grupo_viagem_DTO_pais.getComposicao(),
                                                                            fonte_informacao_DTO_pais.getFonte(),
                                                                            utilizacaa_agencia_viagem_DTO_pais.getTipo(),
                                                                            motivo_viagem_DTO_pais.getMotivo(),
                                                                            null
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

                    return perfis_estimado_turistas;
                    }













    }


    public void load(
            JdbcTemplate connection,
            String orgao_emissor,
            String edicao,
            String titulo_edicao,
            String url_fonte,
            List<Perfil_Estimado_Turistas> perfis
            ){

        PerfilEstimadoTuristasDAO perfil_estimado_turistas_dao = new PerfilEstimadoTuristasDAO(connection);

        PaisDAO pais_dao = new PaisDAO(connection);
        FonteDAO fonte_dao = new FonteDAO(connection);
        UnidadeFederativaBrasilDAO unidade_federativa_brasil_dao = new UnidadeFederativaBrasilDAO(connection);


//        for (Perfil_Estimado_Turistas perfil : perfis) {
//            Integer fkFonte = fonte_dao.getFonteId(titulo_edicao);
//            String composicaoGrupoFamiliar = perfil.getComposicao_grupo_familiar();
//            String fonteInformacaoViagem = perfil.getFonte_informacao_viagem();
//            Integer servicoAgenciaTurismo = perfil.getServico_agencia_turismo();
//            String motivoViagem = perfil.getMotivo_viagem();
//            Double gastoMedioPerCapita = perfil.get
//            Integer ano,
//            Integer fkTotalChegadasMensal,
//            String ufDestino,
//            Integer fkFonteChegadasAnual,
//            Integer fkPaisOrigem
//
//
//        }

    }
}
