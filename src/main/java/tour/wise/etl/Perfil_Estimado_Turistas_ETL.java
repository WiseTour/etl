package tour.wise.etl;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.Fonte_DAO;
import tour.wise.dao.Pais_DAO;
import tour.wise.dao.Perfil_Estimado_Turistas_DAO;
import tour.wise.dao.Unidade_Federativa_Brasil_DAO;
import tour.wise.dto.Chegada_Turistas_Internacionais_Brasil_Anual_DTO;
import tour.wise.dto.Chegada_Turistas_Internacionais_Brasil_Mensal_DTO;
import tour.wise.etl.fichas_sintese.Ficha_Sintese_Brasil_ET;
import tour.wise.etl.fichas_sintese.Ficha_Sintese_Estado_ET;
import tour.wise.etl.fichas_sintese.Ficha_Sintese_Pais_ET;
import tour.wise.model.Perfil_Estimado_Turistas;
import tour.wise.dto.Ficha_Sintese_Pais;
import tour.wise.dto.ficha_sintese_brasil.*;
import tour.wise.dto.ficha_sintese_estado.Ficha_Sintese_Estado;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Perfil_Estimado_Turistas_ETL {

    public void exe(String fonte_perfil, String fonte_chegadas, String fileNameFichaSintesePais, String fileNameFichaSinteseBrasil,String fileNameFichaSinteseEstado, String fileNameChegadas) throws IOException {


        // EXTRACT

        Ficha_Sintese_Brasil_ET ficha_sintese_brasil_et = new Ficha_Sintese_Brasil_ET();

        List<Ficha_Sintese_Brasil> fichas_sintese_brasil = ficha_sintese_brasil_et.extractTransform(fileNameFichaSinteseBrasil, 0, 0);

        Ficha_Sintese_Pais_ET ficha_sintese_pais_etl = new Ficha_Sintese_Pais_ET();

        List<Ficha_Sintese_Pais> fichas_sintese_por_pais = ficha_sintese_pais_etl.extractTransformFicha_Sintese_Pais(fileNameFichaSintesePais,0, 0);

        Ficha_Sintese_Estado_ET ficha_sintese_estado_etl = new Ficha_Sintese_Estado_ET();

        List<Ficha_Sintese_Estado> fichas_sintese_por_estado =  ficha_sintese_estado_etl.extractTransformFicha_Sintese_Estado(fileNameFichaSinteseEstado, 0, 0);

        Chegada_Turistas_Internacionais_Brasil_Mensal_ETL chegada_turistas_internacionais_brasil_Mensal_etl = new Chegada_Turistas_Internacionais_Brasil_Mensal_ETL();

        List<Chegada_Turistas_Internacionais_Brasil_Mensal_DTO> chegadas_turistas_internacionais_brasil_mensal_dto = chegada_turistas_internacionais_brasil_Mensal_etl.transform(chegada_turistas_internacionais_brasil_Mensal_etl.extract(
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

        for (Chegada_Turistas_Internacionais_Brasil_Mensal_DTO chegada_turistas_internacionais_brasil_mensal_dto : chegadas_turistas_internacionais_brasil_mensal_dto) {
            System.out.println(chegada_turistas_internacionais_brasil_mensal_dto);
        }

        System.out.println();
        System.out.println("Ficha Sintese Brasil");

        for (Ficha_Sintese_Brasil ficha_sintese_brasil : fichas_sintese_brasil) {
            System.out.println(ficha_sintese_brasil);
        }

        System.out.println();
        System.out.println("Ficha Sintese por Estado");

        for (Ficha_Sintese_Brasil ficha_sintese_estado : fichas_sintese_por_estado) {
            System.out.println(ficha_sintese_estado);
        }

        System.out.println();
        System.out.println("Ficha Sintese por Pais");

        for (Ficha_Sintese_Brasil ficha_sintese_pais : fichas_sintese_por_pais) {
            System.out.println(ficha_sintese_pais);
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


        List<Perfil_Estimado_Turistas>  perfis_estimado_turistas = new ArrayList<>();

        for (Chegada_Turistas_Internacionais_Brasil_Mensal_DTO chegada_turistas_internacionais_brasil_mensal : chegadas_turistas_internacionais_brasil_mensal_dto) {

                    Integer totalChegadas = chegada_turistas_internacionais_brasil_mensal.getQtdChegadas();
                    String pais = chegada_turistas_internacionais_brasil_mensal.getPais_origem();
                    String uf_destino = chegada_turistas_internacionais_brasil_mensal.getUf_destino();
                    Integer ano = chegada_turistas_internacionais_brasil_mensal.getAno();

                    for (Ficha_Sintese_Brasil ficha_sintese_pais : fichas_sintese_por_pais) {

                        if(((Ficha_Sintese_Pais) ficha_sintese_pais).getPais().equalsIgnoreCase(pais) && ficha_sintese_pais.getAno().equals(ano)){

                            for (Fonte_Informacao fonte_informacao_pais : ficha_sintese_pais.getFontes_informacao()) {

                                for (Composicao_Grupo_Viagem composicao_grupo_viagem_pais : ficha_sintese_pais.getComposicao_grupos_viagem()) {

                                    for (Utilizacao_Agencia_Viagem utilizacao_agencia_viagem_pais : ficha_sintese_pais.getUtilizacao_agencia_viagem()) {

                                        for (Motivo_Viagem motivo_viagem_pais : ficha_sintese_pais.getMotivos()) {

                                            Double totalChegadasFiltrada = (
                                                    totalChegadas // totalChegadas = TOTAL DE CHEGADAS DAQUELE ESTADO DAQUELA NACIONALIDADE (PESQUISAR NO BD)
                                                            * fonte_informacao_pais.getPorcentagem()/100
                                                            * composicao_grupo_viagem_pais.getPorcentagem()/100
                                                            * utilizacao_agencia_viagem_pais.getPorcentagem()/100
                                                            * motivo_viagem_pais.getPorcentagem()
                                            );

                                            for (Destinos_Mais_Visitados_Por_Motivo destinosMaisVisistadosPorMotivo : ficha_sintese_pais.getDestinos_mais_visistados_por_motivo()) {
                                                if(destinosMaisVisistadosPorMotivo.getMotivo().equalsIgnoreCase(motivo_viagem_pais.getMotivo())){
                                                    for (Destino_Mais_Visitado destino : destinosMaisVisistadosPorMotivo.getDestinos_mais_visistado()) {
                                                        if(destino.getDestino().equalsIgnoreCase(uf_destino)){
                                                            totalChegadasFiltrada *= destino.getPorcentagem()/100;
                                                        }
                                                    }
                                                }
                                            }

                                            if(motivo_viagem_pais.getMotivo().equalsIgnoreCase("Lazer")){

                                                for (Motivacao_Viagem_Lazer motivacao_viagem_lazer_pais : ficha_sintese_pais.getMotivacoes_viagem_lazer()) {

                                                    totalChegadasFiltrada *= (motivacao_viagem_lazer_pais.getPorcentagem()/100);

                                                    for (Ficha_Sintese_Estado fichaSinteseEstado : fichas_sintese_por_estado) {

                                                        if(fichaSinteseEstado.getDestino_princiapal().equalsIgnoreCase(uf_destino) && fichaSinteseEstado.getAno().equals(ficha_sintese_pais.getAno())){

                                                            Fonte_Informacao fonte_informacao_estado = fichaSinteseEstado.getFontes_informacao().stream()
                                                                    .filter(item -> item.getFonte().equalsIgnoreCase(fonte_informacao_pais.getFonte()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            Composicao_Grupo_Viagem composicao_grupo_viagem_estado = fichaSinteseEstado.getComposicao_grupos_viagem().stream()
                                                                    .filter(item -> item.getComposicao().equalsIgnoreCase(composicao_grupo_viagem_pais.getComposicao()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            Utilizacao_Agencia_Viagem utilizacao_agencia_viagem_estado = fichaSinteseEstado.getUtilizacao_agencia_viagem().stream()
                                                                    .filter(item -> item.getTipo().equalsIgnoreCase(utilizacao_agencia_viagem_pais.getTipo()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            Motivo_Viagem motivo_viagem_estado = fichaSinteseEstado.getMotivos().stream()
                                                                    .filter(item -> item.getMotivo().equalsIgnoreCase(motivo_viagem_pais.getMotivo()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            Motivacao_Viagem_Lazer motivacao_viagem_lazer_estado = fichaSinteseEstado.getMotivacoes_viagem_lazer().stream()
                                                                    .filter(item -> item.getMotivacao().equalsIgnoreCase(motivacao_viagem_lazer_pais.getMotivacao()))
                                                                    .findFirst()
                                                                    .orElse(null);

                                                            if(fonte_informacao_estado != null) {

                                                                totalChegadasFiltrada *= (fonte_informacao_estado.getPorcentagem()/100);

                                                            }

                                                            if(composicao_grupo_viagem_estado != null) {

                                                                totalChegadasFiltrada *= (composicao_grupo_viagem_estado.getPorcentagem()/100);

                                                            }

                                                            if(utilizacao_agencia_viagem_estado != null) {

                                                                totalChegadasFiltrada *= (utilizacao_agencia_viagem_estado.getPorcentagem()/100);

                                                            }

                                                            if(motivo_viagem_estado != null) {

                                                                totalChegadasFiltrada *= (motivo_viagem_estado.getPorcentagem()/100);

                                                            }


                                                            if(motivacao_viagem_lazer_estado != null) {

                                                                totalChegadasFiltrada *= (
                                                                        motivacao_viagem_lazer_pais.getPorcentagem()/100
                                                                                * motivacao_viagem_lazer_estado.getPorcentagem()/100

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
                                                                                ((Ficha_Sintese_Pais) ficha_sintese_pais).getPais(),
                                                                                null,
                                                                                null,
                                                                                composicao_grupo_viagem_pais.getComposicao(),
                                                                                fonte_informacao_pais.getFonte(),
                                                                                utilizacao_agencia_viagem_pais.getTipo(),
                                                                                motivo_viagem_pais.getMotivo(),
                                                                                motivacao_viagem_lazer_pais.getMotivacao()
                                                                        )
                                                                );
                                                            }
                                                        }

                                                    }

                                                }

                                            }else {

                                                for (Ficha_Sintese_Estado fichaSinteseEstado : fichas_sintese_por_estado) {

                                                    if(fichaSinteseEstado.getDestino_princiapal().equalsIgnoreCase(uf_destino) && fichaSinteseEstado.getAno().equals(ficha_sintese_pais.getAno())){

                                                        Fonte_Informacao fonte_informacao_estado = fichaSinteseEstado.getFontes_informacao().stream()
                                                                .filter(item -> item.getFonte().equalsIgnoreCase(fonte_informacao_pais.getFonte()))
                                                                .findFirst()
                                                                .orElse(null);

                                                        Composicao_Grupo_Viagem composicao_grupo_viagem_estado = fichaSinteseEstado.getComposicao_grupos_viagem().stream()
                                                                .filter(item -> item.getComposicao().equalsIgnoreCase(composicao_grupo_viagem_pais.getComposicao()))
                                                                .findFirst()
                                                                .orElse(null);

                                                        Utilizacao_Agencia_Viagem utilizacao_agencia_viagem_estado = fichaSinteseEstado.getUtilizacao_agencia_viagem().stream()
                                                                .filter(item -> item.getTipo().equalsIgnoreCase(utilizacao_agencia_viagem_pais.getTipo()))
                                                                .findFirst()
                                                                .orElse(null);

                                                        Motivo_Viagem motivo_viagem_estado = fichaSinteseEstado.getMotivos().stream()
                                                                .filter(item -> item.getMotivo().equalsIgnoreCase(motivo_viagem_pais.getMotivo()))
                                                                .findFirst()
                                                                .orElse(null);


                                                        if(fonte_informacao_estado != null) {

                                                            totalChegadasFiltrada *= (fonte_informacao_estado.getPorcentagem()/100);

                                                        }

                                                        if(composicao_grupo_viagem_estado != null) {

                                                            totalChegadasFiltrada *= (composicao_grupo_viagem_estado.getPorcentagem()/100);

                                                        }

                                                        if(utilizacao_agencia_viagem_estado != null) {

                                                            totalChegadasFiltrada *= (utilizacao_agencia_viagem_estado.getPorcentagem()/100);

                                                        }

                                                        if(motivo_viagem_estado != null) {

                                                            totalChegadasFiltrada *= (motivo_viagem_estado.getPorcentagem()/100);

                                                        }

                                                        if((int) Math.round(totalChegadasFiltrada) > 0){
                                                            perfis_estimado_turistas.add(
                                                                    new Perfil_Estimado_Turistas(
                                                                            fonte_perfil,
                                                                            fonte_chegadas,
                                                                            (int) Math.round(totalChegadasFiltrada),
                                                                            ficha_sintese_pais.getAno(),
                                                                            fichaSinteseEstado.getDestino_princiapal(),
                                                                            ((Ficha_Sintese_Pais) ficha_sintese_pais).getPais(),
                                                                            null,
                                                                            null,
                                                                            composicao_grupo_viagem_pais.getComposicao(),
                                                                            fonte_informacao_pais.getFonte(),
                                                                            utilizacao_agencia_viagem_pais.getTipo(),
                                                                            motivo_viagem_pais.getMotivo(),
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

        Perfil_Estimado_Turistas_DAO perfil_estimado_turistas_dao = new Perfil_Estimado_Turistas_DAO(connection);

        Pais_DAO pais_dao = new Pais_DAO(connection);
        Fonte_DAO fonte_dao = new Fonte_DAO(connection);
        Unidade_Federativa_Brasil_DAO unidade_federativa_brasil_dao = new Unidade_Federativa_Brasil_DAO(connection);


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
