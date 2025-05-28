package tour.wise.model;

import java.time.LocalDate;

public class PerfilEstimadoTuristasOrigem {
    private Integer fkOrigemDados;
    private Integer fkPerfilEstimadoTuristas;
    private Integer fkPaisOrigem;
    private String fkUfEntrada;
    private LocalDate dataColeta;

    public PerfilEstimadoTuristasOrigem() {
    }

    public PerfilEstimadoTuristasOrigem(Integer fkOrigemDados, Integer fkPerfilEstimadoTuristas, Integer fkPaisOrigem, String fkUfEntrada, LocalDate dataColeta) {
        this.fkOrigemDados = fkOrigemDados;
        this.fkPerfilEstimadoTuristas = fkPerfilEstimadoTuristas;
        this.fkPaisOrigem = fkPaisOrigem;
        this.fkUfEntrada = fkUfEntrada;
        this.dataColeta = dataColeta;
    }

    public Integer getFkOrigemDados() {
        return fkOrigemDados;
    }

    public void setFkOrigemDados(Integer fkOrigemDados) {
        this.fkOrigemDados = fkOrigemDados;
    }

    public Integer getFkPerfilEstimadoTuristas() {
        return fkPerfilEstimadoTuristas;
    }

    public void setFkPerfilEstimadoTuristas(Integer fkPerfilEstimadoTuristas) {
        this.fkPerfilEstimadoTuristas = fkPerfilEstimadoTuristas;
    }

    public Integer getFkPaisOrigem() {
        return fkPaisOrigem;
    }

    public void setFkPaisOrigem(Integer fkPaisOrigem) {
        this.fkPaisOrigem = fkPaisOrigem;
    }

    public String getFkUfEntrada() {
        return fkUfEntrada;
    }

    public void setFkUfEntrada(String fkUfEntrada) {
        this.fkUfEntrada = fkUfEntrada;
    }

    public LocalDate getDataColeta() {
        return dataColeta;
    }

    public void setDataColeta(LocalDate dataColeta) {
        this.dataColeta = dataColeta;
    }

    @Override
    public String toString() {
        return "PerfilEstimadoTuristasFonte{" +
                "fkOrigemDados=" + fkOrigemDados +
                ", fkPerfilEstimadoTuristas=" + fkPerfilEstimadoTuristas +
                ", fkPaisOrigem=" + fkPaisOrigem +
                ", fkUfEntrada='" + fkUfEntrada + '\'' +
                ", dataColeta=" + dataColeta +
                '}';
    }
}
