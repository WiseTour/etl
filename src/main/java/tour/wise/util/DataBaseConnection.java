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
            url      = "jdbc:mysql://" +
                    ConfigLoader.get("DB_HOST") + ":" +
                    ConfigLoader.get("DB_PORT") + "/" +
                    ConfigLoader.get("DB_NAME");

            username = ConfigLoader.get("DB_USERNAME");
            password = ConfigLoader.get("DB_PASSWORD");

            dataSource = new BasicDataSource();
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);

            // Configura autoCommit = false para todas as conexões
            dataSource.setDefaultAutoCommit(false);

            jdbcTemplate = new JdbcTemplate(dataSource);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao inicializar configurações de banco de dados: " + e.getMessage(), e
            );
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
