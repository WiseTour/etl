package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Perfil_Estimado_TuristasDAO {

    public static List<Integer> insertLoteRetornandoIds(JdbcTemplate jdbcTemplate, List<Object[]> batchArgs) throws SQLException {
        String sqlPerfil = """
            INSERT INTO perfil_estimado_turistas (
                fk_pais_origem, fk_uf_entrada, ano, mes, quantidade_turistas,
                genero, faixa_etaria, via_acesso, composicao_grupo_familiar,
                fonte_informacao_viagem, motivo_viagem, motivacao_viagem_lazer, gasto_media_percapita_em_dolar
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        List<Integer> idsGerados = new ArrayList<>();

        for (Object[] args : batchArgs) {
            // Último é a lista de UFs de destino
            @SuppressWarnings("unchecked")
            List<String> ufsDestino = (List<String>) args[args.length - 1];

            // Penúltimo é a permanência média
            Double permanenciaMedia = (Double) args[args.length - 2];

            // Os 13 primeiros dados são para o perfil
            Object[] dadosPerfil = Arrays.copyOf(args, args.length - 2);

            KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlPerfil, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < dadosPerfil.length; i++) {
                    ps.setObject(i + 1, dadosPerfil[i]);
                }
                return ps;
            }, holder);

            Number idGerado = holder.getKey();
            if (idGerado != null) {
                int idPerfil = idGerado.intValue();
                idsGerados.add(idPerfil);

                Integer fkPaisOrigem = (Integer) dadosPerfil[0];
                String fkUfEntrada = (String) dadosPerfil[1];

                // Chama o método do DAO Destino para inserir os destinos
                DestinoDAO.insertDestinosLote(jdbcTemplate, idPerfil, fkPaisOrigem, fkUfEntrada, ufsDestino, permanenciaMedia);
            }
        }

        return idsGerados;
    }
}
