package tour.wise.dto.ficha_sintese_brasil;

import java.util.List;

public class Destinos_Mais_Visitados_Por_Motivo {
    private String motivo;
    private List<Destino_Mais_Visitado> destinos_mais_visistado;

    public Destinos_Mais_Visitados_Por_Motivo(String motivo, List<Destino_Mais_Visitado> destinos_mais_visistado) {
        this.motivo = motivo;
        this.destinos_mais_visistado = destinos_mais_visistado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public List<Destino_Mais_Visitado> getDestinos_mais_visistado() {
        return destinos_mais_visistado;
    }

    public void setDestinos_mais_visistado(List<Destino_Mais_Visitado> destinos_mais_visistado) {
        this.destinos_mais_visistado = destinos_mais_visistado;
    }

    @Override
    public String toString() {
        return "Destinos_Mais_Visistados_Por_Motivo{" +
                "motivo='" + motivo + '\'' +
                ", destinos_mais_visistado=" + destinos_mais_visistado +
                '}';
    }
}
