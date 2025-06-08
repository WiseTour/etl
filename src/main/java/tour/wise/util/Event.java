package tour.wise.util;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.ConfiguracaoSlackDAO;
import tour.wise.dao.LogDAO;
import tour.wise.model.ConfiguracaoSlack;
import tour.wise.model.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Event {

    public static void registerEvent(JdbcTemplate jdbcTemplate, Connection connection, Log log, String mensagem, Exception e, Boolean slack, String emoji) throws Exception {
        String mensagemFormatada = mensagem.substring(0, 1).toUpperCase() + mensagem.substring(1).toLowerCase();
        log.setMensagem(mensagemFormatada + (e != null ? " - " + e.getMessage() : ""));
        LogDAO.insert(jdbcTemplate, log);
        connection.commit();
        if(slack){
            List<String> webhooks = ConfiguracaoSlackDAO.findWebhooksByEtapa(jdbcTemplate, log.getFkEtapa());
            if (!webhooks.isEmpty()) {
                for (String webhook : webhooks) {
                    Slack.sendNotification(webhook, String.format(":%s: %s", emoji, mensagemFormatada));
                }
            }
        }
        System.out.println(LocalDateTime.now() + ": " + mensagemFormatada + (e != null ? " - " + e.getMessage() : ""));
        if(e != null) throw e;
    }


    public static void registerEvent(JdbcTemplate jdbcTemplate, Connection connection, Log log, String mensagem) throws Exception {
        registerEvent(jdbcTemplate, connection, log, mensagem, null, true, "");
    }

    public static void registerEvent(JdbcTemplate jdbcTemplate, Connection connection, Log log, String mensagem, String emoji) throws Exception {
        registerEvent(jdbcTemplate, connection, log, mensagem, null, true, emoji);
    }

    public static void registerEvent(JdbcTemplate jdbcTemplate, Connection connection, Log log, Exception e, String mensagem) throws Exception {
        registerEvent(jdbcTemplate, connection, log, mensagem, e, true, "");
    }


    public static void registerEvent(JdbcTemplate jdbcTemplate, Connection connection, Log log, String mensagem, Boolean slack, String emoji) throws Exception {
        registerEvent(jdbcTemplate, connection, log, mensagem, null, slack, emoji);
    }


    public static void registerEvent(JdbcTemplate jdbcTemplate, Connection connection, Log log, String mensagem, Exception e, Boolean slack) throws Exception {
        registerEvent(jdbcTemplate, connection, log, mensagem, e, slack, "");
    }

    public static void registerEvent(JdbcTemplate jdbcTemplate, Connection connection, Log log, String mensagem, Boolean slack) throws Exception {
        registerEvent(jdbcTemplate, connection, log, mensagem, null, slack, "");
    }
}
