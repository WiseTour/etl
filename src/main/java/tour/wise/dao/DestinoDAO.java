package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

import tour.wise.model.Destino;

public class DestinoDAO {

    private final JdbcTemplate connection;

    public DestinoDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public void insertLote(List<Destino> destinos) {
        String sql = """
        INSERT INTO destino (
            fk_perfil_estimado_turistas,
            fk_pais_origem,
            fk_uf_destino,
            fk_uf_entrada,
            permanencia_media
        ) VALUES (?, ?, ?, ?, ?)
        """;

        List<Object[]> batchArgs = destinos.stream()
                .map(destino -> new Object[]{
                        destino.getFkPerfilEstimadoTuristas(),
                        destino.getFkPaisOrigem(),
                        destino.getFkUfDestino(),
                        destino.getFkUfEntrada(),
                        destino.getPermanenciaMedia()
                })
                .toList();

        try {
            int[] resultados = connection.batchUpdate(sql, batchArgs);
            System.out.println("[DestinoDAO] Lote de destinos inserido. Registros inseridos: " + resultados.length);
        } catch (Exception e) {
            System.err.println("[DestinoDAO] Erro ao inserir lote de destinos:");
            e.printStackTrace();
            throw e; // Opcional: propagar exceção
        }
    }
}
