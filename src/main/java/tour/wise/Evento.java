package tour.wise;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.DataBase;
import tour.wise.dao.LogDAO;
import tour.wise.model.EEtapa;
import tour.wise.model.ELogCategoria;
import tour.wise.model.Log;
import tour.wise.slack.SlackWiseTour;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Evento {

    public static void registrarEvento(
            ELogCategoria eLogCategoria,
            EEtapa etapa,
            Integer fkOrigemDados,
            Integer fkPerfilEstimadoTuristas,
            Integer fkPaisOrigem,
            String fkUfEntrada,
            Exception exception,
            String mensagem,
            DataBase dataBase,
            SlackWiseTour slackWiseTour,
            String slackEmoji) throws SQLException {

        LogDAO logDAO = new LogDAO(dataBase.getJdbcTemplate());

        if (exception != null){
            logDAO.insert(new Log(
                    eLogCategoria.getId(),
                    etapa.getId(),
                    fkOrigemDados,
                    fkPerfilEstimadoTuristas,
                    fkPaisOrigem,
                    fkUfEntrada,
                    exception.getMessage(),
                    mensagem
            ));
        }else{
            logDAO.insert(new Log(
                    eLogCategoria.getId(),
                    etapa.getId(),
                    fkOrigemDados,
                    fkPerfilEstimadoTuristas,
                    fkPaisOrigem,
                    fkUfEntrada,
                    null,
                    mensagem
            ));
        }

        dataBase.getConnection().commit();

        if(slackWiseTour != null){
            slackWiseTour.sendNotification(String.format("[WiseTour] :%s: %s",slackEmoji, mensagem));
        }

        System.out.println(LocalDateTime.now() + ": " + mensagem);

    }

    public static void registrarEvento(
            ELogCategoria eLogCategoria,
            EEtapa etapa,
            Exception exception,
            String mensagem,
            DataBase dataBase,
            SlackWiseTour slackWiseTour,
            String slackEmoji
    ) throws SQLException {

        registrarEvento(eLogCategoria, etapa, null, null, null, null, null, mensagem, dataBase, slackWiseTour, slackEmoji);
    }

    public static void registrarEvento(ELogCategoria eLogCategoria,
                                       EEtapa etapa,
                                       String mensagem,
                                       DataBase dataBase,
                                       SlackWiseTour slackWiseTour,
                                       String slackEmoji
    ) throws SQLException {

        registrarEvento(eLogCategoria, etapa, null, null, null, null, null, mensagem, dataBase, slackWiseTour, slackEmoji);

    }

    public static void registrarEvento(ELogCategoria eLogCategoria,
                                       EEtapa etapa,
                                       String mensagem,
                                       DataBase dataBase
    ) throws SQLException {

        registrarEvento(eLogCategoria, etapa, null, null, null, null, null, mensagem, dataBase, null, null);

    }
}
