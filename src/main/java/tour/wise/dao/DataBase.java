package tour.wise.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DataBase {

    private final String url;
    private final String username;
    private final String password;
    private final DataSource dataSource;
    private final Connection connection;
    private final JdbcTemplate jdbcTemplate;

    public DataBase() throws SQLException {

        Properties props = new Properties();
        String tempDBName = "";
        String tempHost = "";
        String tempPort = "";
        String tempUsername = "";
        String tempPassword = "";

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Arquivo config.properties não encontrado no classpath.");
            }
            props.load(input);
            tempDBName = props.getProperty("DB_NAME");
            tempHost = props.getProperty("DB_HOST");
            tempPort = props.getProperty("DB_PORT");
            tempUsername = props.getProperty("DB_USERNAME");
            tempPassword = props.getProperty("DB_PASSWORD");


        } catch (IOException e) {
            e.printStackTrace();
        }

        this.url = "jdbc:mysql://" + tempHost + ":" + tempPort + "/" + tempDBName;
        this.username = tempUsername;
        this.password = tempPassword;

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(this.url);
        basicDataSource.setUsername(this.username);
        basicDataSource.setPassword(this.password);

        this.dataSource = basicDataSource;
        this.connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        this.jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, false));

    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Connection getConnection() {
        return connection;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
