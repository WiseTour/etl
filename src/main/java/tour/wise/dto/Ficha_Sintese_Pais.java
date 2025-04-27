package tour.wise.dto;

import tour.wise.dto.ficha_sintese_brasil.*;

import java.util.List;

public class Ficha_Sintese_Pais extends Ficha_Sintese_Brasil {
    private String pais;

    public Ficha_Sintese_Pais(String pais) {
        this.pais = pais;
    }

    public Ficha_Sintese_Pais(Integer ano, List<Genero> genero, List<Faixa_Etaria> faixa_etaria, List<Composicao_Grupo_Viagem> composicao_grupos_viagem, List<Fonte_Informacao> fontes_informacao, List<Utilizacao_Agencia_Viagem> servico_agencia_viagem, List<Motivo_Viagem> motivos, List<Motivacao_Viagem_Lazer> motivacoes_viagem_lazer, List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo, List<Permanencia_Media> permanencia_media, List<Destinos_Mais_Visitados_Por_Motivo> destinos_mais_visistados_por_motivo, String pais) {
        super(ano, genero, faixa_etaria, composicao_grupos_viagem, fontes_informacao, servico_agencia_viagem, motivos, motivacoes_viagem_lazer, gastos_medio_per_capita_brasil_motivo, permanencia_media, destinos_mais_visistados_por_motivo);
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
        return "Ficha_Sintese_Pais{" +
                "pais='" + pais + '\'' +
                super.toString() +
                '}' ;
    }
}
