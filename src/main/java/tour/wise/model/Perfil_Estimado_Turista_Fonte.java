package tour.wise.model;

public class Perfil_Estimado_Turista_Fonte {
    Integer fk_fonte, fk_perfil_estimado_turistas, fk_pais_origem;
    String fk_uf_destino;

    public Perfil_Estimado_Turista_Fonte() {
    }

    public Perfil_Estimado_Turista_Fonte(Integer fk_fonte, Integer fk_perfil_estimado_turistas, Integer fk_pais_origem, String fk_uf_destino) {
        this.fk_fonte = fk_fonte;
        this.fk_perfil_estimado_turistas = fk_perfil_estimado_turistas;
        this.fk_pais_origem = fk_pais_origem;
        this.fk_uf_destino = fk_uf_destino;
    }

    public Integer getFk_fonte() {
        return fk_fonte;
    }

    public void setFk_fonte(Integer fk_fonte) {
        this.fk_fonte = fk_fonte;
    }

    public Integer getFk_perfil_estimado_turistas() {
        return fk_perfil_estimado_turistas;
    }

    public void setFk_perfil_estimado_turistas(Integer fk_perfil_estimado_turistas) {
        this.fk_perfil_estimado_turistas = fk_perfil_estimado_turistas;
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

    @Override
    public String toString() {
        return "Perfil_Estimado_Turista_Fonte{" +
                "fk_fonte=" + fk_fonte +
                ", fk_perfil_estimado_turistas=" + fk_perfil_estimado_turistas +
                ", fk_pais_origem=" + fk_pais_origem +
                ", fk_uf_destino='" + fk_uf_destino + '\'' +
                '}';
    }
}
