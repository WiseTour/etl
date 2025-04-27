package tour.wise.dto.ficha_sintese_brasil;

public class Utilizacao_Agencia_Viagem {
    private String tipo;
    private Double porcentagem;

    public Utilizacao_Agencia_Viagem(String tipo, Double porcentagem) {
        this.tipo = tipo;
        this.porcentagem = porcentagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Utilizacao_Agencia_Viagem{" +
                "tipo='" + tipo + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
