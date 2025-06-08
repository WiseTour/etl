package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.util.DataBaseConnection;
import java.time.LocalDateTime;
import java.util.List;

public class PerfilEstimadoTuristaOrigemDAO {

    private static final JdbcTemplate jdbcTemplate;

    static {
        try {
            jdbcTemplate = DataBaseConnection.getJdbcTemplate();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao inicializar JdbcTemplate no PerfilEstimadoTuristaOrigemDAO: " + e.getMessage(), e);
        }
    }
    public static void insertLote(List<Object[]> batchArgs) {
        String sql = """
        INSERT INTO perfil_estimado_turista_origem
        (fk_origem_dados, fk_perfil_estimado_turistas, fk_pais_origem, fk_uf_entrada, data_coleta)
        VALUES (?, ?, ?, ?, NOW())
    """;


        try {
            int[] resultados = jdbcTemplate.batchUpdate(sql, batchArgs);
            System.out.println(LocalDateTime.now() + "Lote de associação fonte-perfil inserido: " + resultados.length);
        } catch (Exception e) {
            System.err.println(LocalDateTime.now() + "Erro ao inserir lote em Perfil_Estimado_Turista_Fonte:");
            e.printStackTrace();
            throw e;
        }
    }
}
