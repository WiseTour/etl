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
import tour.wise.config.ConfigLoader;
import tour.wise.dao.DataBase;
import tour.wise.dao.LogDAO;
import tour.wise.model.EEtapa;
import tour.wise.model.ELogCategoria;
import tour.wise.model.Log;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class S3 {
    private final S3Client s3;
    private final String bucketName;
    private final LogDAO logDAO;
    private final DataBase dataBase;

    public S3() throws IOException, SQLException {

        this.dataBase = new DataBase();
        this.logDAO = new LogDAO(dataBase.getJdbcTemplate());

        // Verificações individuais das variáveis de ambiente
        String bucketName = ConfigLoader.get("AWS_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'AWS_BUCKET_NAME' não encontrada ou vazia.");
        }
        this.bucketName = bucketName;

        String accessKey = ConfigLoader.get("aws_access_key_id");
        if (accessKey == null || accessKey.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'aws_access_key_id' não encontrada ou vazia.");
        }

        String secretKey = ConfigLoader.get("aws_secret_access_key");
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'aws_secret_access_key' não encontrada ou vazia.");
        }

        String sessionToken = ConfigLoader.get("aws_session_token");
        if (sessionToken == null || sessionToken.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'aws_session_token' não encontrada ou vazia.");
        }

        String region = ConfigLoader.get("AWS_BUCKET_REGION");
        if (region == null || region.isEmpty()) {
            throw new IllegalArgumentException("Variável de ambiente 'AWS_BUCKET_REGION' não encontrada ou vazia.");
        }

        // Conectar ao S3
        try {
            AwsSessionCredentials credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);

            this.s3 = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

            System.out.println("Conexão com o S3 estabelecida com sucesso.");

        } catch (Exception e) {
            logDAO.insert(new Log(
                    ELogCategoria.ERRO.getId(),
                    EEtapa.CONEXAO_S3.getId(),
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));

            dataBase.getConnection().commit();

            String msg = String.format(
                    "Categoria: %d, Etapa: %d, Mensagem: %s",
                    ELogCategoria.ERRO.getId(),
                    EEtapa.CONEXAO_S3.getId(),
                    e.getMessage()
            );

            throw new RuntimeException(msg, e);

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

            return key.endsWith(".xlsx") ?
                    new XSSFWorkbook(inputStream) :
                    new HSSFWorkbook(inputStream);
        } catch (Exception e) {
            logDAO.insert(new Log(
                    ELogCategoria.ERRO.getId(),
                    EEtapa.INCIALIZACAO.getId(),
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));

            dataBase.getConnection().commit();

            String msg = String.format(
                    "Categoria: %d, Etapa: %d, Mensagem: %s",
                    ELogCategoria.ERRO.getId(),
                    EEtapa.INCIALIZACAO.getId(),
                    e.getMessage()
            );

            throw new RuntimeException(msg, e);
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

            System.out.println("Arquivo movido com sucesso de " + sourceKey + " para " + destinationKey);

        } catch (S3Exception e) {
            logDAO.insert(new Log(
                    ELogCategoria.ERRO.getId(),
                    EEtapa.FINALIZACAO.getId(),
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));

            dataBase.getConnection().commit();

            String msg = String.format(
                    "Categoria: %d, Etapa: %d, Mensagem: %s",
                    ELogCategoria.ERRO.getId(),
                    EEtapa.FINALIZACAO.getId(),
                    e.getMessage()
            );
        }
    }


    public List<String> getFileNames(String prefix) throws SQLException {
        List<String> fileNames = new ArrayList<>();

        try {
            // Usando o paginador correto
            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            // Iterar sobre todas as páginas de resultados
            s3.listObjectsV2Paginator(request)
                    .stream()
                    .flatMap(response -> response.contents().stream())
                    .filter(s3Object -> !s3Object.key().endsWith("/"))  // Filtra diretórios
                    .map(S3Object::key)  // Pega apenas a chave (nome do arquivo)
                    .forEach(fileNames::add);

        } catch (S3Exception e) {
            logDAO.insert(new Log(
                    ELogCategoria.ERRO.getId(),
                    EEtapa.FINALIZACAO.getId(),
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));

            dataBase.getConnection().commit();

            String msg = String.format(
                    "Categoria: %d, Etapa: %d, Mensagem: %s",
                    ELogCategoria.ERRO.getId(),
                    EEtapa.FINALIZACAO.getId(),
                    e.getMessage()
            );
        }

        return fileNames;
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

        } catch (S3Exception e) {
            logDAO.insert(new Log(
                    ELogCategoria.ERRO.getId(),
                    EEtapa.FINALIZACAO.getId(),
                    null,
                    null,
                    null,
                    null,
                    e.getMessage()
            ));

            dataBase.getConnection().commit();

            String msg = String.format(
                    "Categoria: %d, Etapa: %d, Mensagem: %s",
                    ELogCategoria.ERRO.getId(),
                    EEtapa.FINALIZACAO.getId(),
                    e.getMessage()
            );
        }

        return fileNames;
    }

}
