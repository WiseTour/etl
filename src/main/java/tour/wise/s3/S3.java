package tour.wise.s3;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;
import tour.wise.Evento;
import tour.wise.config.ConfigLoader;
import tour.wise.dao.DataBase;
import tour.wise.dao.LogDAO;
import tour.wise.model.EEtapa;
import tour.wise.model.ELogCategoria;
import tour.wise.model.Log;
import tour.wise.slack.SlackWiseTour;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class S3 {
    private final S3Client s3;
    private final String bucketName;
    private final SlackWiseTour slackWiseTour;
    private final LogDAO logDAO;
    private final DataBase dataBase;

    public S3(SlackWiseTour slackWiseTour) throws IOException, SQLException {

        this.dataBase = new DataBase();
        this.slackWiseTour = slackWiseTour;
        this.logDAO = new LogDAO(dataBase.getJdbcTemplate());

        // Verificações individuais das variáveis de ambiente
        String bucketName = ConfigLoader.get("AWS_BUCKET_NAME");
        if (bucketName.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'AWS_BUCKET_NAME' não encontrada ou vazia.");
        }

        this.bucketName = bucketName;

        String accessKey = ConfigLoader.get("aws_access_key_id");
        if (accessKey.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'aws_access_key_id' não encontrada ou vazia.");
        }

        String secretKey = ConfigLoader.get("aws_secret_access_key");
        if (secretKey.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'aws_secret_access_key' não encontrada ou vazia.");
        }

        String sessionToken = ConfigLoader.get("aws_session_token");
        if (sessionToken.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'aws_session_token' não encontrada ou vazia.");
        }

        String region = ConfigLoader.get("AWS_BUCKET_REGION");
        if (region.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'AWS_BUCKET_REGION' não encontrada ou vazia.");
        }

        // Conectar ao S3
        try {
            AwsSessionCredentials credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);

            this.s3 = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

            String msg = "Conexão com o bucket: " + bucketName + " estabelecida com sucesso.";

            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.CONEXAO_S3, msg, dataBase, slackWiseTour, "gear" );

        } catch (Exception e) {

            String msg = "Não foi possível realizar a conexão com o bucket" + bucketName + ".";

            Evento.registrarEvento(ELogCategoria.ERRO, EEtapa.CONEXAO_S3, e, msg, dataBase, slackWiseTour, "gear" );

            throw new RuntimeException(e);

        }

    }

    public S3Client getS3() {
        return s3;
    }

    public String getBucketName() {
        return bucketName;
    }

    @Override
    public String toString() {
        return "S3{" +
                "s3=" + s3 +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }

    public Workbook readFile(String key) throws IOException, SQLException {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ZipSecureFile.setMinInflateRatio(0.0);
            InputStream inputStream = s3.getObject(request);

            String msg = "Leitura do arquivo: " + key + " realizada com sucesso.";

            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.INCIALIZACAO, msg, dataBase, slackWiseTour, "gear" );

            return key.endsWith(".xlsx") ?
                    new XSSFWorkbook(inputStream) :
                    new HSSFWorkbook(inputStream);

        } catch (Exception e) {

            String msg = "Não foi possível realizar a leitura do arquivo: " + key + ".";

            Evento.registrarEvento(ELogCategoria.ERRO, EEtapa.INCIALIZACAO, e, msg, dataBase, slackWiseTour, "gear" );

            throw new RuntimeException(e);
        }
    }


    public void moveFile(String sourceKey, String destinationKey) throws SQLException {
        try {
            // 1. Copiar o arquivo para o novo local
            CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                    .sourceBucket(bucketName)
                    .sourceKey(sourceKey)
                    .destinationBucket(bucketName)
                    .destinationKey(destinationKey)
                    .build();

            s3.copyObject(copyRequest);

            // 2. Deletar o arquivo original
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(sourceKey)
                    .build();

            s3.deleteObject(deleteRequest);

            String msg = "Arquivo movido com sucesso de " + sourceKey + " para a pasta: " + destinationKey;

            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.INCIALIZACAO, msg, dataBase, slackWiseTour, "gear" );

        } catch (S3Exception e) {
            String msg = "Não foi possível mover o arquivo: " + sourceKey + " para a pasta: " + destinationKey;

            Evento.registrarEvento(ELogCategoria.ERRO, EEtapa.INCIALIZACAO, e, msg, dataBase, slackWiseTour, "gear" );

            throw new RuntimeException(e);
        }
    }


    public List<String> getFileNames() throws SQLException {
        List<String> fileNames = new ArrayList<>();

        try {
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .delimiter("/")  // Importante: faz o S3 tratar "pastas"
                    .build();

            s3.listObjectsV2Paginator(request)
                    .stream()
                    .flatMap(response -> response.contents().stream())
                    .filter(s3Object -> {
                        String key = s3Object.key();
                        return !key.endsWith("/");  // Ignora pastas
                    })
                    .forEach(s3Object -> fileNames.add(s3Object.key()));

            String msg = fileNames.size() + "arquivos encontrados no bucket: " + bucketName;

            Evento.registrarEvento(ELogCategoria.SUCESSO, EEtapa.INCIALIZACAO, msg, dataBase, slackWiseTour, "gear" );

            return fileNames;

        } catch (S3Exception e) {
            String msg = "Não foi possível encontrar nenhum arquivo no bucket: " + bucketName;

            Evento.registrarEvento(ELogCategoria.ERRO, EEtapa.INCIALIZACAO, e, msg, dataBase, slackWiseTour, "gear" );

            throw new RuntimeException(e);
        }
    }

}
