package tour.wise.dto.perfil;

import java.util.List;

public class PerfilDTO {
    private Double taxaTuristas;
    private Integer quantidadeTuristas;
    private String paisesOrigem;
    private Integer ano;
    private Integer mes;
    private String viaAcesso;
    private String estadoEntrada;
    private String generoDTO;
    private String faixaEtariaDTO;
    private String composicaoGruposViagem;
    private String fonteInformacao;
    private String utilizacaoAgenciaViagemDTO;
    private String motivo;
    private String motivacaoViagemLazer;
    private Double gastosMedioPerCapitaMotivo;
    private List<DestinoDTO> destinosDTO;


    public PerfilDTO() {

    }

    public PerfilDTO(Double taxaTuristas, String paisesOrigem, Integer ano, String generoDTO, String faixaEtariaDTO, String composicaoGruposViagem, String fonteInformacao, String utilizacaoAgenciaViagemDTO, String motivo, String motivacaoViagemLazer, Double gastosMedioPerCapitaMotivo, List<DestinoDTO> destinosDTO) {
        this.taxaTuristas = taxaTuristas;
        this.paisesOrigem = paisesOrigem;
        this.ano = ano;
        this.generoDTO = generoDTO;
        this.faixaEtariaDTO = faixaEtariaDTO;
        this.composicaoGruposViagem = composicaoGruposViagem;
        this.fonteInformacao = fonteInformacao;
        this.utilizacaoAgenciaViagemDTO = utilizacaoAgenciaViagemDTO;
        this.motivo = motivo;
        this.motivacaoViagemLazer = motivacaoViagemLazer;
        this.gastosMedioPerCapitaMotivo = gastosMedioPerCapitaMotivo;
        this.destinosDTO = destinosDTO;
    }

    public PerfilDTO(Double taxaTuristas, Integer quantidadeTuristas, String paisesOrigem, Integer ano, Integer mes, String viaAcesso, String estadoEntrada, String generoDTO, String faixaEtariaDTO, String composicaoGruposViagem, String fonteInformacao, String utilizacaoAgenciaViagemDTO, String motivo, String motivacaoViagemLazer, Double gastosMedioPerCapitaMotivo, List<DestinoDTO> destinosDTO) {
        this.taxaTuristas = taxaTuristas;
        this.quantidadeTuristas = quantidadeTuristas;
        this.paisesOrigem = paisesOrigem;
        this.ano = ano;
        this.mes = mes;
        this.viaAcesso = viaAcesso;
        this.estadoEntrada = estadoEntrada;
        this.generoDTO = generoDTO;
        this.faixaEtariaDTO = faixaEtariaDTO;
        this.composicaoGruposViagem = composicaoGruposViagem;
        this.fonteInformacao = fonteInformacao;
        this.utilizacaoAgenciaViagemDTO = utilizacaoAgenciaViagemDTO;
        this.motivo = motivo;
        this.motivacaoViagemLazer = motivacaoViagemLazer;
        this.gastosMedioPerCapitaMotivo = gastosMedioPerCapitaMotivo;
        this.destinosDTO = destinosDTO;
    }

    public Double getTaxaTuristas() {
        return taxaTuristas;
    }

    public void setTaxaTuristas(Double taxaTuristas) {
        this.taxaTuristas = taxaTuristas;
    }

    public String getPaisesOrigem() {
        return paisesOrigem;
    }

    public void setPaisesOrigem(String paisesOrigem) {
        this.paisesOrigem = paisesOrigem;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getGeneroDTO() {
        return generoDTO;
    }

    public void setGeneroDTO(String generoDTO) {
        this.generoDTO = generoDTO;
    }

    public String getFaixaEtariaDTO() {
        return faixaEtariaDTO;
    }

    public void setFaixaEtariaDTO(String faixaEtariaDTO) {
        this.faixaEtariaDTO = faixaEtariaDTO;
    }

    public String getComposicaoGruposViagem() {
        return composicaoGruposViagem;
    }

    public void setComposicaoGruposViagem(String composicaoGruposViagem) {
        this.composicaoGruposViagem = composicaoGruposViagem;
    }

    public String getFonteInformacao() {
        return fonteInformacao;
    }

    public void setFonteInformacao(String fonteInformacao) {
        this.fonteInformacao = fonteInformacao;
    }

    public String getUtilizacaoAgenciaViagemDTO() {
        return utilizacaoAgenciaViagemDTO;
    }

    public void setUtilizacaoAgenciaViagemDTO(String utilizacaoAgenciaViagemDTO) {
        this.utilizacaoAgenciaViagemDTO = utilizacaoAgenciaViagemDTO;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getMotivacaoViagemLazer() {
        return motivacaoViagemLazer;
    }

    public void setMotivacaoViagemLazer(String motivacaoViagemLazer) {
        this.motivacaoViagemLazer = motivacaoViagemLazer;
    }

    public Double getGastosMedioPerCapitaMotivo() {
        return gastosMedioPerCapitaMotivo;
    }

    public void setGastosMedioPerCapitaMotivo(Double gastosMedioPerCapitaMotivo) {
        this.gastosMedioPerCapitaMotivo = gastosMedioPerCapitaMotivo;
    }

    public List<DestinoDTO> getDestinosDTO() {
        return destinosDTO;
    }

    public void setDestinosDTO(List<DestinoDTO> destinosDTO) {
        this.destinosDTO = destinosDTO;
    }

    public Integer getQuantidadeTuristas() {
        return quantidadeTuristas;
    }

    public void setQuantidadeTuristas(Integer quantidadeTuristas) {
        this.quantidadeTuristas = quantidadeTuristas;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getViaAcesso() {
        return viaAcesso;
    }

    public void setViaAcesso(String viaAcesso) {
        this.viaAcesso = viaAcesso;
    }

    public String getEstadoEntrada() {
        return estadoEntrada;
    }

    public void setEstadoEntrada(String estadoEntrada) {
        this.estadoEntrada = estadoEntrada;
    }

    @Override
    public String toString() {
        return "PerfilDTO{" +
                "taxaTuristas=" + taxaTuristas +
                ", quantidadeTuristas=" + quantidadeTuristas +
                ", paisesOrigem='" + paisesOrigem + '\'' +
                ", ano=" + ano +
                ", mes=" + mes +
                ", viaAcesso='" + viaAcesso + '\'' +
                ", estadoEntrada='" + estadoEntrada + '\'' +
                ", generoDTO='" + generoDTO + '\'' +
                ", faixaEtariaDTO='" + faixaEtariaDTO + '\'' +
                ", composicaoGruposViagem='" + composicaoGruposViagem + '\'' +
                ", fonteInformacao='" + fonteInformacao + '\'' +
                ", utilizacaoAgenciaViagemDTO='" + utilizacaoAgenciaViagemDTO + '\'' +
                ", motivo='" + motivo + '\'' +
                ", motivacaoViagemLazer='" + motivacaoViagemLazer + '\'' +
                ", gastosMedioPerCapitaMotivo=" + gastosMedioPerCapitaMotivo +
                ", destinosDTO=" + destinosDTO +
                '}';
    }
}

