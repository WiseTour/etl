package tour.wise.dto.perfil;

import tour.wise.dto.ficha.sintese.brasil.DestinoMaisVisitadoDTO;

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
    private String motivo;
    private String motivacaoViagemLazer;
    private Double gastosMedioPerCapitaMotivo;
    private DestinoMaisVisitadoDTO destinoMaisVisitado;


    public PerfilDTO() {

    }
    public PerfilDTO(Double taxaTuristas, Integer ano, String generoDTO, String faixaEtariaDTO, String composicaoGruposViagem, String fonteInformacao, String motivo, String motivacaoViagemLazer, Double gastosMedioPerCapitaMotivo, DestinoMaisVisitadoDTO destinoMaisVisitado) {
        this.taxaTuristas = taxaTuristas;
        this.ano = ano;
        this.generoDTO = generoDTO;
        this.faixaEtariaDTO = faixaEtariaDTO;
        this.composicaoGruposViagem = composicaoGruposViagem;
        this.fonteInformacao = fonteInformacao;
        this.motivo = motivo;
        this.motivacaoViagemLazer = motivacaoViagemLazer;
        this.gastosMedioPerCapitaMotivo = gastosMedioPerCapitaMotivo;
        this.destinoMaisVisitado = destinoMaisVisitado;
    }


    public PerfilDTO(Double taxaTuristas, Integer quantidadeTuristas, String paisesOrigem, Integer ano, Integer mes, String viaAcesso, String estadoEntrada, String generoDTO, String faixaEtariaDTO, String composicaoGruposViagem, String fonteInformacao, String motivo, String motivacaoViagemLazer, Double gastosMedioPerCapitaMotivo, DestinoMaisVisitadoDTO destinoMaisVisitado) {
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
        this.motivo = motivo;
        this.motivacaoViagemLazer = motivacaoViagemLazer;
        this.gastosMedioPerCapitaMotivo = gastosMedioPerCapitaMotivo;
        this.destinoMaisVisitado = destinoMaisVisitado;
    }


    public Double getTaxaTuristas() {
        return taxaTuristas;
    }

    public void setTaxaTuristas(Double taxaTuristas) {
        this.taxaTuristas = taxaTuristas;
    }

    public Integer getQuantidadeTuristas() {
        return quantidadeTuristas;
    }

    public void setQuantidadeTuristas(Integer quantidadeTuristas) {
        this.quantidadeTuristas = quantidadeTuristas;
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

    public DestinoMaisVisitadoDTO getDestinoMaisVisitadoDTO() {
        return destinoMaisVisitado;
    }

    public void setDestinoMaisVisitadoDTO(DestinoMaisVisitadoDTO destinoMaisVisitado) {
        this.destinoMaisVisitado = destinoMaisVisitado;
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
                ", motivo='" + motivo + '\'' +
                ", motivacaoViagemLazer='" + motivacaoViagemLazer + '\'' +
                ", gastosMedioPerCapitaMotivo=" + gastosMedioPerCapitaMotivo +
                ", listaDestinosDTO=" + destinoMaisVisitado +
                '}';
    }
}

