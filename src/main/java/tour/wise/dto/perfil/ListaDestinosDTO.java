package tour.wise.dto.perfil;

import java.util.List;

public class ListaDestinosDTO {
    List<DestinoDTO> destinos;
    Double permanenciaMedia;

    public ListaDestinosDTO() {
    }

    public ListaDestinosDTO(List<DestinoDTO> destinos, Double permanenciaMedia) {
        this.destinos = destinos;
        this.permanenciaMedia = permanenciaMedia;
    }

    public List<DestinoDTO> getDestinos() {
        return destinos;
    }

    public void setDestinos(List<DestinoDTO> destinos) {
        this.destinos = destinos;
    }

    public Double getPermanenciaMedia() {
        return permanenciaMedia;
    }

    public void setPermanenciaMedia(Double permanenciaMedia) {
        this.permanenciaMedia = permanenciaMedia;
    }

    @Override
    public String toString() {
        return "ListaDestinosDTO{" +
                "destino=" + destinos +
                ", permanenciaMedia=" + permanenciaMedia +
                '}';
    }
}
