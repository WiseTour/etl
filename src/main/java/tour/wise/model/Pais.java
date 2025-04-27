package tour.wise.model;

import java.util.List;

public class Pais {
    private Integer id_pais;
    private String pais;


    public Pais() {
    }

    public Pais(Integer id_pais, String pais) {
        this.id_pais = id_pais;
        this.pais = pais;
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

    @Override
    public String toString() {
        return "Pais{" +
                "id_pais=" + id_pais +
                ", pais='" + pais + '\'' +
                '}';
    }
}
