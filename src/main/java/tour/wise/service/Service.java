package tour.wise.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Service {

    public List<List<Object>> extract(String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types) {
        try {
            System.out.printf("\nIniciando leitura do arquivo %s\n%n", fileName);

            // Criando um objeto Workbook a partir do arquivo recebido,
            Workbook workbook = loadWorkbook(fileName);

            // Pegando a planilha referenciada em "sheetNumber" do arquivo
            Sheet sheet = workbook.getSheetAt(sheetNumber);

            // Criando nova lista
            List<List<Object>> data = new ArrayList<>();

            // Iterando sobre as linhas da planilha
            for (Row row : sheet) {

                if (row.getRowNum() == header) {
                    System.out.println("\nLendo cabeçalho");

                    for (int i = 0; i < colluns; i++) {
                        String coluna = row.getCell(i).getStringCellValue();
                        System.out.println("Coluna " + i + ": " + coluna);
                    }

                    System.out.println("--------------------");
                    continue;
                }

                // Extraindo valor das células e criando objeto Linha
                System.out.println("Lendo linha " + row.getRowNum());

                List<Object> linha = new ArrayList<>();

                for (int i = 0; i < colluns ; i++) {

                    linha.add(transformTypeCell(row.getCell(i), types.get(i)));
                }


                data.add(linha);
            }

            // Fechando o workbook após a leitura
            workbook.close();

            System.out.println("\nLeitura do arquivo finalizada\n");

            return data;


        } catch (IOException e) {
            // Caso ocorra algum erro durante a leitura do arquivo uma exceção será lançada
            throw new RuntimeException(e);
        }
    }


    public <T> List<T> extractRangeFromAllSheets(
            String fileName,
            int startRow,
            int endRow,
            List<Integer> columns,
            List<String> types,
            Function<List<Object>, T> mapper
    ) {
        List<T> data = new ArrayList<>();

        try (Workbook workbook = loadWorkbook(fileName)) {

            int totalSheets = workbook.getNumberOfSheets();
            System.out.println("\nIniciando leitura de " + totalSheets + " planilhas...\n");

            for (int sheetIndex = 0; sheetIndex < totalSheets; sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                String sheetName = workbook.getSheetName(sheetIndex);
                System.out.println("Lendo planilha: " + sheetName);

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
            }

            System.out.println("\nLeitura de todas as planilhas finalizada\n");
            return data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public  <T> List<T> extractRange(
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

                System.out.println("Lendo linha " + rowNum);
                List<Object> linha = new ArrayList<>();

                for (int i = 0; i < columns.size(); i++) {
                    int colIndex = columns.get(i);
                    String type = types.get(i);

                    linha.add(transformTypeCell(row.getCell(colIndex), type));
                }

                data.add(mapper.apply(linha));
            }

            workbook.close();

            System.out.println("\nLeitura finalizada\n");

            return data;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Workbook loadWorkbook(String fileName) {
        try {
            Path path = Path.of(fileName);
            InputStream excelFile = Files.newInputStream(path);

            System.out.println("\nIniciando leitura do arquivo %s\n".formatted(fileName));

            return fileName.endsWith(".xlsx") ?
                    new XSSFWorkbook(excelFile) :
                    new HSSFWorkbook(excelFile);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSheetName(String fileName, int sheetNumber) {

        try(Workbook workbook = loadWorkbook(fileName)) {

            int numeroDePlanilhas = workbook.getNumberOfSheets();

            if (sheetNumber >= 0 && sheetNumber < numeroDePlanilhas) {
                return workbook.getSheetName(sheetNumber);
            } else {
                System.err.println("Índice fora do intervalo. Total de planilhas: " + numeroDePlanilhas);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Integer getSheetNumber(String fileName) {

        try(Workbook workbook = loadWorkbook(fileName)) {

            Integer sheetNumber = workbook.getNumberOfSheets();


                return sheetNumber;


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Object transformTypeCell(Cell cell, String tipo) {
        if (cell == null) {
            return ""; // ou algum valor padrão, ou até lançar uma exceção personalizada
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
                        if (texto.equals("-") || texto.isEmpty()) {
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



    public Integer parseToInteger(Object obj) {
        if (obj == null) return 0;
        try {
            return (int) Double.parseDouble(obj.toString());
        } catch (NumberFormatException e) {
            return 0; // ou lance uma exceção se quiser validar
        }
    }


}
