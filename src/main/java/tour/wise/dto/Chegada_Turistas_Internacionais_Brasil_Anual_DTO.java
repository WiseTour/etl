package tour.wise.dto;

public class Chegada_Turistas_Internacionais_Brasil_Anual_DTO {
    private Integer mes;
    private Integer ano;
    private Integer qtdChegadas;
    private String via_acesso;
    private String pais_origem;


    public Chegada_Turistas_Internacionais_Brasil_Anual_DTO() {
    }


    public Chegada_Turistas_Internacionais_Brasil_Anual_DTO(Integer mes, Integer ano, Integer chegadas, String via_acesso, String pais_origem) {
        this.mes = mes;
        this.ano = ano;
        this.qtdChegadas = chegadas;
        this.via_acesso = via_acesso;
        this.pais_origem = pais_origem;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getQtdChegadas() {
        return qtdChegadas;
    }

    public void setQtdChegadas(Integer qtdChegadas) {
        this.qtdChegadas = qtdChegadas;
    }

    public String getVia_acesso() {
        return via_acesso;
    }

    public void setVia_acesso(String via_acesso) {
        this.via_acesso = via_acesso;
    }


    public String getPais_origem() {
        return pais_origem;
    }

    public void setPais_origem(String pais_origem) {
        this.pais_origem = pais_origem;
    }

    @Override
    public String toString() {
        return "Chegada_Turistas_Internacionais_Brasil_Mensal_DTO{" +
                "mes=" + mes +
                ", ano=" + ano +
                ", chegadas=" + qtdChegadas +
                ", via_acesso='" + via_acesso + '\'' +
                ", pais_origem='" + pais_origem + '\'' +
                '}';
    }
}
