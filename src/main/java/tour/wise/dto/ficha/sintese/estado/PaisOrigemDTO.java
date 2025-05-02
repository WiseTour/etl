package tour.wise.dto.ficha.sintese.estado;

public class PaisOrigemDTO {
    private String pais;
    private Double porcentagem;

    public PaisOrigemDTO(String pais, Double porcentagem) {
        this.pais = pais;
        this.porcentagem = porcentagem;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Pais_Origem{" +
                "pais='" + pais + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
