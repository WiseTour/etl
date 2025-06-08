package tour.wise;

import tour.wise.util.ConfigLoader;
import tour.wise.etl.ETL;
import tour.wise.util.S3;
import java.util.List;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) throws Exception {

        try {
            ConfigLoader.load(args.length == 0 ? "src/main/resources/config.properties" : args[0]);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a configuração: " + e.getMessage());
            e.printStackTrace();
        }

        List<String> files = S3.getFileNames();

        String caminhoChegadaTurista = null;
        String caminhoArquivoFichaSinteseBrasil = null;
        String caminhoArquivoFichaSintesePaises = null;
        String caminhoArquivoFichaSinteseEstado = null;

        int arquivosEncontrados = 0;

        try {
            caminhoChegadaTurista = files.stream()
                    .filter(file -> file.toLowerCase().contains("chegada"))
                    .toList()
                    .getFirst();
            arquivosEncontrados++;
            System.out.println("Arquivo 'chegada': " + caminhoChegadaTurista);
        } catch (NoSuchElementException e) {
            System.err.println("Arquivo 'chegada' não encontrado.");
        }

        try {
            caminhoArquivoFichaSinteseBrasil = files.stream()
                    .filter(file -> file.toLowerCase().contains("brasil"))
                    .toList()
                    .getFirst();
            arquivosEncontrados++;
            System.out.println("Arquivo 'ficha síntese Brasil': " + caminhoArquivoFichaSinteseBrasil);
        } catch (NoSuchElementException e) {
            System.err.println("Arquivo 'ficha síntese Brasil' não encontrado.");
        }

        try {
            caminhoArquivoFichaSintesePaises = files.stream()
                    .filter(file -> file.toLowerCase().contains("países"))
                    .toList()
                    .getFirst();
            arquivosEncontrados++;
            System.out.println("Arquivo 'ficha síntese países': " + caminhoArquivoFichaSintesePaises);
        } catch (NoSuchElementException e) {
            System.err.println("Arquivo 'ficha síntese países' não encontrado.");
        }

        try {
            caminhoArquivoFichaSinteseEstado = files.stream()
                    .filter(file -> file.toLowerCase().contains("uf"))
                    .toList()
                    .getFirst();
            arquivosEncontrados++;
            System.out.println("Arquivo 'ficha síntese estado': " + caminhoArquivoFichaSinteseEstado);
        } catch (NoSuchElementException e) {
            System.err.println("Arquivo 'ficha síntese estado' não encontrado.");
        }


        if (arquivosEncontrados == 0) {
            System.err.println("Nenhum arquivo correspondente foi encontrado no bucket. Encerrando o ETL...");
            throw new RuntimeException("ETL encerrado: Nenhum arquivo necessário encontrado no S3.");
        } else {

            ETL etl = new ETL();

            etl.exe(
                    caminhoChegadaTurista,
                    caminhoArquivoFichaSintesePaises,
                    caminhoArquivoFichaSinteseBrasil,
                    caminhoArquivoFichaSinteseEstado,
                    "Ministério do Turismo"
            );

        }
    }
}
