package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.util.DataBaseConnection;
import java.util.List;

public class ConfiguracaoSlackDAO {

    private static final JdbcTemplate jdbcTemplate;

    static {
        try {
            jdbcTemplate = DataBaseConnection.getJdbcTemplate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar JdbcTemplate no LogDAO: " + e.getMessage(), e);
        }
    }

    public static List<String> findWebhooksByEtapa(int idEtapa) {
        String sql = """
    SELECT DISTINCT cs.webhook_canal_padrao
    FROM configuracao_slack cs
    INNER JOIN tipo_notificacao_dados tnd
        ON cs.id_configuracao_slack = tnd.fk_configuracao_slack
        AND cs.fk_usuario = tnd.fk_usuario
    WHERE tnd.fk_etapa = ?
      AND cs.ativo = 'sim'
      AND cs.webhook_canal_padrao IS NOT NULL
    """;

        try {
            return jdbcTemplate.query(sql, new Object[]{idEtapa}, (rs, rowNum) -> rs.getString("webhook_canal_padrao"));
        } catch (Exception e) {
            System.err.println("[ConfiguracaoSlackDAO] Erro ao buscar webhooks por etapa:");
            e.printStackTrace();
            throw e;
        }
    }
}
