package tour.wise.dto.ficha.sintese.brasil;

public class UtilizacaaAgenciaViagemDTO {
    private String tipo;
    private Double porcentagem;

    public UtilizacaaAgenciaViagemDTO(String tipo, Double porcentagem) {
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
