package tour.wise.dto.ficha.sintese.brasil;

import java.util.List;

public class CombinacaoDestinoDTO {
    private List<String> destinos;
    private double porcentagem;

    public CombinacaoDestinoDTO(List<String> destinos, double porcentagem) {
        this.destinos = destinos;
        this.porcentagem = porcentagem;
    }

    public List<String> getDestinos() {
        return destinos;
    }

    public double getPorcentagem() {
        return porcentagem;
    }

    @Override
    public String toString() {
        return destinos + " => " + porcentagem + "%";
    }
}

