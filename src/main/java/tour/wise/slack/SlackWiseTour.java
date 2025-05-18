package tour.wise.slack;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

// classe principal responsável por enviar as notificações pro slack via webhook
// utilizando a API oficial do slack p/ java
public class SlackWiseTour {

    private final String webhookUrl;

    // instancia do cliente slack
    private final Slack slack;


    public SlackWiseTour(String webhookUrl) {
        this.webhookUrl = webhookUrl;
        this.slack = Slack.getInstance();
    }

    public void mandarMensagemSimples(String message) {
        try {
            Payload payload = Payload.builder()
                    .text(message)
                    .build();

            WebhookResponse response = slack.send(webhookUrl, payload);

            if (response.getCode() != 200) {
                System.err.println("Falha ao enviar mensagem para o Slack. Código: " + response.getCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem para o Slack: " + e.getMessage());
        }
    }
}
