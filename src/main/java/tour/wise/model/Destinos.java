package tour.wise.model;

public class Destinos {
    private Integer fk_perfil_turistas;
    private Integer fk_pais_origem;
    private String fk_uf_destino;
    private Double permanencia_media;

    public Destinos() {
    }

    public Destinos(Integer fk_perfil_turistas, Integer fk_pais_origem, String fk_uf_destino, Double permanencia_media) {
        this.fk_perfil_turistas = fk_perfil_turistas;
        this.fk_pais_origem = fk_pais_origem;
        this.fk_uf_destino = fk_uf_destino;
        this.permanencia_media = permanencia_media;
    }

    public Integer getFk_perfil_turistas() {
        return fk_perfil_turistas;
    }

    public void setFk_perfil_turistas(Integer fk_perfil_turistas) {
        this.fk_perfil_turistas = fk_perfil_turistas;
    }

    public Integer getFk_pais_origem() {
        return fk_pais_origem;
    }

    public void setFk_pais_origem(Integer fk_pais_origem) {
        this.fk_pais_origem = fk_pais_origem;
    }

    public String getFk_uf_destino() {
        return fk_uf_destino;
    }

    public void setFk_uf_destino(String fk_uf_destino) {
        this.fk_uf_destino = fk_uf_destino;
    }

    public Double getPermanencia_media() {
        return permanencia_media;
    }

    public void setPermanencia_media(Double permanencia_media) {
        this.permanencia_media = permanencia_media;
    }

    @Override
    public String toString() {
        return "Destinos{" +
                "fk_perfil_turistas=" + fk_perfil_turistas +
                ", fk_pais_origem=" + fk_pais_origem +
                ", fk_uf_destino='" + fk_uf_destino + '\'' +
                ", permanencia_media=" + permanencia_media +
                '}';
    }
}
