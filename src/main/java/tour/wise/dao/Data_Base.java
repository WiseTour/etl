package tour.wise.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class Data_Base {

    private final String url;
    private final String username;
    private final String password;
    private final DataSource dataSource;

    public Data_Base() {
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

    }



    public JdbcTemplate getConnection() {
        return new JdbcTemplate(dataSource);
    }
}
