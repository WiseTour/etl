package tour.wise.dto.ficha_sintese_brasil;

public class Motivo_Viagem {
    private String motivo;
    private Double porcentagem;

    public Motivo_Viagem(String motivo, Double porcentagem) {
        this.motivo = motivo;
        this.porcentagem = porcentagem;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Motivo_Viagem{" +
                "motivo='" + motivo + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
