package tour.wise.etl.transform;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.LogDAO;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.dto.perfil.PerfilDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Transform extends Util {

    JdbcTemplate connection;
    LogDAO logDAO;


    public Transform(JdbcTemplate connection) {
        this.connection = connection;
        this.logDAO = new LogDAO(connection);
    }

    public List<ChegadaTuristasInternacionaisBrasilMensalDTO> transformChegadasTuristasInternacionaisBrasilMensal(List<List<Object>> data, String fonte, String edicao) {

        try {
            System.out.println("[INÍCIO] Transformação dos dados iniciada.");
            logDAO.insertLog(
                    1,  // fk_fonte (ajuste conforme necessário)
                    3,  // Categoria: Sucesso (indica que a transformação está sendo iniciada)
                    1,  // Etapa: Extração
                    String.format("Transformação dos dados iniciada. Fonte: %s, Edição: %s", fonte, edicao),
                    LocalDateTime.now(),
                    0,  // Quantidade lida ainda não processada
                    0,  // Quantidade inserida
                    "Chegada_Turistas"
            );

            System.out.println("[INFO] Fonte: " + fonte + ", Edição: " + edicao);
            System.out.println("[INFO] Total de registros brutos recebidos: " + (data != null ? data.size() : 0));
            logDAO.insertLog(
                    1,  // fk_fonte
                    3,  // Categoria: Sucesso
                    1,  // Etapa: Extração
                    String.format("Fonte: %s, Edição: %s. Total de registros brutos recebidos: %d", fonte, edicao, data != null ? data.size() : 0),
                    LocalDateTime.now(),
                    data != null ? data.size() : 0,  // Quantidade lida
                    0,  // Quantidade inserida (ainda não foi convertida)
                    "Chegada_Turistas"
            );

            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas_turistas_internacionais_brasil_mensal_dto = new ArrayList<>();

            int linha = 0;
            for (List<Object> datum : data) {
                try {
                    linha++;
                    String pais_origem = datum.get(2).toString();
                    String uf_destino = datum.get(4).toString();
                    String via_acesso = datum.get(6).toString();
                    Integer ano = Double.valueOf(datum.get(8).toString()).intValue();
                    Integer mes = Double.valueOf(datum.get(10).toString()).intValue();
                    Integer chegada = Double.valueOf(datum.get(11).toString()).intValue();

                    if (chegada > 0) {
                        ChegadaTuristasInternacionaisBrasilMensalDTO chegada_turistas_internacionais_brasil_mensal_dto = new ChegadaTuristasInternacionaisBrasilMensalDTO(
                                mes, ano, chegada, via_acesso, uf_destino, pais_origem
                        );

                        chegadas_turistas_internacionais_brasil_mensal_dto.add(chegada_turistas_internacionais_brasil_mensal_dto);
                    }

                } catch (Exception e) {
                    System.out.println("[ERRO] Falha ao transformar a linha " + linha + ": " + datum);
                    logDAO.insertLog(
                            1,  // fk_fonte
                            1,  // Categoria: Erro
                            1,  // Etapa: Extração
                            String.format("Falha ao transformar a linha %d: %s. Erro: %s", linha, datum, e.getMessage()),
                            LocalDateTime.now(),
                            1,  // Quantidade lida
                            0,  // Nenhuma quantidade inserida
                            "Chegada_Turistas"
                    );
                    e.printStackTrace();
                }
            }

            System.out.println(LocalDateTime.now() + "[FIM] Transformação concluída. Total de registros convertidos: " + chegadas_turistas_internacionais_brasil_mensal_dto.size());
            logDAO.insertLog(
                    1,  // fk_fonte
                    3,  // Categoria: Sucesso
                    1,  // Etapa: Extração
                    String.format("Transformação concluída. Total de registros convertidos: %d", chegadas_turistas_internacionais_brasil_mensal_dto.size()),
                    LocalDateTime.now(),
                    0,  // Quantidade lida (já processada)
                    chegadas_turistas_internacionais_brasil_mensal_dto.size(),  // Quantidade inserida
                    "Chegada_Turistas"
            );

            return chegadas_turistas_internacionais_brasil_mensal_dto;
        } catch (Exception e) {
            // Log no banco
            logDAO.insertLog(
                    1,
                    1, // Erro
                    1,
                    "Erro ao tentar transforma dados de chegada: " + e.getMessage(),
                    LocalDateTime.now(),
                    0,
                    0,
                    "Fonte_Dados"
            );
            throw e;
        }

    }

    public FichaSinteseBrasilDTO transformFichaSinteseBrasil(List<List<List<Object>>> data) {

        try {
            return new FichaSinteseBrasilDTO(
                    transformAno(data, 1),
                    transformListGenero(data, 12),
                    transformListFaixaEtaria(data, 13),
                    transformListComposicoesGrupo(data, 4),
                    transformListFontesInformacao(data, 10),
                    transformListUtilizacaoAgenciaViagem(data, 11),
                    transformListMotivosViagem(data, 2),
                    transformListMotivacaoViagemLazer(data, 3),
                    transformListGastosMedioMotivo(data, 5),
                    transformListPermanenciaMediaMotivo(data, 6),
                    transformListDestinosMaisVisitadosPorMotivo(data, 7)


            );
        } catch (Exception e) {
            // Log no banco
            logDAO.insertLog(
                    1,
                    1, // Erro
                    1,
                    "Erro ao tentar transforma dados de chegada: " + e.getMessage(),
                    LocalDateTime.now(),
                    0,
                    0,
                    "Fonte_Dados"
            );
            throw e;
        }
    }

    public FichaSintesePaisDTO transformFichasSintesePais(List<List<List<Object>>> data) {
        try {
            return new FichaSintesePaisDTO(
                    transformAno(data, 1),
                    transformListGenero(data, 12),
                    transformListFaixaEtaria(data, 13),
                    transformListComposicoesGrupo(data, 4),
                    transformListFontesInformacao(data, 10),
                    transformListUtilizacaoAgenciaViagem(data, 11),
                    transformListMotivosViagem(data, 2),
                    transformListMotivacaoViagemLazer(data, 3),
                    transformListGastosMedioMotivo(data, 5),
                    transformListPermanenciaMediaMotivo(data, 6),
                    transformListDestinosMaisVisitadosPorMotivo(data, 7),
                    extractNomePais(data, 0)
            );
        } catch (Exception e) {
            // Log no banco
            logDAO.insertLog(
                    1,
                    1, // Erro
                    1,
                    "Erro ao tentar transforma dados de chegada: " + e.getMessage(),
                    LocalDateTime.now(),
                    0,
                    0,
                    "Fonte_Dados"
            );
            throw e;
        }
    }

    public FichaSinteseEstadoDTO transformFichasSinteseEstado(List<List<List<Object>>> data) {

        try {
            return new FichaSinteseEstadoDTO(
                    transformAno(data, 1),
                    transformListGenero(data, 14),
                    transformListFaixaEtaria(data, 15),
                    transformListComposicoesGrupo(data, 5),
                    transformListFontesInformacao(data, 13),
                    transformListUtilizacaoAgenciaViagem(data, 12),
                    transformListMotivosViagem(data, 3),
                    transformListMotivacaoViagemLazer(data, 4),
                    transformListGastosMedioMotivo(data, 6),
                    transformListPermanenciaMediaMotivo(data, 7),
                    transformListDestinosMaisVisitadosPorMotivo(data, 9),
                    trasnformListPaisesOrigem(data, 2),
                    trasnformEstado(data, 0),
                    transformListPermanenciaMediaMotivo(data, 8)


            );
        } catch (Exception e) {
            // Log no banco
            logDAO.insertLog(
                    1,
                    1, // Erro
                    1,
                    "Erro ao tentar transforma dados de chegada: " + e.getMessage(),
                    LocalDateTime.now(),
                    0,
                    0,
                    "Fonte_Dados"
            );
            throw e;
        }
    }

    public List<PerfilDTO> transformPerfis(
            ChegadaTuristasInternacionaisBrasilMensalDTO chegada,
            List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas,
            List<FichaSinteseEstadoDTO> fichasSinteseEstadoDTO,
            List<FichaSintesePaisDTO> fichasSintesePaisDTO,
            FichaSinteseBrasilDTO fichaSinteseBrasilDTO
    ) {

        List<PerfilDTO> perfis = new ArrayList<>();

        try {
            System.out.println(LocalDateTime.now() + "[INÍCIO] Criando perfis...");
            logDAO.insertLog(
                    1,  // fk_fonte (ajuste conforme necessário)
                    3,  // Categoria: Sucesso (indica que o processo está começando)
                    1,  // Etapa: Extração (ajuste conforme necessário, ou utilize a etapa correta)
                    "Criando perfis",
                    LocalDateTime.now(),
                    0,  // Quantidade lida ainda não processada
                    0,  // Quantidade inserida
                    "Perfil_Estimado"
            );

            String paisOrigem = chegada.getPaisOrigem();
            String ufDestino = chegada.getUfDestino();
            Integer ano = chegada.getAno();

            Integer qtdChegadas = 0, qtdChegadasPerfil = 0;

            // Tenta encontrar na ficha estadual o pais e o estado

            Optional<FichaSinteseEstadoDTO> fichaEstadoOptional = fichasSinteseEstadoDTO.stream()
                    .filter(f -> f.getDestinoPrincipal().equalsIgnoreCase(ufDestino)
                            && f.getAno().equals(ano) && f.getPaisesOrigem().stream()
                            .anyMatch(p -> p.getPais().equalsIgnoreCase(paisOrigem)))
                    .findFirst();

            if (fichaEstadoOptional.isPresent()) {

                FichaSinteseEstadoDTO fichaEstado = fichaEstadoOptional.get();

                List<PerfilDTO> perfisEstado = createPerfisCombinations(fichaEstado);

                for (PerfilDTO perfilDTOEstado : perfisEstado) {
                    perfilDTOEstado.setPaisesOrigem(paisOrigem);
                    perfilDTOEstado.setEstadoEntrada(ufDestino);
                    perfilDTOEstado.setAno(chegada.getAno());
                    perfilDTOEstado.setMes(chegada.getMes());
                    perfilDTOEstado.setViaAcesso(chegada.getViaAcesso());
                    perfilDTOEstado.setTaxaTuristas(
                            perfilDTOEstado.getTaxaTuristas()
                    );
                    Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilDTOEstado.getTaxaTuristas())).intValue();
                    perfilDTOEstado.setQuantidadeTuristas(qtdTuristas);


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

                int quantidadePaisesFichas = fichaEstado.getPaisesOrigem().size();

                taxaTuristas = taxaTuristas / (quantidadePaises - quantidadePaisesFichas);

                List<PerfilDTO> perfisEstado = createPerfisCombinations(fichaEstado);

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

                List<PerfilDTO> perfisPais = createPerfisCombinations(fichaPais);

                for (PerfilDTO perfilPais : perfisPais) {
                    perfilPais.setPaisesOrigem(paisOrigem);
                    perfilPais.setEstadoEntrada(ufDestino);
                    perfilPais.setAno(chegada.getAno());
                    perfilPais.setMes(chegada.getMes());
                    perfilPais.setViaAcesso(chegada.getViaAcesso());
                    Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilPais.getTaxaTuristas())).intValue();
                    perfilPais.setQuantidadeTuristas(qtdTuristas);

                }

                // Remove todos os perfis com quantidadeTuristas < 1
                perfisPais.removeIf(perfil -> perfil.getQuantidadeTuristas() == null || perfil.getQuantidadeTuristas() < 1);

                perfis.addAll(perfisPais);

                return perfis;
            }

            // Se nenhuma opção for válida, usa a ficha do Brasil

            FichaSinteseBrasilDTO fichaBrasil = fichaSinteseBrasilDTO;

            List<PerfilDTO> perfisBrasil = createPerfisCombinations(fichaBrasil);

            for (PerfilDTO perfilBrasil : perfisBrasil) {
                perfilBrasil.setPaisesOrigem(paisOrigem);
                perfilBrasil.setEstadoEntrada(ufDestino);
                perfilBrasil.setAno(chegada.getAno());
                perfilBrasil.setMes(chegada.getMes());
                perfilBrasil.setViaAcesso(chegada.getViaAcesso());
                Integer qtdTuristas = ((Double) (chegada.getQtdChegadas() * perfilBrasil.getTaxaTuristas())).intValue();
                perfilBrasil.setQuantidadeTuristas(qtdTuristas);
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
