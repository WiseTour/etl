package tour.wise.etl.transform;

import tour.wise.dao.UnidadeFederativaBrasilDAO;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.etl.extract.ExtractUtils;
import tour.wise.model.UnidadeFederativaBrasil;
import tour.wise.util.DataBaseConnection;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class TransformUtils {

    static Double tratarValor(Object valor) {
        if (valor == null) {
            return 0.0;
        }
        return ((Number) valor).doubleValue();
    }

    protected static Integer transformAno(List<List<List<Object>>> data, Integer index) {
        // Apenas acessa e converte o valor necessário
        return ExtractUtils.parseToInteger(data.get(index).get(0).get(1).toString());
    }

    protected static List<GeneroDTO> transformListGenero(List<List<List<Object>>> data, Integer index) {
        List<GeneroDTO> generoDTO = new ArrayList<>(2);

        for (int i = 0; i < 2; i++) {
            String nome = (String) data.get(index).get(i).get(0);
            Double valor = tratarValor(data.get(index).get(i).get(1));
            generoDTO.add(new GeneroDTO(nome, valor));
        }
        return generoDTO;
    }

    protected static List<FaixaEtariaDTO> transformListFaixaEtaria(List<List<List<Object>>> data, Integer index) {
        List<FaixaEtariaDTO> faixaEtariaDTO = new ArrayList<>(6);

        List<List<Object>> faixaEtariaData = data.get(index); // Evita chamadas repetidas

        for (int i = 0; i < 6; i++) {
            String nome = (String) faixaEtariaData.get(i).get(0);
            Double valor = tratarValor(faixaEtariaData.get(i).get(1));

            faixaEtariaDTO.add(new FaixaEtariaDTO(nome, valor));
        }

        return faixaEtariaDTO;
    }

    protected static List<MotivoViagemDTO> transformListMotivosViagem(List<List<List<Object>>> data, Integer index) {
        List<MotivoViagemDTO> motivos = new ArrayList<>(3);

        List<List<Object>> motivosData = data.get(index); // Evita chamadas repetidas

        for (int i = 0; i < 3; i++) {
            String motivo = (String) motivosData.get(i).get(0);
            Double valor = tratarValor(motivosData.get(i).get(1));

            motivos.add(new MotivoViagemDTO(motivo, valor));
        }

        return motivos;
    }

    protected static List<MotivacaoViagemLazerDTO> transformListMotivacaoViagemLazer(List<List<List<Object>>> data, Integer index) {
        List<MotivacaoViagemLazerDTO> motivacoesViagemLazer = new ArrayList<>(6);

        List<List<Object>> motivacoesData = data.get(index); // Evita múltiplas chamadas

        for (int i = 0; i < 6; i++) {

            String motivacao;
            Double valor;

            if(data.size() == 7 && i == 4){
                motivacao = (String) motivacoesData.get(7).get(0);
                valor = tratarValor(motivacoesData.get(i).get(1)) + tratarValor(motivacoesData.get(7).get(1));
            }else{
                motivacao = (String) motivacoesData.get(i).get(0);
                valor = tratarValor(motivacoesData.get(i).get(1));
            }


            motivacoesViagemLazer.add(new MotivacaoViagemLazerDTO(motivacao, valor));
        }

        return motivacoesViagemLazer;
    }

    protected static List<ComposicaoGrupoViagemDTO> transformListComposicoesGrupo(List<List<List<Object>>> composicoesGrupoData, Integer index) {
        List<List<Object>> dadosGrupo = composicoesGrupoData.get(index); // Evita chamadas repetidas
        List<ComposicaoGrupoViagemDTO> composicoes = new ArrayList<>(5);

        for (int i = 0; i < 5; i++) {
            String composicao = (String) dadosGrupo.get(i).get(0);
            Double valor = tratarValor(dadosGrupo.get(i).get(1));

            composicoes.add(new ComposicaoGrupoViagemDTO(composicao, valor));
        }

        return composicoes;
    }

    protected static List<GastoMedioPerCapitaMotivoDTO> transformListGastosMedioMotivo(List<List<List<Object>>> gastosMedioMotivoData, Integer index) {
        List<List<Object>> gastosMedios = gastosMedioMotivoData.get(index); // Evita chamadas repetidas
        List<GastoMedioPerCapitaMotivoDTO> gastos = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            String motivo = (String) gastosMedios.get(i).get(0);
            Double valor = tratarValor(gastosMedios.get(i).get(1));

            gastos.add(new GastoMedioPerCapitaMotivoDTO(motivo, valor));
        }

        return gastos;
    }

    protected static List<PermanenciaMediaDTO> transformListPermanenciaMediaMotivo(List<List<List<Object>>> permanenciasMediaMotivoData, Integer index) {
        List<List<Object>> permanenciasMedia = permanenciasMediaMotivoData.get(index); // Evita chamadas repetidas
        List<PermanenciaMediaDTO> permanencias = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            String motivo = (String) permanenciasMedia.get(i).get(0);
            Double valor = tratarValor(permanenciasMedia.get(i).get(1));

            permanencias.add(new PermanenciaMediaDTO(motivo, valor));
        }


        return permanencias;
    }

    protected static List<FonteInformacaoDTO> transformListFontesInformacao(List<List<List<Object>>> fontesInformacaoData, int index) {
        List<List<Object>> fontesInformacao = fontesInformacaoData.get(index); // Evita chamadas repetidas
        List<FonteInformacaoDTO> fontes = new ArrayList<>(8);

        for (int i = 0; i < 8; i++) {
            String motivo = (String) fontesInformacao.get(i).get(0);
            Double valor = tratarValor(fontesInformacao.get(i).get(1));

            fontes.add(new FonteInformacaoDTO(motivo, valor));
        }


        return fontes;
    }

    protected static List<DestinosMaisVisitadosPorMotivoDTO> transformListDestinosMaisVisitadosPorMotivo(List<List<List<Object>>> data, Integer index) {
        final String[] motivos = { "Lazer", "Negócios, eventos e convenções", "Outros motivos" };
        final int tamanho = motivos.length;

        List<DestinosMaisVisitadosPorMotivoDTO> destinosPorMotivo = new ArrayList<>(tamanho);

        int dataSize = data.size();

        for (int i = 0; i < tamanho; i++) {
            int currentIndex = index + i;

            // Garante que não ocorra IndexOutOfBoundsException
            List<List<Object>> dadosMotivo = currentIndex < dataSize ? data.get(currentIndex) : List.of();

            destinosPorMotivo.add(
                    new DestinosMaisVisitadosPorMotivoDTO(
                            motivos[i],
                            createListDestinosMaisVisitados(dadosMotivo)
                    )
            );
        }

        return destinosPorMotivo;
    }

    protected static List<CombinacaoDestinoDTO> createListDestinosMaisVisitados(List<List<Object>> destinosData) {
        Map<String, Double> destinosMap = new HashMap<>();

        for (List<Object> destinoData : destinosData) {
            String nomeCompleto = destinoData.get(0).toString();
            // Divide em 2 partes só (antes e depois do " - ")
            String[] partes = nomeCompleto.split(" - ", 2);
            String nomeDestino = partes.length > 1 ? partes[1] : nomeCompleto;

            Double valor = tratarValor(destinoData.get(1));

            destinosMap.merge(nomeDestino, valor, Double::sum);
        }

        List<DestinoMaisVisitadoDTO> destinos = new ArrayList<>(destinosMap.size());


        for (Map.Entry<String, Double> entry : destinosMap.entrySet()) {
            destinos.add(new DestinoMaisVisitadoDTO(entry.getKey(), entry.getValue()));
        }

        return estimarCombinacoesDestinos(destinos);
    }

    protected static String extractNomePais(List<List<List<Object>>> data, Integer index) {
        if (data == null || data.size() <= index || data.get(index).isEmpty() || data.get(index).get(0).isEmpty()) {
            return ""; // ou null, dependendo do que for melhor para seu caso
        }
        return data.get(index).get(0).get(0).toString();
    }

    protected static String transformEstado(List<List<List<Object>>> data, Integer index) {
        if (data == null || data.size() <= index || data.get(index).isEmpty() || data.get(index).get(0).isEmpty()) {
            return "";
        }
        return data.get(index).get(0).get(0).toString();
    }

    protected static List<PaisOrigemDTO> transformListPaisesOrigem(List<List<List<Object>>> data, int index) {
        List<List<Object>> rawList = data.get(index);
        List<PaisOrigemDTO> result = new ArrayList<>(rawList.size());
        for (List<Object> item : rawList) {
            result.add(createPaisOrigem(item));
        }
        return result;
    }

    protected static PaisOrigemDTO createPaisOrigem(List<Object> row) {
        Object obj0 = row.get(0);
        String nome = (obj0 instanceof String) ? (String) obj0 : obj0.toString();

        Object obj1 = row.get(1);
        double valor;
        if (obj1 instanceof Number) {
            valor = ((Number) obj1).doubleValue();
        } else {
            valor = tratarValor(obj1);
        }


        return new PaisOrigemDTO(nome, valor);
    }

    public static List<CombinacaoDestinoDTO> estimarCombinacoesDestinos(List<DestinoMaisVisitadoDTO> destinos) {
        List<CombinacaoDestinoDTO> resultadoBruto = new ArrayList<>();
        int n = destinos.size();
        int totalCombinacoes = 1 << n; // 2^n
        double somaTotal = 0.0;

        // Mapeia nome -> sigla
        List<UnidadeFederativaBrasil> ufs = UnidadeFederativaBrasilDAO.findAll(DataBaseConnection.getJdbcTemplate());
        Map<String, String> nomeParaSigla = new HashMap<>();
        for (UnidadeFederativaBrasil uf : ufs) {
            nomeParaSigla.put(uf.getUnidadeFederativa(), uf.getSigla());
        }

        for (int i = 0; i < totalCombinacoes; i++) {
            List<String> destinosIncluidos = new ArrayList<>();
            double probabilidade = 1.0;

            for (int j = 0; j < n; j++) {
                DestinoMaisVisitadoDTO destino = destinos.get(j);
                double p = destino.getPorcentagem() / 100.0;
                boolean incluido = (i & (1 << j)) != 0;

                if (incluido) {
                    probabilidade *= p;
                    destinosIncluidos.add(destino.getDestino());
                } else {
                    probabilidade *= (1 - p);
                }
            }

            resultadoBruto.add(new CombinacaoDestinoDTO(destinosIncluidos, probabilidade));
            somaTotal += probabilidade;
        }

        // Normaliza para que a soma seja 100%
        List<CombinacaoDestinoDTO> resultadoFinal = new ArrayList<>();
        for (CombinacaoDestinoDTO combinacao : resultadoBruto) {
            double porcentagemFinal = (combinacao.getPorcentagem() / somaTotal) * 100.0;
            porcentagemFinal = Math.round(porcentagemFinal * 10.0) / 10.0;

            List<String> destinosOriginal = combinacao.getDestinos();
            List<String> destinosConvertidos = new ArrayList<>();

            if (destinosOriginal.isEmpty()) {
                // Caso não tenha destinos
                String siglaOutros = nomeParaSigla.getOrDefault("Outras Unidades da Federação", "Outras Unidades da Federação");
                destinosConvertidos.add(siglaOutros);
            } else {
                for (String destino : destinosOriginal) {
                    destinosConvertidos.add(nomeParaSigla.getOrDefault(destino, destino));
                }
            }

            resultadoFinal.add(new CombinacaoDestinoDTO(destinosConvertidos, porcentagemFinal));
        }

        return resultadoFinal;
    }


    protected static List<PerfilDTO> createPerfisCombinations(FichaSinteseBrasilDTO fichaSinteseBrasilDTO) {
        List<PerfilDTO> perfis = new ArrayList<>();

        // Mapas para acesso rápido por motivo (chave minúscula)
        Map<String, GastoMedioPerCapitaMotivoDTO> gastosPorMotivo = fichaSinteseBrasilDTO.getGastosMedioPerCapitaMotivo()
                .stream()
                .collect(Collectors.toMap(g -> g.getMotivo().toLowerCase(), Function.identity()));

        Map<String, PermanenciaMediaDTO> permanenciaPorMotivo = fichaSinteseBrasilDTO.getPermanenciaMediaDTO()
                .stream()
                .collect(Collectors.toMap(p -> p.getMotivo().toLowerCase(), Function.identity()));

        Map<String, DestinosMaisVisitadosPorMotivoDTO> destinosPorMotivo = fichaSinteseBrasilDTO.getDestinosMaisVisistadosMotivo()
                .stream()
                .collect(Collectors.toMap(d -> d.getMotivo().toLowerCase(), Function.identity()));

        List<MotivacaoViagemLazerDTO> motivacoesViagemLazer = fichaSinteseBrasilDTO.getMotivacoesViagemLazer();

        for (GeneroDTO generoDTO : fichaSinteseBrasilDTO.getGeneroDTO()) {
            double generoPorc = generoDTO.getPorcentagem();
            String genero = generoDTO.getGenero();

            for (FaixaEtariaDTO faixaEtariaDTO : fichaSinteseBrasilDTO.getFaixaEtariaDTO()) {
                double faixaPorc = faixaEtariaDTO.getPorcentagem();
                String faixa = faixaEtariaDTO.getFaixa_etaria();

                for (ComposicaoGrupoViagemDTO composicaoDTO : fichaSinteseBrasilDTO.getComposicaoGruposViagem()) {
                    double composicaoPorc = composicaoDTO.getPorcentagem();
                    String composicao = composicaoDTO.getComposicao();

                    for (FonteInformacaoDTO fonteDTO : fichaSinteseBrasilDTO.getFontesInformacao()) {
                        double fontePorc = fonteDTO.getPorcentagem();
                        String fonte = fonteDTO.getFonte();

                        for (MotivoViagemDTO motivoDTO : fichaSinteseBrasilDTO.getMotivos()) {
                            double motivoPorc = motivoDTO.getPorcentagem();
                            String motivo = motivoDTO.getMotivo();

                            GastoMedioPerCapitaMotivoDTO gasto = gastosPorMotivo.get(motivo.toLowerCase());
                            PermanenciaMediaDTO permanencia = permanenciaPorMotivo.get(motivo.toLowerCase());
                            DestinosMaisVisitadosPorMotivoDTO destinosMotivo = destinosPorMotivo.get(motivo.toLowerCase());

                            if (gasto == null || permanencia == null || destinosMotivo == null) {
                                throw new IllegalArgumentException("Dados faltantes para o motivo '" + motivo + "'");
                            }

                            for (CombinacaoDestinoDTO destinos : destinosMotivo.getDestinos_mais_visistado()) {
                                double taxaTuristasDestino = destinos.getPorcentagem();

                                double taxaTuristas = (generoPorc / 100.0) *
                                        (faixaPorc / 100.0) *
                                        (fontePorc / 100.0) *
                                        (composicaoPorc / 100.0) *
                                        (motivoPorc / 100.0) *
                                        (taxaTuristasDestino / 100.0);

                                if ("Lazer".equalsIgnoreCase(motivo) && !motivacoesViagemLazer.isEmpty()) {
                                    for (MotivacaoViagemLazerDTO motivacao : motivacoesViagemLazer) {
                                        double motivacaoPorc = motivacao.getPorcentagem();

                                        double taxaComMotivacao = taxaTuristas * (motivacaoPorc / 100.0);


                                        perfis.add(new PerfilDTO(
                                                taxaComMotivacao,
                                                fichaSinteseBrasilDTO.getAno(),
                                                genero,
                                                faixa,
                                                composicao,
                                                fonte,
                                                motivo,
                                                motivacao.getMotivacao(),
                                                gasto.getGasto() * permanencia.getDias(),
                                                destinos.getDestinos(),
                                                permanencia.getDias()
                                        ));
                                    }
                                } else {
                                    perfis.add(new PerfilDTO(
                                            taxaTuristas,
                                            fichaSinteseBrasilDTO.getAno(),
                                            genero,
                                            faixa,
                                            composicao,
                                            fonte,
                                            motivo,
                                            null,
                                            gasto.getGasto() * permanencia.getDias(),
                                            destinos.getDestinos(),
                                            permanencia.getDias()
                                    ));
                                }
                            }
                        }
                    }
                }
            }
        }

        return perfis;
    }
}
