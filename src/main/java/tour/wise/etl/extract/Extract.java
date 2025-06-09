package tour.wise.etl.extract;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Extract {
    
    public static List<List<List<Object>>> extractFichasSinteseEstadoData(Workbook workbook, String fileName, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException {

        try {

            String sheetName = ExtractUtils.getSheetName(workbook, sheetNumber);
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
                        ExtractUtils.extractRange(
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
                        ExtractUtils.extractRange(
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

            workbook.close();
            return data;
        }catch (Exception e) {

            throw e;
        }
    }

    public static List<List<List<Object>>> extractFichasSintesePaisData(Workbook workbook, String fileName, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException {
        try{

            String sheetName = ExtractUtils.getSheetName(workbook, sheetNumber);
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
                        ExtractUtils.extractRange(
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
                        ExtractUtils.extractRange(
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
            workbook.close();
            return data;
        }catch (Exception e) {

            throw e;
        }
    }

    public static List<List<List<Object>>> extractFichaSinteseBrasilData(Workbook workbook, String fileName, Integer sheetNumber, List<Integer> leftColluns, List<Integer> rightColluns, List<String> collunsType) throws IOException {
        System.out.printf(LocalDateTime.now() +  "\n Iniciando leitura do arquivo %s\n%n", fileName);
        try{
            ZipSecureFile.setMinInflateRatio(0.0001);

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
                        ExtractUtils.extractRange(
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
                        ExtractUtils.extractRange(
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


            workbook.close();

            return data;
        }catch (Exception e) {
            throw e;
        }
    }

    public static List<List<Object>> extractChegadasTuristasInternacionaisBrasilMensalData(Workbook workbook, Integer fkFonte, String tabela, String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types) throws IOException {

        List<List<Object>> data = null;

        try {
            data = ExtractUtils.extract(fkFonte, tabela, fileName, sheetNumber, header, colluns, types, workbook);

            workbook.close();

            return data;

        } catch (Exception e) {
            throw e;
        }

    }

}
