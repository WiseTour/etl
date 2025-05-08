package tour.wise;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private final String url = "jdbc:mysql://localhost:3306/WiseTour";
    private final String user = "phelipe";
    private final String password = "Palmeiras220179.";

    public Log() throws SQLException {
    }

    public void inserirLog(int fkFonte, int fkCategoria, int fkEtapa, String mensagem, int lidos, int inseridos, String tabelaDestino) {
        String sql = "INSERT INTO Log (fk_fonte, fk_log_categoria, fk_etapa, mensagem, data_hora, quantidade_lida, quantidade_inserida, tabela_destino) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fkFonte);
            stmt.setInt(2, fkCategoria);
            stmt.setInt(3, fkEtapa);
            stmt.setString(4, mensagem);
            stmt.setInt(5, lidos);
            stmt.setInt(6, inseridos);
            stmt.setString(7, tabelaDestino);

            stmt.executeUpdate();
            System.out.println("Log inserido com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLog(String mensagem, String nivel, String etapa, DateTimeFormatter formatter) {
        String dataHora = LocalDateTime.now().format(formatter);
        System.out.printf("[%s] [%s] [%s] %s%n", dataHora, nivel, etapa, mensagem);
    }

    public int buscarFonteDeDadosPorNome(String nomeFonte) {
        String sql = "SELECT id_fonte_dados FROM Fonte_Dados WHERE titulo_arquivo_fonte = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomeFonte);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_fonte_dados");
            } else {
                System.out.println("Base de dados não encontrada!");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

