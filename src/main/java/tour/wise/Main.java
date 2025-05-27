package tour.wise;

import tour.wise.etl.ETL;
import tour.wise.s3.S3;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        S3 s3 = new S3();

        List<String> files = s3.getFileNames();
        System.out.println(files);

        String caminhoChegadaTurista = files.stream()
                .filter(file -> file.toLowerCase().contains("chegada"))
                .toList().getFirst();
        System.out.println(caminhoChegadaTurista);
        String caminhoArquivoFichaSinteseBrasil = files.stream()
                .filter(file -> file.toLowerCase().contains("brasil"))
                .toList().getFirst();
        System.out.println(caminhoArquivoFichaSinteseBrasil);
        String caminhoArquivoFichaSintesePaises = files.stream()
                .filter(file -> file.toLowerCase().contains("países"))
                .toList().getFirst();
        System.out.println(caminhoArquivoFichaSintesePaises);
        String caminhoArquivoFichaSinteseEstado = files.stream()
                .filter(file -> file.toLowerCase().contains("uf"))
                .toList().getFirst();
        System.out.println(caminhoArquivoFichaSinteseEstado);

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

        Properties props = new Properties();
        props.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));

        s3.moveFile(caminhoChegadaTurista, props.getProperty("S3_PATH_FILES_LOAD_NAME")+"/"+caminhoChegadaTurista);
        s3.moveFile(caminhoArquivoFichaSinteseBrasil, props.getProperty("S3_PATH_FILES_LOAD_NAME")+"/"+caminhoArquivoFichaSinteseBrasil);
        s3.moveFile(caminhoArquivoFichaSinteseEstado, props.getProperty("S3_PATH_FILES_LOAD_NAME")+"/"+caminhoArquivoFichaSinteseEstado);
        s3.moveFile(caminhoArquivoFichaSintesePaises, props.getProperty("S3_PATH_FILES_LOAD_NAME")+"/"+caminhoArquivoFichaSintesePaises);
    }
}
