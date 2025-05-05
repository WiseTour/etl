package tour.wise.dto.perfil;

public class DestinoDTO {
    String destino;

    public DestinoDTO() {
    }

    public DestinoDTO(String destino) {
        this.destino = destino;

    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }



    @Override
    public String toString() {
        return "Destinos{" +
                "destino='" + destino + '\'' +
                '}';
    }
}
