package tour.wise.dto.ficha.sintese.brasil;

public class ComposicaoGrupoViagemDTO {
    private String composicao;
    private Double porcentagem;

    public ComposicaoGrupoViagemDTO(String composicao, Double porcentagem) {
        this.composicao = composicao;
        this.porcentagem = porcentagem;
    }

    public String getComposicao() {
        return composicao;
    }

    public void setComposicao(String composicao) {
        this.composicao = composicao;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Composicao_Grupo_Viagem{" +
                "composicao='" + composicao + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
