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

    public boolean sendNotification(String message) {
        Objects.requireNonNull(message, "A mensagem não pode ser nula");

        try {
            Payload payload = Payload.builder()
                    .text(message)
                    .build();

            WebhookResponse response = slack.send(webhookUrl, payload);
            return response.getCode() == 200;

        } catch (Exception e) {
            return false;
        }
    }
}