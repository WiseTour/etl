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

                ChegadaTuristasInternacionaisBrasilMensalDTO chegada_turistas_internacionais_brasil_mensal_dto = new ChegadaTuristasInternacionaisBrasilMensalDTO(
                        mes, ano, chegada, via_acesso, uf_destino, pais_origem
                );

                chegadas_turistas_internacionais_brasil_mensal_dto.add(chegada_turistas_internacionais_brasil_mensal_dto);

                System.out.println("[TRANSFORMADO] Linha " + linha + " -> " + chegada);
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao transformar a linha " + linha + ": " + datum);
                e.printStackTrace();
            }
        }

        System.out.println("[FIM] Transformação concluída. Total de registros convertidos: " + chegadas_turistas_internacionais_brasil_mensal_dto.size());

        return chegadas_turistas_internacionais_brasil_mensal_dto;
    }


    public void load(JdbcTemplate connection, String orgao_emissor, String edicao, String titulo_edicao, String url_fonte, List<ChegadaTuristasInternacionaisBrasilMensalDTO> chegadas) {

        System.out.println("Iniciando carregamento de chegadas para a fonte: '" + titulo_edicao + "'...");

        PaisDAO pais_dao = new PaisDAO(connection);
        FonteDAO fonte_dao = new FonteDAO(connection);
        UnidadeFederativaBrasilDAO unidade_federativa_brasil_dao = new UnidadeFederativaBrasilDAO(connection);
        ChegadaTuristasInternacionaisBrasilMensalDAO chegada_turistas_internacionais_brasil_Mensal_dao = new ChegadaTuristasInternacionaisBrasilMensalDAO(connection);

        fonte_dao.insertIgnoreFonte(
                titulo_edicao,
                edicao,
                orgao_emissor,
                url_fonte
        );

        Integer fonteId = fonte_dao.getFonteId(titulo_edicao);

        if (fonteId == null) {
            System.out.println("Fonte não encontrada no banco. Interrompendo operação.");
            return;
        }

        System.out.println("Fonte encontrada. ID: " + fonteId);
        System.out.println("Iniciando processamento de " + chegadas.size() + " registros...");

        Integer inseridos = 0;
        Integer ignorados = 0;

        for (ChegadaTuristasInternacionaisBrasilMensalDTO chegada : chegadas) {
            String pais_origem = chegada.getPais_origem();
            String uf_destino = chegada.getUf_destino();
            Integer mes = chegada.getMes();
            Integer ano = chegada.getAno();
            Integer qtdChegadas = chegada.getQtdChegadas();
            String via_acesso = chegada.getVia_acesso();

            boolean dadosIncompletos = false;

            if (pais_origem == null || pais_origem.isBlank()) {
                System.out.println("[AVISO] Dados incompletos: país nulo ou em branco para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (uf_destino == null || uf_destino.isBlank()) {
                System.out.println("[AVISO] Dados incompletos: destino nulo ou em branco para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (mes == null || mes < 1 || mes > 12) {
                System.out.println("[AVISO] Dados incompletos: mês inválido para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (ano == null || ano < 1900 || ano > 2100) {
                System.out.println("[AVISO] Dados incompletos: ano inválido para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (qtdChegadas == null || qtdChegadas < 0) {
                System.out.println("[AVISO] Dados incompletos: número de chegadas inválido para a chegada: " + chegada);
                dadosIncompletos = true;
            }
            if (via_acesso == null || via_acesso.isBlank()) {
                System.out.println("[AVISO] Dados incompletos: via de acesso nula ou em branco para a chegada: " + chegada);
                dadosIncompletos = true;
            }

            if (dadosIncompletos) {
                ignorados++;
                continue;
            }


            Integer id_pais = pais_dao.getPaisId(pais_origem);

            if(id_pais == null){
                pais_dao.insertPais(pais_origem);
                id_pais = pais_dao.getPaisId(pais_origem);
            }

            String uf_sigla = unidade_federativa_brasil_dao.getSiglaPorNome(uf_destino);

            if (uf_sigla == null) {
                System.out.println("Unidade Federativa não encontrada no banco. Interrompendo operação.");
                return;
            }

            System.out.println("Unidade Federativa encontrada. SIGLA: " + uf_sigla);


            if (chegada_turistas_internacionais_brasil_Mensal_dao.isChegadaExistente(mes, ano, uf_sigla, id_pais)) {
                System.out.println("[INFO] Chegada já cadastrada: " +
                        ano + "/" + mes +
                        " - UF: " + uf_destino +
                        " - País: " + pais_origem);
                ignorados++;
                continue;
            }

            try {
                chegada_turistas_internacionais_brasil_Mensal_dao.insertChegada(mes, ano, qtdChegadas, via_acesso, fonteId, id_pais, uf_sigla);
                inseridos++;
            } catch (Exception e) {
                System.out.println("[ERRO] Falha ao inserir registro: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Processamento concluído.");
        System.out.println("Total de registros processados: " + chegadas.size());
        System.out.println("Inseridos: " + inseridos);
        System.out.println("Ignorados (já existentes ou inválidos): " + ignorados);
    }





}


