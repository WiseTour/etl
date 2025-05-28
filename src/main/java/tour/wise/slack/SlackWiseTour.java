package tour.wise.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import java.util.Objects;

public class SlackWiseTour {
    private final String webhookUrl;
    private final Slack slack;

    public SlackWiseTour(String webhookUrl) {
        if (webhookUrl == null || webhookUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("A URL do Webhook não pode ser nula/vazia");
        }
        this.webhookUrl = webhookUrl;
        this.slack = Slack.getInstance();
    }

    public void sendNotification(String message) {
        Objects.requireNonNull(message, "A mensagem não pode ser nula");

        try {
            Payload payload = Payload.builder()
                    .text("*Notificação do WiseTour:*\n" + message)
                    .build();

            WebhookResponse response = slack.send(webhookUrl, payload);
            System.out.println("Código HTTP: " + response.getCode());
            System.out.println("Resposta: " + response.getBody());

            if (response.getCode() == 200) {
                System.out.println("Mensagem enviada com sucesso para o Slack!");
            } else {
                System.out.println("Falha ao enviar mensagem para o Slack. Código: " + response.getCode());
            }

        } catch (Exception e) {
            System.out.println("Erro ao enviar para o Slack: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
