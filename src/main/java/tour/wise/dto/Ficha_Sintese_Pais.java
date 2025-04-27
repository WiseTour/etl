package tour.wise.dto;

import tour.wise.dto.ficha_sintese_brasil.*;

import java.util.List;

public class Ficha_Sintese_Pais extends Ficha_Sintese_Brasil {
    private String pais;

    public Ficha_Sintese_Pais(String pais) {
        this.pais = pais;
    }

    public Ficha_Sintese_Pais(String pais, Integer ano, List<Motivo_Viagem> motivos, List<Motivacao_Viagem_Lazer> motivacao_viagem_lazers, List<Composicao_Grupo_Viagem> composicao_grupos_viagem, List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo, List<Destinos_Mais_Visistados_Por_Motivo> destinos_mais_visistados_por_motivo, List<Fonte_Informacao> fontes_informacao, List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem) {
        super(ano, motivos, motivacao_viagem_lazers, composicao_grupos_viagem, gastos_medio_per_capita_brasil_motivo, destinos_mais_visistados_por_motivo, fontes_informacao, utilizacao_agencia_viagem);
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
