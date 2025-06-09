package tour.wise.etl.extract;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.*;

public class ExtractUtils {


    public static List<List<Object>> extract(Integer fkFonte, String tabela, String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types, Workbook workbook) {

        try {
            System.out.printf("\nIniciando leitura do arquivo %s\n%n", fileName);

            // Pegando a planilha referenciada em "sheetNumber" do arquivo
            Sheet sheet = workbook.getSheetAt(sheetNumber);

            // Criando nova lista
            List<List<Object>> data = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {

                // Extraindo valor das células e criando objeto Linha

                List<Object> linha = new ArrayList<>();

                for (int i = 0; i < colluns ; i++) {

                    linha.add(transformTypeCell(row.getCell(i), types.get(i)));
                }


                data.add(linha);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println(LocalDateTime.now() + "\nLeitura do arquivo finalizada\n");

            return data;


        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> extractRange(
            String fileName,
            Workbook workbook,
            Integer sheetNumber,
            int startRow,
            int endRow,
            List<Integer> columns, // Índices das colunas a serem lidas
            List<String> types, // Tipos das colunas (na mesma ordem de columns)
            Function<List<Object>, T> mapper
    ) {
        try (workbook){

            Sheet sheet = workbook.getSheetAt(sheetNumber);
            List<T> data = new ArrayList<>();

            for (Row row : sheet) {
                int rowNum = row.getRowNum();

                if (rowNum < startRow || rowNum > endRow) {
                    continue;
                }

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
                        if (texto.contains("-") || texto.contains("(") || texto.isEmpty()){
                            return 0;
                        }
                        try {
                            return Double.parseDouble(texto);
                        } catch (NumberFormatException e) {
                            return 0; // ou lançar erro, depende da lógica
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
            return 0; // ou lance uma exceção se quiser validar
        }
    }

    public static List<Integer> getFirstTwoColumnIndexesByValueInRow(Workbook workbook, int sheetNumber, int rowIndex, String value) {
        int numeroDePlanilhas = workbook.getNumberOfSheets();
        List<Integer> colunasEncontradas = new ArrayList<>();

        if (sheetNumber < 0 || sheetNumber >= numeroDePlanilhas) {
            throw new IllegalArgumentException("Índice de planilha fora do intervalo. Total de planilhas: " + numeroDePlanilhas);
        }

        Sheet sheet = workbook.getSheetAt(sheetNumber);
        Row row = sheet.getRow(rowIndex);

        if (row == null) {
            throw new IllegalArgumentException("Linha " + rowIndex + " não encontrada na planilha.");
        }

        for (Cell cell : row) {
            String cellValue = "";

            if (cell.getCellType() == CellType.STRING) {
                cellValue = cell.getStringCellValue().trim();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                double numericValue = cell.getNumericCellValue();
                if (numericValue == (int) numericValue) {
                    cellValue = String.valueOf((int) numericValue);
                } else {
                    cellValue = String.valueOf(numericValue);
                }
            }

            if (cellValue.equalsIgnoreCase(value)) {
                colunasEncontradas.add(cell.getColumnIndex());

                if (colunasEncontradas.size() == 2) {
                    break; // Para após encontrar as duas primeiras
                }
            }
        }

        if (colunasEncontradas.isEmpty()) {
            throw new IllegalArgumentException("Valor \"" + value + "\" não encontrado na linha " + rowIndex + ".");
        }

        return colunasEncontradas;
    }



}
