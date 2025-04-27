package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dto.Chegada_Turistas_Internacionais_Brasil_Mensal_DTO;
import tour.wise.model.Chegada_Turistas_Internacionais_Brasil_Mensal;

public class Chegada_Turistas_Internacionais_Brasil_MensalDAO {
    private JdbcTemplate connection; // Conexão com o banco

    public Chegada_Turistas_Internacionais_Brasil_MensalDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    Unidade_Federativa_Brasil_DAO unidade_federativa_brasil_dao = new Unidade_Federativa_Brasil_DAO(connection);


    // Método para verificar se a chegada já existe no banco
    public boolean isChegadaExistente(Integer mes, Integer ano, String uf_sigla,Integer id_pais) {
        String sqlVerificaExistencia = "SELECT COUNT(*) FROM Chegadas_Turistas_Internacionais_Brasil_Mensal " +
                "WHERE mes = ? AND ano = ? AND fk_uf_sigla = ? AND fk_pais = ?";

        int count = connection.queryForObject(sqlVerificaExistencia, Integer.class, mes, ano, uf_sigla , id_pais);

        return count > 0;
    }

    // Método para inserir a chegada no banco
    public void insertChegada(Integer mes, Integer ano, Integer chegadas, String via_acesso, Integer id_fonte, Integer id_pais, String uf_sigla) {
        String sqlInsertChegada = "INSERT INTO Chegadas_Turistas_Internacionais_Brasil_Mensal " +
                "(mes, ano, chegadas, via_acesso, fk_uf_sigla, fk_fonte, fk_pais) VALUES (?, ?, ?, ?, ?, ?, ?)";

        connection.update(sqlInsertChegada,
                mes,
                ano,
                chegadas,
                via_acesso,
                uf_sigla,
                id_fonte,
                id_pais
        );
        System.out.println("Chegada inserida: " + ano + "/" + mes +
                " - UF: " + uf_sigla + " - País ID: " + id_pais);
    }


}
