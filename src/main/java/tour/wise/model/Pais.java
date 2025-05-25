package tour.wise.model;

public class Pais {

    private int idPais;
    private String nomePais;

    // Construtor padrão
    public Pais() {}

    // Construtor com parâmetro
    public Pais(String nomePais) {
        this.nomePais = nomePais;
    }


    // Construtor com parâmetros
    public Pais(int idPais, String nomePais) {
        this.idPais = idPais;
        this.nomePais = nomePais;
    }

    // Getters e Setters
    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public String getNomePais() {
        return nomePais;
    }

    public void setNomePais(String nomePais) {
        this.nomePais = nomePais;
    }

    @Override
    public String toString() {
        return "Pais{" +
                "idPais=" + idPais +
                ", nomePais='" + nomePais + '\'' +
                '}';
    }
}

