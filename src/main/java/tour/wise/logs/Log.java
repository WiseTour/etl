package tour.wise.logs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private final String url = "jdbc:mysql://localhost:3306/wisetour";
    private final String user = "phelipe";
    private final String password = "Palmeiras220179";

    public void showLog(String msg, String tipo, String etapa, DateTimeFormatter formatter) {
        String dataHora = LocalDateTime.now().format(formatter);
        System.out.println(dataHora + " - " + tipo.toUpperCase() + " [" + etapa + "]: " + msg);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO logs_etl (data_hora, tipo, etapa, mensagem) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, dataHora);
            stmt.setString(2, tipo.toUpperCase());
            stmt.setString(3, etapa);
            stmt.setString(4, msg);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Erro ao salvar log no banco: " + e.getMessage());
        }
    }
}