package tour.wise.dto.ficha.sintese.estado;

import tour.wise.dto.ficha.sintese.brasil.*;

import java.util.List;

public class FichaSinteseEstadoDTO extends FichaSinteseBrasilDTO {
    private List<PaisOrigemDTO> paisesOrigem;
    private String destinoPrincipal;
    private List<PermanenciaMediaDTO> permanenciaMediaDTODestinoPrincipal;

    public FichaSinteseEstadoDTO() {

    }

    public FichaSinteseEstadoDTO(List<PaisOrigemDTO> paisesOrigem, String destinoPrincipal, List<PermanenciaMediaDTO> permanenciaMediaDTODestinoPrincipal) {
        this.paisesOrigem = paisesOrigem;
        this.destinoPrincipal = destinoPrincipal;
        this.permanenciaMediaDTODestinoPrincipal = permanenciaMediaDTODestinoPrincipal;
    }

    public FichaSinteseEstadoDTO(Integer ano, List<GeneroDTO> generoDTO, List<FaixaEtariaDTO> faixaEtariaDTO, List<ComposicaoGrupoViagemDTO> composicaoGruposViagem, List<FonteInformacaoDTO> fontesInformacao, List<UtilizacaoAgenciaViagemDTO> utilizacaoAgenciaViagemDTO, List<MotivoViagemDTO> motivos, List<MotivacaoViagemLazerDTO> motivacoesViagemLazer, List<GastoMedioPerCapitaMotivoDTO> gastosMedioPerCapitaMotivo, List<PermanenciaMediaDTO> permanenciaMediaDTO, List<DestinosMaisVisitadosPorMotivoDTO> destinosMaisVisistadosMotivo, List<PaisOrigemDTO> paisesOrigem, String destinoPrincipal, List<PermanenciaMediaDTO> permanenciaMediaDTODestinoPrincipal) {
        super(ano, generoDTO, faixaEtariaDTO, composicaoGruposViagem, fontesInformacao, utilizacaoAgenciaViagemDTO, motivos, motivacoesViagemLazer, gastosMedioPerCapitaMotivo, permanenciaMediaDTO, destinosMaisVisistadosMotivo);
        this.paisesOrigem = paisesOrigem;
        this.destinoPrincipal = destinoPrincipal;
        this.permanenciaMediaDTODestinoPrincipal = permanenciaMediaDTODestinoPrincipal;
    }

    public List<PaisOrigemDTO> getPaisesOrigem() {
        return paisesOrigem;
    }

    public void setPaisesOrigem(List<PaisOrigemDTO> paisesOrigem) {
        this.paisesOrigem = paisesOrigem;
    }

    public String getDestinoPrincipal() {
        return destinoPrincipal;
    }

    public void setDestinoPrincipal(String destinoPrincipal) {
        this.destinoPrincipal = destinoPrincipal;
    }

    public List<PermanenciaMediaDTO> getPermanenciaMediaDTODestinoPrincipal() {
        return permanenciaMediaDTODestinoPrincipal;
    }

    public void setPermanenciaMediaDTODestinoPrincipal(List<PermanenciaMediaDTO> permanenciaMediaDTODestinoPrincipal) {
        this.permanenciaMediaDTODestinoPrincipal = permanenciaMediaDTODestinoPrincipal;
    }

    @Override
    public String toString() {
        return "FichaSinteseEstadoDTO{" +
                "paisesOrigem=" + paisesOrigem +
                ", destinoPrincipal='" + destinoPrincipal +
                ", permanenciaMediaDTODestinoPrincipal=" + permanenciaMediaDTODestinoPrincipal +
                '\'' +
                super.toString() +
                '}';
    }
}

