package tour.wise.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties properties = new Properties();

    public static void load(String filePath) {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Arquivo de configuração não encontrado em '" + filePath + "'");
            e.printStackTrace();
            // Pode lançar RuntimeException para interromper a aplicação, se desejar:
            // throw new RuntimeException("Arquivo de configuração não encontrado", e);
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de configuração '" + filePath + "'");
            e.printStackTrace();
            // throw new RuntimeException("Erro ao carregar configurações", e);
        } catch (Exception e) {
            System.err.println("Erro inesperado ao carregar o arquivo de configuração '" + filePath + "'");
            e.printStackTrace();
            // throw new RuntimeException("Erro inesperado", e);
        }
    }

    public static String get(String key) {
        try {
            String value = properties.getProperty(key);
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException("Variável '" + key + "' não encontrada ou vazia no config.properties");
            }
            return value;
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao obter propriedade: " + e.getMessage());
            throw e;  // relança para que quem chamou saiba do problema
        } catch (Exception e) {
            System.err.println("Erro inesperado ao obter a propriedade '" + key + "'");
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao obter a propriedade '" + key + "'", e);
        }
    }

}
