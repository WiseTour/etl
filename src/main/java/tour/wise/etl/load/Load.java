package tour.wise.etl.load;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.*;
import tour.wise.model.OrigemDados;
import tour.wise.model.Pais;
import java.util.List;

public class Load {

    public static void loadOrigemDados(JdbcTemplate jdbcTemplate, OrigemDados origemDados){
        try {
            try {
                OrigemDadosDAO.insertIgnore(jdbcTemplate, origemDados);
            }catch (Exception e) {
                throw e;
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public static void loadPais(JdbcTemplate jdbcTemplate, Pais pais){
        try{
            PaisDAO.insertIgnore(jdbcTemplate, pais);
        }catch (Exception e) {
            throw e;
        }

    }

    public static void loadPerfis(JdbcTemplate jdbcTemplate,
            List<Object[]> batchArgs,
            List<Integer> fkPaisesDoLote,
            List<String> fkUfsDoLote,
            List<Object[]> batchFonteArgs,
            String tituloArquivoFonteFichasSinteses
    ){
        try {
            List<Integer> idsInseridos = Perfil_Estimado_TuristasDAO.insertLoteRetornandoIds(jdbcTemplate, batchArgs);
            int fkFonte = OrigemDadosDAO.findByTitulo(jdbcTemplate, tituloArquivoFonteFichasSinteses).getIdOrigemDados();

            for (int i = 0; i < idsInseridos.size(); i++) {
                int fkPerfilEstimado = idsInseridos.get(i);
                int fkPaisInserido = fkPaisesDoLote.get(i);
                String fkUf = fkUfsDoLote.get(i);
                batchFonteArgs.add(new Object[]{fkFonte, fkPerfilEstimado, fkPaisInserido, fkUf});
            }

            PerfilEstimadoTuristaOrigemDAO.insertLote(jdbcTemplate, batchFonteArgs);

        }catch (Exception e) {
            throw e;
        }

    }

}


