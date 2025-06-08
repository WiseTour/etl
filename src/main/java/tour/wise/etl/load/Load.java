package tour.wise.etl.load;

import tour.wise.dao.*;
import tour.wise.model.OrigemDados;
import tour.wise.model.Pais;
import java.time.LocalDateTime;
import java.util.List;

public class Load {

    public static void loadOrigemDados(OrigemDados origemDados){
        try {
            Integer quantidadeInserida;

            try {
                quantidadeInserida = OrigemDadosDAO.insertIgnore(origemDados);
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

    public static void loadPais(Pais pais){
        try{
            PaisDAO.insertIgnore(pais);
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

    public static void loadPerfis(
            List<Object[]> batchArgs,
            List<Integer> fkPaisesDoLote,
            List<String> fkUfsDoLote,
            List<Object[]> batchFonteArgs,
            String tituloArquivoFonteFichasSinteses
    ){
        try {
            List<Integer> idsInseridos = Perfil_Estimado_TuristasDAO.insertLoteRetornandoIds(batchArgs);
            int fkFonte = OrigemDadosDAO.findByTitulo(tituloArquivoFonteFichasSinteses).getIdOrigemDados();

            for (int i = 0; i < idsInseridos.size(); i++) {
                int fkPerfilEstimado = idsInseridos.get(i);
                int fkPaisInserido = fkPaisesDoLote.get(i);
                String fkUf = fkUfsDoLote.get(i);
                batchFonteArgs.add(new Object[]{fkFonte, fkPerfilEstimado, fkPaisInserido, fkUf});
            }

            PerfilEstimadoTuristaOrigemDAO.insertLote(batchFonteArgs);

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


