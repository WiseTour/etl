package tour.wise.dto.ficha.sintese.brasil;

public class MotivacaoViagemLazerDTO {
    String motivacao;
    Double porcentagem;

    public MotivacaoViagemLazerDTO() {
    }

    public MotivacaoViagemLazerDTO(String motivacao, Double porcentagem) {
        this.motivacao = motivacao;
        this.porcentagem = porcentagem;
    }

    public String getMotivacao() {
        return motivacao;
    }

    public void setMotivacao(String motivacao) {
        this.motivacao = motivacao;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Motivacao_Viagem_Lazer{" +
                "motivacao='" + motivacao + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
