package tour.wise.dto.perfil;

public class DestinoDTO {
    String destino;
    Double permanenciaMedia;

    public DestinoDTO() {
    }

    public DestinoDTO(String destino, Double permanenciaMedia) {
        this.destino = destino;
        this.permanenciaMedia = permanenciaMedia;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getPermanenciaMedia() {
        return permanenciaMedia;
    }

    public void setPermanenciaMedia(Double permanenciaMedia) {
        this.permanenciaMedia = permanenciaMedia;
    }

    @Override
    public String toString() {
        return "Destinos{" +
                "destino='" + destino + '\'' +
                ", permanenciaMedia=" + permanenciaMedia +
                '}';
    }
}
