package tour.wise.model;

public class ConfiguracaoSlack {
    private int idConfiguracaoSlack;
    private int fkUsuario;
    private String webhookCanalPadrao;
    private String ativo;

    public ConfiguracaoSlack() {
    }

    public ConfiguracaoSlack(int idConfiguracaoSlack, int fkUsuario, String webhookCanalPadrao, String ativo) {
        this.idConfiguracaoSlack = idConfiguracaoSlack;
        this.fkUsuario = fkUsuario;
        this.webhookCanalPadrao = webhookCanalPadrao;
        this.ativo = ativo;
    }

    public int getIdConfiguracaoSlack() {
        return idConfiguracaoSlack;
    }

    public void setIdConfiguracaoSlack(int idConfiguracaoSlack) {
        this.idConfiguracaoSlack = idConfiguracaoSlack;
    }

    public int getFkUsuario() {
        return fkUsuario;
    }

    public void setFkUsuario(int fkUsuario) {
        this.fkUsuario = fkUsuario;
    }

    public String getWebhookCanalPadrao() {
        return webhookCanalPadrao;
    }

    public void setWebhookCanalPadrao(String webhookCanalPadrao) {
        this.webhookCanalPadrao = webhookCanalPadrao;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

}