package tour.wise.dto.ficha.sintese.brasil;

public class FaixaEtariaDTO {
    private String faixa_etaria;
    private Double porcentagem;

    public FaixaEtariaDTO() {
    }

    public FaixaEtariaDTO(String faixa_etaria, Double porcentagem) {
        this.faixa_etaria = faixa_etaria;
        this.porcentagem = porcentagem;
    }

    public String getFaixa_etaria() {
        return faixa_etaria;
    }

    public void setFaixa_etaria(String faixa_etaria) {
        this.faixa_etaria = faixa_etaria;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Faixa_Etaria{" +
                "faixa_etaria='" + faixa_etaria + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
