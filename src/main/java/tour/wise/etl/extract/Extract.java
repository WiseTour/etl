package tour.wise.etl.extract;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.model.EEtapa;
import tour.wise.model.ELogCategoria;
import tour.wise.model.Log;
import tour.wise.util.Event;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Extract {

    public static List<List<List<Object>>> extractFichasSinteseEstadoData(
            Workbook workbook,
            String fileName,
            Integer sheetNumber,
            List<Integer> leftColluns,
            List<Integer> rightColluns,
            List<String> collunsType,
            JdbcTemplate jdbc,
            Connection connection
    ) throws Exception {
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
                    new int[]{45, 47}, // Gasto médio per capita dia no Brasil
                    new int[]{50, 52}, // Permanência média no Brasil
                    new int[]{55, 57}, // Permanência média na UF de entrada
                    new int[]{71, 72}, // Destinos mais visitados de outras UFs - Lazer
                    new int[]{74, 75}, // Destinos mais visitados de outras UFs - Negócios, eventos e convenções
                    new int[]{77, 78}, // Destinos mais visitados de outras UFs - Outros motivos
                    new int[]{81, 83}  // Utilização de agência de viagem
            );

            List<List<List<Object>>> data = new ArrayList<>();
            data.add(List.of(List.of(estado)));

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

            ranges = List.of(
                    new int[]{7, 14},  // Fonte de informação
                    new int[]{32, 33}, // Gênero
                    new int[]{35, 40}  // Faixa etária
            );

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

            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                    "Extração da Ficha Síntese Estado concluída com sucesso!",
                    false
            );

            return data;

        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                    "Erro ao extrair dados da Ficha Síntese Estado.",
                    e,
                    false
            );
            throw e;
        }
    }


    public static List<List<List<Object>>> extractFichasSintesePaisData(
            Workbook workbook,
            String fileName,
            Integer sheetNumber,
            List<Integer> leftColluns,
            List<Integer> rightColluns,
            List<String> collunsType,
            JdbcTemplate jdbc,
            Connection connection
    ) throws Exception {
        try {
            String sheetName = ExtractUtils.getSheetName(workbook, sheetNumber);
            String pais = sheetName.split("\\s+", 2)[1]; // pais de origem

            // Parâmetros das seções a serem lidas
            List<int[]> ranges = List.of(
                    new int[]{5, 5},  // ano
                    new int[]{7, 9},  // motivo da viagem
                    new int[]{11, 17}, // motivação da vaigem a lazer
                    new int[]{29, 33}, // composição do grupo turístico
                    new int[]{35, 37}, // gasto médio per capita dia no Brasil
                    new int[]{40, 42}, // permanência média no Brasil
                    new int[]{46, 50}, // destinos mais visitados a lazer
                    new int[]{52, 56}, // destinos mais visitados a negócios, eventos e convenções
                    new int[]{58, 62}, // destinos mais visitados outros motivos
                    new int[]{69, 76}  // fonte de informação
            );

            List<List<List<Object>>> data = new ArrayList<>();
            data.add(List.of(List.of(pais)));

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

            ranges = List.of(
                    new int[]{7, 9},   // utilização de agência de viagem
                    new int[]{27, 28}, // gênero
                    new int[]{30, 36}  // faixa etária
            );

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

            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                    "Extração da Ficha Síntese País concluída com sucesso!",
                    false
            );

            return data;

        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                    "Erro ao extrair dados da Ficha Síntese País.",
                    e,
                    false
            );
            throw e;
        }
    }


    public static List<List<List<Object>>> extractFichaSinteseBrasilData(
            Workbook workbook,
            String fileName,
            Integer sheetNumber,
            List<Integer> leftColluns,
            List<Integer> rightColluns,
            List<String> collunsType,
            JdbcTemplate jdbc,
            Connection connection
    ) throws Exception {
        try {
            ZipSecureFile.setMinInflateRatio(0.0001);

            // Parâmetros das seções a serem lidas
            List<int[]> ranges = List.of(
                    new int[]{5, 5},   // ano
                    new int[]{7, 9},   // motivo
                    new int[]{11, 16}, // motivação viagem lazer
                    new int[]{28, 32}, // composição do grupo turístico
                    new int[]{34, 36}, // gasto médio per capita dia no Brasil
                    new int[]{39, 41}, // permanência média no Brasil
                    new int[]{45, 49}, // destinos mais visitados a lazer
                    new int[]{51, 55}, // destinos mais visitados a negócios, eventos e convenções
                    new int[]{57, 61}, // destinos mais visitados outros motivos
                    new int[]{64, 71}, // fonte de informação
                    new int[]{73, 75}  // utilização de agência de viagem
            );

            List<List<List<Object>>> data = new ArrayList<>();
            data.add(List.of(List.of("Brasil")));

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

            ranges = List.of(
                    new int[]{23, 24}, // gênero
                    new int[]{26, 31}  // faixa etária
            );

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

            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                    "Extração da Ficha Síntese Brasil concluída com sucesso!",
                    false
            );

            return data;
        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                    "Erro ao extrair dados da Ficha Síntese Brasil.",
                    e,
                    false
            );
            throw e;
        }
    }


    public static List<List<Object>> extractChegadasTuristasInternacionaisBrasilMensalData(
            Workbook workbook,
            Integer fkFonte,
            String tabela,
            String fileName,
            Integer sheetNumber,
            Integer header,
            Integer colluns,
            List<String> types,
            JdbcTemplate jdbc,
            Connection connection
    ) throws Exception {

        List<List<Object>> data = null;

        try {
            data = ExtractUtils.extract(fkFonte, tabela, fileName, sheetNumber, header, colluns, types, workbook);

            workbook.close();

            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.EXTRACAO.getId()),
                    "Extração dos dados de Chegadas de Turistas Internacionais Brasil Mensal concluída com sucesso!",
                    false
            );

            return data;

        } catch (Exception e) {
            Event.registerEvent(jdbc, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.EXTRACAO.getId()),
                    "Erro na extração dos dados de Chegadas de Turistas Internacionais Brasil Mensal.",
                    e,
                    false
            );
            throw e;
        }

    }


}
