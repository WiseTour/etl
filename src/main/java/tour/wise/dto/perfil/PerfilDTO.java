package tour.wise.dto.perfil;

import java.util.List;

public class PerfilDTO {
    private Double taxaTuristas;
    private String paisesOrigem;
    private Integer ano;
    private String generoDTO;
    private String faixaEtariaDTO;
    private String composicaoGruposViagem;
    private String fontesInformacao;
    private String utilizacaoAgenciaViagemDTO;
    private String motivo;
    private String motivacaoViagemLazer;
    private Double gastosMedioPerCapitaMotivo;
    private List<DestinoDTO> destinosDTO;


    public PerfilDTO() {

    }

    public PerfilDTO(Double taxaTuristas, String paisesOrigem, Integer ano, String generoDTO, String faixaEtariaDTO, String composicaoGruposViagem, String fontesInformacao, String utilizacaoAgenciaViagemDTO, String motivo, String motivacaoViagemLazer, Double gastosMedioPerCapitaMotivo, List<DestinoDTO> destinosDTO) {
        this.taxaTuristas = taxaTuristas;
        this.paisesOrigem = paisesOrigem;
        this.ano = ano;
        this.generoDTO = generoDTO;
        this.faixaEtariaDTO = faixaEtariaDTO;
        this.composicaoGruposViagem = composicaoGruposViagem;
        this.fontesInformacao = fontesInformacao;
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

    public String getFontesInformacao() {
        return fontesInformacao;
    }

    public void setFontesInformacao(String fontesInformacao) {
        this.fontesInformacao = fontesInformacao;
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

    @Override
    public String toString() {
        return "PerfilEstadoDTO{" +
                "taxaTuristas=" + taxaTuristas +
                ", paisesOrigem='" + paisesOrigem + '\'' +
                ", ano=" + ano +
                ", generoDTO='" + generoDTO + '\'' +
                ", faixaEtariaDTO='" + faixaEtariaDTO + '\'' +
                ", composicaoGruposViagem='" + composicaoGruposViagem + '\'' +
                ", fontesInformacao='" + fontesInformacao + '\'' +
                ", utilizacaoAgenciaViagemDTO='" + utilizacaoAgenciaViagemDTO + '\'' +
                ", motivo='" + motivo + '\'' +
                ", motivacoesViagemLazer='" + motivacaoViagemLazer + '\'' +
                ", gastosMedioPerCapitaMotivo=" + gastosMedioPerCapitaMotivo +
                ", destinosDTO=" + destinosDTO +
                '}';
    }
}

