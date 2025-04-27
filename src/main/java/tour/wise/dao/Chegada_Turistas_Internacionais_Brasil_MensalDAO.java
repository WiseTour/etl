package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.model.Chegada_Turistas_Internacionais_Brasil_Mensal;

public class Chegada_Turistas_Internacionais_Brasil_MensalDAO {
    private JdbcTemplate connection; // Conexão com o banco

    public Chegada_Turistas_Internacionais_Brasil_MensalDAO(JdbcTemplate connection) {
        this.connection = connection;
    }



    // Método para verificar se a chegada já existe no banco
    public boolean isChegadaExistente(Chegada_Turistas_Internacionais_Brasil_Mensal chegada, Integer paisId) {
        String sqlVerificaExistencia = "SELECT COUNT(*) FROM Chegadas_Turistas_Internacionais_Brasil_Mensal " +
                "WHERE mes = ? AND ano = ? AND fk_uf_sigla = ? AND fk_pais = ?";

        int count = connection.queryForObject(sqlVerificaExistencia, Integer.class,
                chegada.getMes(), chegada.getAno(), chegada.getDestino().getSigla(), paisId);

        return count > 0;
    }

    // Método para inserir a chegada no banco
    public void insertChegada(Chegada_Turistas_Internacionais_Brasil_Mensal chegada, Integer fonte_id, Integer pais_id, String unidade_federativa_sigla) {
        String sqlInsertChegada = "INSERT INTO Chegadas_Turistas_Internacionais_Brasil_Mensal " +
                "(mes, ano, chegadas, via_acesso, fk_uf_sigla, fk_fonte, fk_pais) VALUES (?, ?, ?, ?, ?, ?, ?)";

        connection.update(sqlInsertChegada,
                chegada.getMes(),
                chegada.getAno(),
                chegada.getChegadas(),
                chegada.getVia_acesso(),
                unidade_federativa_sigla,
                fonte_id,
                pais_id
        );
        System.out.println("Chegada inserida: " + chegada.getAno() + "/" + chegada.getMes() +
                " - UF: " + unidade_federativa_sigla + " - País ID: " + pais_id);
    }


}
