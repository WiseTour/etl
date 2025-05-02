package tour.wise.dto.ficha.sintese.brasil;

public class GastoMedioPerCapitaMotivoDTO {
    private String motivo;
    private Double porcentagem;

    public GastoMedioPerCapitaMotivoDTO(String motivo, Double porcentagem) {
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
        return "Gasto_Medio_Per_Capita_Brasil_Motivo{" +
                "motivo='" + motivo + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
