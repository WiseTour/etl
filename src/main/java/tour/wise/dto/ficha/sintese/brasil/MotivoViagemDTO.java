package tour.wise.dto.ficha.sintese.brasil;

public class MotivoViagemDTO {
    private String motivo;
    private Double porcentagem;

    public MotivoViagemDTO(String motivo, Double porcentagem) {
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
