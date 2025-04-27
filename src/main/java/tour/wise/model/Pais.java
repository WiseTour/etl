package tour.wise.model;

import java.util.List;

public class Pais {
    private Integer id_pais;
    private String pais;
    private String continente;
    private List<Idioma> idiomas;

    public Pais() {
    }

    public Pais(String pais) {
        this.pais = pais;
    }

    public Pais(Integer id_pais, String pais, String continente, List<Idioma> idiomas) {
        this.id_pais = id_pais;
        this.pais = pais;
        this.continente = continente;
        this.idiomas = idiomas;
    }

    public Integer getId_pais() {
        return id_pais;
    }

    public void setId_pais(Integer id_pais) {
        this.id_pais = id_pais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getContinente() {
        return continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

    public List<Idioma> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<Idioma> idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        return "Pais{" +
                "id_pais=" + id_pais +
                ", pais='" + pais + '\'' +
                ", continente='" + continente + '\'' +
                ", idiomas=" + idiomas +
                '}';
    }
}
