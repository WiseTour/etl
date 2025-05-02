package tour.wise.dto.ficha.sintese.brasil;

public class PermanenciaMediaDTO {

    private String motivo;
    private Double procentagem;

    public PermanenciaMediaDTO() {
    }

    public PermanenciaMediaDTO(String motivo, Double procentagem) {
        this.motivo = motivo;
        this.procentagem = procentagem;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Double getProcentagem() {
        return procentagem;
    }

    public void setProcentagem(Double procentagem) {
        this.procentagem = procentagem;
    }

    @Override
    public String toString() {
        return "Permanencia_Media{" +
                "motivo='" + motivo + '\'' +
                ", procentagem=" + procentagem +
                '}';
    }
}
