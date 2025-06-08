package tour.wise.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    public static void load(String filePath) throws IOException {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Variável '" + key + "' não encontrada ou vazia no config.properties");
        }
        return value;
    }
}
