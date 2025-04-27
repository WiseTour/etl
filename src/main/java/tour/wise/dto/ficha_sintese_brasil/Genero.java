package tour.wise.dto.ficha_sintese_brasil;

public class Genero {
    private String genero;
    private Double porcentagem;

    public Genero() {

    }

    public Genero(String genero, Double porcentagem) {
        this.genero = genero;
        this.porcentagem = porcentagem;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Double getPorcentagem() {
        return porcentagem;
    }

    public void setPorcentagem(Double porcentagem) {
        this.porcentagem = porcentagem;
    }

    @Override
    public String toString() {
        return "Genero{" +
                "genero='" + genero + '\'' +
                ", porcentagem=" + porcentagem +
                '}';
    }
}
