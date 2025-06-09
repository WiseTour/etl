package tour.wise.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseConnection {

    private static final String url;
    private static final String username;
    private static final String password;
    private static final BasicDataSource dataSource;
    private static final JdbcTemplate jdbcTemplate;

    static {
        try {
            url = "jdbc:mysql://" +
                    ConfigLoader.get("DB_HOST") + ":" +
                    ConfigLoader.get("DB_PORT") + "/" +
                    ConfigLoader.get("DB_NAME");
        } catch (Exception e) {
            String msg = "Erro ao carregar URL do banco de dados: " + e.getMessage();
            System.err.println(msg);
            throw new RuntimeException(msg, e);
        }

        try {
            username = ConfigLoader.get("DB_USERNAME");
        } catch (Exception e) {
            String msg = "Erro ao carregar usuário do banco de dados: " + e.getMessage();
            System.err.println(msg);
            throw new RuntimeException(msg, e);
        }

        try {
            password = ConfigLoader.get("DB_PASSWORD");
        } catch (Exception e) {
            String msg = "Erro ao carregar senha do banco de dados: " + e.getMessage();
            System.err.println(msg);
            throw new RuntimeException(msg, e);
        }

        try {
            dataSource = new BasicDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            // Configura autoCommit = false para todas as conexões
            dataSource.setDefaultAutoCommit(false);
        } catch (Exception e) {
            String msg = "Erro ao configurar DataSource: " + e.getMessage();
            System.err.println(msg);
            throw new RuntimeException(msg, e);
        }

        try {
            jdbcTemplate = new JdbcTemplate(dataSource);
        } catch (Exception e) {
            String msg = "Erro ao inicializar JdbcTemplate: " + e.getMessage();
            System.err.println(msg);
            throw new RuntimeException(msg, e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            String msg = "Erro ao obter conexão do DataSource: " + e.getMessage();
            System.err.println(msg);
            throw e; // relança a exceção para o chamador tratar
        }
    }

    public static JdbcTemplate getJdbcTemplate() {
        try {
            return jdbcTemplate;
        } catch (Exception e) {
            String msg = "Erro ao obter JdbcTemplate: " + e.getMessage();
            System.err.println(msg);
            throw new RuntimeException(msg, e);
        }
    }

}
