package tour.wise.model;

import java.time.LocalDateTime;

public class Log {

    private Integer idLog;
    private Integer fkLogCategoria;  // fk_log_categoria
    private Integer fkEtapa;             // fk_etapa

    // Campos da FK composta perfil_estimado_turista_origem
    private Integer fkOrigemDados;
    private Integer fkPerfilEstimadoTuristas;
    private Integer fkPaisOrigem;
    private String fkUfEntrada;

    private String mensagem;
    private String erro;
    private LocalDateTime dataHora;

    public Log() {
    }

    public Log(Integer idLog, Integer fkLogCategoria, Integer fkEtapa,
               Integer fkOrigemDados, Integer fkPerfilEstimadoTuristas,
               Integer fkPaisOrigem, String fkUfEntrada, String erro,
               String mensagem) {
        this.idLog = idLog;
        this.fkLogCategoria = fkLogCategoria;
        this.fkEtapa = fkEtapa;
        this.fkOrigemDados = fkOrigemDados;
        this.fkPerfilEstimadoTuristas = fkPerfilEstimadoTuristas;
        this.fkPaisOrigem = fkPaisOrigem;
        this.fkUfEntrada = fkUfEntrada;
        this.mensagem = mensagem;
        this.erro = erro;
        this.dataHora =  LocalDateTime.now();
    }

    public Log(Integer fkLogCategoria, Integer fkEtapa,
               Integer fkOrigemDados, Integer fkPerfilEstimadoTuristas,
               Integer fkPaisOrigem, String fkUfEntrada, String erro,
               String mensagem) {
        this.fkLogCategoria = fkLogCategoria;
        this.fkEtapa = fkEtapa;
        this.fkOrigemDados = fkOrigemDados;
        this.fkPerfilEstimadoTuristas = fkPerfilEstimadoTuristas;
        this.fkPaisOrigem = fkPaisOrigem;
        this.fkUfEntrada = fkUfEntrada;
        this.mensagem = mensagem;
        this.erro = erro;
        this.dataHora = LocalDateTime.now();
    }

    public Log(Integer fkLogCategoria,
               Integer fkEtapa,
               String erro,
               String mensagem) {
        this.fkLogCategoria = fkLogCategoria;
        this.fkEtapa = fkEtapa;
        this.fkOrigemDados = null;
        this.fkPerfilEstimadoTuristas = null;
        this.fkPaisOrigem = null;
        this.fkUfEntrada = null;
        this.mensagem = mensagem;
        this.erro = erro;
        this.dataHora = LocalDateTime.now();
    }

    public Log(Integer fkLogCategoria,
               Integer fkEtapa,
               String mensagem) {
        this.fkLogCategoria = fkLogCategoria;
        this.fkEtapa = fkEtapa;
        this.fkOrigemDados = null;
        this.fkPerfilEstimadoTuristas = null;
        this.fkPaisOrigem = null;
        this.fkUfEntrada = null;
        this.mensagem = mensagem;
        this.erro = null;
        this.dataHora = LocalDateTime.now();
    }


    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public Integer getFkLogCategoria() {
        return fkLogCategoria;
    }

    public void setFkLogCategoria(Integer fkLogCategoria) {
        this.fkLogCategoria = fkLogCategoria;
    }

    public Integer getFkEtapa() {
        return fkEtapa;
    }

    public void setFkEtapa(Integer fkEtapa) {
        this.fkEtapa = fkEtapa;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Log{" +
                "idLog=" + idLog +
                ", fkLogCategoria=" + fkLogCategoria +
                ", fkEtapa=" + fkEtapa +
                ", fkOrigemDados=" + fkOrigemDados +
                ", fkPerfilEstimadoTuristas=" + fkPerfilEstimadoTuristas +
                ", fkPaisOrigem=" + fkPaisOrigem +
                ", fkUfEntrada='" + fkUfEntrada + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", erro='" + erro + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}
