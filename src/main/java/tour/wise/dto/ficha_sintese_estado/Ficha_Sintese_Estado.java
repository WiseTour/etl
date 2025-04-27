package tour.wise.dto.ficha_sintese_estado;

import tour.wise.dto.ficha_sintese_brasil.*;

import java.util.List;

public class Ficha_Sintese_Estado extends Ficha_Sintese_Brasil {
    private List<Pais_Origem> paises_origem;
    private String destino_princiapal;
    private List<Permanencia_Media> permanencia_media_uf;

    public Ficha_Sintese_Estado() {

    }

    public Ficha_Sintese_Estado(List<Pais_Origem> paises_origem, String destino_princiapal, List<Permanencia_Media> permanencia_media_uf) {
        this.paises_origem = paises_origem;
        this.destino_princiapal = destino_princiapal;
        this.permanencia_media_uf = permanencia_media_uf;
    }

    public Ficha_Sintese_Estado(Integer ano, List<Genero> genero, List<Faixa_Etaria> faixa_etaria, List<Composicao_Grupo_Viagem> composicao_grupos_viagem, List<Fonte_Informacao> fontes_informacao, List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem, List<Motivo_Viagem> motivos, List<Motivacao_Viagem_Lazer> motivacoes_viagem_lazer, List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo, List<Permanencia_Media> permanencia_media_brasil, List<Destinos_Mais_Visitados_Por_Motivo> destinos_mais_visistados_por_motivo, List<Pais_Origem> paises_origem, String destino_princiapal, List<Permanencia_Media> permanencia_media_uf) {
        super(ano, genero, faixa_etaria, composicao_grupos_viagem, fontes_informacao, utilizacao_agencia_viagem, motivos, motivacoes_viagem_lazer, gastos_medio_per_capita_brasil_motivo, permanencia_media_brasil, destinos_mais_visistados_por_motivo);
        this.paises_origem = paises_origem;
        this.destino_princiapal = destino_princiapal;
        this.permanencia_media_uf = permanencia_media_uf;
    }

    public List<Pais_Origem> getPaises_origem() {
        return paises_origem;
    }

    public void setPaises_origem(List<Pais_Origem> paises_origem) {
        this.paises_origem = paises_origem;
    }

    public String getDestino_princiapal() {
        return destino_princiapal;
    }

    public void setDestino_princiapal(String destino_princiapal) {
        this.destino_princiapal = destino_princiapal;
    }

    public List<Permanencia_Media> getPermanencia_media_uf() {
        return permanencia_media_uf;
    }

    public void setPermanencia_media_uf(List<Permanencia_Media> permanencia_media_uf) {
        this.permanencia_media_uf = permanencia_media_uf;
    }

    @Override
    public String toString() {
        return "Ficha_Sintese_Estado{" +
                "paises_origem=" + paises_origem +
                ", destino_princiapal='" + destino_princiapal + '\'' +
                ", permanencia_media_uf=" + permanencia_media_uf +
                '}';
    }
}

