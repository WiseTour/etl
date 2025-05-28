package tour.wise.dto.ficha.sintese;

import tour.wise.dto.ficha.sintese.brasil.*;

import java.util.List;

public class FichaSintesePaisDTO extends FichaSinteseBrasilDTO {
    private String pais;

    public FichaSintesePaisDTO(String pais) {
        this.pais = pais;
    }

    public FichaSintesePaisDTO(Integer ano, List<GeneroDTO> generoDTO, List<FaixaEtariaDTO> faixaEtariaDTO, List<ComposicaoGrupoViagemDTO> composicaoGruposViagem, List<FonteInformacaoDTO> fontesInformacao, List<MotivoViagemDTO> motivos, List<MotivacaoViagemLazerDTO> motivacoesViagemLazer, List<GastoMedioPerCapitaMotivoDTO> gastosMedioPerCapitaMotivo, List<PermanenciaMediaDTO> permanenciaMediaDTO, List<DestinosMaisVisitadosPorMotivoDTO> destinosMaisVisistadosMotivo, String pais) {
        super(ano, generoDTO, faixaEtariaDTO, composicaoGruposViagem, fontesInformacao, motivos, motivacoesViagemLazer, gastosMedioPerCapitaMotivo, permanenciaMediaDTO, destinosMaisVisistadosMotivo);
        this.pais = pais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "FichaSintesePaisDTO{" +
                "pais='" + pais + '\'' +
                super.toString() +
                '}';
    }
}
