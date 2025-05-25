package tour.wise.etl.extract;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class S3ExcelReader {

    private final S3Client s3;
    private final String bucketName;

    public S3ExcelReader(String bucketName) {
        this.bucketName = bucketName;
        this.s3 = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();
    }

    public Workbook lerExcelDireto(String key) throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        ZipSecureFile.setMinInflateRatio(0.0);
        InputStream inputStream = s3.getObject(request);
        return new XSSFWorkbook(inputStream);
    }
}


