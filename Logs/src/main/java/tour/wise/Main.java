package tour.wise;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException, SQLException {
        executarLogs();
    }

    public static void executarLogs() throws InterruptedException, SQLException {
        Log log = new Log();
        Random random = new Random();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int idFonte = log.buscarFonteDeDadosPorNome("chegada2019.xlsx");

        // Início do processo
        log.showLog("Iniciando o processo ETL...", "INFO", "Início", formatter);
        log.inserirLog(idFonte, 1, 1, "Iniciando o processo ETL...", 0, 0, "");
        Thread.sleep((random.nextInt(5) + 1) * 1000L);

        // Extração
        log.showLog("Extraindo dados...", "INFO", "Extração", formatter);
        log.inserirLog(idFonte, 1, 1, "Extraindo dados...", 200, 0, "chegadas_2019.xlsx");
        Thread.sleep((random.nextInt(5) + 1) * 1000L);

        // Transformação
        log.showLog("Transformando dados...", "INFO", "Transformação", formatter);
        log.inserirLog(idFonte, 1, 2, "Transformando dados...", 200, 190, "tb_dados_transformados");
        Thread.sleep((random.nextInt(5) + 1) * 1000L);

        // Aviso (WARNING)
        log.showLog("Poucos dados extraídos, alguns dados podem estar faltando. Verifique a origem.", "WARNING", "Extração", formatter);
        log.inserirLog(idFonte, 2, 2, "Poucos dados extraídos, alguns dados podem estar faltando. Verifique a origem.", 200, 190, "tb_dados_brutos");
        Thread.sleep((random.nextInt(5) + 1) * 1000L);

        // Carga
        log.showLog("Carregando dados no banco...", "INFO", "Carga", formatter);
        log.inserirLog(idFonte, 1, 3, "Carregando dados no banco...", 190, 180, "tb_dados_finais");
        Thread.sleep((random.nextInt(5) + 1) * 1000L);

        // Erro (ERROR)
        log.showLog("Falha ao carregar os dados. Banco de dados não encontrado.", "ERROR", "Carga", formatter);
        log.inserirLog(idFonte, 3, 3, "Falha ao carregar os dados. Banco de dados não encontrado.", 190, 0, "tb_dados_finais");

        // Fim do processo
        log.showLog("Processo ETL encerrado!", "INFO", "Fim", formatter);
        log.inserirLog(idFonte, 1, 3, "Processo ETL encerrado!", 0, 0, "");
    }
}
