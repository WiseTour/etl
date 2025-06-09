package tour.wise.dto.ficha.sintese.brasil;

import java.util.List;

public class DestinosMaisVisitadosPorMotivoDTO {
    private String motivo;
    private List<CombinacaoDestinoDTO> destinos_mais_visistado;

    public DestinosMaisVisitadosPorMotivoDTO(String motivo, List<CombinacaoDestinoDTO> destinos_mais_visistado) {
        this.motivo = motivo;
        this.destinos_mais_visistado = destinos_mais_visistado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public List<CombinacaoDestinoDTO> getDestinos_mais_visistado() {
        return destinos_mais_visistado;
    }

    public void setDestinos_mais_visistado(List<CombinacaoDestinoDTO> destinos_mais_visistado) {
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
