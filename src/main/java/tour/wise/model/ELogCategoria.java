package tour.wise.model;

public enum ELogCategoria {
    ERRO(1, "erro"),
    AVISO(2, "aviso"),
    SUCESSO(3, "sucesso"),
    INFO(4, "info");

    private final int id;
    private final String nome;

    ELogCategoria(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public static ELogCategoria fromId(int id) {
        for (ELogCategoria c : ELogCategoria.values()) {
            if (c.getId() == id) {
                return c;
            }
        }
        throw new IllegalArgumentException("ID de Categoria inválido: " + id);
    }

    public static ELogCategoria fromNome(String nome) {
        for (ELogCategoria c : ELogCategoria.values()) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Nome de Categoria inválido: " + nome);
    }
}

