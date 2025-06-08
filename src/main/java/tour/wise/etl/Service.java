package tour.wise.etl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Service {

    public static List<List<Object>> extract(
            Integer fkFonte,
            String tabela,
            String fileName,
            Integer sheetNumber,
            Integer header,
            Integer columns,
            List<String> types,
            Workbook workbook
    ) {
        try {
            System.out.printf("\nIniciando leitura do arquivo %s\n%n", fileName);

            Sheet sheet = workbook.getSheetAt(sheetNumber);
            List<List<Object>> data = new ArrayList<>();

            for (Row row : sheet) {
                List<Object> linha = new ArrayList<>();

                for (int i = 0; i < columns; i++) {
                    linha.add(transformTypeCell(row.getCell(i), types.get(i)));
                }

                data.add(linha);
            }

            workbook.close();
            System.out.println(LocalDateTime.now() + "\nLeitura do arquivo finalizada\n");
            return data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> extractRange(
            String fileName,
            Workbook workbook,
            Integer sheetNumber,
            int startRow,
            int endRow,
            List<Integer> columns,
            List<String> types,
            Function<List<Object>, T> mapper
    ) {
        try (workbook) {
            Sheet sheet = workbook.getSheetAt(sheetNumber);
            List<T> data = new ArrayList<>();

            for (Row row : sheet) {
                int rowNum = row.getRowNum();

                if (rowNum < startRow || rowNum > endRow) continue;

                List<Object> linha = new ArrayList<>();
                for (int i = 0; i < columns.size(); i++) {
                    int colIndex = columns.get(i);
                    String type = types.get(i);

                    linha.add(transformTypeCell(row.getCell(colIndex), type));
                }

                data.add(mapper.apply(linha));
            }

            return data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSheetName(Workbook workbook, int sheetNumber) {
        int numeroDePlanilhas = workbook.getNumberOfSheets();

        if (sheetNumber >= 0 && sheetNumber < numeroDePlanilhas) {
            return workbook.getSheetName(sheetNumber);
        }

        System.err.println("Índice fora do intervalo. Total de planilhas: " + numeroDePlanilhas);
        return null;
    }

    public static Integer getSheetNumber(Workbook workbook) {
        return workbook.getNumberOfSheets();
    }

    private static Object transformTypeCell(Cell cell, String tipo) {
        if (cell == null) {
            return "";
        }
        switch (tipo.toLowerCase()) {
            case "string":
                return cell.getStringCellValue();

            case "numeric":
                switch (cell.getCellType()) {
                    case NUMERIC:
                        return cell.getNumericCellValue();
                    case STRING:
                        String texto = cell.getStringCellValue().trim();
                        if (texto.contains("-") || texto.contains("(") || texto.isEmpty()) {
                            return 0;
                        }
                        try {
                            return Double.parseDouble(texto);
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    case BLANK:
                        return 0;
                    default:
                        return 0;
                }

            case "boolean":
                return cell.getBooleanCellValue();

            case "date":
                return cell.getDateCellValue();

            default:
                throw new IllegalArgumentException("Tipo de leitura não suportado: " + tipo);
        }
    }

    public static Integer parseToInteger(Object obj) {
        if (obj == null) return 0;
        try {
            return (int) Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
