package tour.wise.model;

public class Destino {

    private int fkPerfilEstimadoTuristas;
    private int fkPaisOrigem;
    private String fkUfDestino;
    private String fkUfEntrada;
    private double permanenciaMedia;

    // Construtor padrão
    public Destino() {}

    // Construtor completo
    public Destino(int fkPerfilEstimadoTuristas, int fkPaisOrigem, String fkUfDestino, String fkUfEntrada, double permanenciaMedia) {
        this.fkPerfilEstimadoTuristas = fkPerfilEstimadoTuristas;
        this.fkPaisOrigem = fkPaisOrigem;
        this.fkUfDestino = fkUfDestino;
        this.fkUfEntrada = fkUfEntrada;
        this.permanenciaMedia = permanenciaMedia;
    }

    public Destino(int fkPaisOrigem, String fkUfDestino, String fkUfEntrada, double permanenciaMedia) {
        this.fkPaisOrigem = fkPaisOrigem;
        this.fkUfDestino = fkUfDestino;
        this.fkUfEntrada = fkUfEntrada;
        this.permanenciaMedia = permanenciaMedia;
    }

    // Getters e Setters
    public int getFkPerfilEstimadoTuristas() {
        return fkPerfilEstimadoTuristas;
    }

    public void setFkPerfilEstimadoTuristas(int fkPerfilEstimadoTuristas) {
        this.fkPerfilEstimadoTuristas = fkPerfilEstimadoTuristas;
    }

    public int getFkPaisOrigem() {
        return fkPaisOrigem;
    }

    public void setFkPaisOrigem(int fkPaisOrigem) {
        this.fkPaisOrigem = fkPaisOrigem;
    }

    public String getFkUfDestino() {
        return fkUfDestino;
    }

    public void setFkUfDestino(String fkUfDestino) {
        this.fkUfDestino = fkUfDestino;
    }

    public String getFkUfEntrada() {
        return fkUfEntrada;
    }

    public void setFkUfEntrada(String fkUfEntrada) {
        this.fkUfEntrada = fkUfEntrada;
    }

    public double getPermanenciaMedia() {
        return permanenciaMedia;
    }

    public void setPermanenciaMedia(double permanenciaMedia) {
        this.permanenciaMedia = permanenciaMedia;
    }

    @Override
    public String toString() {
        return "Destino{" +
                "fkPerfilEstimadoTuristas=" + fkPerfilEstimadoTuristas +
                ", fkPaisOrigem=" + fkPaisOrigem +
                ", fkUfDestino='" + fkUfDestino + '\'' +
                ", fkUfEntrada='" + fkUfEntrada + '\'' +
                ", permanenciaMedia=" + permanenciaMedia +
                '}';
    }
}
