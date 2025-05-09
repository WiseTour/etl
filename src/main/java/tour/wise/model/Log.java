package tour.wise.model;

import java.time.LocalDateTime;

public class Log {
    private int idLog;
    private int fkFonte;
    private int fkLogCategoria;
    private int fkEtapa;
    private String mensagem;
    private LocalDateTime dataHora;
    private Integer quantidadeLida;
    private Integer quantidadeInserida;
    private String tabelaDestino;

    // Construtor completo
    public Log(int idLog, int fkFonte, int fkLogCategoria, int fkEtapa, String mensagem,
               LocalDateTime dataHora, Integer quantidadeLida, Integer quantidadeInserida, String tabelaDestino) {
        this.idLog = idLog;
        this.fkFonte = fkFonte;
        this.fkLogCategoria = fkLogCategoria;
        this.fkEtapa = fkEtapa;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
        this.quantidadeLida = quantidadeLida;
        this.quantidadeInserida = quantidadeInserida;
        this.tabelaDestino = tabelaDestino;
    }

    // Construtor sem ID (para inserts automáticos)
    public Log(int fkFonte, int fkLogCategoria, int fkEtapa, String mensagem,
               LocalDateTime dataHora, Integer quantidadeLida, Integer quantidadeInserida, String tabelaDestino) {
        this.fkFonte = fkFonte;
        this.fkLogCategoria = fkLogCategoria;
        this.fkEtapa = fkEtapa;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
        this.quantidadeLida = quantidadeLida;
        this.quantidadeInserida = quantidadeInserida;
        this.tabelaDestino = tabelaDestino;
    }

    // Getters e Setters
    public int getIdLog() {
        return idLog;
    }

    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }

    public int getFkFonte() {
        return fkFonte;
    }

    public void setFkFonte(int fkFonte) {
        this.fkFonte = fkFonte;
    }

    public int getFkLogCategoria() {
        return fkLogCategoria;
    }

    public void setFkLogCategoria(int fkLogCategoria) {
        this.fkLogCategoria = fkLogCategoria;
    }

    public int getFkEtapa() {
        return fkEtapa;
    }

    public void setFkEtapa(int fkEtapa) {
        this.fkEtapa = fkEtapa;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getQuantidadeLida() {
        return quantidadeLida;
    }

    public void setQuantidadeLida(Integer quantidadeLida) {
        this.quantidadeLida = quantidadeLida;
    }

    public Integer getQuantidadeInserida() {
        return quantidadeInserida;
    }

    public void setQuantidadeInserida(Integer quantidadeInserida) {
        this.quantidadeInserida = quantidadeInserida;
    }

    public String getTabelaDestino() {
        return tabelaDestino;
    }

    public void setTabelaDestino(String tabelaDestino) {
        this.tabelaDestino = tabelaDestino;
    }
}

