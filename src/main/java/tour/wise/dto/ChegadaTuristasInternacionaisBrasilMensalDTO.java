package tour.wise.dto;

public class ChegadaTuristasInternacionaisBrasilMensalDTO {
    private Integer mes;
    private Integer ano;
    private Integer qtdChegadas;
    private String viaAcesso;
    private String ufDestino;
    private String paisOrigem;


    public ChegadaTuristasInternacionaisBrasilMensalDTO() {
    }


    public ChegadaTuristasInternacionaisBrasilMensalDTO(Integer mes, Integer ano, Integer qtdChegadas, String viaAcesso, String ufDestino, String paisOrigem) {
        this.mes = mes;
        this.ano = ano;
        this.qtdChegadas = qtdChegadas;
        this.viaAcesso = viaAcesso;
        this.ufDestino = ufDestino;
        this.paisOrigem = paisOrigem;
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

    public String getViaAcesso() {
        return viaAcesso;
    }

    public void setViaAcesso(String viaAcesso) {
        this.viaAcesso = viaAcesso;
    }

    public String getUfDestino() {
        return ufDestino;
    }

    public void setUfDestino(String ufDestino) {
        this.ufDestino = ufDestino;
    }

    public String getPaisOrigem() {
        return paisOrigem;
    }

    public void setPaisOrigem(String paisOrigem) {
        this.paisOrigem = paisOrigem;
    }

    @Override
    public String toString() {
        return "ChegadaTuristasInternacionaisBrasilMensalDTO{" +
                "mes=" + mes +
                ", ano=" + ano +
                ", qtdChegadas=" + qtdChegadas +
                ", viaAcesso='" + viaAcesso + '\'' +
                ", ufDestino='" + ufDestino + '\'' +
                ", paisOrigem='" + paisOrigem + '\'' +
                '}';
    }
}
