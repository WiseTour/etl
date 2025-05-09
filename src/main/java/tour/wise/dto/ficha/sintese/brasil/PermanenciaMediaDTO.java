package tour.wise.dto.ficha.sintese.brasil;

public class PermanenciaMediaDTO {

    private String motivo;
    private Double dias;

    public PermanenciaMediaDTO() {
    }

    public PermanenciaMediaDTO(String motivo, Double dias) {
        this.motivo = motivo;
        this.dias = dias;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Double getDias() {
        return dias;
    }

    public void setDias(Double dias) {
        this.dias = dias;
    }

    @Override
    public String toString() {
        return "Permanencia_Media{" +
                "motivo='" + motivo + '\'' +
                ", procentagem=" + dias +
                '}';
    }
}
