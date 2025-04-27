package tour.wise.model;

public class Unidade_Federativa_Brasil{
   private String sigla;
   private String unidade_federativa;
   private String regiao_brasil;

    public Unidade_Federativa_Brasil() {
    }

    public Unidade_Federativa_Brasil(String unidade_federativa) {
        this.unidade_federativa = unidade_federativa;
    }

    public Unidade_Federativa_Brasil(String sigla, String unidade_federativa, String regiao_brasil) {
        this.sigla = sigla;
        this.unidade_federativa = unidade_federativa;
        this.regiao_brasil = regiao_brasil;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getUnidade_federativa() {
        return unidade_federativa;
    }

    public void setUnidade_federativa(String unidade_federativa) {
        this.unidade_federativa = unidade_federativa;
    }

    public String getRegiao_brasil() {
        return regiao_brasil;
    }

    @Override
    public String toString() {
        return "Unidade_Federativa_Brasil{" +
                "sigla='" + sigla + '\'' +
                ", unidade_federativa='" + unidade_federativa + '\'' +
                ", regiao_brasil='" + regiao_brasil + '\'' +
                '}';
    }

    public void setRegiao_brasil(String regiao_brasil) {
        this.regiao_brasil = regiao_brasil;
    }
}


