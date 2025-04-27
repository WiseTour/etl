package tour.wise.model;

public class Chegada_Turistas_Internacionais_Brasil_Mensal {
    private Unidade_Federativa_Brasil destino;
    private Pais pais_origem;
    private String via_acesso;
    private Integer mes;
    private Integer ano;
    private Integer chegadas;
    private String fonte;
    private String edicao;


    public Chegada_Turistas_Internacionais_Brasil_Mensal() {
    }

    public Chegada_Turistas_Internacionais_Brasil_Mensal(Unidade_Federativa_Brasil destino, Pais pais_origem, String via_acesso, Integer mes, Integer ano, Integer chegadas, String fonte, String edicao) {
        this.destino = destino;
        this.pais_origem = pais_origem;
        this.via_acesso = via_acesso;
        this.mes = mes;
        this.ano = ano;
        this.chegadas = chegadas;
        this.fonte = fonte;
        this.edicao = edicao;
    }

    public Unidade_Federativa_Brasil getDestino() {
        return destino;
    }

    public void setDestino(Unidade_Federativa_Brasil destino) {
        this.destino = destino;
    }

    public Pais getPais_origem() {
        return pais_origem;
    }

    public void setPais_origem(Pais pais_origem) {
        this.pais_origem = pais_origem;
    }

    public String getVia_acesso() {
        return via_acesso;
    }

    public void setVia_acesso(String via_acesso) {
        this.via_acesso = via_acesso;
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

    public Integer getChegadas() {
        return chegadas;
    }

    public void setChegadas(Integer chegadas) {
        this.chegadas = chegadas;
    }

    public String getFonte() {
        return fonte;
    }

    public void setFonte(String fonte) {
        this.fonte = fonte;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    @Override
    public String toString() {
        return "Chegadas_Turistas_Internacionais_Brasil{" +
                "destino=" + destino +
                ", pais_origem=" + pais_origem +
                ", via_acesso='" + via_acesso + '\'' +
                ", mes=" + mes +
                ", ano=" + ano +
                ", chegadas=" + chegadas +
                ", fonte='" + fonte + '\'' +
                ", edicao=" + edicao +
                '}';
    }
}
