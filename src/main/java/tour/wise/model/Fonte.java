package tour.wise.model;

import java.time.LocalDate;

public class Fonte {
    private Integer id_edicao;
    private String titulo_fonte;
    private String edicao;
    private String orgao_emissor;
    private String url;
    private LocalDate data_coleta;
    private String observacoes;

    public Fonte() {
    }

    public Fonte(Integer id_edicao, String titulo_fonte, String edicao, String orgao_emissor, String url, LocalDate data_coleta, String observacoes) {
        this.id_edicao = id_edicao;
        this.titulo_fonte = titulo_fonte;
        this.edicao = edicao;
        this.orgao_emissor = orgao_emissor;
        this.url = url;
        this.data_coleta = data_coleta;
        this.observacoes = observacoes;
    }

    public Integer getId_edicao() {
        return id_edicao;
    }

    public void setId_edicao(Integer id_edicao) {
        this.id_edicao = id_edicao;
    }

    public String getTitulo_fonte() {
        return titulo_fonte;
    }

    public void setTitulo_fonte(String titulo_fonte) {
        this.titulo_fonte = titulo_fonte;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getOrgao_emissor() {
        return orgao_emissor;
    }

    public void setOrgao_emissor(String orgao_emissor) {
        this.orgao_emissor = orgao_emissor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getData_coleta() {
        return data_coleta;
    }

    public void setData_coleta(LocalDate data_coleta) {
        this.data_coleta = data_coleta;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return "Fonte{" +
                "id_edicao=" + id_edicao +
                ", titulo_fonte='" + titulo_fonte + '\'' +
                ", edicao='" + edicao + '\'' +
                ", orgao_emissor='" + orgao_emissor + '\'' +
                ", url='" + url + '\'' +
                ", data_coleta=" + data_coleta +
                ", observacoes='" + observacoes + '\'' +
                '}';
    }
}
