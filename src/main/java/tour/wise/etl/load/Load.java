package tour.wise.etl.load;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.*;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.model.OrigemDados;
import tour.wise.model.Pais;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class Load {

    JdbcTemplate connection;
    LogDAO logDAO;

    public Load(JdbcTemplate connection) {
        this.connection = connection;
        this.logDAO =  new LogDAO(connection);
    }

    public void loadOrigemDados(
            OrigemDados origemDados
    ){
        try {
            Integer quantidadeInserida;

            try {
                OrigemDadosDAO origemDadosDAO =  new OrigemDadosDAO(connection);

                quantidadeInserida = origemDadosDAO.insertIgnore(origemDados);
            }catch (Exception e) {
                // Log no banco
//                logDAO.insertLog(
//                        1,
//                        1, // Erro
//                        1,
//                        "Erro ao tentar achar fonte no banco de dados: " + e.getMessage(),
//                        LocalDateTime.now(),
//                        0,
//                        0,
//                        "Fonte_Dados"
//                );
                throw e;
            }

            if (quantidadeInserida > 0) {

                // Log no banco
//                logDAO.insertLog(
//                        1, // fk_fonte (ajuste conforme necessário)
//                        3, // Sucesso
//                        1, // Carregamento
//                        "Carregamento realizado com sucesso.",
//                        LocalDateTime.now(),
//                        1,
//                        1,
//                        "Fonte_Dados"
//                );

                // Log no console
                System.out.println(LocalDateTime.now() + "Fonte inserida com sucesso: " + origemDados.getTitulo());
                System.out.println(LocalDateTime.now() + "Log de Sucesso: Carregamento realizado com sucesso.");

            } else {
                // Log no banco
//                logDAO.insertLog(
//                        1,
//                        2, // Aviso
//                        1,
//                        "Fonte já existente. Nenhuma inserção foi feita.",
//                        LocalDateTime.now(),
//                        0,
//                        0,
//                        "Fonte_Dados"
//                );

                // Log no console
                System.out.println(LocalDateTime.now() + "Fonte já existente. Nenhuma inserção feita: " + origemDados.getTitulo());
                System.out.println(LocalDateTime.now() + "Log de Aviso: Fonte já existente. Nenhuma inserção foi feita.");
            }
        } catch (Exception e) {
            // Log no banco
//            logDAO.insertLog(
//                    1,
//                    1, // Erro
//                    1,
//                    "Erro ao tentar inserir fonte: " + e.getMessage(),
//                    LocalDateTime.now(),
//                    0,
//                    0,
//                    "Fonte_Dados"
//            );

            // Log no console
            System.err.println("Erro ao tentar inserir a fonte: " + origemDados.getTitulo());
            System.err.println("Mensagem de erro: " + e.getMessage());
            System.err.println("Stack trace do erro:");
            e.printStackTrace(); // Exibe o stack trace no console para depuração
            throw e;
        }
    }

    public void loadPais(Pais pais){
        try{
            PaisDAO paisDAO = new PaisDAO(connection);

            paisDAO.insertIgnore(pais);

        }catch (Exception e) {
            // Log no banco
//            logDAO.insertLog(
//                    1,
//                    1, // Erro
//                    1,
//                    "Erro ao tentar inserir países no banco: " + e.getMessage(),
//                    LocalDateTime.now(),
//                    0,
//                    0,
//                    "Fonte_Dados"
//            );

            e.printStackTrace(); // Exibe o stack trace no console para depuração
            throw e;
        }

    }

    public void loadPerfis(
            List<Object[]> batchArgs,
            List<Integer> fkPaisesDoLote,
            List<String> fkUfsDoLote,
            List<Object[]> batchFonteArgs,
            String tituloArquivoFonteFichasSinteses
    ){
        try {
            Perfil_Estimado_TuristasDAO perfilEstimadoTuristasDAO = new Perfil_Estimado_TuristasDAO(connection);
            OrigemDadosDAO origemDadosDAO = new OrigemDadosDAO(connection);
            PerfilEstimadoTuristaOrigemDAO perfilEstimadoTuristaFonteDAO = new PerfilEstimadoTuristaOrigemDAO(connection);

            List<Integer> idsInseridos = perfilEstimadoTuristasDAO.insertLoteRetornandoIds(batchArgs);
            int fkFonte = origemDadosDAO.findByTitulo(tituloArquivoFonteFichasSinteses).getIdOrigemDados();

            for (int i = 0; i < idsInseridos.size(); i++) {
                int fkPerfilEstimado = idsInseridos.get(i);
                int fkPaisInserido = fkPaisesDoLote.get(i);
                String fkUf = fkUfsDoLote.get(i);
                batchFonteArgs.add(new Object[]{fkFonte, fkPerfilEstimado, fkPaisInserido, fkUf});
            }

            perfilEstimadoTuristaFonteDAO.insertLote(batchFonteArgs);
        }catch (Exception e) {
            // Log no banco
//            logDAO.insertLog(
//                    1,
//                    1, // Erro
//                    1,
//                    "Erro ao tentar inserir países no banco: " + e.getMessage(),
//                    LocalDateTime.now(),
//                    0,
//                    0,
//                    "Fonte_Dados"
//            );

            e.printStackTrace(); // Exibe o stack trace no console para depuração
            throw e;
        }


    }


}


