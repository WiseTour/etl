package tour.wise.model;


public class Perfil_Estimado_Turistas {

    private Integer id_perfil_estimado_visistantes;
    private String genero;
    private String faixa_etaria;
    private String composicao_grupo_familiar;
    private String fonte_informacao_viagem;
    private String servico_agencia_turismo;
    private String motivo_viagem;
    private Double gasto_medio_percapita_em_reais;
    private Integer ano;
    private Integer turistas;
    private String via_acesso;
    private Integer fk_pais_origem;

    public Perfil_Estimado_Turistas() {
    }

    public Perfil_Estimado_Turistas(Integer id_perfil_estimado_visistantes, String genero, String faixa_etaria, String composicao_grupo_familiar, String fonte_informacao_viagem, String servico_agencia_turismo, String motivo_viagem, Double gasto_medio_percapita_em_reais, Integer ano, Integer turistas, String via_acesso,  Integer fk_pais_origem) {
        this.id_perfil_estimado_visistantes = id_perfil_estimado_visistantes;
        this.genero = genero;
        this.faixa_etaria = faixa_etaria;
        this.composicao_grupo_familiar = composicao_grupo_familiar;
        this.fonte_informacao_viagem = fonte_informacao_viagem;
        this.servico_agencia_turismo = servico_agencia_turismo;
        this.motivo_viagem = motivo_viagem;
        this.gasto_medio_percapita_em_reais = gasto_medio_percapita_em_reais;
        this.ano = ano;
        this.turistas = turistas;
        this.via_acesso = via_acesso;
        this.fk_pais_origem = fk_pais_origem;
    }

    public Integer getId_perfil_estimado_visistantes() {
        return id_perfil_estimado_visistantes;
    }

    public void setId_perfil_estimado_visistantes(Integer id_perfil_estimado_visistantes) {
        this.id_perfil_estimado_visistantes = id_perfil_estimado_visistantes;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFaixa_etaria() {
        return faixa_etaria;
    }

    public void setFaixa_etaria(String faixa_etaria) {
        this.faixa_etaria = faixa_etaria;
    }

    public String getComposicao_grupo_familiar() {
        return composicao_grupo_familiar;
    }

    public void setComposicao_grupo_familiar(String composicao_grupo_familiar) {
        this.composicao_grupo_familiar = composicao_grupo_familiar;
    }

    public String getFonte_informacao_viagem() {
        return fonte_informacao_viagem;
    }

    public void setFonte_informacao_viagem(String fonte_informacao_viagem) {
        this.fonte_informacao_viagem = fonte_informacao_viagem;
    }

    public String getServico_agencia_turismo() {
        return servico_agencia_turismo;
    }

    public void setServico_agencia_turismo(String servico_agencia_turismo) {
        this.servico_agencia_turismo = servico_agencia_turismo;
    }

    public String getMotivo_viagem() {
        return motivo_viagem;
    }

    public void setMotivo_viagem(String motivo_viagem) {
        this.motivo_viagem = motivo_viagem;
    }


    public Double getGasto_medio_percapita_em_reais() {
        return gasto_medio_percapita_em_reais;
    }

    public void setGasto_medio_percapita_em_reais(Double gasto_medio_percapita_em_reais) {
        this.gasto_medio_percapita_em_reais = gasto_medio_percapita_em_reais;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getTuristas() {
        return turistas;
    }

    public void setTuristas(Integer turistas) {
        this.turistas = turistas;
    }

    public String getVia_acesso() {
        return via_acesso;
    }

    public void setVia_acesso(String via_acesso) {
        this.via_acesso = via_acesso;
    }

    public Integer getFk_pais_origem() {
        return fk_pais_origem;
    }

    public void setFk_pais_origem(Integer fk_pais_origem) {
        this.fk_pais_origem = fk_pais_origem;
    }

    @Override
    public String toString() {
        return "Perfil_Estimado_Turistas{" +
                "id_perfil_estimado_visistantes=" + id_perfil_estimado_visistantes +
                ", genero='" + genero + '\'' +
                ", faixa_etaria='" + faixa_etaria + '\'' +
                ", composicao_grupo_familiar='" + composicao_grupo_familiar + '\'' +
                ", fonte_informacao_viagem='" + fonte_informacao_viagem + '\'' +
                ", servico_agencia_turismo='" + servico_agencia_turismo + '\'' +
                ", motivo_viagem='" + motivo_viagem + '\'' +
                ", gasto_medio_percapita_em_reais=" + gasto_medio_percapita_em_reais +
                ", ano=" + ano +
                ", turistas=" + turistas +
                ", via_acesso='" + via_acesso + '\'' +
                ", fk_pais_origem=" + fk_pais_origem +
                '}';
    }
}
