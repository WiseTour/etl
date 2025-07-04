package tour.wise.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import tour.wise.model.EEtapa;
import tour.wise.model.ELogCategoria;
import tour.wise.model.Log;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class S3 {
    private static final String bucketName;
    private static final AwsSessionCredentials credentials;
    private static final S3Client s3;

    static {
        try {
            bucketName = ConfigLoader.get("AWS_BUCKET_NAME");
            String accessKey = ConfigLoader.get("aws_access_key_id");
            String secretKey = ConfigLoader.get("aws_secret_access_key");
            String sessionToken = ConfigLoader.get("aws_session_token");
            String region = ConfigLoader.get("AWS_BUCKET_REGION");

            credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);

            s3 = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar configurações do AWS S3: " + e.getMessage(), e);
        }
    }

    public static Workbook readFile(String key) throws Exception {
        Connection conn = null;
        JdbcTemplate jdbc = null;
        InputStream inputStream = null;

        try {
            conn = DataBaseConnection.getConnection();
            jdbc = new JdbcTemplate(new SingleConnectionDataSource(conn, false));

            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            try {
                ZipSecureFile.setMinInflateRatio(0.0);
                inputStream = s3.getObject(request);
            } catch (Exception e) {
                String msg = "Erro ao obter objeto S3 para o arquivo: " + key;
                Event.registerEvent(jdbc, conn,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                        msg, false);
                throw new RuntimeException(msg, e);
            }

            Workbook workbook;
            try {
                if (key.endsWith(".xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                } else {
                    workbook = new HSSFWorkbook(inputStream);
                }
            } catch (Exception e) {
                String msg = "Erro ao criar Workbook para o arquivo: " + key;
                Event.registerEvent(jdbc, conn,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                        msg, false);
                throw new RuntimeException(msg, e);
            }

            String msg = "Leitura do arquivo: " + key + " realizada com sucesso.";
            Event.registerEvent(jdbc, conn,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.INCIALIZACAO.getId()),
                    msg, false);

            return workbook;

        } catch (Exception e) {
            String msg = "Não foi possível realizar a leitura do arquivo: " + key + ". Erro: " + e.getMessage();
            if (jdbc != null && conn != null) {
                try {
                    Event.registerEvent(jdbc, conn,
                            new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                            msg, false);
                } catch (Exception logEx) {
                    System.err.println("Falha ao registrar evento de erro: " + logEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ignored) {}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ignored) {}
            }
        }
    }

    public static void moveFile(String sourceKey, String destinationKey) throws Exception {
        Connection conn = null;
        JdbcTemplate jdbc = null;

        try {
            conn = DataBaseConnection.getConnection();
            jdbc = new JdbcTemplate(new SingleConnectionDataSource(conn, false));

            try {
                // 1. Copiar o arquivo para o novo local
                CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                        .sourceBucket(bucketName)
                        .sourceKey(sourceKey)
                        .destinationBucket(bucketName)
                        .destinationKey(destinationKey)
                        .build();

                s3.copyObject(copyRequest);
            } catch (Exception e) {
                String msg = "Falha ao copiar arquivo de " + sourceKey + " para " + destinationKey;
                Event.registerEvent(jdbc, conn,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.FINALIZACAO.getId()),
                        msg, false);
                throw new RuntimeException(msg, e);
            }

            try {
                // 2. Deletar o arquivo original
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(sourceKey)
                        .build();

                s3.deleteObject(deleteRequest);
            } catch (Exception e) {
                String msg = "Falha ao deletar arquivo original: " + sourceKey;
                Event.registerEvent(jdbc, conn,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.FINALIZACAO.getId()),
                        msg, false);
                throw new RuntimeException(msg, e);
            }

            String msg = "Arquivo movido com sucesso de " + sourceKey + " para a pasta: " + destinationKey;
            Event.registerEvent(jdbc, conn,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.FINALIZACAO.getId()),
                    msg, false);

        } catch (Exception e) {
            String msg = "Não foi possível mover o arquivo: " + sourceKey + " para a pasta: " + destinationKey + ". Erro: " + e.getMessage();
            if (jdbc != null && conn != null) {
                try {
                    Event.registerEvent(jdbc, conn,
                            new Log(ELogCategoria.ERRO.getId(), EEtapa.FINALIZACAO.getId()),
                            msg, false);
                } catch (Exception logEx) {
                    System.err.println("Falha ao registrar evento de erro: " + logEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ignored) {}
            }
        }
    }

    public static List<String> getFileNames() throws Exception {
        Connection conn = null;
        JdbcTemplate jdbc = null;
        List<String> fileNames = new ArrayList<>();

        try {
            conn = DataBaseConnection.getConnection();
            jdbc = new JdbcTemplate(new SingleConnectionDataSource(conn, false));

            ListObjectsV2Request request = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .delimiter("/")  // faz o S3 tratar "pastas"
                    .build();

            try {
                s3.listObjectsV2Paginator(request)
                        .stream()
                        .flatMap(response -> response.contents().stream())
                        .filter(s3Object -> {
                            String key = s3Object.key();
                            return !key.endsWith("/");  // Ignora "pastas"
                        })
                        .forEach(s3Object -> fileNames.add(s3Object.key()));
            } catch (Exception e) {
                String msg = "Erro ao listar objetos no bucket: " + bucketName;
                Event.registerEvent(jdbc, conn,
                        new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                        msg, false);
                throw new RuntimeException(msg, e);
            }

            String msg = fileNames.size() + " arquivos encontrados no bucket: " + bucketName;
            Event.registerEvent(jdbc, conn,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.INCIALIZACAO.getId()),
                    msg, false);

            return fileNames;

        } catch (Exception e) {
            String msg = "Não foi possível encontrar nenhum arquivo no bucket: " + bucketName + ". Erro: " + e.getMessage();
            if (jdbc != null && conn != null) {
                try {
                    Event.registerEvent(jdbc, conn,
                            new Log(ELogCategoria.ERRO.getId(), EEtapa.INCIALIZACAO.getId()),
                            msg, false);
                } catch (Exception logEx) {
                    System.err.println("Falha ao registrar evento de erro: " + logEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ignored) {}
            }
        }
    }


}
