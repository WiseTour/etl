package tour.wise.dto.ficha_sintese_brasil;

public class Gasto_Medio_Per_Capita_Brasil_Motivo {
    private String motivo;
    private Double porcentagem;

    public Gasto_Medio_Per_Capita_Brasil_Motivo(String motivo, Double porcentagem) {
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
