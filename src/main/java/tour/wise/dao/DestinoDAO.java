package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

public class DestinoDAO {

    private static final String SQL_INSERT_DESTINO = """
        INSERT INTO destino (
            fk_perfil_estimado_turistas,
            fk_pais_origem,
            fk_uf_destino,
            fk_uf_entrada,
            permanencia_media
        ) VALUES (?, ?, ?, ?, ?)
    """;

    /**
     * Insere em lote os destinos relacionados a um perfil estimado de turista.
     *
     * @param jdbcTemplate           JdbcTemplate com a conexão configurada
     * @param idPerfil               ID do perfil estimado de turista
     * @param fkPaisOrigem           ID do país de origem
     * @param fkUfEntrada            Sigla da UF de entrada no Brasil
     * @param ufsDestino             Lista de siglas das UFs visitadas (destinos)
     * @param permanenciaMediaDias  Número de dias de permanência média
     */
    public static void insertDestinosLote(
            JdbcTemplate jdbcTemplate,
            int idPerfil,
            int fkPaisOrigem,
            String fkUfEntrada,
            List<String> ufsDestino,
            double permanenciaMediaDias
    ) {
        for (String ufDestino : ufsDestino) {
            jdbcTemplate.update(SQL_INSERT_DESTINO,
                    idPerfil,
                    fkPaisOrigem,
                    ufDestino,
                    fkUfEntrada,
                    permanenciaMediaDias
            );
        }
    }
}

