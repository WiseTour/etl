package tour.wise.dto.ficha.sintese.brasil;

public class DestinoMaisVisitadoDTO {
    private String destino;
    private Double porcentagem;

    public DestinoMaisVisitadoDTO() {

    }

    public DestinoMaisVisitadoDTO(String destino) {
        this.destino = destino;
    }


    public DestinoMaisVisitadoDTO(String destino, Double porcentagem) {
        this.destino = destino;
        this.porcentagem = porcentagem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Destino_Mais_Visistado{" +
                "destino='" + destino + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
