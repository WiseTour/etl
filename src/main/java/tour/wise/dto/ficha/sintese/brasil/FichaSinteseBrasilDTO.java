package tour.wise.dto.ficha.sintese.brasil;

import java.util.List;

public class FichaSinteseBrasilDTO {
    private Integer ano;
    private List<GeneroDTO> generoDTO;
    private List<FaixaEtariaDTO> faixaEtariaDTO;
    private List<ComposicaoGrupoViagemDTO> composicaoGruposViagem;
    private List<FonteInformacaoDTO> fontesInformacao;
    private List<MotivoViagemDTO> motivos;
    private List<MotivacaoViagemLazerDTO> motivacoesViagemLazer;
    private List<GastoMedioPerCapitaMotivoDTO> gastosMedioPerCapitaMotivo;
    private List<PermanenciaMediaDTO> permanenciaMediaDTO;
    private List<DestinosMaisVisitadosPorMotivoDTO> destinosMaisVisistadosMotivo;


    public FichaSinteseBrasilDTO() {
    }

    public FichaSinteseBrasilDTO(Integer ano, List<GeneroDTO> generoDTO, List<FaixaEtariaDTO> faixaEtariaDTO, List<ComposicaoGrupoViagemDTO> composicaoGruposViagem, List<FonteInformacaoDTO> fontesInformacao, List<MotivoViagemDTO> motivos, List<MotivacaoViagemLazerDTO> motivacoesViagemLazer, List<GastoMedioPerCapitaMotivoDTO> gastosMedioPerCapitaMotivo, List<PermanenciaMediaDTO> permanenciaMediaDTO, List<DestinosMaisVisitadosPorMotivoDTO> destinosMaisVisistadosMotivo) {
        this.ano = ano;
        this.generoDTO = generoDTO;
        this.faixaEtariaDTO = faixaEtariaDTO;
        this.composicaoGruposViagem = composicaoGruposViagem;
        this.fontesInformacao = fontesInformacao;
        this.motivos = motivos;
        this.motivacoesViagemLazer = motivacoesViagemLazer;
        this.gastosMedioPerCapitaMotivo = gastosMedioPerCapitaMotivo;
        this.permanenciaMediaDTO = permanenciaMediaDTO;
        this.destinosMaisVisistadosMotivo = destinosMaisVisistadosMotivo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public List<GeneroDTO> getGeneroDTO() {
        return generoDTO;
    }

    public void setGeneroDTO(List<GeneroDTO> generoDTO) {
        this.generoDTO = generoDTO;
    }

    public List<FaixaEtariaDTO> getFaixaEtariaDTO() {
        return faixaEtariaDTO;
    }

    public void setFaixaEtariaDTO(List<FaixaEtariaDTO> faixaEtariaDTO) {
        this.faixaEtariaDTO = faixaEtariaDTO;
    }

    public List<ComposicaoGrupoViagemDTO> getComposicaoGruposViagem() {
        return composicaoGruposViagem;
    }

    public void setComposicaoGruposViagem(List<ComposicaoGrupoViagemDTO> composicaoGruposViagem) {
        this.composicaoGruposViagem = composicaoGruposViagem;
    }

    public List<FonteInformacaoDTO> getFontesInformacao() {
        return fontesInformacao;
    }

    public void setFontesInformacao(List<FonteInformacaoDTO> fontesInformacao) {
        this.fontesInformacao = fontesInformacao;
    }


    public List<MotivoViagemDTO> getMotivos() {
        return motivos;
    }

    public void setMotivos(List<MotivoViagemDTO> motivos) {
        this.motivos = motivos;
    }

    public List<MotivacaoViagemLazerDTO> getMotivacoesViagemLazer() {
        return motivacoesViagemLazer;
    }

    public void setMotivacoesViagemLazer(List<MotivacaoViagemLazerDTO> motivacoesViagemLazer) {
        this.motivacoesViagemLazer = motivacoesViagemLazer;
    }

    public List<GastoMedioPerCapitaMotivoDTO> getGastosMedioPerCapitaMotivo() {
        return gastosMedioPerCapitaMotivo;
    }

    public void setGastosMedioPerCapitaMotivo(List<GastoMedioPerCapitaMotivoDTO> gastosMedioPerCapitaMotivo) {
        this.gastosMedioPerCapitaMotivo = gastosMedioPerCapitaMotivo;
    }

    public List<PermanenciaMediaDTO> getPermanenciaMediaDTO() {
        return permanenciaMediaDTO;
    }

    public void setPermanenciaMediaDTO(List<PermanenciaMediaDTO> permanenciaMediaDTO) {
        this.permanenciaMediaDTO = permanenciaMediaDTO;
    }

    public List<DestinosMaisVisitadosPorMotivoDTO> getDestinosMaisVisistadosMotivo() {
        return destinosMaisVisistadosMotivo;
    }

    public void setDestinosMaisVisistadosMotivo(List<DestinosMaisVisitadosPorMotivoDTO> destinosMaisVisistadosMotivo) {
        this.destinosMaisVisistadosMotivo = destinosMaisVisistadosMotivo;
    }

    @Override
    public String toString() {
        return "FichaSinteseBrasilDTO{" +
                "ano=" + ano +
                ", generoDTO=" + generoDTO +
                ", faixaEtariaDTO=" + faixaEtariaDTO +
                ", composicaoGruposViagem=" + composicaoGruposViagem +
                ", fontesInformacao=" + fontesInformacao +
                ", motivos=" + motivos +
                ", motivacoesViagemLazer=" + motivacoesViagemLazer +
                ", gastosMedioPerCapitaMotivo=" + gastosMedioPerCapitaMotivo +
                ", permanenciaMediaDTO=" + permanenciaMediaDTO +
                ", destinosMaisVisistadosMotivo=" + destinosMaisVisistadosMotivo +
                '}';
    }
}
