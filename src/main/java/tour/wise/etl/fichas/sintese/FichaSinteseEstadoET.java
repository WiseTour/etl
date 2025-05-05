package tour.wise.etl.fichas.sintese;

import org.apache.poi.ss.usermodel.Workbook;
import tour.wise.dto.ficha.sintese.brasil.*;
import tour.wise.dto.ficha.sintese.estado.FichaSinteseEstadoDTO;
import tour.wise.dto.ficha.sintese.estado.PaisOrigemDTO;
import tour.wise.service.Service;
import tour.wise.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tour.wise.service.Service.loadWorkbook;

public class FichaSinteseEstadoET extends FichaSinteseBrasilET {

    Service service = new Service();

    Workbook workbook;

    public List<FichaSinteseEstadoDTO> extractTransformFichasSinteseEstado(String fileName, Integer collun) throws IOException {

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

        List<FichaSinteseEstadoDTO> fichasSinteseEstadoMacroDTO = new ArrayList<>();

        for (List<List<List<Object>>> datum : data) {
            fichasSinteseEstadoMacroDTO.add(transformData(datum));
        }

        return fichasSinteseEstadoMacroDTO;

    }


    public List<List<List<Object>>> extractData(Workbook workbook, String fileName, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException  {

        String sheetName = service.getSheetName(fileName, sheetNumber);
        String estado = sheetName.split("\\s+", 2)[1]; // UF de entrada

        // Parâmetros das seções a serem lidas
        List<int[]> ranges = List.of(
                new int[]{5, 5},  // ano
                new int[]{7, 16}, // país de residência
                new int[]{18, 20}, // motivo da viagem
                new int[]{22, 27}, // motivação da vaigem a lazer
                new int[]{39, 43}, // Composição do grupo turístico
                new int[]{45, 47}, //Gasto médio per capita dia no Brasil
                new int[]{50, 52}, //Permanência média no Brasil
                new int[]{55, 57}, //Permanência média na UF de entrada
                new int[]{71, 72}, // Destinos mais visitados de outras UFs - Lazer
                new int[]{74, 75}, // Destinos mais visitados de outras UFs - Negócios, eventos e convenções
                new int[]{77, 78}, // Destinos mais visitados de outras UFs - Outros motivos
                new int[]{81, 83} // Utilização de agência de viagem
        );

        // Lista para consolidar todos os blocos de dados
        List<List<List<Object>>> data = new ArrayList<>();
        data.add(List.of(List.of(estado)));

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
                new int[]{7, 14}, // Fonte de informação
                new int[]{32, 33}, // Gênero
                new int[]{35, 40} // Faixa etária
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


    @Override
    public FichaSinteseEstadoDTO transformData(List<List<List<Object>>> data) {
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
    }


    protected String trasnformEstado(List<List<List<Object>>> data, Integer index) {
        return data.get(index).get(0).get(0).toString();
    }


    protected List<PaisOrigemDTO> trasnformListPaisesOrigem(List<List<List<Object>>> data, int index) {
        return data.get(index).stream()
                .map(this::createPaisOrigem)
                .collect(Collectors.toList());
    }

    protected PaisOrigemDTO createPaisOrigem(List<Object> row) {
        return new PaisOrigemDTO(
                row.get(0).toString(),
                Double.parseDouble(row.get(1).toString())
        );
    }


}
