package tour.wise.dto.ficha_sintese_estado;

import tour.wise.dto.ficha_sintese_brasil.*;

import java.util.List;

public class Ficha_Sintese_Estado extends Ficha_Sintese_Brasil {
    private String uf_destino;
    private List<Pais_Origem> paises_origem;

    public Ficha_Sintese_Estado(String uf_destino, List<Pais_Origem> paises_origem) {
        this.uf_destino = uf_destino;
        this.paises_origem = paises_origem;
    }

    public Ficha_Sintese_Estado(String uf_destino, Integer ano, List<Pais_Origem> paises_origem, List<Motivo_Viagem> motivos, List<Motivacao_Viagem_Lazer> motivacao_viagem_lazers, List<Composicao_Grupo_Viagem> composicao_grupos_viagem, List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo,  List<Fonte_Informacao> fontes_informacao, List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem) {
        super(ano, motivos, motivacao_viagem_lazers, composicao_grupos_viagem, gastos_medio_per_capita_brasil_motivo, null, fontes_informacao, utilizacao_agencia_viagem);
        this.uf_destino = uf_destino;
        this.paises_origem = paises_origem;
    }

    public String getUf_destino() {
        return uf_destino;
    }

    public void setUf_destino(String uf_destino) {
        this.uf_destino = uf_destino;
    }

    public List<Pais_Origem> getPaises_origem() {
        return paises_origem;
    }

    public void setPaises_origem(List<Pais_Origem> paises_origem) {
        this.paises_origem = paises_origem;
    }

    @Override
    public String toString() {
        return "Ficha_Sintese_Estado{" +
                "uf_destino='" + uf_destino + '\'' +
                ", paises_origem=" + paises_origem +
                + '\'' + super.toString() +
                '}' ;
    }
}
