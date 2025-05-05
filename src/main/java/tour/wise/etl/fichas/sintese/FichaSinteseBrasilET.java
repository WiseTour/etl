package tour.wise.etl.fichas.sintese;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.perfil.DestinoDTO;
import tour.wise.dto.perfil.ListaDestinosDTO;
import tour.wise.dto.perfil.PerfilDTO;
import tour.wise.service.Service;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tour.wise.service.Service.loadWorkbook;

public class FichaSinteseBrasilET {

    Service service = new Service();
    Workbook workbook;

    public FichaSinteseBrasilDTO extractTransformFichaSinteseBrasil(String fileName, Integer collun) throws IOException {

        // EXTRACT

        ZipSecureFile.setMinInflateRatio(0.0001);

        workbook = loadWorkbook(fileName);

        List<List<List<Object>>> data = extractData(
                fileName,
                workbook,
                1,
                List.of(1, 3+collun),
                List.of(10, 12+collun),
                List.of("string", "numeric"));


        workbook.close();

        // TRANSFORM

        FichaSinteseBrasilDTO fichas_sintese_brasil = transformData(data);

        return fichas_sintese_brasil;

    }

    public List<List<List<Object>>> extractData(String fileName, Workbook workbook, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException {

        // Parâmetros das seções a serem lidas
        List<int[]> ranges = List.of(
                new int[]{5, 5}, // ano
                new int[]{7, 9}, // motivo
                new int[]{11, 16}, // motivação viagem lazer
                new int[]{28, 32}, // composição do grupo turístico
                new int[]{34, 36}, // Gasto médio per capita dia no Brasil
                new int[]{39, 41}, // Permanência média no Brasil
                new int[]{45, 49}, // Destinos mais visitados a lazer
                new int[]{51, 55}, // Destinos mais visitados a negócios, eventos e convenções
                new int[]{57, 61}, // Destinos mais visitados utros motivos
                new int[]{64, 71}, // Fonte de informação
                new int[]{73, 75} // Utilização de agência de viagem
        );

        // Lista para consolidar todos os blocos de dados
        List<List<List<Object>>> data = new ArrayList<>();
        data.add(List.of(List.of("Brasil")));

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            leftColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }

        // Parâmetros das seções a serem lidas
        ranges = List.of(
                new int[]{23, 24}, // Gênero
                new int[]{26, 31} // faixa etária
        );

        // Leitura dos dados e consolidação
        for (int[] range : ranges) {

            data.add(
                    service.extractRange(
                            fileName,
                            workbook,
                            sheetNumber,
                            range[0],
                            range[1],
                            rightColluns,
                            collunsType,
                            Function.identity()
                    )
            );

        }


        for (List<List<Object>> datum : data) {
            System.out.println(datum);
        }

        workbook.close();

        return data;
    }

    public FichaSinteseBrasilDTO transformData(List<List<List<Object>>> data) {
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
    }

    protected Integer transformAno(List<List<List<Object>>> data, Integer index) {
        // Apenas acessa e converte o valor necessário
        return service.parseToInteger(data.get(index).get(0).get(1).toString());
    }

    protected List<GeneroDTO> transformListGenero(List<List<List<Object>>> data, Integer index) {
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

    protected List<FaixaEtariaDTO> transformListFaixaEtaria(List<List<List<Object>>> data, Integer index) {
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

    protected List<MotivoViagemDTO> transformListMotivosViagem(List<List<List<Object>>> data, Integer index) {
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

    protected List<MotivacaoViagemLazerDTO> transformListMotivacaoViagemLazer(List<List<List<Object>>> data, Integer index) {
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

    protected List<ComposicaoGrupoViagemDTO> transformListComposicoesGrupo(List<List<List<Object>>> composicoesGrupoData, Integer index) {
        // Inicializa a lista com o tamanho exato para evitar realocações desnecessárias
        List<ComposicaoGrupoViagemDTO> composicoes = new ArrayList<>(composicoesGrupoData.get(index).size());

        // Itera diretamente sobre os dados
        for (List<Object> composicaoGrupoData : composicoesGrupoData.get(index)) {
            composicoes.add(createComposicaoGrupo(composicaoGrupoData));
        }

        return composicoes;
    }

    protected ComposicaoGrupoViagemDTO createComposicaoGrupo(List<Object> values) {
        // Cria o DTO diretamente a partir dos valores fornecidos
        return new ComposicaoGrupoViagemDTO(
                values.get(0).toString(),
                Double.parseDouble(values.get(1).toString())
        );
    }

    protected List<GastoMedioPerCapitaMotivoDTO> transformListGastosMedioMotivo(List<List<List<Object>>> gastosMedioMotivoData, Integer index) {
        // Inicializa a lista com o tamanho exato necessário para evitar alocações excessivas
        List<GastoMedioPerCapitaMotivoDTO> gastos = new ArrayList<>(gastosMedioMotivoData.get(index).size());

        for (List<Object> gastoMedioMotivoData : gastosMedioMotivoData.get(index)) {
            String motivo = gastoMedioMotivoData.get(0).toString();
            double valor = Double.parseDouble(gastoMedioMotivoData.get(1).toString());
            gastos.add(new GastoMedioPerCapitaMotivoDTO(motivo, valor));
        }

        return gastos;
    }

    protected List<PermanenciaMediaDTO> transformListPermanenciaMediaMotivo(List<List<List<Object>>> permanenciasMediaMotivoData, Integer index) {
        // Inicializa a lista com o tamanho exato necessário para evitar alocações excessivas
        List<PermanenciaMediaDTO> permanencias = new ArrayList<>(permanenciasMediaMotivoData.get(index).size());

        for (List<Object> permanenciaMediaMotivoData : permanenciasMediaMotivoData.get(index)) {
            String motivo = permanenciaMediaMotivoData.get(0).toString();
            double valor = Double.parseDouble(permanenciaMediaMotivoData.get(1).toString());
            permanencias.add(new PermanenciaMediaDTO(motivo, valor));
        }

        return permanencias;
    }

    protected List<FonteInformacaoDTO> transformListFontesInformacao(List<List<List<Object>>> fontesInformacaoData, int index) {
        List<FonteInformacaoDTO> fontes = new ArrayList<>(fontesInformacaoData.get(index).size());

        for (List<Object> fonteInformacaoData : fontesInformacaoData.get(index)) {
            String descricao = fonteInformacaoData.get(0).toString();
            double valor = Double.parseDouble(fonteInformacaoData.get(1).toString());

            fontes.add(new FonteInformacaoDTO(descricao, valor));
        }

        return fontes;
    }

    protected List<UtilizacaoAgenciaViagemDTO> transformListUtilizacaoAgenciaViagem(List<List<List<Object>>> utilizacoesAgenciaViagemData, int index) {
        List<UtilizacaoAgenciaViagemDTO> agencias = new ArrayList<>(utilizacoesAgenciaViagemData.get(index).size());

        for (List<Object> utilizacaoAgenciaViagemData : utilizacoesAgenciaViagemData.get(index)) {
            String descricao = utilizacaoAgenciaViagemData.get(0).toString();
            double valor = Double.parseDouble(utilizacaoAgenciaViagemData.get(1).toString());

            agencias.add(new UtilizacaoAgenciaViagemDTO(descricao, valor));
        }

        return agencias;
    }

    protected List<DestinosMaisVisitadosPorMotivoDTO> transformListDestinosMaisVisitadosPorMotivo(List<List<List<Object>>> data, Integer index) {
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

    protected List<DestinoMaisVisitadoDTO> createListDestinosMaisVisitados(List<List<Object>> destinosData) {
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






}
