package tour.wise.model;

public class Chegada_Turistas_Internacionais_Brasil_Mensal {
    private Integer id_chegada_turistas_internacionais_brasil_mensal;
    private Integer mes;
    private Integer ano;
    private Integer chegadas;
    private String via_acesso;
    private Integer fk_fonte_dados;
    private Integer fk_pais_origem;


    public Chegada_Turistas_Internacionais_Brasil_Mensal() {
    }


    public Chegada_Turistas_Internacionais_Brasil_Mensal(Integer id_chegada_turistas_internacionais_brasil_mensal, Integer mes, Integer ano, Integer chegadas, String via_acesso, Integer fk_fonte_dados, Integer fk_pais_origem) {
        this.id_chegada_turistas_internacionais_brasil_mensal = id_chegada_turistas_internacionais_brasil_mensal;
        this.mes = mes;
        this.ano = ano;
        this.chegadas = chegadas;
        this.via_acesso = via_acesso;

        this.fk_fonte_dados = fk_fonte_dados;
        this.fk_pais_origem = fk_pais_origem;
    }

    public Integer getId_chegada_turistas_internacionais_brasil_mensal() {
        return id_chegada_turistas_internacionais_brasil_mensal;
    }

    public void setId_chegada_turistas_internacionais_brasil_mensal(Integer id_chegada_turistas_internacionais_brasil_mensal) {
        this.id_chegada_turistas_internacionais_brasil_mensal = id_chegada_turistas_internacionais_brasil_mensal;
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

    public String getVia_acesso() {
        return via_acesso;
    }

    public void setVia_acesso(String via_acesso) {
        this.via_acesso = via_acesso;
    }


    public Integer getFk_fonte_dados() {
        return fk_fonte_dados;
    }

    public void setFk_fonte_dados(Integer fk_fonte_dados) {
        this.fk_fonte_dados = fk_fonte_dados;
    }

    public Integer getFk_pais_origem() {
        return fk_pais_origem;
    }

    public void setFk_pais_origem(Integer fk_pais_origem) {
        this.fk_pais_origem = fk_pais_origem;
    }

    @Override
    public String toString() {
        return "Chegada_Turistas_Internacionais_Brasil_Mensal{" +
                "id_chegada_turistas_internacionais_brasil_mensal=" + id_chegada_turistas_internacionais_brasil_mensal +
                ", mes=" + mes +
                ", ano=" + ano +
                ", chegadas=" + chegadas +
                ", via_acesso='" + via_acesso + '\'' +
                ", fk_fonte_dados=" + fk_fonte_dados +
                ", fk_pais_origem=" + fk_pais_origem +
                '}';
    }
}
