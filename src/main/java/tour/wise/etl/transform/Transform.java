package tour.wise.etl.transform;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.LogDAO;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.dto.perfil.PerfilDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Transform{

    public static List<ChegadaTuristasInternacionaisBrasilMensalDTO> transformChegadasTuristasInternacionaisBrasilMensal(List<List<Object>> data, String fonte, String edicao) {

        System.out.println("[INÍCIO] Transformação dos dados iniciada.");
        System.out.println("[INFO] Fonte: " + fonte + ", Edição: " + edicao);
        System.out.println("[INFO] Total de registros brutos recebidos: " + (data != null ? data.size() : 0));

        if (data == null || data.isEmpty()) {
            return Collections.emptyList();
        }

        List<ChegadaTuristasInternacionaisBrasilMensalDTO> result = new ArrayList<>(data.size());

        int linha = 0;
        for (List<Object> row : data) {
            linha++;
            try {
                String paisOrigem = String.valueOf(row.get(2));
                String ufDestino = String.valueOf(row.get(4));
                String viaAcesso = String.valueOf(row.get(6));

                Object anoObj = row.get(8);
                Object mesObj = row.get(10);
                Object chegadaObj = row.get(11);

                if (anoObj == null || mesObj == null || chegadaObj == null) {
                    continue; // pula linhas com dados essenciais faltando
                }

                // Use parsing direto sem Double para economizar tempo e memória
                int ano = (anoObj instanceof Number) ? ((Number) anoObj).intValue() : Integer.parseInt(anoObj.toString());
                int mes = (mesObj instanceof Number) ? ((Number) mesObj).intValue() : Integer.parseInt(mesObj.toString());
                int chegada = (chegadaObj instanceof Number) ? ((Number) chegadaObj).intValue() : Integer.parseInt(chegadaObj.toString());

                if (chegada > 0) {
                    result.add(new ChegadaTuristasInternacionaisBrasilMensalDTO(
                            mes, ano, chegada, viaAcesso, ufDestino, paisOrigem
                    ));
                }

            } catch (Exception e) {
                System.err.println("[ERRO] Linha " + linha + ": " + row + " - " + e.getMessage());
                // Considerar log mais detalhado no futuro
            }
        }

        System.out.println("[FIM] Transformação concluída. Total de registros convertidos: " + result.size());
        return result;
    }

    public static FichaSinteseBrasilDTO transformFichaSinteseBrasil(List<List<List<Object>>> data) {

        try {
            return new FichaSinteseBrasilDTO(
                    TransformUtils.transformAno(data, 1),
                    TransformUtils.transformListGenero(data, 12),
                    TransformUtils.transformListFaixaEtaria(data, 13),
                    TransformUtils.transformListComposicoesGrupo(data, 4),
                    TransformUtils.transformListFontesInformacao(data, 10),
                    TransformUtils.transformListMotivosViagem(data, 2),
                    TransformUtils.transformListMotivacaoViagemLazer(data, 3),
                    TransformUtils.transformListGastosMedioMotivo(data, 5),
                    TransformUtils.transformListPermanenciaMediaMotivo(data, 6),
                    TransformUtils.transformListDestinosMaisVisitadosPorMotivo(data, 7)


            );
        } catch (Exception e) {

            throw e;
        }
    }

    public static FichaSintesePaisDTO transformFichasSintesePais(List<List<List<Object>>> data) {
        try {
            return new FichaSintesePaisDTO(
                    TransformUtils.transformAno(data, 1),
                    TransformUtils.transformListGenero(data, 12),
                    TransformUtils.transformListFaixaEtaria(data, 13),
                    TransformUtils.transformListComposicoesGrupo(data, 4),
                    TransformUtils.transformListFontesInformacao(data, 10),
                    TransformUtils.transformListMotivosViagem(data, 2),
                    TransformUtils.transformListMotivacaoViagemLazer(data, 3),
                    TransformUtils.transformListGastosMedioMotivo(data, 5),
                    TransformUtils.transformListPermanenciaMediaMotivo(data, 6),
                    TransformUtils.transformListDestinosMaisVisitadosPorMotivo(data, 7),
                    TransformUtils.extractNomePais(data, 0)
            );

        } catch (Exception e) {
            // Log no banco
//            logDAO.insertLog(
//                    1,
//                    1, // Erro
//                    1,
//                    "Erro ao tentar transforma dados de chegada: " + e.getMessage(),
//                    LocalDateTime.now(),
//                    0,
//                    0,
//                    "Fonte_Dados"
//            );
            throw e;
        }
    }

    public static FichaSinteseEstadoDTO transformFichasSinteseEstado(List<List<List<Object>>> data) {

        try {
            return new FichaSinteseEstadoDTO(
                    TransformUtils.transformAno(data, 1),
                    TransformUtils.transformListGenero(data, 14),
                    TransformUtils.transformListFaixaEtaria(data, 15),
                    TransformUtils.transformListComposicoesGrupo(data, 5),
                    TransformUtils.transformListFontesInformacao(data, 13),
                    TransformUtils.transformListMotivosViagem(data, 3),
                    TransformUtils.transformListMotivacaoViagemLazer(data, 4),
                    TransformUtils.transformListGastosMedioMotivo(data, 6),
                    TransformUtils.transformListPermanenciaMediaMotivo(data, 7),
                    TransformUtils.transformListDestinosMaisVisitadosPorMotivo(data, 9),
                    TransformUtils.transformListPaisesOrigem(data, 2),
                    TransformUtils.transformEstado(data, 0),
                    TransformUtils.transformListPermanenciaMediaMotivo(data, 8)
            );
        } catch (Exception e) {
            // Log no banco
//            logDAO.insertLog(
//                    1,
//                    1, // Erro
//                    1,
//                    "Erro ao tentar transforma dados de chegada: " + e.getMessage(),
//                    LocalDateTime.now(),
//                    0,
//                    0,
//                    "Fonte_Dados"
//            );
            throw e;
        }
    }

    public static List<PerfilDTO> transformPerfis(
            ChegadaTuristasInternacionaisBrasilMensalDTO chegada,
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas,
            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO,
            List<FichaSintesePaisDTO> fichasSintesePaisDTO,
            FichaSinteseBrasilDTO fichaSinteseBrasilDTO
    ) {

        List<PerfilDTO> perfis = new ArrayList<>();

        try {

//            logDAO.insertLog(
//                    1,  // fk_fonte (ajuste conforme necessário)
//                    3,  // Categoria: Sucesso (indica que o processo está começando)
//                    1,  // Etapa: Extração (ajuste conforme necessário, ou utilize a etapa correta)
//                    "Criando perfis",
//                    LocalDateTime.now(),
//                    0,  // Quantidade lida ainda não processada
//                    0,  // Quantidade inserida
//                    "Perfil_Estimado"
//            );

            String paisOrigem = chegada.getPaisOrigem();
            String ufDestino = chegada.getUfDestino();
            Integer ano = chegada.getAno();


            // Tenta encontrar na ficha estadual o pais e o estado

            Optional<FichaSinteseEstadoDTO> fichaEstadoOptional = fichasSinteseEstadoDTO.stream()
                    .filter(f -> f.getDestinoPrincipal().equalsIgnoreCase(ufDestino)
                            && f.getAno().equals(ano) && f.getPaisesOrigem().stream()
                            .anyMatch(p -> p.getPais().equalsIgnoreCase(paisOrigem)))
                    .findFirst();

            if (fichaEstadoOptional.isPresent()) {

                FichaSinteseEstadoDTO fichaEstado = fichaEstadoOptional.get();

                List<PerfilDTO> perfisEstado = TransformUtils.createPerfisCombinations(fichaEstado);

                for (PerfilDTO perfilDTOEstado : perfisEstado) {
                    perfilDTOEstado.setPaisesOrigem(paisOrigem);
                    perfilDTOEstado.setEstadoEntrada(ufDestino);
                    perfilDTOEstado.setAno(chegada.getAno());
                    perfilDTOEstado.setMes(chegada.getMes());
                    perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                    perfilDTOEstado.setTaxaTuristas(
                            perfilDTOEstado.getTaxaTuristas()
                    );
                    if (Double.isInfinite(perfilDTOEstado.getTaxaTuristas())) {
                        System.out.println(perfilDTOEstado.getTaxaTuristas() + "TAXA - ESTADO/PAIS é infinito!");
                    }
                    Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilDTOEstado.getTaxaTuristas())).intValue();
                    perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
                    if (Double.isInfinite(qtdTuristas)) {
                        System.out.println(qtdTuristas + "quantidade - ESTADO/PAIS é infinito!");
                    }

                }

                perfisEstado.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);

                perfis.addAll(perfisEstado);

                return perfis;
            }

            // Tenta encontrar na ficha estadual apenas o estado ignorando o pais

            Optional<FichaSinteseEstadoDTO> fichaEstadoOptionalOutrosPaises = fichasSinteseEstadoDTO.stream()
                    .filter(f -> f.getDestinoPrincipal().equalsIgnoreCase(ufDestino)
                            && f.getAno().equals(ano))
                    .findFirst();

            if (fichaEstadoOptionalOutrosPaises.isPresent()) {

                FichaSinteseEstadoDTO fichaEstado = fichaEstadoOptionalOutrosPaises.get();

                Double taxaTuristas = 100.00;

                for (PaisOrigemDTO paisOrigemDTO : fichaEstado.getPaisesOrigem()) {
                    taxaTuristas -= paisOrigemDTO.getPorcentagem();
                }

                long quantidadePaises = chegadas.stream()
                        .filter(f -> f.getUfDestino().equalsIgnoreCase(ufDestino)
                                && f.getAno().equals(ano))
                        .count();

                int quantidadePaisesFichas = (int) fichaEstado.getPaisesOrigem()
                        .stream()
                        .filter(paisOrigemDTO -> chegadas.stream()
                                .anyMatch(c -> c.getUfDestino().equalsIgnoreCase(ufDestino)
                                        && c.getAno().equals(ano)
                                        && c.getPaisOrigem().equalsIgnoreCase(paisOrigemDTO.getPais())
                                )
                        )
                        .count();

                int denominador = (int) (quantidadePaises - quantidadePaisesFichas);
                if (denominador != 0) {
                    taxaTuristas = taxaTuristas / denominador;
                }

                List<PerfilDTO> perfisEstado = TransformUtils.createPerfisCombinations(fichaEstado);

                for (PerfilDTO perfilEstado : perfisEstado) {
                    perfilEstado.setPaisesOrigem(paisOrigem);
                    perfilEstado.setEstadoEntrada(ufDestino);
                    perfilEstado.setAno(chegada.getAno());
                    perfilEstado.setMes(chegada.getMes());
                    perfilEstado.setViaAcesso(chegada.getViaAcesso());

                    perfilEstado.setTaxaTuristas(
                            perfilEstado.getTaxaTuristas() *
                                    taxaTuristas / 100
                    );
                    if (Double.isInfinite(perfilEstado.getTaxaTuristas())) {

                    }
                    Double taxaAtualizada = perfilEstado.getTaxaTuristas();
                    Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * taxaAtualizada)).intValue();

                    perfilEstado.setQuantidadeTuristas(qtdTuristas);

                }

                perfisEstado.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);

                perfis.addAll(perfisEstado);

                return perfis;
            }

            // Tenta encontrar na ficha do país, ignorando o estado

            Optional<FichaSintesePaisDTO> fichaPaisOptional = fichasSintesePaisDTO.stream()
                    .filter(f -> f.getPais().equalsIgnoreCase(paisOrigem)
                            && f.getAno().equals(ano))
                    .findFirst();

            if (fichaPaisOptional.isPresent()) {
                FichaSintesePaisDTO fichaPais = fichaPaisOptional.get();

                List<PerfilDTO> perfisPais = TransformUtils.createPerfisCombinations(fichaPais);

                for (PerfilDTO perfilPais : perfisPais) {
                    perfilPais.setPaisesOrigem(paisOrigem);
                    perfilPais.setEstadoEntrada(ufDestino);
                    perfilPais.setAno(chegada.getAno());
                    perfilPais.setMes(chegada.getMes());
                    perfilPais.setViaAcesso(chegada.getViaAcesso());
                    Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilPais.getTaxaTuristas())).intValue();
                    perfilPais.setQuantidadeTuristas(qtdTuristas);

                    if (Double.isInfinite(perfilPais.getTaxaTuristas())) {
                        System.out.println(perfilPais.getTaxaTuristas() + "TAXA - PAIS é infinito!");
                    }
                    if (Double.isInfinite(qtdTuristas)) {
                        System.out.println(qtdTuristas + "QTD - PAIS é infinito!");
                    }

                }

                // Remove todos os perfis com quantidadeTuristas < 1
                perfisPais.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);

                perfis.addAll(perfisPais);

                return perfis;
            }

            // Se nenhuma opção for válida, usa a ficha do Brasil

            FichaSinteseBrasilDTO fichaBrasil = fichaSinteseBrasilDTO;

            List<PerfilDTO> perfisBrasil = TransformUtils.createPerfisCombinations(fichaBrasil);

            for (PerfilDTO perfilBrasil : perfisBrasil) {
                perfilBrasil.setPaisesOrigem(paisOrigem);
                perfilBrasil.setEstadoEntrada(ufDestino);
                perfilBrasil.setAno(chegada.getAno());
                perfilBrasil.setMes(chegada.getMes());
                perfilBrasil.setViaAcesso(chegada.getViaAcesso());
                Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilBrasil.getTaxaTuristas())).intValue();
                perfilBrasil.setQuantidadeTuristas(qtdTuristas);
                if (Double.isInfinite(perfilBrasil.getTaxaTuristas())) {
                    System.out.println(perfilBrasil.getTaxaTuristas() + "TAXA - BRASIL é infinito!");
                }
                if (Double.isInfinite(qtdTuristas)) {
                    System.out.println(qtdTuristas + "QTD - BRASIL é infinito!");
                }

            }

            // Remove todos os perfis com quantidadeTuristas < 1
            perfisBrasil.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);
            perfis.addAll(perfisBrasil);

            return perfis;

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

}
