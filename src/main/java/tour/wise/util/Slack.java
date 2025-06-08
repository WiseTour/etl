package tour.wise.util;

import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import java.util.Objects;

public class Slack {
    private static final com.slack.api.Slack slack;

    static {
        try {
            slack = com.slack.api.Slack.getInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar Slack: " + e.getMessage(), e);
        }
    }

    public static void sendNotification(String webhookUrl, String message) {
        Objects.requireNonNull(message, "A mensagem não pode ser nula");

        try {
            Payload payload = Payload.builder()
                    .text("*Notificação do WiseTour:*\n" + message)
                    .build();

            WebhookResponse response = slack.send(webhookUrl, payload);

            if (response.getCode() == 200) {
                System.out.println("Mensagem enviada com sucesso para o Slack!");
            } else {
                System.out.println("Falha ao enviar mensagem para o Slack. Código: " + response.getCode());
            }

        } catch (Exception e) {
            System.out.println("Erro ao enviar mensagem para o canal do Slack: " + webhookUrl +": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
