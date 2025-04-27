package tour.wise.dto.ficha_sintese_brasil;

public class Fonte_Informacao {
    private String fonte;
    private Double porcentagem;

    public Fonte_Informacao(String fonte, Double porcentagem) {
        this.fonte = fonte;
        this.porcentagem = porcentagem;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Fonte_Informacao{" +
                "fonte='" + fonte + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
