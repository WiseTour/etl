package tour.wise.dto.ficha_sintese_brasil;

import java.util.List;

public class Ficha_Sintese_Brasil {
    private Integer ano;
    private List<Genero> genero;
    private List<Faixa_Etaria> faixa_etaria;
    private List<Composicao_Grupo_Viagem> composicao_grupos_viagem;
    private List<Fonte_Informacao> fontes_informacao;
    private List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem;
    private List<Motivo_Viagem> motivos;
    private List<Motivacao_Viagem_Lazer> motivacoes_viagem_lazer;
    private List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo;
    private List<Permanencia_Media> permanencia_media_brasil;
    private List<Destinos_Mais_Visitados_Por_Motivo> destinos_mais_visistados_por_motivo;


    public Ficha_Sintese_Brasil() {
    }

    public Ficha_Sintese_Brasil(Integer ano, List<Genero> genero, List<Faixa_Etaria> faixa_etaria, List<Composicao_Grupo_Viagem> composicao_grupos_viagem, List<Fonte_Informacao> fontes_informacao, List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem, List<Motivo_Viagem> motivos, List<Motivacao_Viagem_Lazer> motivacoes_viagem_lazer, List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo, List<Permanencia_Media> permanencia_media_brasil, List<Destinos_Mais_Visitados_Por_Motivo> destinos_mais_visistados_por_motivo) {
        this.ano = ano;
        this.genero = genero;
        this.faixa_etaria = faixa_etaria;
        this.composicao_grupos_viagem = composicao_grupos_viagem;
        this.fontes_informacao = fontes_informacao;
        this.utilizacao_agencia_viagem = utilizacao_agencia_viagem;
        this.motivos = motivos;
        this.motivacoes_viagem_lazer = motivacoes_viagem_lazer;
        this.gastos_medio_per_capita_brasil_motivo = gastos_medio_per_capita_brasil_motivo;
        this.permanencia_media_brasil = permanencia_media_brasil;
        this.destinos_mais_visistados_por_motivo = destinos_mais_visistados_por_motivo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public List<Genero> getGenero() {
        return genero;
    }

    public void setGenero(List<Genero> genero) {
        this.genero = genero;
    }

    public List<Faixa_Etaria> getFaixa_etaria() {
        return faixa_etaria;
    }

    public void setFaixa_etaria(List<Faixa_Etaria> faixa_etaria) {
        this.faixa_etaria = faixa_etaria;
    }

    public List<Composicao_Grupo_Viagem> getComposicao_grupos_viagem() {
        return composicao_grupos_viagem;
    }

    public void setComposicao_grupos_viagem(List<Composicao_Grupo_Viagem> composicao_grupos_viagem) {
        this.composicao_grupos_viagem = composicao_grupos_viagem;
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

    public void setUtilizcao_agencia_viagem(List<Utilizacao_Agencia_Viagem> utilizacao_agencia_viagem) {
        this.utilizacao_agencia_viagem = utilizacao_agencia_viagem;
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

    public void setMotivacoes_viagem_lazer(List<Motivacao_Viagem_Lazer> motivacoes_viagem_lazer) {
        this.motivacoes_viagem_lazer = motivacoes_viagem_lazer;
    }

    public List<Gasto_Medio_Per_Capita_Brasil_Motivo> getGastos_medio_per_capita_brasil_motivo() {
        return gastos_medio_per_capita_brasil_motivo;
    }

    public void setGastos_medio_per_capita_brasil_motivo(List<Gasto_Medio_Per_Capita_Brasil_Motivo> gastos_medio_per_capita_brasil_motivo) {
        this.gastos_medio_per_capita_brasil_motivo = gastos_medio_per_capita_brasil_motivo;
    }

    public List<Permanencia_Media> getPermanencia_media_brasil() {
        return permanencia_media_brasil;
    }

    public void setPermanencia_media_brasil(List<Permanencia_Media> permanencia_media_brasil) {
        this.permanencia_media_brasil = permanencia_media_brasil;
    }

    public List<Destinos_Mais_Visitados_Por_Motivo> getDestinos_mais_visistados_por_motivo() {
        return destinos_mais_visistados_por_motivo;
    }

    public void setDestinos_mais_visistados_por_motivo(List<Destinos_Mais_Visitados_Por_Motivo> destinos_mais_visistados_por_motivo) {
        this.destinos_mais_visistados_por_motivo = destinos_mais_visistados_por_motivo;
    }

    @Override
    public String toString() {
        return "Ficha_Sintese_Brasil{" +
                "ano=" + ano +
                ", genero=" + genero +
                ", faixa_etaria=" + faixa_etaria +
                ", composicao_grupos_viagem=" + composicao_grupos_viagem +
                ", fontes_informacao=" + fontes_informacao +
                ", servico_agencia_viagem=" + utilizacao_agencia_viagem +
                ", motivos=" + motivos +
                ", motivacoes_viagem_lazer=" + motivacoes_viagem_lazer +
                ", gastos_medio_per_capita_brasil_motivo=" + gastos_medio_per_capita_brasil_motivo +
                ", permanencia_mediar=" + permanencia_media_brasil +
                ", destinos_mais_visistados_por_motivo=" + destinos_mais_visistados_por_motivo +
                '}';
    }
}
