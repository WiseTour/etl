package tour.wise.model;

public class PerfilEstimadoTuristas {

    private Integer idPerfilEstimadoTuristas;
    private Integer fkPaisOrigem;
    private String fkUfEntrada; // CHAR(2) -> String de tamanho 2
    private Integer ano;
    private Integer mes;
    private Integer quantidadeTuristas;
    private String genero;
    private String faixaEtaria;
    private String viaAcesso;
    private String composicaoGrupoFamiliar;
    private String fonteInformacaoViagem;
    private Integer servicoAgenciaTurismo; // pode ser null, então Integer
    private String motivoViagem;
    private String motivacaoViagemLazer;
    private Double gastoMediaPercapitaEmDolar;

    // Construtores
    public PerfilEstimadoTuristas() {}

    public PerfilEstimadoTuristas(Integer idPerfilEstimadoTuristas, Integer fkPaisOrigem, String fkUfEntrada, Integer ano, Integer mes,
                                  Integer quantidadeTuristas, String genero, String faixaEtaria, String viaAcesso,
                                  String composicaoGrupoFamiliar, String fonteInformacaoViagem,
                                  Integer servicoAgenciaTurismo, String motivoViagem, String motivacaoViagemLazer,
                                  Double gastoMediaPercapitaEmDolar) {
        this.idPerfilEstimadoTuristas = idPerfilEstimadoTuristas;
        this.fkPaisOrigem = fkPaisOrigem;
        this.fkUfEntrada = fkUfEntrada;
        this.ano = ano;
        this.mes = mes;
        this.quantidadeTuristas = quantidadeTuristas;
        this.genero = genero;
        this.faixaEtaria = faixaEtaria;
        this.viaAcesso = viaAcesso;
        this.composicaoGrupoFamiliar = composicaoGrupoFamiliar;
        this.fonteInformacaoViagem = fonteInformacaoViagem;
        this.servicoAgenciaTurismo = servicoAgenciaTurismo;
        this.motivoViagem = motivoViagem;
        this.motivacaoViagemLazer = motivacaoViagemLazer;
        this.gastoMediaPercapitaEmDolar = gastoMediaPercapitaEmDolar;
    }

    // Getters e Setters
    public Integer getIdPerfilEstimadoTuristas() {
        return idPerfilEstimadoTuristas;
    }

    public void setIdPerfilEstimadoTuristas(Integer idPerfilEstimadoTuristas) {
        this.idPerfilEstimadoTuristas = idPerfilEstimadoTuristas;
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

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getQuantidadeTuristas() {
        return quantidadeTuristas;
    }

    public void setQuantidadeTuristas(Integer quantidadeTuristas) {
        this.quantidadeTuristas = quantidadeTuristas;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFaixaEtaria() {
        return faixaEtaria;
    }

    public void setFaixaEtaria(String faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }

    public String getViaAcesso() {
        return viaAcesso;
    }

    public void setViaAcesso(String viaAcesso) {
        this.viaAcesso = viaAcesso;
    }

    public String getComposicaoGrupoFamiliar() {
        return composicaoGrupoFamiliar;
    }

    public void setComposicaoGrupoFamiliar(String composicaoGrupoFamiliar) {
        this.composicaoGrupoFamiliar = composicaoGrupoFamiliar;
    }

    public String getFonteInformacaoViagem() {
        return fonteInformacaoViagem;
    }

    public void setFonteInformacaoViagem(String fonteInformacaoViagem) {
        this.fonteInformacaoViagem = fonteInformacaoViagem;
    }

    public Integer getServicoAgenciaTurismo() {
        return servicoAgenciaTurismo;
    }

    public void setServicoAgenciaTurismo(Integer servicoAgenciaTurismo) {
        this.servicoAgenciaTurismo = servicoAgenciaTurismo;
    }

    public String getMotivoViagem() {
        return motivoViagem;
    }

    public void setMotivoViagem(String motivoViagem) {
        this.motivoViagem = motivoViagem;
    }

    public String getMotivacaoViagemLazer() {
        return motivacaoViagemLazer;
    }

    public void setMotivacaoViagemLazer(String motivacaoViagemLazer) {
        this.motivacaoViagemLazer = motivacaoViagemLazer;
    }

    public Double getGastoMediaPercapitaEmDolar() {
        return gastoMediaPercapitaEmDolar;
    }

    public void setGastoMediaPercapitaEmDolar(Double gastoMediaPercapitaEmDolar) {
        this.gastoMediaPercapitaEmDolar = gastoMediaPercapitaEmDolar;
    }

    @Override
    public String toString() {
        return "PerfilEstimadoTuristas{" +
                "idPerfilEstimadoTuristas=" + idPerfilEstimadoTuristas +
                ", fkPaisOrigem=" + fkPaisOrigem +
                ", fkUfEntrada='" + fkUfEntrada + '\'' +
                ", ano=" + ano +
                ", mes=" + mes +
                ", quantidadeTuristas=" + quantidadeTuristas +
                ", genero='" + genero + '\'' +
                ", faixaEtaria='" + faixaEtaria + '\'' +
                ", viaAcesso='" + viaAcesso + '\'' +
                ", composicaoGrupoFamiliar='" + composicaoGrupoFamiliar + '\'' +
                ", fonteInformacaoViagem='" + fonteInformacaoViagem + '\'' +
                ", servicoAgenciaTurismo=" + servicoAgenciaTurismo +
                ", motivoViagem='" + motivoViagem + '\'' +
                ", motivacaoViagemLazer='" + motivacaoViagemLazer + '\'' +
                ", gastoMediaPercapitaEmDolar=" + gastoMediaPercapitaEmDolar +
                '}';
    }
}

