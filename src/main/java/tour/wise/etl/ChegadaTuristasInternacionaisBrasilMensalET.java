package tour.wise.etl;

import org.springframework.jdbc.core.JdbcTemplate;
import tour.wise.dao.ChegadaTuristasInternacionaisBrasilMensalDAO;
import tour.wise.dao.FonteDAO;
import tour.wise.dao.PaisDAO;
import tour.wise.dao.UnidadeFederativaBrasilDAO;
import tour.wise.dto.ChegadaTuristasInternacionaisBrasilMensalDTO;
import tour.wise.service.Service;

import java.util.ArrayList;
import java.util.List;

public class ChegadaTuristasInternacionaisBrasilMensalET {

    Service service = new Service();

    public List<List<Object>> extract(String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types) {

        System.out.println("[INÍCIO] Extração de dados iniciada.");
        System.out.println("[INFO] Arquivo: " + fileName);
        System.out.println("[INFO] Planilha (sheet): " + sheetNumber);
        System.out.println("[INFO] Linha de cabeçalho: " + header);
        System.out.println("[INFO] Quantidade de colunas esperadas: " + colluns);
        System.out.println("[INFO] Tipos esperados: " + types);

        List<List<Object>> data = null;

        try {
            data = service.extract(fileName, sheetNumber, header, colluns, types);



            System.out.println("[SUCESSO] ETL finalizado com sucesso. Total de registros extraídos: " + (data != null ? data.size() : 0));
        } catch (Exception e) {
            System.out.println("[ERRO] Falha na extração dos dados: " + e.getMessage());
            e.printStackTrace();
        }



        return data;
    }

    public List<List<Object>> extractBatch(String fileName, Integer sheetNumber, Integer header, Integer colluns, List<String> types, Integer batchSize) {

        System.out.println("[INÍCIO] Extração de dados iniciada por lote.");
        System.out.println("[INFO] Arquivo: " + fileName);
        System.out.println("[INFO] Planilha (sheet): " + sheetNumber);
        System.out.println("[INFO] Linha de cabeçalho: " + header);
        System.out.println("[INFO] Quantidade de colunas esperadas: " + colluns);
        System.out.println("[INFO] Tipos esperados: " + types);
        System.out.println("[INFO] Tamanho do lote: " + batchSize);

        List<List<Object>> allData = new ArrayList<>();

        try {
            int offset = 0;
            List<List<Object>> batch;

            do {
                // Aqui você precisa que o método do service aceite offset e batchSize
                batch = service.extractBatch(fileName, sheetNumber, header, colluns, types, offset, batchSize);
                if (batch != null && !batch.isEmpty()) {
                    allData.addAll(batch);
                    System.out.println("[LOTE] Registros extraídos no lote atual: " + batch.size());
                    offset += batchSize;
                } else {
                    break;
                }
            } while (batch.size() == batchSize);

            System.out.println("[SUCESSO] ETL finalizado com sucesso. Total de registros extraídos: " + allData.size());
        } catch (Exception e) {
            System.out.println("[ERRO] Falha na extração dos dados: " + e.getMessage());
            e.printStackTrace();
        }

        return allData;
    }


    public List<ChegadaTuristasInternacionaisBrasilMensalDTO> transform(List<List<Object>> data, String fonte, String edicao) {

        System.out.println("[INÍCIO] Transformação dos dados iniciada.");
        System.out.println("[INFO] Fonte: " + fonte + ", Edição: " + edicao);
        System.out.println("[INFO] Total de registros brutos recebidos: " + (data != null ? data.size() : 0));

        List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas_turistas_internacionais_brasil_mensal_dto = new ArrayList<>();

        int linha = 0;
        for (List<Object> datum : data) {
            try {
                linha++;
                String pais_origem = datum.get(2).toString();
                String uf_destino = datum.get(4).toString();
                String via_acesso = datum.get(6).toString();
                Integer ano = Double.valueOf(datum.get(8).toString()).intValue();
                Integer mes = Double.valueOf(datum.get(10).toString()).intValue();
                Integer chegada = Double.valueOf(datum.get(11).toString()).intValue();

                if(chegada > 0){
                    ChegadaTuristasInternacionaisBrasilMensalDTO chegada_turistas_internacionais_brasil_mensal_dto = new ChegadaTuristasInternacionaisBrasilMensalDTO(
                            mes, ano, chegada, via_acesso, uf_destino, pais_origem
                    );

                    chegadas_turistas_internacionais_brasil_mensal_dto.add(chegada_turistas_internacionais_brasil_mensal_dto);
                }

            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao transformar a linha " + linha + ": " + datum);
                e.printStackTrace();
            }
        }

        System.out.println("[FIM] Transformação concluída. Total de registros convertidos: " + chegadas_turistas_internacionais_brasil_mensal_dto.size());

        return chegadas_turistas_internacionais_brasil_mensal_dto;
    }








}


