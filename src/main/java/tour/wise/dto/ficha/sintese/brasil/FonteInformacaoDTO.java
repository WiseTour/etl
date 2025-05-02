package tour.wise.dto.ficha.sintese.brasil;

public class FonteInformacaoDTO {
    private String fonte;
    private Double porcentagem;

    public FonteInformacaoDTO(String fonte, Double porcentagem) {
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
