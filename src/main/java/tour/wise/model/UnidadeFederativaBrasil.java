package tour.wise.model;

public class UnidadeFederativaBrasil {

    private String sigla;               // CHAR(2)
    private String unidadeFederativa;   // VARCHAR(45)
    private String regiao;              // VARCHAR(45), pode ser null

    // Construtor padrão
    public UnidadeFederativaBrasil() {}

    // Construtor com parâmetros
    public UnidadeFederativaBrasil(String sigla, String unidadeFederativa, String regiao) {
        this.sigla = sigla;
        this.unidadeFederativa = unidadeFederativa;
        this.regiao = regiao;
    }

    // Getters e Setters
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getUnidadeFederativa() {
        return unidadeFederativa;
    }

    public void setUnidadeFederativa(String unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    @Override
    public String toString() {
        return "UnidadeFederativaBrasil{" +
                "sigla='" + sigla + '\'' +
                ", unidadeFederativa='" + unidadeFederativa + '\'' +
                ", regiao='" + regiao + '\'' +
                '}';
    }
}
