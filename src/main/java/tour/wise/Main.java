package tour.wise;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.config.ConfigLoader;
import tour.wise.dao.DataBase;
import tour.wise.dao.LogDAO;
import tour.wise.etl.ETL;
import tour.wise.s3.S3;
import tour.wise.slack.SlackWiseTour;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        try {
            ConfigLoader.load(args.length == 0 ? "src/main/resources/config.properties" : args[0]);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a configuração: " + e.getMessage());
            e.printStackTrace();
        }

        S3 s3 = new S3(new SlackWiseTour(ConfigLoader.get("SLACK_URL")));

        List<String> files = s3.getFileNames();

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
                    "Chegadas 2019",
                    "Ministério do Turismo",
                    "2019",
                    caminhoArquivoFichaSintesePaises,
                    caminhoArquivoFichaSinteseBrasil,
                    caminhoArquivoFichaSinteseEstado,
                    "Fichas Síntese 2019",
                    "Ministério do Turismo",
                    "2019"
            );

            s3.moveFile(caminhoChegadaTurista, ConfigLoader.get("S3_PATH_FILES_LOAD_NAME") + "/" + caminhoChegadaTurista);
            s3.moveFile(caminhoArquivoFichaSinteseBrasil, ConfigLoader.get("S3_PATH_FILES_LOAD_NAME") + "/" + caminhoArquivoFichaSinteseBrasil);
            s3.moveFile(caminhoArquivoFichaSinteseEstado, ConfigLoader.get("S3_PATH_FILES_LOAD_NAME") + "/" + caminhoArquivoFichaSinteseEstado);
            s3.moveFile(caminhoArquivoFichaSintesePaises, ConfigLoader.get("S3_PATH_FILES_LOAD_NAME") + "/" + caminhoArquivoFichaSintesePaises);
        }
    }
}
