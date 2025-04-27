package tour.wise.dto.ficha_sintese_brasil;

import java.util.List;

public class Ficha_Sintese_Brasil {
    private Integer ano;
    private List<Motivo_Viagem> motivos;
    private List<Motivacao_Viagem_Lazer> motivacoes_viagem_lazer;
    private List<Composicao_Grupo_Viagem> composicao_grupos_viagem;
    private List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo;
    private List<Destinos_Mais_Visistados_Por_Motivo> destinos_mais_visistados_por_motivo;
    private List<Fonte_Informacao> fontes_informacao;
    private List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem;

    public Ficha_Sintese_Brasil() {
    }

    public Ficha_Sintese_Brasil(Integer ano, List<Motivo_Viagem> motivos, List<Motivacao_Viagem_Lazer> motivacao_viagem_lazers, List<Composicao_Grupo_Viagem> composicao_grupos_viagem, List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo, List<Destinos_Mais_Visistados_Por_Motivo> destinos_mais_visistados_por_motivo, List<Fonte_Informacao> fontes_informacao, List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem) {
        this.ano = ano;
        this.motivos = motivos;
        this.motivacoes_viagem_lazer = motivacao_viagem_lazers;
        this.composicao_grupos_viagem = composicao_grupos_viagem;
        this.gastos_medio_per_capita_brasil_motivo = gastos_medio_per_capita_brasil_motivo;
        this.destinos_mais_visistados_por_motivo = destinos_mais_visistados_por_motivo;
        this.fontes_informacao = fontes_informacao;
        this.utilizacao_agencia_viagem = utilizacao_agencia_viagem;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public List<Motivo_Viagem> getMotivos() {
        return motivos;
    }

    public void setMotivos(List<Motivo_Viagem> motivos) {
        this.motivos = motivos;
    }

    public List<Motivacao_Viagem_Lazer> getMotivacoes_viagem_lazer() {
        return motivacoes_viagem_lazer;
    }

    public void setMotivacoes_viagem_lazer(List<Motivacao_Viagem_Lazer> motivacao_viagem_lazers) {
        this.motivacoes_viagem_lazer = motivacao_viagem_lazers;
    }

    public List<Composicao_Grupo_Viagem> getComposicao_grupos_viagem() {
        return composicao_grupos_viagem;
    }

    public void setComposicao_grupos_viagem(List<Composicao_Grupo_Viagem> composicao_grupos_viagem) {
        this.composicao_grupos_viagem = composicao_grupos_viagem;
    }

    public List<Gasto_Medio_Per_Capita_Brasil_Motivo> getGastos_medio_per_capita_brasil_motivo() {
        return gastos_medio_per_capita_brasil_motivo;
    }

    public void setGastos_medio_per_capita_brasil_motivo(List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo) {
        this.gastos_medio_per_capita_brasil_motivo = gastos_medio_per_capita_brasil_motivo;
    }

    public List<Destinos_Mais_Visistados_Por_Motivo> getDestinos_mais_visistados_por_motivo() {
        return destinos_mais_visistados_por_motivo;
    }

    public void setDestinos_mais_visistados_por_motivo(List<Destinos_Mais_Visistados_Por_Motivo> destinos_mais_visistados_por_motivo) {
        this.destinos_mais_visistados_por_motivo = destinos_mais_visistados_por_motivo;
    }

    public List<Fonte_Informacao> getFontes_informacao() {
        return fontes_informacao;
    }

    public void setFontes_informacao(List<Fonte_Informacao> fontes_informacao) {
        this.fontes_informacao = fontes_informacao;
    }

    public List<Utilizacao_Agencia_Viagem> getUtilizacao_agencia_viagem() {
        return utilizacao_agencia_viagem;
    }

    public void setUtilizacao_agencia_viagem(List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem) {
        this.utilizacao_agencia_viagem = utilizacao_agencia_viagem;
    }

    @Override
    public String toString() {
        return "Ficha_Sintese_Brasil{" +
                "ano=" + ano +
                ", motivos=" + motivos +
                ", motivacao_viagem_lazers=" + motivacoes_viagem_lazer +
                ", composicao_grupos_viagem=" + composicao_grupos_viagem +
                ", gastos_medio_per_capita_brasil_motivo=" + gastos_medio_per_capita_brasil_motivo +
                ", destinos_mais_visistados_por_motivo=" + destinos_mais_visistados_por_motivo +
                ", fontes_informacao=" + fontes_informacao +
                ", utilizacao_agencia_viagem=" + utilizacao_agencia_viagem +
                '}';
    }
}
