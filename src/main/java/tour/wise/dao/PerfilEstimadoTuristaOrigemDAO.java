package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PerfilEstimadoTuristaOrigemDAO {

    private final JdbcTemplate connection; // Conexão com o banco

    public PerfilEstimadoTuristaOrigemDAO(JdbcTemplate connection) {
        this.connection = connection;
    }


    public void insertLote(List<Object[]> batchArgs) {
        String sql = """
        INSERT INTO perfil_estimado_turista_origem
        (fk_origem_dados, fk_perfil_estimado_turistas, fk_pais_origem, fk_uf_entrada, data_coleta)
        VALUES (?, ?, ?, ?, NOW())
    """;


        try {
            int[] resultados = connection.batchUpdate(sql, batchArgs);
            System.out.println(LocalDateTime.now() + "Lote de associação fonte-perfil inserido: " + resultados.length);
        } catch (Exception e) {
            System.err.println(LocalDateTime.now() + "Erro ao inserir lote em Perfil_Estimado_Turista_Fonte:");
            e.printStackTrace();
            throw e;
        }
    }
}
