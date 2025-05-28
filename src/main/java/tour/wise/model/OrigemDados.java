package tour.wise.model;

public class OrigemDados {
    private Integer idOrigemDados;
    private String titulo;
    private String edicao;
    private String orgaoEmissor;

    public OrigemDados(Integer idOrigemDados, String titulo, String edicao, String orgaoEmissor, String urlOrigem) {
        this.idOrigemDados = idOrigemDados;
        this.titulo = titulo;
        this.edicao = edicao;
        this.orgaoEmissor = orgaoEmissor;
    }

    public OrigemDados(String titulo, String edicao, String orgaoEmissor) {
        this.titulo = titulo;
        this.edicao = edicao;
        this.orgaoEmissor = orgaoEmissor;
    }

    public OrigemDados() {
    }


    public Integer getIdOrigemDados() {
        return idOrigemDados;
    }

    public void setIdOrigemDados(Integer idOrigemDados) {
        this.idOrigemDados = idOrigemDados;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    @Override
    public String toString() {
        return "OrigemDados{" +
                "idOrigemDados=" + idOrigemDados +
                ", titulo='" + titulo + '\'' +
                ", edicao='" + edicao + '\'' +
                ", orgaoEmissor='" + orgaoEmissor + '\'' +
                '}';
    }
}
