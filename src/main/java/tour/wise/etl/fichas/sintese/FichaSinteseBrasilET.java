package tour.wise.etl.fichas.sintese;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.service.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tour.wise.service.Service.loadWorkbook;

public class FichaSinteseBrasilET {

    Service service = new Service();
    Workbook workbook;

    public List<FichaSinteseBrasilDTO> extractTransform(String fileName, Integer startCollun, Integer endCollun) throws IOException {
        ZipSecureFile.setMinInflateRatio(0.0001);

        workbook = loadWorkbook(fileName);

        List<List<List<List<Object>>>> data = new ArrayList<>();
        for(Integer j = startCollun; j <= endCollun; j++){
            data.add(extract(
                    workbook,
                    1,
                    List.of(1, 3+j),
                    List.of(10, 12+j),
                    List.of("string", "numeric")));

        }

        workbook.close();

        // TRANSFORM

        List<FichaSinteseBrasilDTO> fichas_sintese_brasil = new ArrayList<>();

        for (List<List<List<Object>>> datum : data) {
            fichas_sintese_brasil.add(transform(datum));
            System.out.println(datum);
        }

        return fichas_sintese_brasil;

    }


    public List<List<List<Object>>> extract(Workbook workbook, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException {

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

        System.out.println("\nETL finalizado com sucesso.");

        return data;
    }

    public FichaSinteseBrasilDTO transform(List<List<List<Object>>> data) {
        return new FichaSinteseBrasilDTO(
                transformAno(data, 1),
                transformGenero(data, 12),
                transformFaixaEtaria(data, 13),
                transformComposicoesGrupo(data, 4),
                transformFontesInformacao(data, 10),
                transformUtilizacaoAgenciaViagem(data, 11),
                transformMotivosViagem(data, 2),
                transformMotivacaoViagemLazer(data, 3),
                transformGastosMedioMotivo(data, 5),
                transformPermanenciaMediaMotivo(data, 6),
                transformDestinosMaisVisitadosPorMotivo(data, 7)


        );
    }

    protected Integer transformAno(List<List<List<Object>>> data, Integer index) {
        return service.parseToInteger(data.get(index).getFirst().get(1).toString());
    }

    protected List<GeneroDTO> transformGenero(List<List<List<Object>>> data, Integer index) {
        List<GeneroDTO> generoDTO = new ArrayList<>();


        for (int i = 0; i < 2; i++) {
            generoDTO.add(
                    new GeneroDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString()
                            )
                    )
            );

        }


        return generoDTO;
    }

    protected List<FaixaEtariaDTO> transformFaixaEtaria(List<List<List<Object>>> data, Integer index) {
        List<FaixaEtariaDTO> faixa_etariaDTO = new ArrayList<>();


        for (int i = 0; i < 6; i++) {
            faixa_etariaDTO.add(
                    new FaixaEtariaDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString()
                            )
                    )
            );

        }


        return faixa_etariaDTO;
    }


    protected List<MotivoViagemDTO> transformMotivosViagem(List<List<List<Object>>> data, Integer index) {
        List<MotivoViagemDTO> motivos = new ArrayList<>();


        for (int i = 0; i < 3; i++) {
            motivos.add(
                    new MotivoViagemDTO(
                            data.get(index).get(i).get(0).toString(),
                            Double.parseDouble(data.get(index).get(i).get(1).toString()
                            )
                    )
            );

        }


        return motivos;
    }



    protected List<MotivacaoViagemLazerDTO> transformMotivacaoViagemLazer(List<List<List<Object>>> data, Integer index) {
        List<MotivacaoViagemLazerDTO> motivacoes_viagem_lazer = new ArrayList<>();

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


    protected List<ComposicaoGrupoViagemDTO> transformComposicoesGrupo(List<List<List<Object>>> composicoesGrupoData, Integer index) {
        List<ComposicaoGrupoViagemDTO> composicoes = new ArrayList<>();

        for (List<Object> composicaoGrupoData : composicoesGrupoData.get(index)) {
            composicoes.add(createComposicaoGrupo(composicaoGrupoData));
        }

        return composicoes;
    }

    protected ComposicaoGrupoViagemDTO createComposicaoGrupo(List<Object> values) {
        return new ComposicaoGrupoViagemDTO(
                values.get(0).toString(),
                Double.parseDouble(values.get(1).toString())
        );
    }

    protected List<GastoMedioPerCapitaMotivoDTO> transformGastosMedioMotivo(List<List<List<Object>>> gastosMedioMotivoData, Integer index) {
        List<GastoMedioPerCapitaMotivoDTO> gastos = new ArrayList<>();

        for (List<Object> gastoMedioMotivoData : gastosMedioMotivoData.get(index)) {
            String motivo = gastoMedioMotivoData.get(0).toString();
            double valor = Double.parseDouble(gastoMedioMotivoData.get(1).toString());
            gastos.add(new GastoMedioPerCapitaMotivoDTO(motivo, valor));
        }

        return gastos;
    }

    protected List<PermanenciaMediaDTO> transformPermanenciaMediaMotivo(List<List<List<Object>>> permanenciasMediaMotivoData, Integer index) {
        List<PermanenciaMediaDTO> permanencias = new ArrayList<>();

        for (List<Object> permanenciaMediaMotivoData : permanenciasMediaMotivoData.get(index)) {
            String motivo = permanenciaMediaMotivoData.get(0).toString();
            double valor = Double.parseDouble(permanenciaMediaMotivoData.get(1).toString());
            permanencias.add(new PermanenciaMediaDTO(motivo, valor));
        }

        return permanencias;
    }


    protected List<DestinosMaisVisitadosPorMotivoDTO> transformDestinosMaisVisitadosPorMotivo(List<List<List<Object>>> data, Integer index) {
        String[] motivos = {
                "Lazer",
                "Negócios, eventos e convenções",
                "Outros motivos"
        };

        List<DestinosMaisVisitadosPorMotivoDTO> destinosPorMotivo = new ArrayList<>();



        for (int i = 0; i < motivos.length; i++) {
            destinosPorMotivo.add(
                    new DestinosMaisVisitadosPorMotivoDTO(
                            motivos[i],
                            createDestinos(data.get(index + i))
                    )
            );
        }


        return destinosPorMotivo;
    }

    protected List<DestinoMaisVisitadoDTO> createDestinos(List<List<Object>> destinosData) {
        List<DestinoMaisVisitadoDTO> destinos = new ArrayList<>();

        for (List<Object> destinoData : destinosData) {
            String nomeDestino = destinoData.get(0).toString().split(" - ")[1];
            double valor = Double.parseDouble(destinoData.get(1).toString());

            DestinoMaisVisitadoDTO dto = new DestinoMaisVisitadoDTO(nomeDestino, valor);
            destinos.add(dto);
        }

        DestinoMaisVisitadoDTO dto = new DestinoMaisVisitadoDTO("Outros estados", 100.0 - destinos.getFirst().getPorcentagem() - destinos.getLast().getPorcentagem());
        destinos.add(dto);

        return destinos;
    }


    protected List<FonteInformacaoDTO> transformFontesInformacao(List<List<List<Object>>> fontesInformacaoData, int index) {
        List<FonteInformacaoDTO> fontes = new ArrayList<>();

        for (List<Object> fonteInformacaoData : fontesInformacaoData.get(index)) {
            String descricao = fonteInformacaoData.get(0).toString();
            double valor = Double.parseDouble(fonteInformacaoData.get(1).toString());

            fontes.add(new FonteInformacaoDTO(descricao, valor));
        }

        return fontes;
    }

    protected List<UtilizacaaAgenciaViagemDTO> transformUtilizacaoAgenciaViagem(List<List<List<Object>>> utilizacoesAgenciaViagemData, int index) {
        List<UtilizacaaAgenciaViagemDTO> agencias = new ArrayList<>();

        for (List<Object> utilizacaoAgenciaViagemData : utilizacoesAgenciaViagemData.get(index)) {
            String descricao = utilizacaoAgenciaViagemData.get(0).toString();
            double valor = Double.parseDouble(utilizacaoAgenciaViagemData.get(1).toString());

            agencias.add(new UtilizacaaAgenciaViagemDTO(descricao, valor));
        }

        return agencias;
    }




}
