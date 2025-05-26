package tour.wise.model;

public class Unidade_Federativa_Brasil{
   private String sigla;
   private String unidade_federativa;
   private String regiao;

    public Unidade_Federativa_Brasil() {
    }

    public Unidade_Federativa_Brasil(String unidade_federativa) {
        this.unidade_federativa = unidade_federativa;
    }

    public Unidade_Federativa_Brasil(String sigla, String unidade_federativa, String regiao) {
        this.sigla = sigla;
        this.unidade_federativa = unidade_federativa;
        this.regiao = regiao;
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

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    @Override
    public String toString() {
        return "Unidade_Federativa_Brasil{" +
                "sigla='" + sigla + '\'' +
                ", unidade_federativa='" + unidade_federativa + '\'' +
                ", regiao_brasil='" + regiao + '\'' +
                '}';
    }

}


