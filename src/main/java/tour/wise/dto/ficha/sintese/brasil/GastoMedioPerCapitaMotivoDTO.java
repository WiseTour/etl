package tour.wise.dto.ficha.sintese.brasil;

public class GastoMedioPerCapitaMotivoDTO {
    private String motivo;
    private Double gasto;

    public GastoMedioPerCapitaMotivoDTO(String motivo, Double gasto) {
        this.motivo = motivo;
        this.gasto = gasto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Double getGasto() {
        return gasto;
    }

    public void setGasto(Double gasto) {
        this.gasto = gasto;
    }

    @Override
    public String toString() {
        return "Gasto_Medio_Per_Capita_Brasil_Motivo{" +
                "motivo='" + motivo + '\'' +
                ", gasto=" + gasto +
                '}';
    }
}
