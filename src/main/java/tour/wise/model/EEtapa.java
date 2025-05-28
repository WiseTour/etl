package tour.wise.model;

public enum EEtapa {
    INCIALIZACAO(1, "inicializacao"),
    CONEXAO_S3(2, "conexao_s3"),
    EXTRACAO(3, "extracao"),
    TRATAMENTO(4, "tratamento"),
    TRANSFORMACAO(5, "transformacao"),
    CARREGAMENTO(6, "carregamento"),
    FINALIZACAO(7, "finalizacao");

    private final int id;
    private final String nome;

    EEtapa(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static EEtapa fromId(int id) {
        for (EEtapa etapa : EEtapa.values()) {
            if (etapa.getId() == id) {
                return etapa;
            }
        }
        throw new IllegalArgumentException("ID de Etapa inválido: " + id);
    }

    public static EEtapa fromNome(String nome) {
        for (EEtapa etapa : EEtapa.values()) {
            if (etapa.getNome().equalsIgnoreCase(nome)) {
                return etapa;
            }
        }
        throw new IllegalArgumentException("Nome de Etapa inválido: " + nome);
    }
}
