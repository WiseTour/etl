package tour.wise.model;


public class Perfil_Estimado_Turistas {

    private Integer id_perfil_estimado_visistantes;
    private String fonte_perfil;
    private String fonte_chegadas;
    private Integer total_chegadas;
    private Integer ano;
    private String unidade_federativa_destino;
    private String pais_origem;
    private String genero;
    private String faixa_etaria;
    private String composicao_grupo_familiar;
    private String fonte_informacao_viagem;
    private String servico_agencia_turismo;
    private String motivo_viagem;
    private String motivacao_viagem_lazer;



    public Perfil_Estimado_Turistas() {
    }

    public Perfil_Estimado_Turistas(String fonte_perfil, String fonte_chegadas, Integer total_chegadas, Integer ano, String unidade_federativa_destino, String pais_origem, String genero, String faixa_etaria, String composicao_grupo_familiar, String fonte_informacao_viagem, String servico_agencia_turismo, String motivo_viagem, String motivacao_viagem_lazer) {
        this.fonte_perfil = fonte_perfil;
        this.fonte_chegadas = fonte_chegadas;
        this.total_chegadas = total_chegadas;
        this.ano = ano;
        this.unidade_federativa_destino = unidade_federativa_destino;
        this.pais_origem = pais_origem;
        this.genero = genero;
        this.faixa_etaria = faixa_etaria;
        this.composicao_grupo_familiar = composicao_grupo_familiar;
        this.fonte_informacao_viagem = fonte_informacao_viagem;
        this.servico_agencia_turismo = servico_agencia_turismo;
        this.motivo_viagem = motivo_viagem;
        this.motivacao_viagem_lazer = motivacao_viagem_lazer;
    }

    public Integer getId_perfil_estimado_visistantes() {
        return id_perfil_estimado_visistantes;
    }

    public void setId_perfil_estimado_visistantes(Integer id_perfil_estimado_visistantes) {
        this.id_perfil_estimado_visistantes = id_perfil_estimado_visistantes;
    }

    public String getFonte_perfil() {
        return fonte_perfil;
    }

    public void setFonte_perfil(String fonte_perfil) {
        this.fonte_perfil = fonte_perfil;
    }

    public String getFonte_chegadas() {
        return fonte_chegadas;
    }

    public void setFonte_chegadas(String fonte_chegadas) {
        this.fonte_chegadas = fonte_chegadas;
    }

    public Integer getTotal_chegadas() {
        return total_chegadas;
    }

    public void setTotal_chegadas(Integer total_chegadas) {
        this.total_chegadas = total_chegadas;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getUnidade_federativa_destino() {
        return unidade_federativa_destino;
    }

    public void setUnidade_federativa_destino(String unidade_federativa_destino) {
        this.unidade_federativa_destino = unidade_federativa_destino;
    }

    public String getPais_origem() {
        return pais_origem;
    }

    public void setPais_origem(String pais_origem) {
        this.pais_origem = pais_origem;
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

    public String getMotivacao_viagem_lazer() {
        return motivacao_viagem_lazer;
    }

    public void setMotivacao_viagem_lazer(String motivacao_viagem_lazer) {
        this.motivacao_viagem_lazer = motivacao_viagem_lazer;
    }

    @Override
    public String toString() {
        return "Perfil_Estimado_Turistas{" +
                "id_perfil_estimado_visistantes=" + id_perfil_estimado_visistantes +
                ", fonte_perfil='" + fonte_perfil + '\'' +
                ", fonte_chegadas='" + fonte_chegadas + '\'' +
                ", total_chegadas=" + total_chegadas +
                ", ano=" + ano +
                ", unidade_federativa_destino='" + unidade_federativa_destino + '\'' +
                ", pais_origem='" + pais_origem + '\'' +
                ", genero='" + genero + '\'' +
                ", faixa_etaria='" + faixa_etaria + '\'' +
                ", composicao_grupo_familiar='" + composicao_grupo_familiar + '\'' +
                ", fonte_informacao_viagem='" + fonte_informacao_viagem + '\'' +
                ", servico_agencia_turismo='" + servico_agencia_turismo + '\'' +
                ", motivo_viagem='" + motivo_viagem + '\'' +
                ", motivacao_viagem_lazer='" + motivacao_viagem_lazer + '\'' +
                '}';
    }
}
