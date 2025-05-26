package tour.wise.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Perfil_Estimado_TuristasDAO {

    private JdbcTemplate connection; // Conexão com o banco

    public Perfil_Estimado_TuristasDAO(JdbcTemplate connection) {
        this.connection = connection;
    }

    public List<Integer> insertLoteRetornandoIds(List<Object[]> batchArgs) {
        String sql = """
        INSERT INTO perfil_estimado_turistas (
            fk_pais_origem, fk_uf_entrada, ano, mes, quantidade_turistas,
            genero, faixa_etaria, via_acesso, composicao_grupo_familiar,
            fonte_informacao_viagem, motivo_viagem, motivacao_viagem_lazer, gasto_media_percapita_em_dolar
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        List<Integer> idsGerados = new ArrayList<>();

        for (Object[] args : batchArgs) {
            KeyHolder holder = new GeneratedKeyHolder();
            connection.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
                return ps;
            }, holder);

            Number id = holder.getKey();
            if (id != null) {
                idsGerados.add(id.intValue());
            }
        }

        return idsGerados;
    }






}
