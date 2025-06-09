package tour.wise.etl.load;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.*;
import tour.wise.model.ELogCategoria;
import tour.wise.model.Log;
import tour.wise.model.OrigemDados;
import tour.wise.model.Pais;
import tour.wise.util.Event;
import tour.wise.model.EEtapa;
import java.sql.Connection;
import java.util.List;

public class Load {

    public static void loadOrigemDados(JdbcTemplate jdbcTemplate, Connection connection, OrigemDados origemDados) throws Exception {
        try {
            OrigemDadosDAO.insertIgnore(jdbcTemplate, origemDados);
            Event.registerEvent(jdbcTemplate, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.CARREGAMENTO.getId()),
                    String.format("OrigemDados carregado com sucesso: %s", origemDados.toString()),
                    false
            );
        } catch (Exception e) {
            Event.registerEvent(jdbcTemplate, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.CARREGAMENTO.getId()),
                    String.format("Erro ao carregar OrigemDados: %s", origemDados.toString()),
                    e,
                    false
            );
            throw e;
        }
    }

    public static void loadPais(JdbcTemplate jdbcTemplate, Connection connection, Pais pais) throws Exception {
        try{
            PaisDAO.insertIgnore(jdbcTemplate, pais);
            Event.registerEvent(jdbcTemplate, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.CARREGAMENTO.getId()),
                    String.format("Pais carregado com sucesso: %s", pais.toString()),
                    false
            );
        }catch (Exception e) {
            Event.registerEvent(jdbcTemplate, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.CARREGAMENTO.getId()),
                    String.format("Erro ao carregar Pais: %s", pais.toString()),
                    e,
                    false
            );
            throw e;
        }
    }

    public static void loadPerfis(JdbcTemplate jdbcTemplate,
                                  Connection connection,
                                  List<Object[]> batchArgs,
                                  List<Integer> fkPaisesDoLote,
                                  List<String> fkUfsDoLote,
                                  List<Object[]> batchFonteArgs,
                                  String tituloArquivoFonteFichasSinteses
    ) throws Exception {
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

            Event.registerEvent(jdbcTemplate, connection,
                    new Log(ELogCategoria.SUCESSO.getId(), EEtapa.CARREGAMENTO.getId()),
                    String.format("Perfis carregados com sucesso. Fonte: %s, Total: %d", tituloArquivoFonteFichasSinteses, idsInseridos.size()),
                    false
            );
        }catch (Exception e) {
            Event.registerEvent(jdbcTemplate, connection,
                    new Log(ELogCategoria.ERRO.getId(), EEtapa.CARREGAMENTO.getId()),
                    String.format("Erro ao carregar perfis. Fonte: %s", tituloArquivoFonteFichasSinteses),
                    e,
                    false
            );
            throw e;
        }
    }

}


