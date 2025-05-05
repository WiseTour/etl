package tour.wise.etl.fichas.sintese;

import org.apache.poi.ss.usermodel.Workbook;
import tour.wise.dto.ficha.sintese.FichaSintesePaisDTO;
import tour.wise.service.Service;
import tour.wise.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static tour.wise.service.Service.loadWorkbook;

public class FichaSintesePaisET extends FichaSinteseBrasilET {

    Util util = new Util();
    Service service = new Service();

    Workbook workbook;


    public List<FichaSintesePaisDTO> extractTransformFichasSintesePais(String fileName, Integer collun) throws IOException {

        // EXTRACT

        workbook = loadWorkbook(fileName);

        List<List<List<List<Object>>>> data = new ArrayList<>();
        for(Integer i = 1; i < service.getSheetNumber(fileName); i++ ){
            data.add(extractData(
                    workbook,
                    fileName,
                    i,
                    List.of(1, 3+collun),
                    List.of(10, 12+collun),
                    List.of("string", "numeric")));


        }

        workbook.close();

        // TRANSFORM

        List<FichaSintesePaisDTO> fichas_sintese_por_pais = new ArrayList<>();

        for (List<List<List<Object>>> datum : data) {
            fichas_sintese_por_pais.add(transformData(datum));
        }


        return fichas_sintese_por_pais;

    }

    public List<List<List<Object>>> extractData(Workbook workbook, String fileName, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException  {

        String sheetName = service.getSheetName(fileName, sheetNumber);
        String pais = sheetName.split("\\s+", 2)[1]; // pais de origem

        // Parâmetros das seções a serem lidas
        List<int[]> ranges = List.of(
                new int[]{5, 5}, // ano
                new int[]{7, 9}, // motivo da viagem
                new int[]{11, 17}, // motivacao da vaigem a lazer
                new int[]{29, 33}, // composicao do grupo turístico
                new int[]{35, 37}, // gasto médio percapita dia no Brasil
                new int[]{40, 42}, // permanencia média no Brasil
                new int[]{46, 50}, // destinos mais visitados a lazer
                new int[]{52, 56}, // destinos mais visitados a negócios, eventos e convenções
                new int[]{58, 62}, // destinos mais visitados outros motivos
                new int[]{69, 76} // fonte de informação
        );

        // Lista para consolidar todos os blocos de dados
        List<List<List<Object>>> data = new ArrayList<>();
        data.add(List.of(List.of(pais)));

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
                new int[]{7, 9}, // utilização de agência de viagem
                new int[]{27, 28}, // gênero
                new int[]{30, 36} // faixa etária
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

        System.out.println("\nETL finalizado com sucesso.");

        return data;
    }


    @Override
    public FichaSintesePaisDTO transformData(List<List<List<Object>>> data) {
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
    }

    protected String extractNomePais(List<List<List<Object>>> data, Integer index) {
        return data.get(index).get(0).get(0).toString();
    }


}
