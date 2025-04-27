package tour.wise.model;

public class Chegada_Turistas_Internacionais_Brasil_Anual {
    private Integer id;
    private Integer ano;
    private Integer chegadas;
    private String fk_uf_sigla;
    private Integer fk_fonte;
    private Integer fk_pais;


    public Chegada_Turistas_Internacionais_Brasil_Anual() {
    }

    public Chegada_Turistas_Internacionais_Brasil_Anual(Integer id, Integer ano, Integer chegadas, String fk_uf_sigla, Integer fk_fonte, Integer fk_pais) {
        this.id = id;
        this.ano = ano;
        this.chegadas = chegadas;
        this.fk_uf_sigla = fk_uf_sigla;
        this.fk_fonte = fk_fonte;
        this.fk_pais = fk_pais;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getFk_uf_sigla() {
        return fk_uf_sigla;
    }

    public void setFk_uf_sigla(String fk_uf_sigla) {
        this.fk_uf_sigla = fk_uf_sigla;
    }

    public Integer getFk_fonte() {
        return fk_fonte;
    }

    public void setFk_fonte(Integer fk_fonte) {
        this.fk_fonte = fk_fonte;
    }

    public Integer getFk_pais() {
        return fk_pais;
    }

    public void setFk_pais(Integer fk_pais) {
        this.fk_pais = fk_pais;
    }

    @Override
    public String toString() {
        return "Chegada_Turistas_Internacionais_Brasil_Anual{" +
                "id=" + id +
                ", ano=" + ano +
                ", chegadas=" + chegadas +
                ", fk_uf_sigla=" + fk_uf_sigla +
                ", fk_fonte=" + fk_fonte +
                ", fk_pais=" + fk_pais +
                '}';
    }
}
