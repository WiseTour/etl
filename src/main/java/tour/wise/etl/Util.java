package tour.wise.etl;

import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Util {

    Service service = new Service();

    public  Integer transformAno(List<List<List<Object>>> data, Integer index) {
        // Apenas acessa e converte o valor necessário
        return service.parseToInteger(data.get(index).get(0).get(1).toString());
    }

    public  List<GeneroDTO> transformListGenero(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<GeneroDTO> generoDTO = new ArrayList<>(2);

        // Itera diretamente sobre os índices conhecidos (0 e 1)
        for (int i = 0; i < 2; i++) {
            generoDTO.add(
                    new GeneroDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return generoDTO;
    }

    public  List<FaixaEtariaDTO> transformListFaixaEtaria(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<FaixaEtariaDTO> faixa_etariaDTO = new ArrayList<>(6);

        // Itera sobre os 6 primeiros elementos conhecidos
        for (int i = 0; i < 6; i++) {
            faixa_etariaDTO.add(
                    new FaixaEtariaDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return faixa_etariaDTO;
    }

    public List<MotivoViagemDTO> transformListMotivosViagem(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<MotivoViagemDTO> motivos = new ArrayList<>(3);

        // Itera sobre os 3 primeiros elementos conhecidos
        for (int i = 0; i < 3; i++) {
            motivos.add(
                    new MotivoViagemDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return motivos;
    }

    public List<MotivacaoViagemLazerDTO> transformListMotivacaoViagemLazer(List<List<List<Object>>> data, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<MotivacaoViagemLazerDTO> motivacoes_viagem_lazer = new ArrayList<>(6);

        // Itera sobre os 6 primeiros elementos conhecidos
        for (int i = 0; i <= 5; i++) {
            motivacoes_viagem_lazer.add(
                    new MotivacaoViagemLazerDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString())
                    )
            );
        }

        return motivacoes_viagem_lazer;
    }

    public  List<ComposicaoGrupoViagemDTO> transformListComposicoesGrupo(List<List<List<Object>>> composicoesGrupoData, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<ComposicaoGrupoViagemDTO> composicoes = new ArrayList<>(composicoesGrupoData.get(index).size());

        // Itera diretamente sobre os dados
        for (List<Object> composicaoGrupoData : composicoesGrupoData.get(index)) {
            composicoes.add(createComposicaoGrupo(composicaoGrupoData));
        }

        return composicoes;
    }

    public ComposicaoGrupoViagemDTO createComposicaoGrupo(List<Object> values) {
        // Cria o DTO diretamente a partir dos valores fornecidos
        return new ComposicaoGrupoViagemDTO(
                values.get(0).toString(),
                Double.parseDouble(values.get(1).toString())
        );
    }

    public List<GastoMedioPerCapitaMotivoDTO> transformListGastosMedioMotivo(List<List<List<Object>>> gastosMedioMotivoData, Integer index) {
        // Inicializa a lista com o tamanho exato necessário para evitar alocações excessivas
        List<GastoMedioPerCapitaMotivoDTO> gastos = new ArrayList<>(gastosMedioMotivoData.get(index).size());

        for (List<Object> gastoMedioMotivoData : gastosMedioMotivoData.get(index)) {
            String motivo = gastoMedioMotivoData.get(0).toString();
            double valor = Double.parseDouble(gastoMedioMotivoData.get(1).toString());
            gastos.add(new GastoMedioPerCapitaMotivoDTO(motivo, valor));
        }

        return gastos;
    }

    public List<PermanenciaMediaDTO> transformListPermanenciaMediaMotivo(List<List<List<Object>>> permanenciasMediaMotivoData, Integer index) {
        // Inicializa a lista com o tamanho exato necessário para evitar alocações excessivas
        List<PermanenciaMediaDTO> permanencias = new ArrayList<>(permanenciasMediaMotivoData.get(index).size());

        for (List<Object> permanenciaMediaMotivoData : permanenciasMediaMotivoData.get(index)) {
            String motivo = permanenciaMediaMotivoData.get(0).toString();
            double valor = Double.parseDouble(permanenciaMediaMotivoData.get(1).toString());
            permanencias.add(new PermanenciaMediaDTO(motivo, valor));
        }

        return permanencias;
    }

    public List<FonteInformacaoDTO> transformListFontesInformacao(List<List<List<Object>>> fontesInformacaoData, int index) {
        List<FonteInformacaoDTO> fontes = new ArrayList<>(fontesInformacaoData.get(index).size());

        for (List<Object> fonteInformacaoData : fontesInformacaoData.get(index)) {
            String descricao = fonteInformacaoData.get(0).toString();
            double valor = Double.parseDouble(fonteInformacaoData.get(1).toString());

            fontes.add(new FonteInformacaoDTO(descricao, valor));
        }

        return fontes;
    }

    public List<UtilizacaoAgenciaViagemDTO> transformListUtilizacaoAgenciaViagem(List<List<List<Object>>> utilizacoesAgenciaViagemData, int index) {
        List<UtilizacaoAgenciaViagemDTO> agencias = new ArrayList<>(utilizacoesAgenciaViagemData.get(index).size());

        for (List<Object> utilizacaoAgenciaViagemData : utilizacoesAgenciaViagemData.get(index)) {
            String descricao = utilizacaoAgenciaViagemData.get(0).toString();
            double valor = Double.parseDouble(utilizacaoAgenciaViagemData.get(1).toString());

            agencias.add(new UtilizacaoAgenciaViagemDTO(descricao, valor));
        }

        return agencias;
    }

    public List<DestinosMaisVisitadosPorMotivoDTO> transformListDestinosMaisVisitadosPorMotivo(List<List<List<Object>>> data, Integer index) {
        // Definir os motivos diretamente
        String[] motivos = { "Lazer", "Negócios, eventos e convenções", "Outros motivos" };

        // Inicializa a lista de DestinosMaisVisitadosPorMotivoDTO
        List<DestinosMaisVisitadosPorMotivoDTO> destinosPorMotivo = new ArrayList<>(motivos.length);

        // Processa cada motivo e cria o DTO correspondente
        for (int i = 0; i < motivos.length; i++) {
            destinosPorMotivo.add(
                    new DestinosMaisVisitadosPorMotivoDTO(
                            motivos[i],
                            createListDestinosMaisVisitados(data.get(index + i))
                    )
            );
        }

        return destinosPorMotivo;
    }

    public List<DestinoMaisVisitadoDTO> createListDestinosMaisVisitados(List<List<Object>> destinosData) {
        // Usamos um mapa para armazenar destinos e suas porcentagens, evitando a duplicação
        Map<String, Double> destinosMap = new HashMap<>();

        // Processa cada destino e adiciona/atualiza o valor no mapa
        for (List<Object> destinoData : destinosData) {
            String nomeDestino = destinoData.get(0).toString().split(" - ")[1];
            double valor = Double.parseDouble(destinoData.get(1).toString());

            destinosMap.merge(nomeDestino, valor, Double::sum);
        }

        // Cria a lista de DTOs a partir do mapa
        List<DestinoMaisVisitadoDTO> destinos = new ArrayList<>(destinosMap.size());
        for (Map.Entry<String, Double> entry : destinosMap.entrySet()) {
            destinos.add(new DestinoMaisVisitadoDTO(entry.getKey(), entry.getValue()));
        }

        // Calcula o total e adiciona "Outros estados" se necessário
        double total = destinos.stream().mapToDouble(DestinoMaisVisitadoDTO::getPorcentagem).sum();
        double outrosValor = 100.0 - total;
        if (outrosValor > 0) {
            destinos.add(new DestinoMaisVisitadoDTO("Outros estados", outrosValor));
        }

        return destinos;
    }

    public String extractNomePais(List<List<List<Object>>> data, Integer index) {
        return data.get(index).get(0).get(0).toString();
    }

    public String trasnformEstado(List<List<List<Object>>> data, Integer index) {
        return data.get(index).get(0).get(0).toString();
    }

    public List<PaisOrigemDTO> trasnformListPaisesOrigem(List<List<List<Object>>> data, int index) {
        return data.get(index).stream()
                .map(this::createPaisOrigem)
                .collect(Collectors.toList());
    }

    public PaisOrigemDTO createPaisOrigem(List<Object> row) {
        return new PaisOrigemDTO(
                row.get(0).toString(),
                Double.parseDouble(row.get(1).toString())
        );
    }

}
