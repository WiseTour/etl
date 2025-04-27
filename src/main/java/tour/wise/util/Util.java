package tour.wise.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public List<String> getAllFilesName(String dirName){
        File dir = new File(dirName);
        File[] filesArray = dir.listFiles();

        List<String> listFilesName = new ArrayList<>();

        if (filesArray != null) {
            for (File file : filesArray) {
                if (file.isFile()) {
                    listFilesName.add(file.getName());
                    System.out.println(file.getName());
                }
            }
        } else {
            System.out.println("Diretório não encontrado ou está vazio.");
        }

        return listFilesName;

    }


}
