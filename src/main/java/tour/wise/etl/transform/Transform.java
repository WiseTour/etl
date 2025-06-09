package tour.wise.etl.transform;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dto.chegada.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.model.EEtapa;
import tour.wise.model.ELogCategoria;
import tour.wise.model.Log;
import tour.wise.util.Event;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Transform{

    public static List<ChegadaTuristasInternacionaisBrasilMensalDTO> transformChegadasTuristasInternacionaisBrasilMensal(
            JdbcTemplate jdbc,
            Connection connection,
            List<List<Object>> data,
            String fonte,
            String edicao
    ) throws Exception {
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
                String viaAcesso = TransformUtils.padronizarViaAcesso(String.valueOf(row.get(6)));

                Object anoObj = row.get(8);
                Object mesObj = row.get(10);
                Object chegadaObj = row.get(11);

                if (anoObj == null || mesObj == null || chegadaObj == null) {
                    continue; // pula linhas com dados essenciais faltando
                }

                int ano = (anoObj instanceof Number) ? ((Number) anoObj).intValue() : Integer.parseInt(anoObj.toString());
                int mes = (mesObj instanceof Number) ? ((Number) mesObj).intValue() : Integer.parseInt(mesObj.toString());
                int chegada = (chegadaObj instanceof Number) ? ((Number) chegadaObj).intValue() : Integer.parseInt(chegadaObj.toString());

                if (chegada > 0) {
                    ChegadaTuristasInternacionaisBrasilMensalDTO dto = new ChegadaTuristasInternacionaisBrasilMensalDTO(
                            mes, ano, chegada, viaAcesso, ufDestino, paisOrigem
                    );
                    result.add(dto);
                }
            } catch (Exception e) {
                // Log de erro para a linha que falhou, continua o loop
                Event.registerEvent(jdbc, connection,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        String.format("Erro ao transformar linha %d da edição %s (fonte: %s).", linha, edicao, fonte),
                        e,
                        false
                );
            }
        }

        Event.registerEvent(jdbc, connection,
                new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                String.format("Transformação das %d linhas da edição %s (fonte: %s) concluída com sucesso.", linha, edicao, fonte),
                false
        );

        return result;
    }

    public static FichaSinteseBrasilDTO transformFichaSinteseBrasil(
            JdbcTemplate jdbc,
            Connection connection,
            List<List<List<Object>>> data
    ) throws Exception {
        try {
            FichaSinteseBrasilDTO dto = new FichaSinteseBrasilDTO(
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

            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Transformação da Ficha Síntese Brasil concluída com sucesso!",
                    false
            );

            return dto;

        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Erro ao transformar Ficha Síntese Brasil.",
                    e,
                    false
            );
            throw e;
        }
    }

    public static FichaSintesePaisDTO transformFichasSintesePais(
            JdbcTemplate jdbc,
            Connection connection,
            List<List<List<Object>>> data
    ) throws Exception {
        try {
            FichaSintesePaisDTO dto = new FichaSintesePaisDTO(
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

            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Transformação da Ficha Síntese País concluída com sucesso!",
                    false
            );

            return dto;

        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Erro ao transformar Ficha Síntese País.",
                    e,
                    false
            );
            throw e;
        }
    }

    public static FichaSinteseEstadoDTO transformFichasSinteseEstado(
            JdbcTemplate jdbc,
            Connection connection,
            List<List<List<Object>>> data
    ) throws Exception {
        try {
            FichaSinteseEstadoDTO dto = new FichaSinteseEstadoDTO(
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

            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Transformação da Ficha Síntese Estado concluída com sucesso!",
                    false
            );

            return dto;

        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    "Erro ao transformar Ficha Síntese Estado.",
                    e,
                    false
            );
            throw e;
        }
    }

    public static List<PerfilDTO> transformPerfis(
            JdbcTemplate jdbc,
            Connection connection,
            ChegadaTuristasInternacionaisBrasilMensalDTO chegada,
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas,
            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO,
            List<FichaSintesePaisDTO> fichasSintesePaisDTO,
            FichaSinteseBrasilDTO fichaSinteseBrasilDTO
    ) throws Exception {

        List<PerfilDTO> perfis = new ArrayList<>();

        String paisOrigem = chegada.getPaisOrigem();
        String ufDestino = chegada.getUfDestino();
        Integer ano = chegada.getAno();

        try {
            // Primeira tentativa: Ficha do Estado com país
            try {
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
                        perfilDTOEstado.setAno(ano);
                        perfilDTOEstado.setMes(chegada.getMes());
                        perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());

                        Integer qtdTuristas = (int) Math.round(chegada.getQtdChegadas() * perfilDTOEstado.getTaxaTuristas());
                        perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);
                    }

                    perfisEstado.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);
                    perfis.addAll(perfisEstado);

                    Event.registerEvent(jdbc, connection,
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            String.format("Transformação de perfis realizada com Ficha do Estado (%s, %s, %d).", paisOrigem, ufDestino, ano),
                            false);

                    return perfis;
                }
            } catch (Exception e) {
                Event.registerEvent(jdbc, connection,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        String.format("Erro na transformação com Ficha do Estado (%s, %s, %d).", paisOrigem, ufDestino, ano),
                        e, false);
            }

            // Segunda tentativa: Ficha do Estado ignorando país
            try {
                Optional<FichaSinteseEstadoDTO> fichaEstadoOptionalOutrosPaises = fichasSinteseEstadoDTO.stream()
                        .filter(f -> f.getDestinoPrincipal().equalsIgnoreCase(ufDestino)
                                && f.getAno().equals(ano))
                        .findFirst();

                if (fichaEstadoOptionalOutrosPaises.isPresent()) {
                    FichaSinteseEstadoDTO fichaEstado = fichaEstadoOptionalOutrosPaises.get();

                    double taxaTuristas = 100.00;

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
                                            && c.getPaisOrigem().equalsIgnoreCase(paisOrigemDTO.getPais())))
                            .count();

                    int denominador = (int) (quantidadePaises - quantidadePaisesFichas);
                    if (denominador != 0) {
                        taxaTuristas = taxaTuristas / denominador;
                    }

                    List<PerfilDTO> perfisEstado = TransformUtils.createPerfisCombinations(fichaEstado);

                    for (PerfilDTO perfilEstado : perfisEstado) {
                        perfilEstado.setPaisesOrigem(paisOrigem);
                        perfilEstado.setEstadoEntrada(ufDestino);
                        perfilEstado.setAno(ano);
                        perfilEstado.setMes(chegada.getMes());
                        perfilEstado.setViaAcesso(chegada.getViaAcesso());

                        perfilEstado.setTaxaTuristas(perfilEstado.getTaxaTuristas() * taxaTuristas / 100);

                        Integer qtdTuristas = (int) Math.round(chegada.getQtdChegadas() * perfilEstado.getTaxaTuristas());
                        perfilEstado.setQuantidadeTuristas(qtdTuristas);
                    }

                    perfisEstado.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);
                    perfis.addAll(perfisEstado);

                    Event.registerEvent(jdbc, connection,
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            String.format("Transformação de perfis realizada com Ficha do Estado ignorando país (%s, %s, %d).", paisOrigem, ufDestino, ano),
                            false);

                    return perfis;
                }
            } catch (Exception e) {
                Event.registerEvent(jdbc, connection,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        String.format("Erro na transformação com Ficha do Estado ignorando país (%s, %s, %d).", paisOrigem, ufDestino, ano),
                        e, false);
            }

            // Terceira tentativa: Ficha do País
            try {
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
                        perfilPais.setAno(ano);
                        perfilPais.setMes(chegada.getMes());
                        perfilPais.setViaAcesso(chegada.getViaAcesso());

                        Integer qtdTuristas = (int) Math.round(chegada.getQtdChegadas() * perfilPais.getTaxaTuristas());
                        perfilPais.setQuantidadeTuristas(qtdTuristas);
                    }

                    perfisPais.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);
                    perfis.addAll(perfisPais);

                    Event.registerEvent(jdbc, connection,
                            new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                            String.format("Transformação de perfis realizada com Ficha do País (%s, %d).", paisOrigem, ano),
                            false);

                    return perfis;
                }
            } catch (Exception e) {
                Event.registerEvent(jdbc, connection,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        String.format("Erro na transformação com Ficha do País (%s, %d).", paisOrigem, ano),
                        e, false);
            }

            // Última tentativa: Ficha do Brasil
            try {
                List<PerfilDTO> perfisBrasil = TransformUtils.createPerfisCombinations(fichaSinteseBrasilDTO);

                for (PerfilDTO perfilBrasil : perfisBrasil) {
                    perfilBrasil.setPaisesOrigem(paisOrigem);
                    perfilBrasil.setEstadoEntrada(ufDestino);
                    perfilBrasil.setAno(ano);
                    perfilBrasil.setMes(chegada.getMes());
                    perfilBrasil.setViaAcesso(chegada.getViaAcesso());

                    Integer qtdTuristas = (int) Math.round(chegada.getQtdChegadas() * perfilBrasil.getTaxaTuristas());
                    perfilBrasil.setQuantidadeTuristas(qtdTuristas);
                }

                perfisBrasil.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);
                perfis.addAll(perfisBrasil);

                Event.registerEvent(jdbc, connection,
                        new Log(ELogCategoria.SUCESSO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        String.format("Transformação de perfis realizada com Ficha do Brasil (%s, %s, %d).", paisOrigem, ufDestino, ano),
                        false);

                return perfis;

            } catch (Exception e) {
                Event.registerEvent(jdbc, connection,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                        String.format("Erro na transformação com Ficha do Brasil (%s, %s, %d).", paisOrigem, ufDestino, ano),
                        e, false);
                throw e;
            }

        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.TRANSFORMACAO.getId()),
                    String.format("Erro crítico ao processar chegada (%s, %s, %d).", paisOrigem, ufDestino, ano),
                    e, false);
            throw e;
        }
    }


}
